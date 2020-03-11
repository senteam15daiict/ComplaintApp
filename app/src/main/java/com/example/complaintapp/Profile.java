package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    Button vDelete_Account,vLogout;
    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        vDelete_Account = (Button) findViewById(R.id.Delete_Account);
        vLogout = (Button) findViewById(R.id.Logout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.Profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Post_complaint:
                        startActivity(new Intent(getApplicationContext(),Citizen_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.History:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Near_by:
                        startActivity(new Intent(getApplicationContext(),Near_by.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Notifications:
                        startActivity(new Intent(getApplicationContext(),Notification.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Profile:
                        return true;
                }
                return false;
            }
        });

        vDelete_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Profile.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your account from the system and you wont be able to access the app.");

                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        assert firebaseUser != null;
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(Profile.this,"1 -- " + Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                        String uid = firebaseUser.getUid();
                        //Toast.makeText(citizen_home.this,key,Toast.LENGTH_LONG).show();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User_citizen").child(uid);
                        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Profile.this,"Account Deleted",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Profile.this,MainActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(Profile.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog  =  dialog.create();
                alertDialog.show();
            }
        });

        vLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
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
