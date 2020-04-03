package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class Corporation_home extends AppCompatActivity {

    int backButtonCount = 0;
    Toolbar vCorporation_Home_Page_bar;
    //RecyclerView vCorporation_Home_Recycle_View;
    DatabaseReference databaseReference;
    FirebaseAuth fauth;
    String Corporation_Id;
    //List<Complaint> Complaint_List = new ArrayList<>();
    //LinearLayoutManager linearLayoutManager;
    //Complaint_Adapter complaint_adapter;
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
        getSupportActionBar().setTitle("Complaints");
        vCorporation_Home_View_Pager = (ViewPager) findViewById(R.id.Corporation_Home_View_Pager);
        vCorporation_Home_Page_Tab = (TabLayout) findViewById(R.id.Corporation_Home_Page_Tab);
        my_Tabs_Accessor_Adapter = new Tabs_Accessor_Adapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        my_Tabs_Accessor_Adapter.User_Type = "Corporation";
        vCorporation_Home_View_Pager.setAdapter(my_Tabs_Accessor_Adapter);

        //complaint_adapter = new Complaint_Adapter(Complaint_List,"Pending","Corporation");
        fauth = FirebaseAuth.getInstance();
        Corporation_Id = fauth.getCurrentUser().getUid();

        vCorporation_Home_Page_Tab = (TabLayout) findViewById(R.id.Corporation_Home_Page_Tab);
        vCorporation_Home_Page_Tab.setupWithViewPager(vCorporation_Home_View_Pager);

        /*vCorporation_Home_Recycle_View = (RecyclerView) findViewById(R.id.Corporation_Home_Recycle_View);
        linearLayoutManager = new LinearLayoutManager(this);
        vCorporation_Home_Recycle_View.setLayoutManager(linearLayoutManager);
        vCorporation_Home_Recycle_View.setAdapter(complaint_adapter);*/

        bottomNavigationView.setSelectedItemId(R.id.Corporation_View_complaint);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.Corporation_View_complaint:
                        return true;

                    case R.id.Corporation_Notifications:
                        startActivity(new Intent(getApplicationContext(),Corporation_Notification.class));
                        overridePendingTransition(0,0);
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

    /*@Override
    protected void onStart() {
        super.onStart();



        /*databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Receiver_Pending").child(Corporation_Id)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            Complaint complaint = dataSnapshot1.getValue(Complaint.class);
                            Complaint_List.add(complaint);
                            complaint_adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                /*.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            dataSnapshot1.getRef().addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Complaint complaint = dataSnapshot.getValue(Complaint.class);
                                    Complaint_List.add(complaint);
                                    complaint_adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }*/
        /*databaseReference = FirebaseDatabase.getInstance().getReference().child("Complaints_Receiver").child(Corporation_Id);
        FirebaseRecyclerOptions<Complaint> options =
                new FirebaseRecyclerOptions.Builder<Complaint>()
                .setQuery(databaseReference,Complaint.class)
                .build();


        FirebaseRecyclerAdapter<Complaint,Get_Complaints_View_Holder> adapter =
                new FirebaseRecyclerAdapter<Complaint, Get_Complaints_View_Holder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Get_Complaints_View_Holder holder, int position, @NonNull Complaint model) {
                        holder.vResolved_Complaint_layout_User_name.setText(model.Citizen_User_Name);
                        holder.vResolved_Complaint_layout_Address.setText(model.Address);
                    }

                    @NonNull
                    @Override
                    public Get_Complaints_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resolved_complaint_layout,parent,false);
                        Get_Complaints_View_Holder view_holder = new Get_Complaints_View_Holder(view);
                        return view_holder;
                    }
                };
        vCorporation_Home_Recycle_View.setAdapter(adapter);
        adapter.startListening();
    }

    public static class Get_Complaints_View_Holder extends RecyclerView.ViewHolder
    {
        TextView vResolved_Complaint_layout_User_name,vResolved_Complaint_layout_Address;

        public Get_Complaints_View_Holder(@NonNull View itemView) {
            super(itemView);
            vResolved_Complaint_layout_User_name = itemView.findViewById(R.id.Resolved_Complaint_layout_User_name);
            vResolved_Complaint_layout_Address = itemView.findViewById(R.id.Resolved_Complaint_layout_Address);

        }

    }*/

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
