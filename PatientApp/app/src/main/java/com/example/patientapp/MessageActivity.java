package com.example.patientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference reference,ref;
    TextView recievername;
    String recieverUID;
    EditText txt_send;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<Chat> mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        recievername = findViewById(R.id.username);
        txt_send = findViewById(R.id.text_send);
        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        reference = FirebaseDatabase.getInstance().getReference("User").child("Paitent").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recieverUID = dataSnapshot.child("caretaker").getValue().toString();
                ref =FirebaseDatabase.getInstance().getReference("CareTakers").child(recieverUID);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        recievername.setText( dataSnapshot.child("fullname").getValue().toString());
                        readMessage(auth.getUid(),recieverUID);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MessageActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void readMessage(final String uid, final String recieverUID) {
        mChat=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat=snapshot.getValue(Chat.class);
                    if (chat.getReciver().equals(uid)&&chat.getSender().equals(recieverUID) ||
                            chat.getReciver().equals(recieverUID) && chat.getSender().equals(uid))
                    {
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(getApplicationContext(),mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void SubmitMessage(View view) {

        String message = txt_send.getText().toString();
        if (!message.equals("")){
            sendMessage(auth.getUid(),recieverUID, message);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "You Can't Send Empty Message", Toast.LENGTH_SHORT).show();
        }
        txt_send.setText("");

    }

    private void sendMessage(String uid, String recieverUID, String message) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",uid);
        hashMap.put("reciver",recieverUID);
        hashMap.put("message",message);
        reference.child("Chats").push().setValue(hashMap);
    }


    public void back(View view) {
    }
}
