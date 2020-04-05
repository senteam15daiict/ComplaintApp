package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Field;

public class Complaint_Resolved_Full extends AppCompatActivity {

    TextView vComplaint_Resolved_Full_User_Name,vComplaint_Resolved_Full_Time,vComplaint_Resolved_Full_Complaint_Type;
    TextView vComplaint_Resolved_Full_Address,vComplaint_Resolved_Full_Description;
    TextView vComplaint_Resolved_Full_Feedback_Best_Count,vComplaint_Resolved_Full_Feedback_Normal_Count,vComplaint_Resolved_Full_Feedback_Worst_Count;
    TextView vComplaint_Resolved_Full_Corporation_User_Name,vComplaint_Resolved_Full_Corporation_Time,vComplaint_Resolved_Full_Corporation_Response_Description;
    ImageView vComplaint_Resolved_Full_Complaint_Image,vComplaint_Resolved_Full_Corporation_Response_Image;
    CircleImageView vComplaint_Resolved_Full_Profile_Image,vComplaint_Resolved_Full_Complaint_Type_Image,vComplaint_Resolved_Full_Corporation_Profile_Image;
    LinearLayout vBlack_Line_1,vBlack_Line_2;
    RelativeLayout vResponse_Title;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Response r1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint__resolved__full);

        vComplaint_Resolved_Full_User_Name = (TextView) findViewById(R.id.Complaint_Resolved_Full_User_Name);
        vComplaint_Resolved_Full_Time = (TextView) findViewById(R.id.Complaint_Resolved_Full_Time);
        vComplaint_Resolved_Full_Complaint_Type = (TextView) findViewById(R.id.Complaint_Resolved_Full_Complaint_Type);
        vComplaint_Resolved_Full_Address = (TextView) findViewById(R.id.Complaint_Resolved_Full_Address);
        vComplaint_Resolved_Full_Description = (TextView) findViewById(R.id.Complaint_Resolved_Full_Description);
        vComplaint_Resolved_Full_Feedback_Best_Count = (TextView) findViewById(R.id.Complaint_Resolved_Full_Feedback_Best_Count);
        vComplaint_Resolved_Full_Feedback_Normal_Count = (TextView) findViewById(R.id.Complaint_Resolved_Full_Feedback_Normal_Count);
        vComplaint_Resolved_Full_Feedback_Worst_Count = (TextView) findViewById(R.id.Complaint_Resolved_Full_Feedback_Worst_Count);
        vComplaint_Resolved_Full_Corporation_User_Name = (TextView) findViewById(R.id.Complaint_Resolved_Full_Corporation_User_Name);
        vComplaint_Resolved_Full_Corporation_Time = (TextView) findViewById(R.id.Complaint_Resolved_Full_Corporation_Time);
        vComplaint_Resolved_Full_Corporation_Response_Description = (TextView) findViewById(R.id.Complaint_Resolved_Full_Corporation_Response_Description);
        vComplaint_Resolved_Full_Complaint_Image = (ImageView) findViewById(R.id.Complaint_Resolved_Full_Complaint_Image);
        vComplaint_Resolved_Full_Corporation_Response_Image = (ImageView) findViewById(R.id.Complaint_Resolved_Full_Corporation_Response_Image);
        vComplaint_Resolved_Full_Profile_Image = (CircleImageView) findViewById(R.id.Complaint_Resolved_Full_Profile_Image);
        vComplaint_Resolved_Full_Complaint_Type_Image = (CircleImageView) findViewById(R.id.Complaint_Resolved_Full_Complaint_Type_Image);
        vComplaint_Resolved_Full_Corporation_Profile_Image = (CircleImageView) findViewById(R.id.Complaint_Resolved_Full_Corporation_Profile_Image);
        vBlack_Line_1 = (LinearLayout) findViewById(R.id.Black_Line_1);
        vBlack_Line_2 = (LinearLayout) findViewById(R.id.Black_Line_2);
        vResponse_Title = (RelativeLayout) findViewById(R.id.Response_Title);
        r1 = new Response();

        Bundle bundle = getIntent().getExtras();
        //Toast.makeText(Complaint_Pending_Full.this,"hii",Toast.LENGTH_LONG).show();
        assert bundle != null;
        final String Citizen_User_Id = bundle.get("Citizen_User_Id").toString();
        final String Corporation_User_Id = bundle.get("Corporation_User_Id").toString();
        final String Complaint_Id = bundle.get("Complaint_Id").toString();

        r1.Citizen_User_Id = Citizen_User_Id;
        r1.Corporation_User_Id = Corporation_User_Id;
        r1.Complaint_Id = Complaint_Id;

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Complaint_Responses").child(Corporation_User_Id).child(Citizen_User_Id).child(Complaint_Id)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    getResponse();
                }
                else{
                    setInvisible();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Citizen").child(Citizen_User_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String url = dataSnapshot.child("Profile_Image_Url").toString();
                            if(!url.equals("")){
                                Picasso.get().load(url).into(vComplaint_Resolved_Full_Profile_Image);
                                //Toast.makeText(Complaint_Pending_Full.this,"hello",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        databaseReference.child("Complaints_Sender_Resolved").child(Citizen_User_Id).child(Corporation_User_Id).child(Complaint_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Complaint c1 = dataSnapshot.getValue(Complaint.class);

                            int x1 = getResId("ic_" +c1.Type,R.drawable.class);
                            if(x1 != -1){
                                vComplaint_Resolved_Full_Complaint_Type_Image.setImageResource(x1);
                            }
                            vComplaint_Resolved_Full_User_Name.setText(c1.Citizen_User_Name);
                            vComplaint_Resolved_Full_Address.setText(c1.Address);
                            vComplaint_Resolved_Full_Time.setText(c1.date);
                            vComplaint_Resolved_Full_Complaint_Type.setText(c1.Type);
                            vComplaint_Resolved_Full_Description.setText(c1.Description);

                            if(!c1.Image_Url.equals("")){
                                Picasso.get().load(c1.Image_Url).into(vComplaint_Resolved_Full_Complaint_Image);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        vComplaint_Resolved_Full_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] lat = new String[1];
                final String[] lon = new String[1];
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Complaints_Sender_Resolved").child(Citizen_User_Id).child(Corporation_User_Id).child(Complaint_Id)
                        .addValueEventListener(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    lat[0] = dataSnapshot.child("lat").getValue().toString();
                                    lon[0] = dataSnapshot.child("lon").getValue().toString();

                                    vComplaint_Resolved_Full_Address.setText(lat[0] + "_" + lon[0]);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                String x = vComplaint_Resolved_Full_Address.getText().toString();
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
                //vComplaint_Pending_Full_Address.setText(address);
                //Toast.makeText(Complaint_Pending_Full.this,lat[0] + " -- " + lon[0],Toast.LENGTH_LONG).show();

                if(!lon[0].equals("")){
                    Intent intent = new Intent(Complaint_Resolved_Full.this,MapsActivity2.class);
                    final Bundle b1 = new Bundle();
                    b1.putString("Lat", lat[0]);
                    b1.putString("Lon", lon[0]);
                    intent.putExtras(b1);
                    //Toast.makeText(Complaint_Pending_Full.this,address,Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }

            }
        });

    }

    private void getResponse() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaint_Responses").child(r1.Corporation_User_Id).child(r1.Citizen_User_Id).child(r1.Complaint_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String url = dataSnapshot.child("Image_Url").getValue().toString();
                            if(!url.equals("")){
                                Picasso.get().load(url).into(vComplaint_Resolved_Full_Corporation_Response_Image);
                            }
                            String text = dataSnapshot.child("Response_Text").getValue().toString();
                            vComplaint_Resolved_Full_Corporation_Response_Description.setText(text);
                            String date = dataSnapshot.child("Date").getValue().toString();
                            vComplaint_Resolved_Full_Corporation_Time.setText(date);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Corporation").child(r1.Corporation_User_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String url = dataSnapshot.child("Profile_Image_Url").getValue().toString();
                    if(!url.equals("")){
                        Picasso.get().load(url).into(vComplaint_Resolved_Full_Corporation_Profile_Image);
                    }
                    String name = dataSnapshot.child("User_name").getValue().toString();
                    vComplaint_Resolved_Full_Corporation_User_Name.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setInvisible() {
        vResponse_Title.setVisibility(View.INVISIBLE);
        vBlack_Line_1.setVisibility(View.INVISIBLE);
        vBlack_Line_2.setVisibility(View.INVISIBLE);
        vComplaint_Resolved_Full_Corporation_User_Name.setVisibility(View.INVISIBLE);
        vComplaint_Resolved_Full_Corporation_Response_Image.setVisibility(View.INVISIBLE);
        vComplaint_Resolved_Full_Corporation_Profile_Image.setVisibility(View.INVISIBLE);
        vComplaint_Resolved_Full_Corporation_Time.setVisibility(View.INVISIBLE);
        vComplaint_Resolved_Full_Corporation_Response_Description.setVisibility(View.INVISIBLE);
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
