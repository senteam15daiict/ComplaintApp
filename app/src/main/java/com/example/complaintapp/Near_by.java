package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class Near_by extends AppCompatActivity {

    int backButtonCount = 0,PlacePickerRequestCode = 2;
    Toolbar vNear_by_Page_Bar;
    Spinner vCountry_Spinner,vState_Spinner,vDistrict_Spinner,vComplaint_Type_Spinner,vRadius_Location_Spinner;
    Button vLocation;
    ArrayList<String> Country_List,State_List,District_List,Complaint_Type_List,Radius_Location_List;
    ArrayAdapter<String> Country_Adapter,State_Adapter,District_Adapter,Complaint_Type_Adapter,Radius_Location_Adapter;
    DatabaseReference databaseReference,databaseReference1;
    String Country,State,District,lat,lon,Complaint_Type,Radius;
    HashMap<String , Double> hashMap;

    RecyclerView vNear_by_RecyclerView;
    List<Complaint> Pending_List,On_The_Job_List,Resolved_List;
    Complaint_Adapter Pending_Adapter,On_The_Job_Adapter,Resolved_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        vNear_by_Page_Bar = (Toolbar) findViewById(R.id.Near_by_Page_Bar);
        setSupportActionBar(vNear_by_Page_Bar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Near By");

        vNear_by_RecyclerView = (RecyclerView) findViewById(R.id.Near_by_RecyclerView);
        vNear_by_RecyclerView.setLayoutManager(new LinearLayoutManager(Near_by.this));
        lat = "nil";
        lon = "nil";
        hashMap = new HashMap<>();


        Pending_List = new ArrayList<>();
        On_The_Job_List = new ArrayList<>();
        Resolved_List = new ArrayList<>();
        Pending_Adapter = new Complaint_Adapter(Pending_List,"Pending","Citizen");
        On_The_Job_Adapter = new Complaint_Adapter(On_The_Job_List,"On_The_Job","Citizen");
        Resolved_Adapter = new Complaint_Adapter(Resolved_List,"Resolved","Citizen");

        bottomNavigationView.setSelectedItemId(R.id.Near_by);

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

    private void setLocationData() {
        final String[] country = new String[1];
        final String[] state = new String[1];
        final String district;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Corporation_Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Country_Adapter.clear();
                Country_Adapter.add("Country");
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Country_Adapter.add(dataSnapshot1.getKey());
                }
                Country_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        vCountry_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country[0] = parent.getItemAtPosition(position).toString();
                if(!country[0].equals("Country")){
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Corporation_Location").child(country[0]).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            State_Adapter.clear();
                            State_Adapter.add("State");
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                State_Adapter.add(dataSnapshot1.getKey());
                            }
                            State_Adapter.notifyDataSetChanged();

                            vState_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    state[0] = parent.getItemAtPosition(position).toString();
                                    if(!state[0].equals("State")){
                                        databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("Corporation_Location").child(country[0]).child(state[0]).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                District_Adapter.clear();
                                                District_Adapter.add("District");
                                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    District_Adapter.add(dataSnapshot1.getKey());
                                                }
                                                District_Adapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else{
                                        District_Adapter.clear();
                                        District_Adapter.add("District");
                                        District_Adapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    State_Adapter.clear();
                    State_Adapter.add("State");
                    State_Adapter.notifyDataSetChanged();
                    District_Adapter.clear();
                    District_Adapter.add("District");
                    District_Adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PlacePickerRequestCode){
            assert data != null;
            lat = data.getStringExtra("lat");
            lon = data.getStringExtra("long");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.near_by_set_data_button,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.Near_By_Near_Location){
            AlertDialog.Builder mbuilder = new AlertDialog.Builder(Near_by.this);
            @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.near_location_layout,null);
            mbuilder.setTitle("Set Data");
            vCountry_Spinner = mView.findViewById(R.id.Country_Spinner);
            vState_Spinner = mView.findViewById(R.id.State_Spinner);
            vDistrict_Spinner = mView.findViewById(R.id.District_Spinner);
            vComplaint_Type_Spinner = mView.findViewById(R.id.Complaint_Type_Spinner);
            vRadius_Location_Spinner = mView.findViewById(R.id.Radius_Location_Spinner);
            vLocation = mView.findViewById(R.id.Set_Location);

            Country_List = new ArrayList<>();
            State_List = new ArrayList<>();
            District_List = new ArrayList<>();
            Complaint_Type_List = new ArrayList<>();
            Radius_Location_List = new ArrayList<>();

            Country_Adapter = new ArrayAdapter<String>(Near_by.this,android.R.layout.simple_spinner_dropdown_item,Country_List);
            State_Adapter = new ArrayAdapter<String>(Near_by.this,android.R.layout.simple_spinner_dropdown_item,State_List);
            District_Adapter = new ArrayAdapter<String>(Near_by.this,android.R.layout.simple_spinner_dropdown_item,District_List);
            Complaint_Type_Adapter = new ArrayAdapter<String>(Near_by.this,android.R.layout.simple_spinner_dropdown_item,Complaint_Type_List);
            Radius_Location_Adapter = new ArrayAdapter<String>(Near_by.this,android.R.layout.simple_spinner_dropdown_item,Radius_Location_List);

            vCountry_Spinner.setAdapter(Country_Adapter);
            vState_Spinner.setAdapter(State_Adapter);
            vDistrict_Spinner.setAdapter(District_Adapter);
            vComplaint_Type_Spinner.setAdapter(Complaint_Type_Adapter);
            vRadius_Location_Spinner.setAdapter(Radius_Location_Adapter);

            Complaint_Type_Adapter.add("Complaint Type");
            Complaint_Type_Adapter.add("Pending");
            Complaint_Type_Adapter.add("On_The_Job");
            Complaint_Type_Adapter.add("Resolved");

            Radius_Location_Adapter.add("Radius Around Selected Location");
            Radius_Location_Adapter.add("100 meters");
            Radius_Location_Adapter.add("500 meters");
            Radius_Location_Adapter.add("1 KM");
            Radius_Location_Adapter.add("5 KM");
            Radius_Location_Adapter.add("10 KM");
            Radius_Location_Adapter.add("None");

            hashMap.put("100 meters",0.1);
            hashMap.put("500 meters",0.5);
            hashMap.put("1 KM",1.0);
            hashMap.put("5 KM",5.0);
            hashMap.put("10 KM",10.0);
            setLocationData();

            vLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Near_by.this,MapsActivity.class);
                    startActivityForResult(intent,PlacePickerRequestCode);
                }
            });

            mbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Country = vCountry_Spinner.getSelectedItem().toString();
                    State = vState_Spinner.getSelectedItem().toString();
                    District = vDistrict_Spinner.getSelectedItem().toString();
                    Complaint_Type = vComplaint_Type_Spinner.getSelectedItem().toString();
                    Radius = vRadius_Location_Spinner.getSelectedItem().toString();

                    if(Country.equals("Country") || State.equals("State") || District.equals("District") || Complaint_Type.equals("Complaint Type") || Radius.equals("Radius Around Selected Location") || lat.equals("nil") || lon.equals("nil")){
                        Toast.makeText(Near_by.this,"Please Enter Valid Data",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(Radius.equals("None")){
                        setRecyclerViewData(1);
                    }
                    else{
                        setRecyclerViewData(0);
                    }
                }
            });

            mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            mbuilder.setView(mView);
            AlertDialog dialog = mbuilder.create();
            dialog.show();
        }
        else if(item.getItemId() == R.id.Near_By_District){
            AlertDialog.Builder mbuilder = new AlertDialog.Builder(Near_by.this);
            View mView = getLayoutInflater().inflate(R.layout.district_layout,null);
            mbuilder.setTitle("Set Data");
            vCountry_Spinner = mView.findViewById(R.id.Country_Spinner);
            vState_Spinner = mView.findViewById(R.id.State_Spinner);
            vDistrict_Spinner = mView.findViewById(R.id.District_Spinner);
            vComplaint_Type_Spinner = mView.findViewById(R.id.Complaint_Type_Spinner);

            Country_List = new ArrayList<>();
            State_List = new ArrayList<>();
            District_List = new ArrayList<>();
            Complaint_Type_List = new ArrayList<>();

            Country_Adapter = new ArrayAdapter<String>(Near_by.this,android.R.layout.simple_spinner_dropdown_item,Country_List);
            State_Adapter = new ArrayAdapter<String>(Near_by.this,android.R.layout.simple_spinner_dropdown_item,State_List);
            District_Adapter = new ArrayAdapter<String>(Near_by.this,android.R.layout.simple_spinner_dropdown_item,District_List);
            Complaint_Type_Adapter = new ArrayAdapter<String>(Near_by.this,android.R.layout.simple_spinner_dropdown_item,Complaint_Type_List);

            vCountry_Spinner.setAdapter(Country_Adapter);
            vState_Spinner.setAdapter(State_Adapter);
            vDistrict_Spinner.setAdapter(District_Adapter);
            vComplaint_Type_Spinner.setAdapter(Complaint_Type_Adapter);

            Complaint_Type_Adapter.add("Complaint Type");
            Complaint_Type_Adapter.add("Pending");
            Complaint_Type_Adapter.add("On_The_Job");
            Complaint_Type_Adapter.add("Resolved");
            setLocationData();

            mbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Country = vCountry_Spinner.getSelectedItem().toString();
                    State = vState_Spinner.getSelectedItem().toString();
                    District = vDistrict_Spinner.getSelectedItem().toString();
                    Complaint_Type = vComplaint_Type_Spinner.getSelectedItem().toString();

                    if(Country.equals("Country") || State.equals("State") || District.equals("District") || Complaint_Type.equals("Complaint Type")){
                        Toast.makeText(Near_by.this,"Please Enter Valid Data",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setRecyclerViewData(1);
                }
            });

            mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            mbuilder.setView(mView);
            AlertDialog dialog = mbuilder.create();
            dialog.show();
        }
        return true;
    }

    private void setRecyclerViewData(int f) {
        if(f == 1) {
            if (Complaint_Type.equals("Pending")) {
                Pending_List.clear();
                vNear_by_RecyclerView.removeAllViews();
                vNear_by_RecyclerView.swapAdapter(Pending_Adapter,false);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference1 = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Corporation_Location").child(Country).child(State).child(District)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (!Objects.equals(dataSnapshot1.getKey(), "1")) {
                                        String Corporation_Id = dataSnapshot1.getKey();
                                        databaseReference1.child("Complaints_Receiver_Pending").child(Corporation_Id).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    Complaint complaint = dataSnapshot1.getValue(Complaint.class);
                                                    Pending_List.add(complaint);
                                                    Pending_Adapter.notifyDataSetChanged();
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
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
            else if(Complaint_Type.equals("On_The_Job")){
                On_The_Job_List.clear();
                vNear_by_RecyclerView.removeAllViews();
                vNear_by_RecyclerView.swapAdapter(On_The_Job_Adapter,false);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference1 = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Corporation_Location").child(Country).child(State).child(District)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (!Objects.equals(dataSnapshot1.getKey(), "1")) {
                                        String Corporation_Id = dataSnapshot1.getKey();
                                        databaseReference1.child("Complaints_Receiver_On_The_Job").child(Corporation_Id).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    Complaint complaint = dataSnapshot1.getValue(Complaint.class);
                                                    On_The_Job_List.add(complaint);
                                                    On_The_Job_Adapter.notifyDataSetChanged();
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
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
            else{
                Resolved_List.clear();
                vNear_by_RecyclerView.removeAllViews();
                vNear_by_RecyclerView.swapAdapter(Resolved_Adapter,false);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference1 = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Corporation_Location").child(Country).child(State).child(District)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (!Objects.equals(dataSnapshot1.getKey(), "1")) {
                                        String Corporation_Id = dataSnapshot1.getKey();
                                        databaseReference1.child("Complaints_Receiver_Resolved").child(Corporation_Id).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    Complaint complaint = dataSnapshot1.getValue(Complaint.class);
                                                    Resolved_List.add(complaint);
                                                    Resolved_Adapter.notifyDataSetChanged();
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
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        }
        else{
            double lat1 = Double.parseDouble(lat);
            double lon1 = Double.parseDouble(lon);

            lat1 = Math.toRadians(lat1);
            lon1 = Math.toRadians(lon1);

            final Double distanceLimit = hashMap.get(Radius);

            if (Complaint_Type.equals("Pending")) {
                Pending_List.clear();
                vNear_by_RecyclerView.removeAllViews();
                vNear_by_RecyclerView.swapAdapter(Pending_Adapter,false);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference1 = FirebaseDatabase.getInstance().getReference();
                final double finalLat = lat1;
                final double finalLon = lon1;
                databaseReference.child("Corporation_Location").child(Country).child(State).child(District)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (!Objects.equals(dataSnapshot1.getKey(), "1")) {
                                        String Corporation_Id = dataSnapshot1.getKey();
                                        assert Corporation_Id != null;
                                        databaseReference1.child("Complaints_Receiver_Pending").child(Corporation_Id).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    Complaint complaint = dataSnapshot1.getValue(Complaint.class);
                                                    assert complaint != null;
                                                    double lat2 = Double.parseDouble(complaint.lat);
                                                    double lon2 = Double.parseDouble(complaint.lon);

                                                    lat2 = Math.toRadians(lat2);
                                                    lon2 = Math.toRadians(lon2);

                                                    double dlat = lat2 - finalLat;
                                                    double dlon = lon2 - finalLon;

                                                    double a =  Math.pow(Math.sin(dlat / 2), 2)
                                                            + Math.cos(finalLat) * Math.cos(lat2)
                                                            * Math.pow(Math.sin(dlon / 2),2);

                                                    double c = 2 * Math.asin(Math.sqrt(a));

                                                    double r = 6371;
                                                    double distance = c*r;
                                                    //Log.d("Distance :",String.valueOf(distance));
                                                    if(distance <= distanceLimit){
                                                        Pending_List.add(complaint);
                                                        Pending_Adapter.notifyDataSetChanged();
                                                        //Log.d("added :","-----------");
                                                    }
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
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
            else if(Complaint_Type.equals("On_The_Job")){
                On_The_Job_List.clear();
                vNear_by_RecyclerView.removeAllViews();
                vNear_by_RecyclerView.swapAdapter(On_The_Job_Adapter,false);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference1 = FirebaseDatabase.getInstance().getReference();
                final double finalLat = lat1;
                final double finalLon = lon1;
                databaseReference.child("Corporation_Location").child(Country).child(State).child(District)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (!Objects.equals(dataSnapshot1.getKey(), "1")) {
                                        String Corporation_Id = dataSnapshot1.getKey();
                                        assert Corporation_Id != null;
                                        databaseReference1.child("Complaints_Receiver_On_The_Job").child(Corporation_Id).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    Complaint complaint = dataSnapshot1.getValue(Complaint.class);
                                                    assert complaint != null;
                                                    double lat2 = Double.parseDouble(complaint.lat);
                                                    double lon2 = Double.parseDouble(complaint.lon);

                                                    lat2 = Math.toRadians(lat2);
                                                    lon2 = Math.toRadians(lon2);

                                                    double dlat = lat2 - finalLat;
                                                    double dlon = lon2 - finalLon;

                                                    double a =  Math.pow(Math.sin(dlat / 2), 2)
                                                            + Math.cos(finalLat) * Math.cos(lat2)
                                                            * Math.pow(Math.sin(dlon / 2),2);

                                                    double c = 2 * Math.asin(Math.sqrt(a));

                                                    double r = 6371;
                                                    double distance = c*r;

                                                    if(distance <= distanceLimit){
                                                        On_The_Job_List.add(complaint);
                                                        On_The_Job_Adapter.notifyDataSetChanged();
                                                    }
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
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
            else{
                Resolved_List.clear();
                vNear_by_RecyclerView.removeAllViews();
                vNear_by_RecyclerView.swapAdapter(Resolved_Adapter,false);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference1 = FirebaseDatabase.getInstance().getReference();
                final double finalLat = lat1;
                final double finalLon = lon1;
                databaseReference.child("Corporation_Location").child(Country).child(State).child(District)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (!Objects.equals(dataSnapshot1.getKey(), "1")) {
                                        String Corporation_Id = dataSnapshot1.getKey();
                                        assert Corporation_Id != null;
                                        databaseReference1.child("Complaints_Receiver_Resolved").child(Corporation_Id).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    Complaint complaint = dataSnapshot1.getValue(Complaint.class);
                                                    assert complaint != null;
                                                    double lat2 = Double.parseDouble(complaint.lat);
                                                    double lon2 = Double.parseDouble(complaint.lon);

                                                    lat2 = Math.toRadians(lat2);
                                                    lon2 = Math.toRadians(lon2);

                                                    double dlat = lat2 - finalLat;
                                                    double dlon = lon2 - finalLon;

                                                    double a =  Math.pow(Math.sin(dlat / 2), 2)
                                                            + Math.cos(finalLat) * Math.cos(lat2)
                                                            * Math.pow(Math.sin(dlon / 2),2);

                                                    double c = 2 * Math.asin(Math.sqrt(a));

                                                    double r = 6371;
                                                    double distance = c*r;

                                                    if(distance <= distanceLimit){
                                                        Resolved_List.add(complaint);
                                                        Resolved_Adapter.notifyDataSetChanged();
                                                    }
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
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
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
