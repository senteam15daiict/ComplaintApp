package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.Objects;

public class Complaint_Pending_Full extends AppCompatActivity {

    ImageView vComplaint_Pending_Full_Complaint_Image,vComplaint_Pending_Full_Agree,vComplaint_Pending_Full_Disagree;
    CircleImageView vComplaint_Pending_Full_Profile_Image,vComplaint_Pending_Full_Complaint_Type_Image;
    DatabaseReference databaseReference;
    TextView vComplaint_Pending_Full_User_name,vComplaint_Pending_Full_Time,vComplaint_Pending_Full_Complaint_Type;
    TextView vComplaint_Pending_Full_Address,vComplaint_Pending_Full_Description;
    TextView vComplaint_Pending_Full_Agree_Count,vComplaint_Pending_Full_Disagree_Count;
    Button vChange_To_On_The_Job;
    String address;
    FirebaseAuth firebaseAuth;
    String User_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint__pending__full);

        vComplaint_Pending_Full_User_name = (TextView) findViewById(R.id.Complaint_Pending_Full_User_name);
        vComplaint_Pending_Full_Address = (TextView) findViewById(R.id.Complaint_Pending_Full_Address);
        vComplaint_Pending_Full_Time = (TextView) findViewById(R.id.Complaint_Pending_Full_Time);
        vComplaint_Pending_Full_Complaint_Type = (TextView) findViewById(R.id.Complaint_Pending_Full_Complaint_Type);
        vComplaint_Pending_Full_Agree_Count = (TextView) findViewById(R.id.Complaint_Pending_Full_Agree_Count);
        vComplaint_Pending_Full_Disagree_Count = (TextView) findViewById(R.id.Complaint_Pending_Full_Disagree_Count);
        vComplaint_Pending_Full_Description = (TextView) findViewById(R.id.Complaint_Pending_Full_Description);
        vComplaint_Pending_Full_Complaint_Image = (ImageView) findViewById(R.id.Complaint_Pending_Full_Complaint_Image);
        vComplaint_Pending_Full_Agree = (ImageView) findViewById(R.id.Complaint_Pending_Full_Agree);
        vComplaint_Pending_Full_Disagree = (ImageView) findViewById(R.id.Complaint_Pending_Full_Disagree);
        vComplaint_Pending_Full_Profile_Image = (CircleImageView) findViewById(R.id.Complaint_Pending_Full_Profile_Image);
        vComplaint_Pending_Full_Complaint_Type_Image = (CircleImageView) findViewById(R.id.Complaint_Pending_Full_Complaint_Type_Image);
        vChange_To_On_The_Job = (Button) findViewById(R.id.Change_To_On_The_Job);

        firebaseAuth = FirebaseAuth.getInstance();
        User_Id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String Citizen_User_Id = Objects.requireNonNull(bundle.get("Citizen_User_Id")).toString();
        final String Corporation_User_Id = Objects.requireNonNull(bundle.get("Corporation_User_Id")).toString();
        final String Complaint_Id = Objects.requireNonNull(bundle.get("Complaint_Id")).toString();
        final String User_Type = Objects.requireNonNull(bundle.get("User_Type")).toString();

        /*Log.d("Citizen_User_Id = ",Citizen_User_Id);
        Log.d("Corporation_User_Id = ",Corporation_User_Id);
        Log.d("Complaint_Id = ",Complaint_Id);
        Log.d("User_Type = ",User_Type);*/


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Citizen").child(Citizen_User_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String url = Objects.requireNonNull(dataSnapshot.child("Profile_Image_Url").getValue()).toString();
                            if(!url.equals("")){
                                Picasso.get().load(url).into(vComplaint_Pending_Full_Profile_Image);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Sender_Pending").child(Citizen_User_Id).child(Corporation_User_Id).child(Complaint_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            Complaint c1 = dataSnapshot.getValue(Complaint.class);
                            assert c1 != null;
                            int x1 = getResId("ic_" +c1.Type,R.drawable.class);
                            if(x1 != -1){
                                vComplaint_Pending_Full_Complaint_Type_Image.setImageResource(x1);
                            }
                            vComplaint_Pending_Full_User_name.setText(c1.Citizen_User_Name);
                            vComplaint_Pending_Full_Address.setText(c1.Address);
                            vComplaint_Pending_Full_Time.setText(c1.date);
                            vComplaint_Pending_Full_Complaint_Type.setText(c1.Type);
                            vComplaint_Pending_Full_Description.setText(c1.Description);
                            address = vComplaint_Pending_Full_Address.getText().toString();

                            if(!c1.Image_Url.equals("")){
                                Picasso.get().load(c1.Image_Url).into(vComplaint_Pending_Full_Complaint_Image);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        setDataAD(Complaint_Id);

        vComplaint_Pending_Full_Agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Agreement").child("Disagree").child(Complaint_Id).child(User_Id).removeValue();
                databaseReference.child("Agreement").child("Agree").child(Complaint_Id).child(User_Id).setValue("1");
            }
        });

        vComplaint_Pending_Full_Disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Agreement").child("Agree").child(Complaint_Id).child(User_Id).removeValue();
                databaseReference.child("Agreement").child("Disagree").child(Complaint_Id).child(User_Id).setValue("1");
            }
        });

        vComplaint_Pending_Full_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] lat = new String[1];
                final String[] lon = new String[1];
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Complaints_Sender_Pending").child(Citizen_User_Id).child(Corporation_User_Id).child(Complaint_Id)
                        .addValueEventListener(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    lat[0] = Objects.requireNonNull(dataSnapshot.child("lat").getValue()).toString();
                                    lon[0] = Objects.requireNonNull(dataSnapshot.child("lon").getValue()).toString();

                                    vComplaint_Pending_Full_Address.setText(lat[0] + "_" + lon[0]);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                String x = vComplaint_Pending_Full_Address.getText().toString();
                lat[0] = "";
                lon[0] = "";
                int f = 0;
                for(int i=0;i<x.length();i++){
                    if(f == 1){
                        lon[0] += x.charAt(i);
                    }
                    else{
                        if(x.charAt(i) == '_'){
                            f = 1;
                        }
                        else{
                            lat[0] += x.charAt(i);
                        }
                    }
                }
                if(!lon[0].equals("")){
                    Intent intent = new Intent(Complaint_Pending_Full.this,MapsActivity2.class);
                    final Bundle b1 = new Bundle();
                    b1.putString("Lat", lat[0]);
                    b1.putString("Lon", lon[0]);
                    intent.putExtras(b1);
                    startActivity(intent);
                }


            }
        });

        vChange_To_On_The_Job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User_Type.equals("Citizen")){
                    Toast.makeText(Complaint_Pending_Full.this,"Your are not Authorized to Change the Status of the complaint",Toast.LENGTH_LONG).show();
                }
                else{
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Complaints_Sender_Pending").child(Citizen_User_Id).child(Corporation_User_Id).child(Complaint_Id)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Complaint c1 = dataSnapshot.getValue(Complaint.class);
                                        assert c1 != null;
                                        c1.Status = "On_The_Job";
                                        databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("Complaints_Sender_On_The_Job").child(Citizen_User_Id).child(Corporation_User_Id)
                                                .child(Complaint_Id).setValue(c1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    databaseReference.child("Complaints_Sender_Pending").child(Citizen_User_Id).child(Corporation_User_Id).child(Complaint_Id)
                            .removeValue();
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Complaints_Receiver_Pending").child(Corporation_User_Id).child(Citizen_User_Id).child(Complaint_Id)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Complaint c1 = dataSnapshot.getValue(Complaint.class);
                                        assert c1 != null;
                                        c1.Status = "On_The_Job";
                                        databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("Complaints_Receiver_On_The_Job").child(Corporation_User_Id).child(Citizen_User_Id)
                                                .child(Complaint_Id).setValue(c1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    databaseReference.child("Complaints_Receiver_Pending").child(Corporation_User_Id).child(Citizen_User_Id).child(Complaint_Id)
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Complaint_Pending_Full.this,"Updated Successfully",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(Complaint_Pending_Full.this,"Error! " + Objects.requireNonNull(task.getException()).toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Intent intent = new Intent(Complaint_Pending_Full.this,Complaint_On_The_Job_Full.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void setDataAD(String Complaint_Id) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Agreement").child("Agree").child(Complaint_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    long size = dataSnapshot.getChildrenCount();
                    vComplaint_Pending_Full_Agree_Count.setText(String.valueOf(size));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Agreement").child("Disagree").child(Complaint_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    long size = dataSnapshot.getChildrenCount();
                    vComplaint_Pending_Full_Disagree_Count.setText(String.valueOf(size));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
