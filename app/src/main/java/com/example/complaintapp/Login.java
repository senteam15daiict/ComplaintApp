package com.example.complaintapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.Objects;

public class Login extends AppCompatActivity {

    int backButtonCount = 0;
    EditText vEmail, vPassword;
    TextView vForgot_Password, vCreate_Account, vTo_Open_Corporation_Login;
    String User_Id;
    Button vLogin;
    FirebaseAuth fauth;
    Toolbar vCitizen_Login_Page_Bar;
    DatabaseReference databaseReference;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        vEmail = (EditText) findViewById(R.id.Login_Citizen_Email);
        vPassword = (EditText) findViewById(R.id.Login_Citizen_Password);
        vForgot_Password = (TextView) findViewById(R.id.Forgot_Password);
        vCreate_Account = (TextView) findViewById(R.id.Create_Account);
        vTo_Open_Corporation_Login = (TextView) findViewById(R.id.To_Open_Corporation_Login);
        vLogin = (Button) findViewById(R.id.Login_Button);
        fauth = FirebaseAuth.getInstance();
        vCitizen_Login_Page_Bar = (Toolbar) findViewById(R.id.Citizen_Login_Page_Bar);
        setSupportActionBar(vCitizen_Login_Page_Bar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Citizen Login");
        databaseReference = FirebaseDatabase.getInstance().getReference();

        vLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = vEmail.getText().toString().trim();
                final String password = vPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    vEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    vPassword.setError("Password is required");
                    return;
                }

                if (password.length() <= 5) {
                    vPassword.setError("Password length must be >= 6");
                    return;
                }

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Citizen");
                Query query = databaseReference.orderByChild("Email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).isEmailVerified()) {
                                                Toast.makeText(Login.this, "Login Succesfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Login.this, Citizen_home.class));
                                                finish();
                                            } else {
                                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseAuth.getInstance().signOut();
                                                            Toast.makeText(Login.this, "Verification Mail Sended Succesfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Login.this, "Error! " + Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            Toast.makeText(Login.this, "Error! " + Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        } else {
                            Toast.makeText(Login.this, "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        TextView create_acc = (TextView) findViewById(R.id.Create_Account);
        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent create_acc_intent = new Intent(Login.this, Sign_Up.class);
                startActivity(create_acc_intent);
            }
        });

        vForgot_Password.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(Login.this, "Reset Link Sent to your mail", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

        vCreate_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Sign_Up.class));
                finish();
            }
        });

        vTo_Open_Corporation_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Login_Corporation.class));
                finish();
            }
        });
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

    ;
}
