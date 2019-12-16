package com.example.patientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    ArrayList<String> categories = new ArrayList<String>();
    DatabaseReference reference;
    String spinnervalue = null;
    FirebaseAuth auth;
    Spinner spinner;
    EditText name, uname, contact, pass, confpass;
    String fullname , username , contactno , passwd , type , uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.nametxt);
        uname = findViewById(R.id.unametxt);
        contact = findViewById(R.id.contacttxt);
        pass = findViewById(R.id.passtxt);
        confpass = findViewById(R.id.confpasstxt);
        auth = FirebaseAuth.getInstance();
        spinner = (Spinner) findViewById(R.id.doctxt);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnervalue = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(dataAdapter);

    }

    public void signUp(View view) {
        if (!((name.getText().toString().isEmpty()) || (uname.getText().toString().isEmpty()) || (contact.getText().toString().isEmpty()) || (pass.getText().toString().isEmpty()) || (confpass.getText().toString().isEmpty())  )) {

            if (pass.getText().toString().matches(confpass.getText().toString()))
            {
                int len = pass.getText().toString().length();
                if (len >= 6)
                {
                    String email = uname.getText().toString()+"@gmail.com";
                    final String password = pass.getText().toString();

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
                                reference = FirebaseDatabase.getInstance().getReference("CareTakers");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                        {
                                            String name = dataSnapshot1.child("fullname").getValue().toString();
                                            if (name.equals(spinner.getItemAtPosition(0).toString()))
                                            {
                                                fullname = name;
                                                username =dataSnapshot1.child("uname").getValue().toString();
                                                contactno = dataSnapshot1.child("contactno").getValue().toString();
                                                passwd = dataSnapshot1.child("password").getValue().toString();
                                                uid = dataSnapshot1.getKey();
                                                type = dataSnapshot1.child("type").getValue().toString();
                                            }

                                        }

                                        reference = FirebaseDatabase.getInstance().getReference("CareTakers").child(uid);
                                        reference.removeValue();
                                        reference = FirebaseDatabase.getInstance().getReference("CareTakers").child(uid);
                                        HashMap<String ,String> caretaker = new HashMap<>();
                                        caretaker.put("fullname" , fullname);
                                        caretaker.put("contactno" , contactno);
                                        caretaker.put("password" , passwd);
                                        caretaker.put("uname" , username);
                                        caretaker.put("patient" , auth.getUid());
                                        caretaker.put("type" , type);
                                        reference.setValue(caretaker).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                reference = FirebaseDatabase.getInstance().getReference("User").child("Paitent").child(auth.getUid());
                                                HashMap<String, String> patient = new HashMap<>();
                                                patient.put("fullname", name.getText().toString());
                                                patient.put("uname", uname.getText().toString());
                                                patient.put("contactno", contact.getText().toString());
                                                patient.put("password", pass.getText().toString());
                                                patient.put("caretaker", uid);
                                                patient.put("type" , "patient");
                                                reference.setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                                            startActivity(intent);
                                                        }

                                                    }
                                                });
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "Unmatched Password", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Please fill the form carefully", Toast.LENGTH_SHORT).show();
        }

    }




    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CareTakers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    categories.add(dataSnapshot1.child("fullname").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
