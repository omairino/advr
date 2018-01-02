package com.example.lenovo.start;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class start extends AppCompatActivity implements LocationListener{
    Button loginBtn,signupbtn,searchBtn;
    LocationManager locationManager;
    Double lat,lng;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loginBtn=(Button)findViewById(R.id.loginBtn);
        signupbtn=(Button)findViewById(R.id.signupBtn);
        searchBtn=(Button)findViewById(R.id.searchBtn);
    firebaseAuth=FirebaseAuth.getInstance();
    firebaseUser=firebaseAuth.getCurrentUser();
    if(firebaseUser!=null){
        finish();
        startActivity(new Intent(this,ProfileActivity.class));
    }


     getper();


    }
    private boolean isGPSEnabled(){
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private void checkGPSEnabledAndProceed(){
        if(isGPSEnabled()){
            Toast.makeText(start.this, "GPS is enabled.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(start.this, "GPS is disabled, show alert", Toast.LENGTH_SHORT).show();
            //buildAlertMessageNoGps();
        }
    }
    public void signupBtnClicked(View view) {
        Intent signupIntent=new Intent(this,SignupActivity.class);
        signupIntent.putExtra("lat",Double.toString(lat));
        signupIntent.putExtra("lng",Double.toString(lng));
        startActivity(signupIntent);
    }

    public void loginBtnClicked(View view) {
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));


    }

    @Override
    public void onLocationChanged(Location location) {
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
        ((TextView)findViewById(R.id.tv1)).setText(Double.toString(lat)+" "+Double.toString(lng));
    }
}
