package com.example.droidsutra;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import static java.lang.String.valueOf;

public class Databasehelper extends SQLiteOpenHelper {
    Bird myBird;
    public static final String DATABASE_NAME = "Main_Database.db";

    public static final String TABLE_NAME = "Observations_Table";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "LOCATION";
    public static final String COL_3 = "SIZE";
    public static final String COL_4 = "COLOUR";
    public static final String COL_5 = "DATE";
    public static final String COL_6 = "TIME";
    public static final String COL_7 = "CATEGORY";
    public static final String COL_8 = "DESCRIPTION";
    public static final String COL_9 = "NOTE";

    public static final String CATEGORY_TABLE_NAME = "Category_Table";
    public static final String CAT_COL_1 = "NAME";

    public static File dir = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/DroidSutraFiles");
    public Databasehelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (NAME TEXT, LOCATION TEXT, SIZE CHAR, COLOUR TEXT, DATE TEXT, TIME TEXT, CATEGORY TEXT, DESCRIPTION TEXT, NOTE TEXT)");
        db.execSQL("CREATE TABLE " + CATEGORY_TABLE_NAME + " (NAME TEXT, SUB TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String location, String size, String colour, String date, String time, String category, String description, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,name);
        contentValues.put(COL_2,location);
        contentValues.put(COL_3,size);
        contentValues.put(COL_4,colour);
        contentValues.put(COL_5,date);
        contentValues.put(COL_6,time);
        contentValues.put(COL_7,category);
        contentValues.put(COL_8,description);
        contentValues.put(COL_9,note);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertData_CategoryTable(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_COL_1,name);
        long result = db.insert(CATEGORY_TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        return res;
    }

    public Cursor getAllData_CategoryTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME,null);
        return res;
    }

    public Cursor getData(String Atrr_Name, String Atrr_Value) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + Atrr_Name + " = " + "'" + Atrr_Value + "'",null);
        return res;
    }

    public Cursor getNames(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Name FROM " + TABLE_NAME + " WHERE Name  = '" + name + "'",null);
        return res;
    }

    public Cursor getData_mutlipleAttr(String where_clause) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + where_clause, null);
        return res;
    }

    public void deleteData_CategoryTable(String Cat_Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("DELETE FROM " + CATEGORY_TABLE_NAME + " WHERE Name = " + Cat_Name,null);
        db.delete(CATEGORY_TABLE_NAME, "Name = ?", new String[]{Cat_Name});
    }

    public void deleteData_ObservationsTable(String Date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "Date < ?", new String[]{Date});
    }

    public boolean ExportNow(View view) {
       String columnString = "NAME, LOCATION, SIZE, COLOUR, DATE, TIME, CATEGORY, DESCRIPTION, NOTE\n";

        Cursor res = getAllData();

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();


        if (root.canWrite()){

            dir.mkdirs();
            file   =   new File(dir, "DroidSutra.csv");
            FileOutputStream out   =   null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(columnString.getBytes());
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    String combinedString;
                    String input = res.getString(1);
                    String regex = ",";
                    String output = input.replaceAll(regex, "");
                    //output = URLEncoder.encode(output, "utf-8");
                    //System.out.println(output);
                    combinedString =
                            res.getString(0)+" ," +
                                    res.getString(1)+" ," +
                                    res.getString(2)+" ," +
                                    res.getString(3)+" ," +
                                    res.getString(4)+" ," +
                                    res.getString(5)+" ," +
                                    res.getString(6)+" ," +
                                    res.getString(7)+" ," +
                                    res.getString(8)+" ," +
                                    ("\n");
                    out.write(combinedString.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }
        else {
            return false;
        }
    }

    public void createPdf(String sometext, View view){
        // create a new document
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            PdfDocument document = null;
            document = new PdfDocument();
            Cursor res = getAllData();
            int count_res = res.getCount();

            if (count_res <= 18) {
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1500, 750, 1).create();
                newpage(document, pageInfo, view, res);
            }
            else if (count_res > 18 && count_res <= 36) {
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1500, 750, 1).create();
                newpage(document, pageInfo, view, res);
                res.moveToPosition(17);
                PdfDocument.PageInfo pageInfo_2 = new PdfDocument.PageInfo.Builder(1500, 750, 2).create();
                newpage(document, pageInfo_2, view, res);
            }
            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/DroidSutra/";
            File file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + "DroidSutra.pdf";
            File filePath = new File(targetPdf);
            try {
                document.writeTo(new FileOutputStream(filePath));
                //Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                //Log.e("main", "error "+e.toString());
                //Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
            }
            // close the document
            document.close();
        }
    }

    public void newpage(PdfDocument document, PdfDocument.PageInfo pageInfo, View view, Cursor res) {
        //PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1500, 750, 1).create();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            PdfDocument.Page page = null;
            page = document.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            Bitmap logo = BitmapFactory.decodeResource(view.getResources(), R.drawable.logo_resized);
            Bitmap resized = Bitmap.createScaledBitmap(logo,(int)(logo.getWidth()*0.7), (int)(logo.getHeight()*0.7), true);
            canvas.drawBitmap(resized, 60,20, null);

            paint.setColor(Color.rgb(128,0,0));
            Typeface typeface = ResourcesCompat.getFont(view.getContext(), R.font.fondamento);
            paint.setTypeface(typeface);
            paint.setTextSize(30);
            canvas.drawText("OBSERVATIONS TABLE", 240, 100, paint);

            Date currentTime = Calendar.getInstance().getTime();
            paint.setTextSize(20);
            canvas.drawText("Exported as on:", 240, 120, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText(""+currentTime, 380, 120, paint);
            paint.setColor(Color.rgb(128,0,0));
            canvas.drawText("Thanks for using DroidSutra - Bird Tracking App", 1020, 620, paint);
            canvas.drawText("To reach out to us, follow the link:", 1020, 640, paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(18);
            canvas.drawText("https://github.com/DBSE-teaching/isee2019-DroidSutra", 1020, 660, paint);

            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setAntiAlias(true);

            canvas.drawLine(
                    20, // startX
                    170, // startY
                    canvas.getWidth() - 20, // stopX
                    170, // stopY
                    paint // Paint
            );

            canvas.drawLine(
                    20, // startX
                    500, // startY
                    canvas.getWidth() - 20, // stopX
                    500, // stopY
                    paint // Paint
            );

            Bitmap end_logo = BitmapFactory.decodeResource(view.getResources(), R.drawable.man_resized);
            Bitmap end_logo_resized = Bitmap.createScaledBitmap(end_logo,(int)(end_logo.getWidth()*0.7), (int)(end_logo.getHeight()*0.7), true);
            canvas.drawBitmap(end_logo_resized, 900,550, null);

            paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(24);
            typeface = ResourcesCompat.getFont(view.getContext(), R.font.priyati);
            paint.setTypeface(typeface);
            String columnString = "NAME, LOCATION, SIZE, COLOUR, DATE, TIME, CATEGORY, DESCRIPTION, NOTE\n";
            if(MainActivity.language_chosen=="en") {
                canvas.drawText("NAME", 80, 200, paint);
                canvas.drawText("SIZE", 160, 200, paint);
                canvas.drawText("COLOUR", 240, 200, paint);
                canvas.drawText("DATE", 320, 200, paint);
                canvas.drawText("TIME", 400, 200, paint);
                canvas.drawText("CATEGORY", 480, 200, paint);
                canvas.drawText("DESCRIPTION", 600, 200, paint);
                canvas.drawText("NOTE", 800, 200, paint);
                canvas.drawText("LOCATION", 880, 200, paint);
            }
            if(MainActivity.language_chosen=="de") {
                canvas.drawText("Name", 80, 200, paint);
                canvas.drawText("Größe", 160, 200, paint);
                canvas.drawText("Farbe", 240, 200, paint);
                canvas.drawText("Datum", 320, 200, paint);
                canvas.drawText("Zeit", 400, 200, paint);
                canvas.drawText("Kategorie", 480, 200, paint);
                canvas.drawText("Beschreibung", 600, 200, paint);
                canvas.drawText("Hinweis", 800, 200, paint);
                canvas.drawText("Ort", 880, 200, paint);
            }


            int y_cord = 215;
            int count = 1;
            String count_string;
            int count_obs;

            while (res.moveToNext() && count <=18) {
                count_obs = res.getPosition() + 1;
                count_string = valueOf(count_obs);
                canvas.drawText(count_string, 60, y_cord, paint);
                canvas.drawText(res.getString(0), 80, y_cord, paint);
                canvas.drawText(res.getString(2), 160, y_cord, paint);
                canvas.drawText(res.getString(3), 240, y_cord, paint);
                canvas.drawText(res.getString(4), 320, y_cord, paint);
                canvas.drawText(res.getString(5), 400, y_cord, paint);
                canvas.drawText(res.getString(6), 480, y_cord, paint);
                canvas.drawText(res.getString(7), 600, y_cord, paint);
                canvas.drawText(res.getString(8), 800, y_cord, paint);
                canvas.drawText(res.getString(1), 880, y_cord, paint);
                y_cord = y_cord + 15;
                count = count + 1;
            }


            // finish the page
            document.finishPage(page);
        }
    }
}
