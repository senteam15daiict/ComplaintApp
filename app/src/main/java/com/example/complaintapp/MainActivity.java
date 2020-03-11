package com.example.complaintapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth fauth;
    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fauth = FirebaseAuth.getInstance();
        if(fauth.getCurrentUser() == null){
            Toast.makeText(MainActivity.this,"Please First Login",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,Login.class));
        }
        else{
            startActivity(new Intent(MainActivity.this,Citizen_home.class));
        }
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
