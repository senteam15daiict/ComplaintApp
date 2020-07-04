package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Sign_Up_Corporation extends AppCompatActivity {

    EditText vCorporation_User_Name,vCorporation_Phone_Number,vCorporation_Email;
    EditText vCorporation_Password,vCorporation_Security_Key;
    Spinner vCorporation_Country,vCorporation_State,vCorporation_District;
    Button  vCorporation_Sign_Up;
    TextView vCorporation_Login_Screen,vCorporation_Register_As_Citizen;
    DatabaseReference databaseReference;
    FirebaseAuth fauth;
    int backButtonCount = 0;
    Toolbar vCorporation_Sign_Up_Page_bar;
    ArrayList<String> Country_List,State_List,District_List;
    ArrayAdapter<String> Country_Adapter,State_Adapter,District_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up__corporation);

        vCorporation_User_Name = (EditText) findViewById(R.id.Corporation_User_Name);
        vCorporation_Phone_Number = (EditText) findViewById(R.id.Corporation_Phone_Number);
        vCorporation_Email = (EditText) findViewById(R.id.Corporation_Email);
        vCorporation_Password = (EditText) findViewById(R.id.Corporation_Password);
        vCorporation_Security_Key = (EditText) findViewById(R.id.Corporation_Security_Key);
        vCorporation_Country = (Spinner) findViewById(R.id.Corporation_Country);
        vCorporation_State = (Spinner) findViewById(R.id.Corporation_State);
        vCorporation_District = (Spinner) findViewById(R.id.Corporation_District);
        vCorporation_Sign_Up = (Button) findViewById(R.id.Corporation_Sign_Up);
        vCorporation_Login_Screen = (TextView) findViewById(R.id.Corporation_Login_Screen);
        vCorporation_Register_As_Citizen = (TextView) findViewById(R.id.Corporation_Register_As_Citizen);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        fauth = FirebaseAuth.getInstance();
        vCorporation_Sign_Up_Page_bar = (Toolbar) findViewById(R.id.Corporation_Sign_Up_Page_bar);
        setSupportActionBar(vCorporation_Sign_Up_Page_bar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Corporation Sign Up");

        Country_List = new ArrayList<>();
        State_List = new ArrayList<>();
        District_List = new ArrayList<>();

        Country_Adapter = new ArrayAdapter<String>(Sign_Up_Corporation.this,android.R.layout.simple_spinner_dropdown_item,Country_List);
        State_Adapter = new ArrayAdapter<String>(Sign_Up_Corporation.this,android.R.layout.simple_spinner_dropdown_item,State_List);
        District_Adapter = new ArrayAdapter<String>(Sign_Up_Corporation.this,android.R.layout.simple_spinner_dropdown_item,District_List);

        vCorporation_Country.setAdapter(Country_Adapter);
        vCorporation_State.setAdapter(State_Adapter);
        vCorporation_District.setAdapter(District_Adapter);

        setLocationData();

        vCorporation_Login_Screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_Up_Corporation.this,Login.class));
                finish();
            }
        });

        vCorporation_Register_As_Citizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_Up_Corporation.this,Sign_Up.class));
                finish();
            }
        });

        vCorporation_Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = vCorporation_User_Name.getText().toString().trim();
                String phone_number = vCorporation_Phone_Number.getText().toString().trim();
                String email = vCorporation_Email.getText().toString().trim();
                String password = vCorporation_Password.getText().toString();
                String security_key = vCorporation_Security_Key.getText().toString();
                final String country = vCorporation_Country.getSelectedItem().toString();
                final String state = vCorporation_State.getSelectedItem().toString();
                final String district = vCorporation_District.getSelectedItem().toString();

                Location l1 = new Location(
                        country,
                        state,
                        district
                );

                if(country.equals("Country") || state.equals("State") || district.equals("District")){
                    Toast.makeText(Sign_Up_Corporation.this,"Please select Location",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(user_name)){
                    vCorporation_User_Name.setError("Please Enter User Name");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    vCorporation_Password.setError("Please Enter Password");
                    return;
                }

                if(TextUtils.isEmpty(security_key)){
                    vCorporation_Security_Key.setError("Please Enter Security Key");
                    return;
                }

                if(phone_number.length() != 10){
                    vCorporation_Phone_Number.setError("Pls Enter Valid Phone Number");
                    return;
                }
                else{
                    int f = 0;
                    for(int i=0;i<10;i++){
                        if (!(phone_number.charAt(i) >= '0' && phone_number.charAt(i) <= '9')) {
                            f = 1;
                            break;
                        }
                    }
                    if(f == 1){
                        vCorporation_Phone_Number.setError("Pls Enter valid phone Number");
                        return;
                    }
                }

                final Corporation  c1 = new Corporation(
                        user_name,
                        email,
                        phone_number,
                        password,
                        security_key,
                        l1
                );

                fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String User_Id = Objects.requireNonNull(FirebaseAuth.getInstance().getInstance().getCurrentUser()).getUid();
                            FirebaseDatabase.getInstance().getReference("Corporation")
                                    .child(User_Id)
                                    .setValue(c1);
                            Map<String,String> m = new HashMap<>();
                            m.put("Open","1");
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Corporation_Location").child(country).child(state).child(district)
                                    .child(User_Id).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Sign_Up_Corporation.this,"Registered Succesfully",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Sign_Up_Corporation.this,Login.class));
                                        finish();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(Sign_Up_Corporation.this,"Error!" + task.getException(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    private void setLocationData() {
        final String[] country = new String[1];
        final String[] state = new String[1];
        final String district;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Corporation_Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Country_Adapter.clear();
                Country_Adapter.add("Country");
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Country_Adapter.add(dataSnapshot1.getKey());
                }
                Country_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        vCorporation_Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country[0] = parent.getItemAtPosition(position).toString();
                if(!country[0].equals("Country")){
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Corporation_Location").child(country[0]).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            State_Adapter.clear();
                            State_Adapter.add("State");
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                State_Adapter.add(dataSnapshot1.getKey());
                            }
                            State_Adapter.notifyDataSetChanged();

                            vCorporation_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    state[0] = parent.getItemAtPosition(position).toString();
                                    if(!state[0].equals("State")){
                                        databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("Corporation_Location").child(country[0]).child(state[0]).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                District_Adapter.clear();
                                                District_Adapter.add("District");
                                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    District_Adapter.add(dataSnapshot1.getKey());
                                                }
                                                District_Adapter.notifyDataSetChanged();
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
