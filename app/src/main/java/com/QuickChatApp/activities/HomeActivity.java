package com.QuickChatApp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import android.widget.ImageView;
import android.widget.TextView;

import com.QuickChatApp.R;
import com.QuickChatApp.clases.User;
import com.QuickChatApp.adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
   FirebaseAuth auth;
   RecyclerView mainUserRecycler;
   UserAdapter adapter;
   FirebaseDatabase database;
   ArrayList<User> userArrayList;
   ImageView img_logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        userArrayList= new ArrayList<>();
        DatabaseReference reference=database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           for (DataSnapshot dataSnapshot:snapshot.getChildren()){
               User user= dataSnapshot.getValue(User.class);
               userArrayList.add(user);
           }
           adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        img_logOut=findViewById(R.id.img_logOut);
        mainUserRecycler=findViewById(R.id. mainUserRecycler);
        mainUserRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter= new UserAdapter(HomeActivity.this,userArrayList);
        mainUserRecycler.setAdapter(adapter);


        img_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 show();


            }
        });
        if (auth.getCurrentUser()== null){
            startActivity( new Intent(HomeActivity.this,RegistrationActivity.class));
        }

    }

    private  void  show(){
        Dialog dialog= new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.dilog_layout);
        TextView yesBtn,NoBtn;
        yesBtn=dialog.findViewById(R.id.yesBtn);
        NoBtn=dialog.findViewById(R.id.noBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this,RegistrationActivity.class));
            }
        });
        NoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}