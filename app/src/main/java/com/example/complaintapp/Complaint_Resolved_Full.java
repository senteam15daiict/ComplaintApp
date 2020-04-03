package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

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
        String Citizen_User_Id = bundle.get("Citizen_User_Id").toString();
        String Corporation_User_Id = bundle.get("Corporation_User_Id").toString();
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
