package com.example.lenovo.start;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.start.Models.userInfo;
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

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextView tv;
    EditText updates;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        tv=(TextView)findViewById(R.id.tv);
        updates=(EditText)findViewById(R.id.updatesTxt);
        tv.setText(firebaseUser.getEmail().toString());

get();

    }



    public void get(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference scoresRef = database.getReference();
       /* scoresRef.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
               updates.setText(dataSnapshot.child("Description").getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            // ...
        });*/




    }
    public void logoutUser(View view) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(getApplicationContext(),start.class));


    }

    public void updateInfo(View view) {
         String updates1=updates.getText().toString();

        firebaseUser=firebaseAuth.getCurrentUser();





        if(TextUtils.isEmpty(updates1)){
            Toast.makeText(ProfileActivity.this,"No Updates Entered",Toast.LENGTH_LONG).show();
        }
        else {
            databaseReference.child(firebaseUser.getUid()).child("Description").setValue(updates1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Saved Information Successfully!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Information Not Saved!!!!", Toast.LENGTH_LONG).show();
                    }
                }

            });



        }




    }
}
