package com.example.lenovo.start;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;

public class start extends AppCompatActivity implements LocationListener{
    Button loginBtn,signupbtn,searchBtn;
    LocationManager locationManager;
    Double lat,lng;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loginBtn=(Button)findViewById(R.id.loginBtn);
        signupbtn=(Button)findViewById(R.id.signupBtn);
        searchBtn=(Button)findViewById(R.id.searchBtn);


      //  ((TextView)findViewById(R.id.textView2)).setText(Double.toString(CalculationByDistance(jenin,omarioptics))+"");
        if(cheakInt()){
          //  Toast.makeText(start.this,"No Internet",Toast.LENGTH_LONG);

    firebaseAuth=FirebaseAuth.getInstance();
    firebaseUser=firebaseAuth.getCurrentUser();

        }else {Toast.makeText(start.this,"No Internet",Toast.LENGTH_LONG).show();}




     getper();


    }




    private boolean isGPSEnabled(){
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private void checkGPSEnabledAndProceed(){
        if(isGPSEnabled()){
          // Toast.makeText(start.this, "GPS is enabled.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(start.this, "GPS is disabled, show alert", Toast.LENGTH_SHORT).show();
            //buildAlertMessageNoGps();
        }
    }




    public void signupBtnClicked(View view) {
        try{
        if(cheakInt()) {
            Toast.makeText(start.this, "No Internet", Toast.LENGTH_LONG);
            Intent  signupIntent = new Intent(this, SignupActivity.class);

            signupIntent.putExtra("lat", Double.toString(lat));
            signupIntent.putExtra("lng", Double.toString(lng));
            startActivity(signupIntent);
        }
        else{Toast.makeText(start.this,"No Wifi",Toast.LENGTH_LONG).show();}
    }catch (Exception e){
        Toast.makeText(start.this, "Get Coordinates", Toast.LENGTH_LONG).show();
    }
    }



    public void loginBtnClicked(View view) {


        try{
            if (cheakInt()) {
                firebaseAuth = FirebaseAuth.getInstance();

                Intent  signupIntent2 = new Intent(this, LoginActivity.class);

                signupIntent2.putExtra("lat", Double.toString(lat));
                signupIntent2.putExtra("lng", Double.toString(lng));
                startActivity(signupIntent2);


            } else {
                Toast.makeText(start.this, "No Wifi", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(start.this, "Get Coordinates", Toast.LENGTH_LONG).show();
        }


        }

    @Override
    public void onLocationChanged(Location location) {

       // getResources().se
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
       Toast.makeText(start.this,"GPS IS ON",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(start.this,"GPS IS OFF",Toast.LENGTH_SHORT).show();
    }


    public void getper(){

        ActivityCompat.requestPermissions(start.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkGPSEnabledAndProceed();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(start.this,"no permission ",Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
    }

    public void searchBtnClicked(View view) {

   try {
       if (cheakInt()) {
           Toast.makeText(start.this, "No Internet", Toast.LENGTH_LONG);

           Intent signupIntent1 = new Intent(this, NearbyPlaces.class);
           signupIntent1.putExtra("lat", Double.toString(lat));
           signupIntent1.putExtra("lng", Double.toString(lng));
           startActivity(signupIntent1);
       } else {
           Toast.makeText(start.this, "No Internet", Toast.LENGTH_LONG).show();
       }
   }catch (Exception e)
   {
       Toast.makeText(start.this, "Get Coordinates", Toast.LENGTH_LONG).show();
   }
    }

    public boolean cheakInt(){


        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            return connected;
        }
        else{
            connected = false;
            return connected;
        }
    }


}
