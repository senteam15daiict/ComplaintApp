package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_Up extends AppCompatActivity {

    EditText vEmail,vPassword,vPhone_Number,vUser_Name;
    Button vSign_Up;
    TextView vLogin_Screen;
    FirebaseAuth fauth;
    DatabaseReference databaseReference;
    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);

        vEmail = (EditText) findViewById(R.id.Citizen_Email);
        vPassword = (EditText) findViewById(R.id.Citizen_Password);
        vPhone_Number = (EditText) findViewById(R.id.Citizen_Phone_Number);
        vUser_Name = (EditText) findViewById(R.id.Citizen_User_Name);
        vSign_Up = (Button) findViewById(R.id.Citizen_Sign_Up);
        vLogin_Screen = (TextView) findViewById(R.id.Login_Screen);
        fauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Citizen");

        vSign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = vUser_Name.getText().toString();
                final String phoneNumber = vPhone_Number.getText().toString();
                final String email = vEmail.getText().toString();
                final String password = vPassword.getText().toString();
                final Boolean isUser = true;

                if(TextUtils.isEmpty(email)){
                    vEmail.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    vPassword.setError("Password is required");
                    return;
                }

                if(TextUtils.isEmpty(name)){
                    vUser_Name.setError("Pls Enter Name");
                    return;
                }

                if(TextUtils.isEmpty(phoneNumber)){
                    vPhone_Number.setError("Pls Enter Phone Number");
                    return;
                }

                if(phoneNumber.length() != 10){
                    vPhone_Number.setError("Pls Enter Valid Phone Number");
                    return;
                }
                else{
                    int f = 0;
                    for(int i=0;i<10;i++){
                        if(!(phoneNumber.charAt(i) >= '0' && phoneNumber.charAt(i)<= '9')){
                            f = 1;
                        }
                    }
                    if(f == 1){
                        vPhone_Number.setError("Pls Enter valid phone Number");
                        return;
                    }
                }

                if(password.length() <= 5){
                    vPassword.setError("Password length must be >= 6");
                    return;
                }

                fauth = FirebaseAuth.getInstance();
                fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Citizen c1 = new Citizen(
                                    name,
                                    password,
                                    email,
                                    phoneNumber
                            );

                            FirebaseDatabase.getInstance().getReference("User_citizen")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(c1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Sign_Up.this,"User Created",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                }
                            });
                            //Toast.makeText(create_acc.this,"User Created",Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(Sign_Up.this,"Error! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        vLogin_Screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_Up.this,Login.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    };
}
