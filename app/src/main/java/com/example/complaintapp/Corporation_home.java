package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import notification.Token;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class Corporation_home extends AppCompatActivity {

    int backButtonCount = 0;
    Toolbar vCorporation_Home_Page_bar;
    DatabaseReference databaseReference;
    FirebaseAuth fauth;
    String Corporation_Id;
    ViewPager vCorporation_Home_View_Pager;
    TabLayout vCorporation_Home_Page_Tab;
    Tabs_Accessor_Adapter my_Tabs_Accessor_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        vCorporation_Home_Page_bar = (Toolbar) findViewById(R.id.Corporation_Home_Page_bar);
        setSupportActionBar(vCorporation_Home_Page_bar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Complaints");
        vCorporation_Home_View_Pager = (ViewPager) findViewById(R.id.Corporation_Home_View_Pager);
        vCorporation_Home_Page_Tab = (TabLayout) findViewById(R.id.Corporation_Home_Page_Tab);
        my_Tabs_Accessor_Adapter = new Tabs_Accessor_Adapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        my_Tabs_Accessor_Adapter.User_Type = "Corporation";
        vCorporation_Home_View_Pager.setAdapter(my_Tabs_Accessor_Adapter);

        fauth = FirebaseAuth.getInstance();
        Corporation_Id = Objects.requireNonNull(fauth.getCurrentUser()).getUid();

        vCorporation_Home_Page_Tab = (TabLayout) findViewById(R.id.Corporation_Home_Page_Tab);
        vCorporation_Home_Page_Tab.setupWithViewPager(vCorporation_Home_View_Pager);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            //Log.d("7890","((((((())))))))");
                            return;
                        }

                        String refreshToken = Objects.requireNonNull(task.getResult()).getToken();
                        updateToken(refreshToken);
                    }
                });


        bottomNavigationView.setSelectedItemId(R.id.Corporation_View_complaint);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.Corporation_View_complaint:
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

    private void updateToken(String token){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tokens");
        notification.Token token1 = new Token(token);
        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(token1);
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
