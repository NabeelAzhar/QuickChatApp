package com.QuickChatApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.QuickChatApp.R;
import com.QuickChatApp.clases.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.QuickChatApp.activities.ChatActivity.rImage;
import static com.QuickChatApp.activities.ChatActivity.sImage;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;

    public MessageAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType== ITEM_SEND){
           View view= LayoutInflater.from(context).inflate(R.layout.sender_item,parent,false);
           return  new SenderViewHolder(view);
        } else {
            View view= LayoutInflater.from(context).inflate(R.layout.receiver_item,parent,false);
            return  new ReceiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages= messagesArrayList.get(position);

        if (holder.getClass()==SenderViewHolder.class){
        SenderViewHolder viewHolder= ( SenderViewHolder) holder;
        viewHolder.txtMessages.setText(messages.getMessage());
            Picasso.get().load(sImage).into(viewHolder.circleImageView);

        } else {

            ReceiverViewHolder viewHolder= ( ReceiverViewHolder) holder;
            viewHolder.txtMessages.setText(messages.getMessage());
            Picasso.get().load(rImage).into(viewHolder.circleImageView);

        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
      Messages messages= messagesArrayList.get(position);
      if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
          return ITEM_SEND;
      } else {
          return ITEM_RECEIVE;
      }
    }

    class  SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtMessages;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.profile_image);
            txtMessages=itemView.findViewById(R.id.txtMessages);
        }
    }
    class  ReceiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtMessages;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.profile_image);
            txtMessages=itemView.findViewById(R.id.txtMessages);
        }
    }
}
