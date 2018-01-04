package com.example.lenovo.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.start.Models.ArrayListClass;
import com.example.lenovo.start.Models.userInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NearbyPlaces extends AppCompatActivity {

ListView listView;
ProgressDialog progressDialog;
TextView tv;
String lat,lng;
String property,secondLat,secondLng;
String desc="";
    private ArrayList<userInfo> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nearby_places);

            listView=(ListView)findViewById(R.id.listview1);
            tv=(TextView)findViewById(R.id.textView);

            progressDialog=new ProgressDialog(this);
        LatLng latLng=new LatLng(Double.parseDouble(getIntent().getStringExtra("lat")),Double.parseDouble(getIntent().getStringExtra("lng")));

        new GetPlaces().execute(latLng);



            lat=getIntent().getStringExtra("lat");
            lng=getIntent().getStringExtra("lng");
            tv.setText(lat+" "+lng);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the selected item text from ListView
                    userInfo selectedItem =  (userInfo) parent.getItemAtPosition(position);
                    desc=selectedItem.getUsername();
                    secondLat=selectedItem.getLat();
                    secondLng=selectedItem.getLng();
                    property=selectedItem.getProperty();
                    gotoMap();

                }
            });
    }






    public class GetPlaces extends AsyncTask<LatLng,Void,Void>{
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference scoresRef = database.getReference();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Searching..");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(final LatLng... coords) {




            scoresRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    arrayList.clear();
                    progressDialog.dismiss();
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        userInfo userInfo=ds.getValue(userInfo.class);
                        if(CalculationByDistance(coords[0],new LatLng(Double.parseDouble(userInfo.getLat()),Double.parseDouble(userInfo.getLng())))<=100.0) {
                            arrayList.add(userInfo);
                        }
                    }

                    ArrayListClass arrayListClass=new ArrayListClass(NearbyPlaces.this,arrayList);
                    listView.setAdapter(arrayListClass);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Searching End",Toast.LENGTH_LONG).show();
        }
    }

    public void gotoMap() {
        LinearLayout mainLayout = (LinearLayout)
                findViewById(R.id.nearbyLayout);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        TextView tv=(TextView)popupView.findViewById(R.id.txtView);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, 400, focusable);

        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        tv.setText(desc);
        Button btn=(Button)popupView.findViewById(R.id.mapBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent=new Intent(getApplicationContext(),MapsActivity.class);
                mapIntent.putExtra("firstLat",lat);
                mapIntent.putExtra("firstLng",lng);
                mapIntent.putExtra("secondLat",secondLat);
                mapIntent.putExtra("secondLng",secondLng);
                mapIntent.putExtra("name",property);

                startActivity(mapIntent);

            }
        });
        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}
