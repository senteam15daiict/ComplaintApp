package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import notification.Client;
import notification.Data;
import notification.MyResponse;
import notification.Sender;
import notification.Token;
import retrofit2.Call;
import retrofit2.Callback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class Corporation_Response extends AppCompatActivity {

    Toolbar vCorporation_Response_Page_bar;
    EditText vResponse_Description;
    Button vAdd_Response_Image, vPost_Response, vBack_To_On_The_Job, vContinue_Without_Response;
    ImageView vResponse_Image;
    int GalleryPick = 1;
    Uri vFilePath;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Response r1;
    Bundle bundle;
    APIService apiService;
    boolean notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation__response);

        vCorporation_Response_Page_bar =  findViewById(R.id.Corporation_Response_Page_bar);
        vResponse_Description = (EditText) findViewById(R.id.Response_Description);
        vAdd_Response_Image = (Button) findViewById(R.id.Add_Response_Image);
        vPost_Response = (Button) findViewById(R.id.Post_Response);
        vResponse_Image = (ImageView) findViewById(R.id.Response_Image);
        vBack_To_On_The_Job = (Button) findViewById(R.id.Back_To_On_The_Job);
        vContinue_Without_Response = (Button) findViewById(R.id.Continue_Without_Response);
        progressDialog = new ProgressDialog(Corporation_Response.this);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        notify = false;

        setSupportActionBar(vCorporation_Response_Page_bar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Response");
        vResponse_Image.setVisibility(View.INVISIBLE);

        bundle = getIntent().getExtras();
        assert bundle != null;
        final String Citizen_User_Id = Objects.requireNonNull(bundle.get("Citizen_User_Id")).toString();
        final String Corporation_User_Id = Objects.requireNonNull(bundle.get("Corporation_User_Id")).toString();
        final String Complaint_Id = Objects.requireNonNull(bundle.get("Complaint_Id")).toString();
        r1 = new Response();

        r1.Citizen_User_Id = Citizen_User_Id;
        r1.Corporation_User_Id = Corporation_User_Id;
        r1.Complaint_Id = Complaint_Id;

        vBack_To_On_The_Job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vResponse_Image.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Corporation_Response.this,Complaint_On_The_Job_Full.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                if(vFilePath == null){
                    vResponse_Image.setVisibility(View.INVISIBLE);
                }
            }
        });

        vAdd_Response_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GalleryPick);
            }
        });

        vContinue_Without_Response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Uploading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                uploadResponsePost("",1);

            }
        });

        vPost_Response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Uploading");
                progressDialog.setMessage("Your Response is Being Uploaded");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                r1.Response_Text = vResponse_Description.getText().toString();
                r1.Response_Text = r1.Response_Text.trim();
                databaseReference = FirebaseDatabase.getInstance().getReference();

                if(r1.Response_Text.equals("") && vFilePath == null){
                    progressDialog.dismiss();
                    Toast.makeText(Corporation_Response.this,"Please Add Description or Image",Toast.LENGTH_LONG).show();
                }
                else{
                    if(vFilePath != null)
                    {
                        storageReference = FirebaseStorage.getInstance().getReference().child("Response_Images");
                        final StorageReference imagepath = storageReference.child(r1.Corporation_User_Id + "_" + r1.Complaint_Id + ".jpg");
                        imagepath.putFile(vFilePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                final Task<Uri> DownloadUrl = imagepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String DownloadUrl = uri.toString();
                                        uploadResponsePost(DownloadUrl,2);
                                    }
                                });
                            }
                        });
                    }
                    else {
                        uploadResponsePost("",2);
                    }
                    databaseReference = FirebaseDatabase.getInstance().getReference();

                    databaseReference.child("Agree").child(Complaint_Id).removeValue();
                    databaseReference.child("Disagree").child(Complaint_Id).removeValue();
                }

            }
        });
    }

    private void uploadResponsePost(String downloadUrl,int x) {
        r1.Date = java.text.DateFormat.getDateTimeInstance().format(new Date());
        r1.Image_Url = downloadUrl;

        if(x == 2) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Complaint_Responses").child(r1.Corporation_User_Id).child(r1.Citizen_User_Id).child(r1.Complaint_Id).setValue(r1);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Sender_On_The_Job").child(r1.Citizen_User_Id).child(r1.Corporation_User_Id).child(r1.Complaint_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Complaint c1 = dataSnapshot.getValue(Complaint.class);
                            assert c1 != null;
                            c1.Status = "Resolved";
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Complaints_Sender_Resolved").child(r1.Citizen_User_Id).child(r1.Corporation_User_Id)
                                    .child(r1.Complaint_Id).setValue(c1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference.child("Complaints_Sender_On_The_Job").child(r1.Citizen_User_Id).child(r1.Corporation_User_Id).child(r1.Complaint_Id)
                .removeValue();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Receiver_On_The_Job").child(r1.Corporation_User_Id).child(r1.Citizen_User_Id).child(r1.Complaint_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Complaint c1 = dataSnapshot.getValue(Complaint.class);
                            assert c1 != null;
                            c1.Status = "Resolved";
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Complaints_Receiver_Resolved").child(r1.Corporation_User_Id).child(r1.Citizen_User_Id)
                                    .child(r1.Complaint_Id).setValue(c1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference.child("Complaints_Receiver_On_The_Job").child(r1.Corporation_User_Id).child(r1.Citizen_User_Id).child(r1.Complaint_Id)
                .removeValue();

        progressDialog.dismiss();
        Notification(r1.Corporation_User_Id,r1.Citizen_User_Id,r1.Complaint_Id);

        Toast.makeText(Corporation_Response.this,"added succefully",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Corporation_Response.this,Complaint_Resolved_Full.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void Notification(String transmitter_Corporation_Id,String receiver_Citizen_Id,String complaint_id) {
        notify = true;
        String msg  = vResponse_Description.getText().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Corporation").child(transmitter_Corporation_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userName = Objects.requireNonNull(dataSnapshot.child("User_name").getValue()).toString();
                        if(notify){
                            sendNotification(receiver_Citizen_Id,userName,msg,transmitter_Corporation_Id,complaint_id);
                        }
                        notify = false;
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendNotification(String receiver_Citizen_Id, String userName, String msg,String transmitter_Corporation_Id,String complaint_id) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Query query = databaseReference.orderByKey().equalTo(receiver_Citizen_Id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Token token = dataSnapshot1.getValue(Token.class);
                    Data data = new Data(transmitter_Corporation_Id,R.drawable.ic_notification_icon,userName + " : " + msg,"Complaint Resolved"
                            ,receiver_Citizen_Id,"Citizen",complaint_id);

                    assert token != null;
                    notification.Sender sender = new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        assert response.body() != null;
                                        if(response.body().success != 1){
                                            Toast.makeText(Corporation_Response.this,"Failed",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        vFilePath = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),vFilePath);
            vResponse_Image.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
