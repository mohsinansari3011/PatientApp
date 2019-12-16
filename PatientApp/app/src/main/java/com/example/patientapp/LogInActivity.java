package com.example.patientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {
    EditText uname, pass;
    FirebaseAuth auth;
    DatabaseReference reference;
    String type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        uname = findViewById(R.id.usernametxt);
        pass = findViewById(R.id.passwordtxt);
        auth = FirebaseAuth.getInstance();
    }

    public void Login(View view) {

        String email = uname.getText().toString()+"@gmail.com";
        String password = pass.getText().toString();

        if (uname.getText().toString().isEmpty() && password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill the form carefully", Toast.LENGTH_SHORT).show();
            return;
        } else if (uname.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "enter uname", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "enter pw", Toast.LENGTH_SHORT).show();
            return;
        } else if (!(email.isEmpty() || password.isEmpty())) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
/*                        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                        progressDialog.setTitle("Processing...");
                        progressDialog.setMessage("Please wait...");

                        progressDialog.setCancelable(false);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();


                            }
                        }).start();
                        progressDialog.show();*/
                        reference = FirebaseDatabase.getInstance().getReference("User").child("Paitent").child(auth.getUid());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                type = dataSnapshot.child("type").getValue().toString();

                                if (type.equals("patient")) {
                                    Toast.makeText(getApplicationContext(), "SuccessFull", Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(getApplicationContext(), HomeNavActivity.class);
                                    startActivity(intent);

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Enter Correct Email Address and password ", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {


                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to login", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    public void signUp(View view) {

        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);

    }
}
