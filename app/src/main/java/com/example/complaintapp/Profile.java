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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    TextView vCitizen_User_Name_below_Image;
    StorageReference Citizen_Profile_Image_Reference;
    FirebaseAuth fauth;
    DatabaseReference firebaseDatabase;
    String Current_Citizen_Id;
    ProgressDialog progressDialog;


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
        Citizen_Profile_Image_Reference = FirebaseStorage.getInstance().getReference().child("Citizen_Profile_Images");
        fauth = FirebaseAuth.getInstance();
        Current_Citizen_Id = fauth.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        vCitizen_Profile_Page_Bar = (Toolbar) findViewById(R.id.Citizen_Profile_Page_Bar);
        setSupportActionBar(vCitizen_Profile_Page_Bar);
        getSupportActionBar().setTitle("Profile");

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

                    case R.id.Notifications:
                        startActivity(new Intent(getApplicationContext(),Notification.class));
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

        vDelete_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Profile.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your account from the system and you wont be able to access the app.");

                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        assert firebaseUser != null;
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(Profile.this,"1 -- " + Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                        String uid = firebaseUser.getUid();
                        //Toast.makeText(citizen_home.this,key,Toast.LENGTH_LONG).show();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Citizen").child(uid);
                        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Profile.this,"Account Deleted",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Profile.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(Profile.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });


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

    private void get_Citizen_Profile_Info() {
        firebaseDatabase.child("Citizen").child(Current_Citizen_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String Profile_Image_Ref = dataSnapshot.child("Profile_Image_Url").getValue().toString();
                            String Citizen_User_Name = dataSnapshot.child("User_Name").getValue().toString();
                            vCitizen_User_Name_below_Image.setText(Citizen_User_Name);
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

                Uri resultUri = result.getUri();

                final StorageReference filepath = Citizen_Profile_Image_Reference.child(Current_Citizen_Id + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Profile.this,"Succefully Uploded",Toast.LENGTH_SHORT).show();

                            final Task<Uri> DownloadUrl = filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String DownloadUrl = uri.toString();
                                    firebaseDatabase.child("Citizen").child(Current_Citizen_Id).child("Profile_Image_Url")
                                            .setValue(DownloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Profile.this,"Saved In Database Too......",Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();
                                                    }
                                                    else{
                                                        Toast.makeText(Profile.this,"Error! "+task.getException().toString(),Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();
                                                    }

                                                }
                                            });

                                }
                            });
                        }
                        else{
                            Toast.makeText(Profile.this,"Error! " + task.getException().toString(),Toast.LENGTH_LONG ).show();
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
