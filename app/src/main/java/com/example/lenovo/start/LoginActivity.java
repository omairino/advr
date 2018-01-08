package com.example.lenovo.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText emailTxt,passwordTxt;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTxt=(EditText)findViewById(R.id.emailTxt);
        passwordTxt=(EditText)findViewById(R.id.passwordTxt);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);





    }
    public void LoginUser(View view) {
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.setTitle("Logging User..");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                    } else {
                        Toast.makeText(LoginActivity.this, "Login unsuccesful!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    public void gotoSignup(View view) {


      String  lat = getIntent().getStringExtra("lat");
      String  lng = getIntent().getStringExtra("lng");


        Intent  signupIntent2 = new Intent(this,SignupActivity.class);

        signupIntent2.putExtra("lat",lat);
        signupIntent2.putExtra("lng", lng);
        startActivity(signupIntent2);

    }
}
