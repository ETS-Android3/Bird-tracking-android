package com.example.droidsutra;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.String.valueOf;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    public static double current_longitude, current_latitude;
    public AutoCompleteTextView input_search;
    public Button button, ic_gps, btnClear;
    public MarkerOptions marker;
    public Marker marker_current, marker_dragged;
    Bird myBird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        input_search = (AutoCompleteTextView) findViewById(R.id.input_search);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_search.setText("");
            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        marker = new MarkerOptions();

        ic_gps = (Button) findViewById(R.id.ic_gps);
        ic_gps.performClick();

        // Add a marker in Sydney and move the camera



        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);



        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();
            }
        });




        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(7000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                 @Override
                                 public void onMarkerDragStart(Marker marker) {

                                 }

                                 @Override
                                 public void onMarkerDrag(Marker marker) {

                                 }

                                 @Override
                                 public void onMarkerDragEnd(Marker marker) {
                                     LatLng pos = marker.getPosition();
                                     mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16));

                                     double longitude = pos.longitude;
                                     double latitude = pos.latitude;

                                     Geocoder myLocation = new Geocoder(MapsActivity.this, Locale.getDefault());
                                     List<Address> myList = null;
                                     try {
                                         myList = myLocation.getFromLocation(latitude, longitude, 1);
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                     }
                                     Address address = (Address) myList.get(0);
                                     String addressStr = "";
                                     addressStr += address.getAddressLine(0);

                                     input_search.setText(addressStr);

                                     showMessage( getResources().getString(R.string.sel_loc), addressStr,
                                             getResources().getString(R.string.loc_at)+ "\n" + addressStr,1);

                                 }
                             });
    }


    public void showMessage(String title, final String addressStr, final String Message, int Type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message + "\n\n"+getResources().getString(R.string.click_yes) +
                "\n\n"+getResources().getString(R.string.click_no));

        if (Type == 1) {
            builder.setPositiveButton(getResources().getString(R.string.toast_yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    myBird.location = addressStr;
                    Intent intent = new Intent(MapsActivity.this, FAB_Attributes_2.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            builder.setNegativeButton(getResources().getString(R.string.toast_no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        } else {
            builder.setPositiveButton(getResources().getString(R.string.toast_yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    myBird.location = addressStr;
                    Intent intent = new Intent(MapsActivity.this, FAB_Attributes_2.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            builder.setNegativeButton(getResources().getString(R.string.toast_no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }


        builder.show();
    }


    public void fetchCurrentLocation(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(
                this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            current_longitude = location.getLongitude();
                            current_latitude = location.getLatitude();

                            Geocoder myLocation = new Geocoder(MapsActivity.this, Locale.getDefault());
                            List<Address> myList = null;
                            try {
                                myList = myLocation.getFromLocation(current_latitude, current_longitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Address address = (Address) myList.get(0);
                            String addressStr = "";
                            addressStr += address.getAddressLine(0);
                            showMessage(getResources().getString(R.string.sel_loc), addressStr,
                                    getResources().getString(R.string.loc_at)+"\n" + addressStr, 1);

                            LatLng current_location = new LatLng(current_latitude, current_longitude);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_location, 16));
                            if (marker_dragged != null) {
                                marker_dragged.remove();
                            }
                            if (marker_current != null) {
                                marker_current.remove();
                            }
                            marker_current = mMap.addMarker(marker.position(current_location).title(getResources().getString(R.string.sel_loc)).draggable(true));
                            input_search.setText(addressStr);
                        }
                    }
                }

        );
    }

    private void geoLocate() {
        String searchString = input_search.getText().toString();

        if (TextUtils.isEmpty(searchString)) {
            Toast.makeText(this, getResources().getString(R.string.plz_enter_maps), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> addressList = new ArrayList<>();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        double latitude = 0, longitude = 0;
        String address = null;
        try {
            list = geocoder.getFromLocationName(searchString, 1);
            for(int i = 0; i < list.size(); i++){
                address = list.get(i).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                latitude = list.get(i).getLatitude();
                longitude = list.get(i).getLongitude();

            }
        } catch (IOException e) {
        }

//        if (latitude != 0 && longitude != 0) {
//            LatLng new_location = new LatLng(latitude, longitude);
//            String lat_str = valueOf(latitude);
//            String lon_str = valueOf(longitude);
//            Toast.makeText(this, lat_str + " " + lon_str, Toast.LENGTH_SHORT).show();
//        }
//        else {
//            String lat_str = valueOf(latitude);
//            String lon_str = valueOf(longitude);
//            Toast.makeText(this, lat_str + " " + lon_str, Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, getResources().getString(R.string.loc_not_found), Toast.LENGTH_SHORT).show();
//        }

        if (latitude != 0 && longitude != 0) {
            LatLng new_location = new LatLng(latitude, longitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new_location, 16));
            if (marker_current != null) {
                marker_current.remove();
            }

            if (marker_dragged != null) {
                marker_dragged.remove();
            }
            marker_dragged = mMap.addMarker(marker.position(new_location).title(getResources().getString(R.string.sel_loc)).draggable(true));

            input_search.setText(address);
            showMessage(getResources().getString(R.string.sel_loc), address,
                getResources().getString(R.string.loc_at)+"\n" + address.toString(), 1);
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.loc_not_found), Toast.LENGTH_SHORT).show();
        }

    }


}