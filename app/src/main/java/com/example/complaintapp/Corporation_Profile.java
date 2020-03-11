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
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Corporation_Profile extends AppCompatActivity {

    int backButtonCount = 0;
    Button vCorporation_Logout,vCorporation_Delete_Account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation__profile);

        vCorporation_Delete_Account = (Button) findViewById(R.id.Corporation_Delete_Account);
        vCorporation_Logout = (Button) findViewById(R.id.Corporation_Logout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.Corporation_Profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.Corporation_View_complaint:
                        startActivity(new Intent(getApplicationContext(),Corporation_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Corporation_Notifications:
                        startActivity(new Intent(getApplicationContext(),Corporation_Notification.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Corporation_Profile:
                        return true;
                }
                return false;
            }
        });

        vCorporation_Delete_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Corporation_Profile.this);
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
                                    Toast.makeText(Corporation_Profile.this,"1 -- " + Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                        String uid = firebaseUser.getUid();
                        //Toast.makeText(citizen_home.this,key,Toast.LENGTH_LONG).show();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Corporation").child(uid);
                        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Corporation_Profile.this,"Account Deleted",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Corporation_Profile.this,MainActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(Corporation_Profile.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
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

        vCorporation_Logout.setOnClickListener(new View.OnClickListener() {
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
