package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Corporation_Notification extends AppCompatActivity {

    int backButtonCount = 0;
    Toolbar vCorporation_Notifications_Page_Bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation__notification);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        vCorporation_Notifications_Page_Bar = findViewById(R.id.Corporation_Notifications_Page_Bar);
        setSupportActionBar(vCorporation_Notifications_Page_Bar);
        getSupportActionBar().setTitle("Notifications");

        bottomNavigationView.setSelectedItemId(R.id.Corporation_Notifications);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.Corporation_View_complaint:
                        startActivity(new Intent(getApplicationContext(),Corporation_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Corporation_Notifications:
                        return true;

                    case R.id.Corporation_Profile:
                        startActivity(new Intent(getApplicationContext(),Corporation_Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
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
