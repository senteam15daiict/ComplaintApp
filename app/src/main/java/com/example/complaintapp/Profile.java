package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    Button vDelete_Account,vLogout;
    int backButtonCount = 0,GalleryPick = 1;
    Toolbar vCitizen_Profile_Page_Bar;
    CircleImageView vCitizen_Profile_Image,vCitizen_Profile_Image_Selector;
    TextView vCitizen_User_Name_below_Image,vCitizen_Phone_Number;
    StorageReference Citizen_Profile_Image_Reference;
    FirebaseAuth fauth;
    FirebaseUser firebaseUser;
    DatabaseReference firebaseDatabase,databaseReference,databaseReference1,databaseReference2;
    String Citizen_Id;
    ProgressDialog progressDialog;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        vDelete_Account = (Button) findViewById(R.id.Delete_Account);
        vLogout = (Button) findViewById(R.id.Logout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        vCitizen_Profile_Image = (CircleImageView)  findViewById(R.id.Citizen_Profile_Image);
        vCitizen_Profile_Image_Selector = (CircleImageView) findViewById(R.id.Citizen_Profile_Image_Selector);
        vCitizen_User_Name_below_Image = (TextView) findViewById(R.id.Citizen_User_Name_below_Image);
        vCitizen_Phone_Number = (TextView) findViewById(R.id.Citizen_Phone_Number);
        Citizen_Profile_Image_Reference = FirebaseStorage.getInstance().getReference().child("Citizen_Profile_Images");
        fauth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Citizen_Id = fauth.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        vCitizen_Profile_Page_Bar = (Toolbar) findViewById(R.id.Citizen_Profile_Page_Bar);
        setSupportActionBar(vCitizen_Profile_Page_Bar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Profile");

        get_Citizen_Profile_Info();

        bottomNavigationView.setSelectedItemId(R.id.Profile);

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
                        startActivity(new Intent(getApplicationContext(),Near_by.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Profile:
                        return true;
                }
                return false;
            }
        });

        vCitizen_Profile_Image_Selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GalleryPick);
            }
        });

        vCitizen_User_Name_below_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder =new AlertDialog.Builder(Profile.this);
                View mview =getLayoutInflater().inflate(R.layout.edit_text_layout,null);
                mbuilder.setTitle("Edit User Name");

                EditText vedit_text_layout = mview.findViewById(R.id.edit_text_layout);
                vedit_text_layout.setText(vCitizen_User_Name_below_Image.getText().toString());

                mbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = vedit_text_layout.getText().toString().trim();
                        vCitizen_User_Name_below_Image.setText(name);
                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Citizen").child(Citizen_Id).child("User_Name").setValue(name);
                    }
                });

                mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mbuilder.setView(mview);
                AlertDialog dialog =mbuilder.create();
                dialog.show();
            }
        });

        vCitizen_Phone_Number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder =new AlertDialog.Builder(Profile.this);
                View mview =getLayoutInflater().inflate(R.layout.edit_text_layout,null);
                mbuilder.setTitle("Edit Phone Number");

                EditText vedit_text_layout = mview.findViewById(R.id.edit_text_layout);
                vedit_text_layout.setInputType(InputType.TYPE_CLASS_PHONE);
                vedit_text_layout.setText(vCitizen_Phone_Number.getText().toString());

                mbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String number = vedit_text_layout.getText().toString().trim();
                        vCitizen_Phone_Number.setText(number);
                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Citizen").child(Citizen_Id).child("Phone_Number").setValue(number);
                    }
                });

                mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mbuilder.setView(mview);
                AlertDialog dialog =mbuilder.create();
                dialog.show();
            }
        });

        vDelete_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Profile.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your account from the system and you wont be able to access the app.");

                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.setTitle("Deleting Account");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        deleteComplaints();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Citizen").child(Citizen_Id);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                    String url = Objects.requireNonNull(dataSnapshot.child("Profile_Image_Url").getValue()).toString();
                                    if(!url.equals("")){
                                        deleteImage(url);
                                    }

                                    databaseReference = FirebaseDatabase.getInstance().getReference();
                                    databaseReference.child("Citizen").child(Citizen_Id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(Profile.this,"Deleted Succefully",Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(Profile.this,MainActivity.class));
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        progressDialog.dismiss();
                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog  =  dialog.create();
                alertDialog.show();
            }
        });

        vLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
    }

    private void deleteComplaints() {
        
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Sender_Pending").child(Citizen_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String Corporation_Id = dataSnapshot1.getKey();
                        databaseReference1 = FirebaseDatabase.getInstance().getReference();
                        assert Corporation_Id != null;
                        databaseReference1.child("Complaints_Receiver_Pending").child(Corporation_Id).child(Citizen_Id).removeValue();
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                            String url = Objects.requireNonNull(dataSnapshot2.child("Image_Url").getValue()).toString();
                            if(!url.equals("")){
                                deleteImage(url);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Complaints_Sender_Pending").child(Citizen_Id).removeValue();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Sender_On_The_Job").child(Citizen_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String Corporation_Id = dataSnapshot1.getKey();
                        databaseReference1 = FirebaseDatabase.getInstance().getReference();
                        assert Corporation_Id != null;
                        databaseReference1.child("Complaints_Receiver_On_The_Job").child(Corporation_Id).child(Citizen_Id).removeValue();
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                            String url = Objects.requireNonNull(dataSnapshot2.child("Image_Url").getValue()).toString();
                            if(!url.equals("")){
                                deleteImage(url);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Complaints_Sender_On_The_Job").child(Citizen_Id).removeValue();


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Sender_Resolved").child(Citizen_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String Corporation_Id = dataSnapshot1.getKey();
                        databaseReference1 = FirebaseDatabase.getInstance().getReference();
                        assert Corporation_Id != null;
                        databaseReference1.child("Complaints_Receiver_Resolved").child(Corporation_Id).child(Citizen_Id).removeValue();
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                            String url = Objects.requireNonNull(dataSnapshot2.child("Image_Url").getValue()).toString();
                            if(!url.equals("")){
                                deleteImage(url);
                            }

                            String complaint_Id = dataSnapshot2.getKey();
                            databaseReference2 = FirebaseDatabase.getInstance().getReference();
                            assert complaint_Id != null;
                            databaseReference2.child("Complaint_Responses").child(Corporation_Id).child(Citizen_Id).child(complaint_Id)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                String url1 = Objects.requireNonNull(dataSnapshot.child("Image_Url").getValue()).toString();
                                                if(!url1.equals("")){
                                                    deleteImage(url1);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                            databaseReference2.child("Complaint_Responses").child(Corporation_Id).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Complaints_Sender_Resolved").child(Citizen_Id).removeValue();
    }

    private void deleteImage(String url) {
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("Image deleted","3000");
                }
            }
        });
    }

    private void get_Citizen_Profile_Info() {
        firebaseDatabase.child("Citizen").child(Citizen_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String Profile_Image_Ref = Objects.requireNonNull(dataSnapshot.child("Profile_Image_Url").getValue()).toString();
                            String Citizen_User_Name = Objects.requireNonNull(dataSnapshot.child("User_Name").getValue()).toString();
                            String Phone_Number = Objects.requireNonNull(dataSnapshot.child("Phone_Number").getValue()).toString();
                            vCitizen_User_Name_below_Image.setText(Citizen_User_Name);
                            vCitizen_Phone_Number.setText(Phone_Number);
                            if(!Profile_Image_Ref.equals("")){
                                Picasso.get().load(Profile_Image_Ref).into(vCitizen_Profile_Image);
                            }
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

        if(requestCode == GalleryPick && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            CropImage.activity(uri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                progressDialog.setTitle("Set Profile Image");
                progressDialog.setMessage("Your Profile Image is Uploading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                assert result != null;
                Uri resultUri = result.getUri();

                final StorageReference filepath = Citizen_Profile_Image_Reference.child(Citizen_Id + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Profile.this,"Succefully Uploded",Toast.LENGTH_SHORT).show();

                            final Task<Uri> DownloadUrl = filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String DownloadUrl = uri.toString();
                                    firebaseDatabase.child("Citizen").child(Citizen_Id).child("Profile_Image_Url")
                                            .setValue(DownloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Profile.this,"Saved In Database Too......",Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();
                                                    }
                                                    else{
                                                        Toast.makeText(Profile.this,"Error! "+ Objects.requireNonNull(task.getException()).toString(),Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();
                                                    }

                                                }
                                            });

                                }
                            });
                        }
                        else{
                            Toast.makeText(Profile.this,"Error! " + Objects.requireNonNull(task.getException()).toString(),Toast.LENGTH_LONG ).show();
                            progressDialog.dismiss();
                        }
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
