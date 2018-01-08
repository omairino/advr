package com.example.lenovo.start;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.start.Models.ArrayListClass;
import com.example.lenovo.start.Models.Items;
import com.example.lenovo.start.Models.userInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    boolean answer=false;
    TextView tv;
    EditText updates;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    private ArrayList<String> arrayList = new ArrayList<>(1000);
    ArrayAdapter<String> adapter;
   int index=0;
    ListView l2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        tv=(TextView)findViewById(R.id.tv);
        updates=(EditText)findViewById(R.id.updatesTxt);
        l2=(ListView)findViewById(R.id.l2) ;
        adapter=new ArrayAdapter<String>(ProfileActivity.this,android.R.layout.simple_list_item_1,arrayList);
get();


        deleteitem();

    }


   static String select;
    private void deleteitem()
    {

        l2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(ProfileActivity.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + position);
                final int positionToRemove = position;
                 //select= (String) l2.getItemAtPosition(position);
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReference.child(firebaseUser.getUid()).child("description").child(String.valueOf(positionToRemove)).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfileActivity.this, "Saved Information Successfully!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Information Not Saved!!!!", Toast.LENGTH_LONG).show();
                                }
                                //  updates.setText("");
                            }

                        });


                        adapter.notifyDataSetChanged();
                    }});
                adb.show();
            }
        });
    }





    public void get(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference scoresRef = database.getReference();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        try{
        scoresRef.child(firebaseUser.getUid()).child("description").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arrayList.clear();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(!dsp.getValue().equals("")){
                    arrayList.add(String.valueOf(dsp.getValue()));}
                }

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                   if(dsp.getValue().equals("")){
                    index=Integer.parseInt(dsp.getKey());
                   break;}
                   else
                       index=arrayList.size();
                }


                l2.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void logoutUser(View view) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(getApplicationContext(),start.class));


    }








    public void addtoList(View view) {

        final String updates1=updates.getText().toString();

        firebaseUser=firebaseAuth.getCurrentUser();

        if(TextUtils.isEmpty(updates1)){
            Toast.makeText(ProfileActivity.this,"No Updates Entered",Toast.LENGTH_LONG).show();
        }
        else {
            databaseReference.child(firebaseUser.getUid()).child("description").child(String.valueOf(index)).setValue(updates1).addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Saved Information Successfully!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Information Not Saved!!!!", Toast.LENGTH_LONG).show();
                    }
                    updates.setText("");
                }

            });
    }

}
}