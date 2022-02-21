package com.QuickChatApp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.QuickChatApp.adapter.MessageAdapter;
import com.QuickChatApp.clases.Messages;
import com.QuickChatApp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String ReceiveImage, ReceiveName,ReceiveUid,SenderUid;
    CircleImageView profile1_image;
    TextView receiver_Name;

    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public  static  String sImage;
    public  static  String rImage;

    CardView sendBtn;
    EditText editMessage;
     String senderRoom, receiverRoom;

     RecyclerView message_adapter;

    ArrayList<Messages>messagesArrayList;

    MessageAdapter Adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        Intent intent = getIntent();
        ReceiveName=intent.getStringExtra("name");
        ReceiveImage=intent.getStringExtra("ReceiveImage");
        ReceiveUid=intent.getStringExtra("uid");
        sendBtn=findViewById(R.id.sendBtn);

        message_adapter=findViewById(R.id.message_adapter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        message_adapter.setLayoutManager(linearLayoutManager);
        editMessage=findViewById(R.id.editMessage);
        profile1_image=findViewById(R.id. profile1_image);
        Picasso.get().load(ReceiveImage).into(profile1_image);
        receiver_Name=findViewById(R.id.receiver_Name);
        receiver_Name.setText(" "+ReceiveName);
        SenderUid=firebaseAuth.getUid();

        senderRoom=SenderUid+ReceiveUid;
        receiverRoom=ReceiveUid+SenderUid;
        messagesArrayList= new ArrayList<>();

        DatabaseReference databaseReference= database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference= database.getReference().child("chat").child(senderRoom).child("messages");
        chatReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                   messagesArrayList.clear();
                 for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                     Messages messages=dataSnapshot.getValue(Messages.class);
                     messagesArrayList.add(messages);
                 }
                 if(messagesArrayList.size()>0) {
                     Adapter = new MessageAdapter(ChatActivity.this, messagesArrayList);
                     message_adapter.setAdapter(Adapter);
                     Adapter.notifyDataSetChanged();
                 }else{
                     message_adapter.setVisibility(View.GONE);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sImage=snapshot.child("imageUri").getValue().toString();
                rImage=ReceiveImage;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=editMessage.getText().toString();

                if (message.isEmpty()){
                    Toast.makeText(ChatActivity.this,"Please entre message",Toast.LENGTH_SHORT).show();
                    return;
                }
                editMessage.setText("");
                Date date= new Date();
                Messages messages= new Messages(message,SenderUid,date.getTime());
                database.getReference().child("chat")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chat")
                                .child(receiverRoom)
                                .child("messages")
                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                    }
                });

            }
        });


    }
}