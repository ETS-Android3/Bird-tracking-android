package com.example.droidsutra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Timer;
import java.util.TimerTask;

public class FAB_Successful extends AppCompatActivity {
    Button btnviewAll;
    ImageButton btnExportData;
    Databasehelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab__successful);
        myDb = new Databasehelper(this);

        //HomeScreen(this);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(FAB_Successful.this, HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 2500);
    }


//    public void viewAll() {
//        btnviewAll.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Cursor res = myDb.getAllData();
//                        if(res.getCount() == 0) {
//                            // show message
//                            showMessage("Error","Nothing found");
//                            return;
//                        }
//
//                        StringBuffer buffer = new StringBuffer();
//                        while (res.moveToNext()) {
//                            buffer.append("Name :"+ res.getString(0)+"\n");
//                            buffer.append("Location :"+ res.getString(1)+"\n");
//                            buffer.append("Size :"+ res.getString(2)+"\n");
//                            buffer.append("Colour :"+ res.getString(3)+"\n");
//                            buffer.append("Date :"+ res.getString(4)+"\n");
//                            buffer.append("Time :"+ res.getString(5)+"\n");
//                            buffer.append("Category :"+ res.getString(6)+"\n\n");
////                            TextView textView = (TextView) findViewById(R.id.data_from_db);
////                            textView.setText(buffer.toString());
//                    }
//
//                        // Show all data
//                        showMessage("Data",buffer.toString());
//
//                    }
//                }
//        );
//    }
//
//    public void showMessage(String title,String Message){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setMessage(Message);
//        builder.show();

//    public void ExportData() {
//        btnExportData.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                            Toast.makeText(FAB_Successful.this, "Here", Toast.LENGTH_SHORT).show();
//                            new ExportDatabaseCSVTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//                        } else {
//                            Toast.makeText(FAB_Successful.this, "Here2", Toast.LENGTH_SHORT).show();
//                            new ExportDatabaseCSVTask().execute();
//                        }
//                    }
//
//}
//        );
//    }

    public void HomeScreen(FAB_Successful view) {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}

