package com.example.patientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class DistanceTravelledActivity extends AppCompatActivity {
  /*  FirebaseAuth auth;
    DatabaseReference reference,ref;
    TextView patientname;
    TextView caretaker;
    String patientUID;
    String caretakerUID;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);


        date();

        /*caretaker= findViewById(R.id.caretaker);
        patientname = findViewById(R.id.patientname);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("CareTakers").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientUID = dataSnapshot.child("patient").getValue().toString();
                caretaker.setText(dataSnapshot.child("fullname").getValue().toString());
                ref =FirebaseDatabase.getInstance().getReference("User").child("Paitent").child(patientUID);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        patientname.setText( dataSnapshot.child("fullname").getValue().toString());


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DistanceTravelledActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void date() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.text_view_date);
        textViewDate.setText(currentDate);
    }
}
