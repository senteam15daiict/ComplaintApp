package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;

public class Sign_Up_Corporation extends AppCompatActivity {

    EditText vCorporation_User_Name,vCorporation_Phone_Number,vCorporation_Email;
    EditText vCorporation_Password,vCorporation_Security_Key;
    EditText vCorporation_Country,vCorporation_State,vCorporation_District;
    Button  vCorporation_Sign_Up;
    TextView vCorporation_Login_Screen,vCorporation_Register_As_Citizen;
    DatabaseReference databaseReference;
    FirebaseAuth fauth;
    int backButtonCount = 0;
    Toolbar vCorporation_Sign_Up_Page_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up__corporation);

        vCorporation_User_Name = (EditText) findViewById(R.id.Corporation_User_Name);
        vCorporation_Phone_Number = (EditText) findViewById(R.id.Corporation_Phone_Number);
        vCorporation_Email = (EditText) findViewById(R.id.Corporation_Email);
        vCorporation_Password = (EditText) findViewById(R.id.Corporation_Password);
        vCorporation_Security_Key = (EditText) findViewById(R.id.Corporation_Security_Key);
        vCorporation_Country = (EditText) findViewById(R.id.Corporation_Country);
        vCorporation_State = (EditText) findViewById(R.id.Corporation_State);
        vCorporation_District = (EditText) findViewById(R.id.Corporation_District);
        vCorporation_Sign_Up = (Button) findViewById(R.id.Corporation_Sign_Up);
        vCorporation_Login_Screen = (TextView) findViewById(R.id.Corporation_Login_Screen);
        vCorporation_Register_As_Citizen = (TextView) findViewById(R.id.Corporation_Register_As_Citizen);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        fauth = FirebaseAuth.getInstance();
        vCorporation_Sign_Up_Page_bar = (Toolbar) findViewById(R.id.Corporation_Sign_Up_Page_bar);
        setSupportActionBar(vCorporation_Sign_Up_Page_bar);
        getSupportActionBar().setTitle("Corporation Sign Up");

        vCorporation_Login_Screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_Up_Corporation.this,Login.class));
                finish();
            }
        });

        vCorporation_Register_As_Citizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_Up_Corporation.this,Sign_Up.class));
                finish();
            }
        });

        vCorporation_Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = vCorporation_User_Name.getText().toString();
                String phone_number = vCorporation_Phone_Number.getText().toString();
                String email = vCorporation_Email.getText().toString();
                String password = vCorporation_Password.getText().toString();
                String security_key = vCorporation_Security_Key.getText().toString();
                final String country = vCorporation_Country.getText().toString();
                String state = vCorporation_State.getText().toString();
                String district = vCorporation_District.getText().toString();

                Location l1 = new Location(
                        country,
                        state,
                        district
                );

                final Corporation  c1 = new Corporation(
                        user_name,
                        email,
                        phone_number,
                        password,
                        security_key,
                        l1
                );

                fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseDatabase.getInstance().getReference("Corporation")
                                    .child(fauth.getInstance().getCurrentUser().getUid())
                                    .setValue(c1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    Toast.makeText(Sign_Up_Corporation.this,"Registered Succesfully",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Sign_Up_Corporation.this,Login.class));
                                    finish();
                                }

                            });
                        }
                        else{
                            Toast.makeText(Sign_Up_Corporation.this,"Error!" + task.getException(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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
