package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;

public class Corporation_Response extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation__response);

        vResponse_Description = (EditText) findViewById(R.id.Response_Description);
        vAdd_Response_Image = (Button) findViewById(R.id.Add_Response_Image);
        vPost_Response = (Button) findViewById(R.id.Post_Response);
        vResponse_Image = (ImageView) findViewById(R.id.Response_Image);
        vBack_To_On_The_Job = (Button) findViewById(R.id.Back_To_On_The_Job);
        vContinue_Without_Response = (Button) findViewById(R.id.Continue_Without_Response);
        progressDialog = new ProgressDialog(Corporation_Response.this);

        bundle = getIntent().getExtras();
        //Toast.makeText(Complaint_Pending_Full.this,"hii",Toast.LENGTH_LONG).show();
        //assert bundle != null;
        final String Citizen_User_Id = bundle.get("Citizen_User_Id").toString();
        final String Corporation_User_Id = bundle.get("Corporation_User_Id").toString();
        final String Complaint_Id = bundle.get("Complaint_Id").toString();
        r1 = new Response();

        r1.Citizen_User_Id = Citizen_User_Id;
        r1.Corporation_User_Id = Corporation_User_Id;
        r1.Complaint_Id = Complaint_Id;
        //Log.d("aaa","hi");

        vBack_To_On_The_Job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Corporation_Response.this,Complaint_On_The_Job_Full.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
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
                Intent intent = new Intent(Corporation_Response.this,Complaint_Resolved_Full.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
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
                        Log.d("abcd0","0");

                        storageReference = FirebaseStorage.getInstance().getReference().child("Response_Images");
                        Log.d("abcd1","1");
                        final StorageReference imagepath = storageReference.child(r1.Corporation_User_Id + "_" + r1.Complaint_Id + ".jpg");
                        Log.d("abcd2","2");

                        imagepath.putFile(vFilePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                final Task<Uri> DownloadUrl = imagepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String DownloadUrl = uri.toString();
                                        Log.d("abcd3","3");
                                        uploadResponsePost(DownloadUrl);
                                    }
                                });
                            }
                        });
                        //progressDialog.dismiss();
                    }
                    else {
                        uploadResponsePost("");
                    }
                }

            }
        });

    }

    private void uploadResponsePost(String downloadUrl) {
        //Log.d("abcd4","4");

        r1.Date = java.text.DateFormat.getDateTimeInstance().format(new Date());
        r1.Image_Url = downloadUrl;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Complaint_Responses").child(r1.Corporation_User_Id).child(r1.Citizen_User_Id).child(r1.Complaint_Id).setValue(r1);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Sender_On_The_Job").child(r1.Citizen_User_Id).child(r1.Corporation_User_Id).child(r1.Complaint_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Complaint c1 = dataSnapshot.getValue(Complaint.class);
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
                            c1.Status = "Resolved";
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Complaints_Receiver_Resolved").child(r1.Corporation_User_Id).child(r1.Citizen_User_Id)
                                    .child(r1.Complaint_Id).setValue(c1);
                            Toast.makeText(Corporation_Response.this,"added succefully",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        databaseReference.child("Complaints_Receiver_On_The_Job").child(r1.Corporation_User_Id).child(r1.Citizen_User_Id).child(r1.Complaint_Id)
                .removeValue();

        progressDialog.dismiss();
        Intent intent = new Intent(Corporation_Response.this,Complaint_Resolved_Full.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vFilePath = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),vFilePath);
            vResponse_Image.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
