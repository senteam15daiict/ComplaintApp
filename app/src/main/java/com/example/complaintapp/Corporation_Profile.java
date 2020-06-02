package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Corporation_Profile extends AppCompatActivity {

    int backButtonCount = 0,GalleryPick = 1;
    Button vCorporation_Logout,vCorporation_Delete_Account,vAdd_Type_Button,vRemove_Type_Button;
    Toolbar vCorporation_Profile_Page_bar;
    CircleImageView vCorporation_Profile_Image,vCorporation_Profile_Image_Selector;
    TextView vCorporation_User_Name_below_Image;
    FirebaseAuth fauth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    StorageReference storageReference;
    String Corporation_Id;
    ProgressDialog progressDialog;
    Spinner vCorporation_Available_Complaint_Types,vCorporation_Not_Available_Complaint_Types;
    List<Complaint_Type_Data> available_Type_List,not_available_Type_List;
    Complaint_Type_Adapter availableSpinnerAdapter,notavailableSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation__profile);

        vCorporation_Delete_Account = (Button) findViewById(R.id.Corporation_Delete_Account);
        vCorporation_Logout = (Button) findViewById(R.id.Corporation_Logout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        vCorporation_Profile_Page_bar = findViewById(R.id.Corporation_Profile_Page_bar);
        vCorporation_User_Name_below_Image = (TextView) findViewById(R.id.Corporation_User_Name_below_Image);
        vCorporation_Profile_Image = (CircleImageView) findViewById(R.id.Corporation_Profile_Image);
        vCorporation_Profile_Image_Selector = (CircleImageView) findViewById(R.id.Corporation_Profile_Image_Selector);
        fauth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Corporation_Id = Objects.requireNonNull(fauth.getCurrentUser()).getUid();
        progressDialog = new ProgressDialog(this);
        vCorporation_Available_Complaint_Types = (Spinner) findViewById(R.id.Corporation_Available_Complaint_Types);
        vCorporation_Not_Available_Complaint_Types = (Spinner) findViewById(R.id.Corporation_Not_Available_Complaint_Types);
        vRemove_Type_Button = (Button) findViewById(R.id.Remove_Type_Button);
        vAdd_Type_Button = (Button) findViewById(R.id.Add_Type_Button);

        available_Type_List = new ArrayList<Complaint_Type_Data>();
        not_available_Type_List = new ArrayList<Complaint_Type_Data>();

        setCorporationData();

        availableSpinnerAdapter = new Complaint_Type_Adapter(Corporation_Profile.this,R.layout.complaint_type_layout,available_Type_List);
        vCorporation_Available_Complaint_Types.setAdapter(availableSpinnerAdapter);
        availableSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        notavailableSpinnerAdapter = new Complaint_Type_Adapter(Corporation_Profile.this,R.layout.complaint_type_layout,not_available_Type_List);
        vCorporation_Not_Available_Complaint_Types.setAdapter(notavailableSpinnerAdapter);
        notavailableSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setSpinnersData();

        setSupportActionBar(vCorporation_Profile_Page_bar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Profile");

        bottomNavigationView.setSelectedItemId(R.id.Corporation_Profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.Corporation_View_complaint:
                        startActivity(new Intent(getApplicationContext(),Corporation_home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Corporation_Profile:
                        return true;
                }
                return false;
            }
        });



        vCorporation_Profile_Image_Selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GalleryPick);
            }
        });

        vCorporation_Delete_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Corporation_Profile.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your account from the system and you wont be able to access the app.");

                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.setTitle("Deleting Account");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        //Log.d("Corporation_Id = ",Corporation_Id);
                        deleteComplaints();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Corporation").child(Corporation_Id);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String country = Objects.requireNonNull(dataSnapshot.child("location").child("Country").getValue()).toString();
                                    String state = Objects.requireNonNull(dataSnapshot.child("location").child("State").getValue()).toString();
                                    String district = Objects.requireNonNull(dataSnapshot.child("location").child("District").getValue()).toString();
                                    String url = Objects.requireNonNull(dataSnapshot.child("Profile_Image_Url").getValue()).toString();

                                    if(!url.equals("")){
                                        deleteImage(url);
                                    }

                                    databaseReference = FirebaseDatabase.getInstance().getReference();
                                    databaseReference.child("Corporation_Location").child(country).child(state).child(district).child(Corporation_Id)
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                databaseReference = FirebaseDatabase.getInstance().getReference();
                                                databaseReference.child("Corporation").child(Corporation_Id).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                Toast.makeText(Corporation_Profile.this,"Deleted Succefully",Toast.LENGTH_SHORT).show();
                                                                                startActivity(new Intent(Corporation_Profile.this,MainActivity.class));
                                                                                finish();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                else{
                                                                    Toast.makeText(Corporation_Profile.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                            }
                                            else{
                                                Toast.makeText(Corporation_Profile.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
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

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog  =  dialog.create();
                alertDialog.show();
            }
        });

        vCorporation_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        vAdd_Type_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vCorporation_Not_Available_Complaint_Types != null && vCorporation_Not_Available_Complaint_Types.getSelectedItem() != null){
                    Complaint_Type_Data d2 = (Complaint_Type_Data) vCorporation_Not_Available_Complaint_Types.getSelectedItem();
                    final String selectedType = d2.iconName;
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Corporation").child(Corporation_Id).child("Types").child(selectedType).setValue("1")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        setSpinnersData();
                                    }
                                }
                            });

                }

            }
        });

        vRemove_Type_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vCorporation_Available_Complaint_Types != null &&  vCorporation_Available_Complaint_Types.getSelectedItem() != null && available_Type_List.size() >= 1){
                    Complaint_Type_Data d1 = (Complaint_Type_Data) vCorporation_Available_Complaint_Types.getSelectedItem();
                    String selectedtype = d1.iconName;
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Corporation").child(Corporation_Id).child("Types").child(selectedtype).setValue("0")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        setSpinnersData();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void deleteComplaints() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Receiver_Pending").child(Corporation_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String Citizen_Id = dataSnapshot1.getKey();
                        databaseReference1 = FirebaseDatabase.getInstance().getReference();
                        assert Citizen_Id != null;
                        databaseReference1.child("Complaints_Sender_Pending").child(Citizen_Id).child(Corporation_Id).removeValue();
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
        databaseReference.child("Complaints_Receiver_Pending").child(Corporation_Id).removeValue();


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Receiver_On_The_Job").child(Corporation_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String Citizen_Id = dataSnapshot1.getKey();
                        databaseReference1 = FirebaseDatabase.getInstance().getReference();
                        assert Citizen_Id != null;
                        databaseReference1.child("Complaints_Sender_On_The_Job").child(Citizen_Id).child(Corporation_Id).removeValue();
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
        databaseReference.child("Complaints_Receiver_On_The_Job").child(Corporation_Id).removeValue();


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Complaints_Receiver_Resolved").child(Corporation_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String Citizen_Id = dataSnapshot1.getKey();
                        databaseReference1 = FirebaseDatabase.getInstance().getReference();
                        assert Citizen_Id != null;
                        databaseReference1.child("Complaints_Sender_Resolved").child(Citizen_Id).child(Corporation_Id).removeValue();
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
        databaseReference.child("Complaints_Receiver_Resolved").child(Corporation_Id).removeValue();
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

    private void setCorporationData() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Corporation").child(Corporation_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userName = Objects.requireNonNull(dataSnapshot.child("User_name").getValue()).toString();
                    String url = Objects.requireNonNull(dataSnapshot.child("Profile_Image_Url").getValue()).toString();

                    vCorporation_User_Name_below_Image.setText(userName);
                    if(!url.equals("")){
                        Picasso.get().load(url).into(vCorporation_Profile_Image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setSpinnersData() {
        availableSpinnerAdapter.clear();
        notavailableSpinnerAdapter.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Corporation").child(Corporation_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.child("Types").getChildren()){
                        String typeName =  dataSnapshot1.getKey();
                        String available = Objects.requireNonNull(dataSnapshot1.getValue()).toString();
                        int x1 =  getResId("ic_" +typeName,R.drawable.class);

                        if(available.equals("1")){
                            availableSpinnerAdapter.add(new Complaint_Type_Data(x1,typeName));
                        }
                        else{
                            notavailableSpinnerAdapter.add(new Complaint_Type_Data(x1, typeName));
                        }
                    }
                    availableSpinnerAdapter.notifyDataSetChanged();
                    notavailableSpinnerAdapter.notifyDataSetChanged();
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

                storageReference = FirebaseStorage.getInstance().getReference().child("Corporation_Profile_Images");
                databaseReference = FirebaseDatabase.getInstance().getReference();
                assert result != null;
                Uri resultUri = result.getUri();

                final StorageReference filepath = storageReference.child(Corporation_Id + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {

                            final Task<Uri> DownloadUrl = filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String DownloadUrl = uri.toString();
                                    databaseReference.child("Corporation").child(Corporation_Id).child("Profile_Image_Url")
                                            .setValue(DownloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Corporation_Profile.this,"Succefully Uploded",Toast.LENGTH_SHORT).show();
                                                        //Toast.makeText(Corporation_Profile.this,"Saved In Database Too......",Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();
                                                    }
                                                    else{
                                                        Toast.makeText(Corporation_Profile.this,"Error! "+ Objects.requireNonNull(task.getException()).toString(),Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();
                                                    }

                                                }
                                            });

                                }
                            });
                        }
                        else{
                            Toast.makeText(Corporation_Profile.this,"Error! " + task.getException().toString(),Toast.LENGTH_LONG ).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        }
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
