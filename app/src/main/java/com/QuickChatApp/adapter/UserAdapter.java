package com.QuickChatApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.QuickChatApp.R;
import com.QuickChatApp.activities.ChatActivity;
import com.QuickChatApp.activities.HomeActivity;
import com.QuickChatApp.clases.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {

    Context homeActivity;
    ArrayList<User> userArrayList;
    public UserAdapter(HomeActivity homeActivity, ArrayList<User> userArrayList) {
        this.homeActivity=homeActivity;
        this.userArrayList=userArrayList;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view= LayoutInflater.from(homeActivity).inflate(R.layout.item_user_row,parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
      User user= userArrayList.get(position);
      holder.user_name.setText(user.name);
      holder.user_status.setText(user.status);
        Picasso.get().load(user.imageUri).into(holder.user_profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(homeActivity, ChatActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("ReceiveImage",user.getImageUri());
                intent.putExtra("uid",user.getUid());
                homeActivity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView user_profile;
        TextView user_name,user_status;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            user_profile = itemView.findViewById(R.id.user_image) ;
            user_name = itemView.findViewById(R.id.user_name) ;
            user_status = itemView.findViewById(R.id.user_status) ;
        }
    }
}
