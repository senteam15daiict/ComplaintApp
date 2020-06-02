package com.example.complaintapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Citizen_History_Pending_Fragment extends Fragment {

    View Pending_Complaints_View;
    RecyclerView vCitizen_History_Pending_Recycler_View;
    DatabaseReference databaseReference;
    FirebaseAuth fauth;
    String Citizen_Id;
    List<Complaint> Complaint_List = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    Complaint_Adapter complaint_adapter;

    public Citizen_History_Pending_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Pending_Complaints_View = inflater.inflate(R.layout.fragment_citizen__history__pending_, container, false);

        vCitizen_History_Pending_Recycler_View = (RecyclerView) Pending_Complaints_View.findViewById(R.id.Citizen_History_Pending_Recycler_View);
        vCitizen_History_Pending_Recycler_View.setLayoutManager(new LinearLayoutManager(getContext()));

        fauth = FirebaseAuth.getInstance();
        Citizen_Id = fauth.getCurrentUser().getUid();
        complaint_adapter = new Complaint_Adapter(Complaint_List,"Pending","Citizen");
        linearLayoutManager = new LinearLayoutManager(getContext());
        vCitizen_History_Pending_Recycler_View.setLayoutManager(linearLayoutManager);
        vCitizen_History_Pending_Recycler_View.setAdapter(complaint_adapter);
        return Pending_Complaints_View;
    }

    @Override
    public void onStart() {
        super.onStart();
        Complaint_List.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Sender_Pending").child(Citizen_Id)
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
    }


}
