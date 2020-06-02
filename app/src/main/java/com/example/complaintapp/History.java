package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class History extends AppCompatActivity {

    int backButtonCount = 0;
    Toolbar vCitizen_History_Page_Bar;
    ViewPager vCitizen_History_View_Pager;
    TabLayout vCitizen_History_Page_Tab;
    Tabs_Accessor_Adapter my_Tabs_Accessor_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        vCitizen_History_Page_Bar = (Toolbar) findViewById(R.id.Citizen_History_Page_Bar);
        setSupportActionBar(vCitizen_History_Page_Bar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("History");

        vCitizen_History_View_Pager = (ViewPager) findViewById(R.id.Citizen_History_View_Pager);
        vCitizen_History_Page_Tab = (TabLayout) findViewById(R.id.Citizen_History_Page_Tab);
        my_Tabs_Accessor_Adapter = new Tabs_Accessor_Adapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        my_Tabs_Accessor_Adapter.User_Type = "Citizen";
        vCitizen_History_View_Pager.setAdapter(my_Tabs_Accessor_Adapter);


        vCitizen_History_Page_Tab = (TabLayout) findViewById(R.id.Citizen_History_Page_Tab);
        vCitizen_History_Page_Tab.setupWithViewPager(vCitizen_History_View_Pager);

        bottomNavigationView.setSelectedItemId(R.id.History);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Post_complaint:
                        startActivity(new Intent(getApplicationContext(),Citizen_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.History:
                        return true;

                    case R.id.Near_by:
                        startActivity(new Intent(getApplicationContext(),Near_by.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
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
