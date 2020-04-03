package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login_Corporation extends AppCompatActivity {

    EditText vCorporation_Login_Email,vCorporation_Login_Password,vCorporation_Login_Security_Key;
    Button vCorporation_Login_Button;
    TextView vCorporation_Create_Account,vCorporation_Forgot_Password,vTo_Open_Citizen_Login;
    FirebaseAuth fauth;
    DatabaseReference databaseReference;
    int backButtonCount = 0;
    Toolbar vCorporation_Login_Page_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__corporation);

        vCorporation_Login_Email = (EditText) findViewById(R.id.Corporation_Login_Email);
        vCorporation_Login_Password = (EditText) findViewById(R.id.Corporation_Login_Password);
        vCorporation_Login_Security_Key = (EditText) findViewById(R.id.Corporation_Login_Security_Key);
        vCorporation_Login_Button = (Button) findViewById(R.id.Corporation_Login_Button);
        vCorporation_Create_Account = (TextView) findViewById(R.id.Corporation_Create_Account);
        vCorporation_Forgot_Password = (TextView) findViewById(R.id.Corporation_Forgot_Password);
        vTo_Open_Citizen_Login = (TextView) findViewById(R.id.To_Open_Citizen_Login);
        fauth = FirebaseAuth.getInstance();
        vCorporation_Login_Page_bar = (Toolbar) findViewById(R.id.Corporation_Login_Page_bar);
        setSupportActionBar(vCorporation_Login_Page_bar);
        getSupportActionBar().setTitle("Corporation Login");

        vCorporation_Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = vCorporation_Login_Email.getText().toString();
                final String password = vCorporation_Login_Password.getText().toString();
                final String security_key = vCorporation_Login_Security_Key.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("Corporation");
                Query query = databaseReference.orderByChild("Email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //Corporation c1 = new Corporation();
                                String c1Password = snapshot.child("Password").getValue().toString();
                                String c1Security_Key = snapshot.child("Security_Key").getValue().toString();
                                Log.d(c1Password,c1Security_Key);
                                Toast.makeText(Login_Corporation.this,  c1Password + " security key = " + c1Security_Key, Toast.LENGTH_LONG).show();
                                if (c1Password.equals(password) && c1Security_Key.equals(security_key)) {

                                    fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(Login_Corporation.this, Corporation_home.class));
                                                finish();
                                            } else {
                                                Toast.makeText(Login_Corporation.this, "Error! " + task.getException(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(Login_Corporation.this,"Please Enter Valid Password and Security Key",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        vCorporation_Create_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Corporation.this,Sign_Up.class));
                finish();
            }
        });

        vCorporation_Forgot_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetemail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to recieve the reset link");
                passwordResetDialog.setView(resetemail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = resetemail.getText().toString();

                        fauth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login_Corporation.this,"Reset Link Sent to your mail",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login_Corporation.this,"Error! " + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                passwordResetDialog.create().show();
            }
        });

        vTo_Open_Citizen_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Corporation.this,Login.class));
                finish();
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
