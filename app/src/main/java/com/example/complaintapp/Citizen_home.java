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
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Citizen_home extends AppCompatActivity {

    int backButtonCount = 0,GalleryPick = 1,PlacePickerRequestCode = 2;
    Toolbar vCitizen_Home_Page_bar;
    EditText vComplaint_Description,vComplaint_Address;
    Button vComplaint_Submit;
    Complaint c1;
    Location location;
    String Transmitter_Citizen_Id,Receiver_Corporation_Id,Address,Complaint_type,Complaint_Image_Url;
    String Description,date,vCitizen_User_Name,Complaint_Id;
    FirebaseAuth fauth;
    DatabaseReference databaseReference;
    StorageReference Complaint_Image_Reference;
    String Input_Country,Input_State,Input_District;
    Button vCitizen_Location;
    Button vComplaint_Image_Select_Button;
    Button vRemove_Image;
    ImageView vComplaint_Image_View;
    Uri vFilePath;
    ProgressDialog progressDialog;
    Spinner vCountry_Spinner,vState_Spinner,vDistrict_Spinner,vCitizen_Home_Type_Spinner,vCitizen_Home_Corporation_List;
    ValueEventListener listener;
    ArrayList<String> Country_List,State_List,District_List;
    ArrayAdapter<String> Country_Adapter,State_Adapter,District_Adapter;
    List<Complaint_Type_Data> l1;
    Complaint_Type_Adapter type_adapter;
    List<String> Corporation_List;
    Corporation_List_Adapter Corporation_List_Adapter;
    APIService apiService;
    boolean notify;

    TextView vGps_Address;
    String lat,lon,gpsCountry,gpsState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        vComplaint_Description = (EditText) findViewById(R.id.Complaint_Description);
        vComplaint_Submit = (Button) findViewById(R.id.Complaint_Submit);
        vCitizen_Home_Page_bar = (Toolbar) findViewById(R.id.Citizen_Home_Page_bar);
        vCitizen_Location = (Button) findViewById(R.id.Citizen_Location);
        vComplaint_Image_Select_Button = (Button) findViewById(R.id.Complaint_Image_Select_Button);
        vRemove_Image = (Button) findViewById(R.id.Remove_Image);
        vComplaint_Image_View = (ImageView) findViewById(R.id.Complaint_Image_View);
        vComplaint_Address = (EditText) findViewById(R.id.Complaint_Address);
        Complaint_Image_Reference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        vCountry_Spinner = (Spinner) findViewById(R.id.Country_Spinner);
        vState_Spinner = (Spinner) findViewById(R.id.State_Spinner);
        vDistrict_Spinner = (Spinner) findViewById(R.id.District_Spinner);
        vCitizen_Home_Type_Spinner = (Spinner) findViewById(R.id.Citizen_Home_Type_Spinner);
        vCitizen_Home_Corporation_List = (Spinner) findViewById(R.id.Citizen_Home_Corporation_List);
        Country_List = new ArrayList<>();
        State_List = new ArrayList<>();
        District_List = new ArrayList<>();
        Corporation_List = new ArrayList<>();
        l1 = new ArrayList<>();
        type_adapter = new Complaint_Type_Adapter(Citizen_home.this,R.layout.complaint_type_layout,l1);
        vCitizen_Home_Type_Spinner.setAdapter(type_adapter);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Corporation_List_Adapter = new Corporation_List_Adapter(Citizen_home.this,R.layout.complaint_type_layout,Corporation_List);
        vCitizen_Home_Corporation_List.setAdapter(Corporation_List_Adapter);
        Corporation_List_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        notify = false;

        vGps_Address = (TextView) findViewById(R.id.Gps_Address);

        setSupportActionBar(vCitizen_Home_Page_bar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Post Complaint");

        vComplaint_Image_View.setVisibility(View.GONE);

        Country_Adapter = new ArrayAdapter<String>(Citizen_home.this,android.R.layout.simple_spinner_dropdown_item,Country_List);
        State_Adapter = new ArrayAdapter<String>(Citizen_home.this,android.R.layout.simple_spinner_dropdown_item,State_List);
        District_Adapter = new ArrayAdapter<String>(Citizen_home.this,android.R.layout.simple_spinner_dropdown_item,District_List);

        vCountry_Spinner.setAdapter(Country_Adapter);
        vState_Spinner.setAdapter(State_Adapter);
        vDistrict_Spinner.setAdapter(District_Adapter);
        vCitizen_Home_Corporation_List.setAdapter(Corporation_List_Adapter);

        setLocationData();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful()){
                    //Log.d("7890","((((((())))))))");
                    return;
                }

                String refreshToken = Objects.requireNonNull(task.getResult()).getToken();
                updateToken(refreshToken);
            }
        });

        fauth = FirebaseAuth.getInstance();
        Transmitter_Citizen_Id = Objects.requireNonNull(fauth.getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Address = vComplaint_Address.getText().toString();
        Description = vComplaint_Description.getText().toString();

        vComplaint_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Description = vComplaint_Description.getText().toString();
                Address = vComplaint_Address.getText().toString();

                Input_Country = vCountry_Spinner.getSelectedItem().toString();
                Input_State = vState_Spinner.getSelectedItem().toString();
                Input_District = vDistrict_Spinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(Address)){
                    vComplaint_Address.setError("Please Enter Address");
                    return;
                }

                if(TextUtils.isEmpty(Description)){
                    vComplaint_Description.setError("Please Enter Complaint Description");
                    return;
                }

                if(Input_Country.equals("Country") || Input_State.equals("State") || Input_District.equals("District")){
                    Toast.makeText(Citizen_home.this,"Please Select the District",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(lat == null || lon == null){
                    Toast.makeText(Citizen_home.this,"Please Add GPS location",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!gpsCountry.equals(Input_Country) || !gpsState.equals(Input_State)){
                    Toast.makeText(Citizen_home.this,"Selected State and Gps location is not Same",Toast.LENGTH_LONG).show();
                    return;
                }

                if(vCitizen_Home_Corporation_List.getSelectedItem() == null){
                    Toast.makeText(Citizen_home.this,"Please Select the Corporation",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(vCitizen_Home_Type_Spinner.getSelectedItem() == null){
                    Toast.makeText(Citizen_home.this,"Please Select the Type of Complaint",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setTitle("Uploading");
                progressDialog.setMessage("Your Complaint is Being Uploaded...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                submit();


            }
        });

        vDistrict_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Input_Country = vCountry_Spinner.getSelectedItem().toString();
                Input_State = vState_Spinner.getSelectedItem().toString();
                Input_District = parent.getItemAtPosition(position).toString();

                if(!Input_Country.equals("Country") && !Input_State.equals("State") && !Input_District.equals("District")){
                    setSpinnerData(Input_Country,Input_State,Input_District);
                }
                else{
                    type_adapter.clear();
                    Corporation_List_Adapter.clear();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vCitizen_Home_Corporation_List.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Receiver_Corporation_Id = parent.getItemAtPosition(position).toString();
                type_adapter.clear();
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Corporation").child(Receiver_Corporation_Id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.child("Types").getChildren()){
                                String type = dataSnapshot1.getKey();
                                String available = Objects.requireNonNull(dataSnapshot1.getValue()).toString();
                                int x1 = getResId("ic_" + type ,R.drawable.class);
                                if(available.equals("1")){
                                    type_adapter.add(new Complaint_Type_Data(x1,type));
                                }
                            }
                            type_adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bottomNavigationView.setSelectedItemId(R.id.Post_complaint);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Post_complaint:
                        return true;

                    case R.id.History:
                        startActivity(new Intent(getApplicationContext(), History.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.Near_by:
                        startActivity(new Intent(getApplicationContext(), Near_by.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.Profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        vCitizen_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Citizen_home.this,MapsActivity.class);
                startActivityForResult(intent,PlacePickerRequestCode);
            }
        });

        vComplaint_Image_Select_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vComplaint_Image_View.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GalleryPick);
                if(vFilePath == null){
                    vComplaint_Image_View.setImageDrawable(null);
                    vComplaint_Image_View.setVisibility(View.GONE);
                }
            }
        });

        vRemove_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vFilePath = null;
                vComplaint_Image_View.setImageDrawable(null);
                vComplaint_Image_View.setVisibility(View.GONE);
            }
        });
    }

    private void setLocationData() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Corporation_Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Country_Adapter.clear();
                Country_Adapter.add("Country");
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
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
                Input_Country = parent.getItemAtPosition(position).toString();
                if(!Input_Country.equals("Country")){
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Corporation_Location").child(Input_Country).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            State_Adapter.clear();
                            State_Adapter.add("State");
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                State_Adapter.add(dataSnapshot1.getKey());
                            }
                            State_Adapter.notifyDataSetChanged();
                            vState_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Input_State = parent.getItemAtPosition(position).toString();
                                    if(!Input_State.equals("State") && !Input_Country.equals("Country")){
                                        databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("Corporation_Location").child(Input_Country).child(Input_State)
                                                .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                District_Adapter.clear();
                                                District_Adapter.add("District");
                                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    District_Adapter.add(dataSnapshot1.getKey());
                                                }
                                                District_Adapter.notifyDataSetChanged();
                                                vDistrict_Spinner.setSelection(0);
                                                type_adapter.clear();
                                                Corporation_List_Adapter.clear();
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

    private void setSpinnerData(final String country, final String state, final String district) {
        l1.clear();
        Corporation_List.clear();
        type_adapter.notifyDataSetChanged();
        Corporation_List_Adapter.notifyDataSetChanged();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Corporation_Location").child(country).child(state).child(district)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            final String id = dataSnapshot1.getKey();
                            assert id != null;
                            if(!id.equals("1")) {
                                Corporation_List_Adapter.add(id);
                            }
                        }
                        Corporation_List_Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void submit() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        date = java.text.DateFormat.getDateTimeInstance().format(new Date());
        databaseReference.child("Citizen").child(Transmitter_Citizen_Id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        if(dataSnapshot1.exists()){
                            vCitizen_User_Name = Objects.requireNonNull(dataSnapshot1.child("User_Name").getValue()).toString();
                            Complaint_Id = databaseReference.child(Transmitter_Citizen_Id).child(Receiver_Corporation_Id).push().getKey();

                            if(vFilePath!= null){
                                Complaint_Image_Reference = FirebaseStorage.getInstance().getReference().child("Complaint_Images");
                                final StorageReference imagepath = Complaint_Image_Reference.child(Transmitter_Citizen_Id + "_" + Complaint_Id + ".jpg");

                                imagepath.putFile(vFilePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        final Task<Uri> DownloadUrl = imagepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                final String DownloadUrl = uri.toString();
                                                uploadComplaintPost(DownloadUrl);
                                            }
                                        });
                                    }
                                });
                            }
                            else{
                                uploadComplaintPost("");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void uploadComplaintPost(String url) {

        location = new Location(Input_Country,Input_State,Input_District);
        Complaint_Type_Data d1 = (Complaint_Type_Data) vCitizen_Home_Type_Spinner.getSelectedItem();
        Complaint_type = d1.iconName;
        Complaint_Image_Url = url;

        c1 = new Complaint(
                Transmitter_Citizen_Id,
                Receiver_Corporation_Id,
                location,
                date,
                Complaint_Image_Url,
                Address,
                Complaint_type,
                Description,
                vCitizen_User_Name,
                Complaint_Id,
                lat,
                lon
        );

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Complaints_Sender_Pending");

        databaseReference.child(Transmitter_Citizen_Id)
                .child(Receiver_Corporation_Id)
                .child(Complaint_Id)
                .setValue(c1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            databaseReference = FirebaseDatabase.getInstance().getReference("Complaints_Receiver_Pending");
                            databaseReference.child(Receiver_Corporation_Id)
                                    .child(Transmitter_Citizen_Id)
                                    .child(Complaint_Id)
                                    .setValue(c1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Citizen_home.this,"Posted Succesfully",Toast.LENGTH_LONG).show();
                                                Notification(Transmitter_Citizen_Id,Receiver_Corporation_Id,Complaint_Id);
                                            }
                                            else{
                                                Toast.makeText(Citizen_home.this,"Error! "+ Objects.requireNonNull(task.getException()).toString(),Toast.LENGTH_LONG).show();
                                            }
                                            progressDialog.dismiss();
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(Citizen_home.this,"Error! "+ Objects.requireNonNull(task.getException()).toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void Notification(String transmitter_Citizen_Id,String receiver_Corporation_Id,String complaint_id) {
        notify = true;
        String msg  = vComplaint_Description.getText().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Citizen").child(transmitter_Citizen_Id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userName = Objects.requireNonNull(dataSnapshot.child("User_Name").getValue()).toString();
                        if(notify){
                            sendNotification(receiver_Corporation_Id,userName,msg,transmitter_Citizen_Id,complaint_id);
                        }
                        notify = false;
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendNotification(String receiver_corporation_id, String userName, String msg,String transmitter_Citizen_Id,String complaint_Id) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Query query = databaseReference.orderByKey().equalTo(receiver_corporation_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Token token = dataSnapshot1.getValue(Token.class);
                    Data data = new Data(transmitter_Citizen_Id,R.drawable.ic_notification_icon,userName + ":" + msg,"New Complaint"
                            ,receiver_corporation_id,"Corporation",complaint_Id);

                    assert token != null;
                    notification.Sender sender = new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        assert response.body() != null;
                                        if(response.body().success != 1){
                                            Toast.makeText(Citizen_home.this,"Failed",Toast.LENGTH_LONG).show();
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

    private void updateToken(String token){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tokens");
        notification.Token token1 = new Token(token);
        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(token1);
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
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick && resultCode == RESULT_OK && data != null && data.getData() != null){
            vFilePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),vFilePath);
                vComplaint_Image_View.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == PlacePickerRequestCode  && resultCode == RESULT_OK ){
            assert data != null;
            lat = data.getStringExtra("lat");
            lon = data.getStringExtra("long");
            gpsCountry = data.getStringExtra("Country");
            gpsState = data.getStringExtra("State");

            vGps_Address.setText("latitude = " + lat + " longtitude = " + lon);
        }
    }
}