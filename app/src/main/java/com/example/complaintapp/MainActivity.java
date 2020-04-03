package com.example.complaintapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth fauth;
    int backButtonCount = 0;
    Toolbar vMain_Activity_Toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vMain_Activity_Toolbar = (Toolbar) findViewById(R.id.Main_Activity_Toolbar);

        setSupportActionBar(vMain_Activity_Toolbar);
        getSupportActionBar().setTitle("Complaint App");


        fauth = FirebaseAuth.getInstance();
        if(fauth.getCurrentUser() == null){
            Toast.makeText(MainActivity.this,"Please First Login",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,Login.class));
        }
        else{
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Citizen").child(uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        startActivity(new Intent(MainActivity.this,Citizen_home.class));
                        finish();
                    }
                    else{
                        startActivity(new Intent(MainActivity.this,Corporation_home.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


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
