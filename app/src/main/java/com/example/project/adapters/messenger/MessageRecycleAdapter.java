package com.example.project.adapters.messenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
 * adapter class for the recycler view to implement bulleting board ui
 */

public class MessageRecycleAdapter extends RecyclerView.Adapter<MessageRecycleAdapter.MessageViewHolder> {

    private int USER_1 = 2;
    private int USER_2 = 1;

    public MessageRecycleAdapter() {

    }



    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageViewHolder messageViewHolder;
        View view;
        if(viewType == USER_1){
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_1, parent, false);
        }else{
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_2, parent, false);
        }
        messageViewHolder = new MessageViewHolder(view); // view holder
        return messageViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        holder.mMessage.setText("this is a random text message");
        //holder.mUserImage.setText("3d ago");
        // holder.mUserImage.setImageResource(...);
        // holder.mMessageTypeImage.setImageResource(...);

    }

    @Override
    public int getItemViewType(int position) {
        if(position%2 == 0){
            return USER_2;
        }
        return USER_1;
    }

    @Override
    public int getItemCount() {
        return 10;

    }


    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView mMessage;
        ImageView mUserImage;

        MessageViewHolder(View itemView) {
            super(itemView);
            mUserImage = itemView.findViewById(R.id.message_user1_image_id);
            mMessage = itemView.findViewById(R.id.message_user1_message_id);

        }
    }


}
