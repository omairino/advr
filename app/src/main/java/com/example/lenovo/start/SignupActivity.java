package com.example.lenovo.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lenovo.start.Models.userInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignupActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Button createUserBtn;
    EditText emailTxt,passwordTxt,nameTxt,addressTxt,propertyTxt,numTxt;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        createUserBtn=(Button)findViewById(R.id.createUserBtn);
        emailTxt=(EditText)findViewById(R.id.emailTxt);
        passwordTxt=(EditText)findViewById(R.id.passwordTxt);
        nameTxt=(EditText)findViewById(R.id.nameTxt);
        addressTxt=(EditText)findViewById(R.id.addressTxt);
        propertyTxt=(EditText)findViewById(R.id.propertyTxt);
        numTxt=(EditText)findViewById(R.id.numTxt);

        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser=firebaseAuth.getCurrentUser();

        ((EditText)findViewById(R.id.latTxt)).setText(getIntent().getStringExtra("lat"));
        ((EditText)findViewById(R.id.lngTxt)).setText(getIntent().getStringExtra("lng"));
    }

    public void createUserBtnClicked(View view) {
        String email=emailTxt.getText().toString();
        String password=passwordTxt.getText().toString();
        progressDialog.setTitle("Registering User..");
        progressDialog.show();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(SignupActivity.this,"Please enter email",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(SignupActivity.this,"Please enter password",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Registered Successfully!", Toast.LENGTH_LONG).show();
                        nameTxt.setVisibility(View.VISIBLE);
                        addressTxt.setVisibility(View.VISIBLE);
                        numTxt.setVisibility(View.VISIBLE);
                        propertyTxt.setVisibility(View.VISIBLE);
                        ((Button)findViewById(R.id.sendInfoBtn)).setVisibility(View.VISIBLE);
                        ((LinearLayout) findViewById(R.id.linearLayout1)).setVisibility(View.VISIBLE);
                        createUserBtn.setEnabled(false);
                        emailTxt.setEnabled(false);
                        passwordTxt.setEnabled(false);

                    } else {
                        Toast.makeText(SignupActivity.this, "Registered Unsuccessfully!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    public void addInfo(View view) {
        String name=nameTxt.getText().toString();
        String address=addressTxt.getText().toString();
        String property=propertyTxt.getText().toString();
        String number=numTxt.getText().toString();
        String lat=getIntent().getStringExtra("lat");
        String lng = getIntent().getStringExtra("lng");

        FirebaseUser user=firebaseAuth.getCurrentUser();
      //  String[] xxx={" "};
       // List nameList = new ArrayList<String>(Arrays.asList(xxx));

        userInfo userInfo=new userInfo(name,address,property,number,lat,lng,null);
        if((TextUtils.isEmpty(name)||TextUtils.isEmpty(address)||TextUtils.isEmpty(property)||TextUtils.isEmpty(number))){
            Toast.makeText(SignupActivity.this,"Please Fill Information!",Toast.LENGTH_LONG).show();
        }
        else {
            databaseReference.child(user.getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Saved Information Successfully!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),start.class));
                    } else {
                        Toast.makeText(SignupActivity.this, "Information Not Saved!!!!", Toast.LENGTH_LONG).show();
                    }
                }

            });
        }
    }


}
