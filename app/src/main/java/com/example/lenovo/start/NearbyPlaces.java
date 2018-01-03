package com.example.lenovo.start;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.lenovo.start.Models.ArrayListClass;
import com.example.lenovo.start.Models.userInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

            get();
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






    public void get(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference scoresRef = database.getReference();
        progressDialog.setTitle("Wait..");
        progressDialog.show();

        scoresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                progressDialog.dismiss();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    userInfo userInfo=ds.getValue(userInfo.class);
                    arrayList.add(userInfo);
                }

                ArrayListClass arrayListClass=new ArrayListClass(NearbyPlaces.this,arrayList);
                listView.setAdapter(arrayListClass);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
}
