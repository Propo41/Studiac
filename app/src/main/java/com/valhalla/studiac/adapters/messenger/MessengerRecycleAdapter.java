package com.valhalla.studiac.adapters.messenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.valhalla.studiac.R;

/*
 * adapter class for the recycler view to implement bulleting board ui
 */

public class MessengerRecycleAdapter extends RecyclerView.Adapter<MessengerRecycleAdapter.MessengerViewHolder> {

    private OnItemClickListener mListener;

    public MessengerRecycleAdapter() {

    }

    public interface OnItemClickListener {
        void onSelectMessage(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public MessengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessengerViewHolder messengerViewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messenger, parent, false);
        messengerViewHolder = new MessengerViewHolder(view, mListener); // view holder
        return messengerViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MessengerViewHolder holder, int position) {

        holder.mUserName.setText("Sagol");
        holder.mTimeSent.setText("3d ago");
        // holder.mUserImage.setImageResource(...);
        // holder.mMessageTypeImage.setImageResource(...);

    }


    @Override
    public int getItemCount() {
        //  return mCourseItems.size();
        return 10;

    }


    static class MessengerViewHolder extends RecyclerView.ViewHolder {
        TextView mUserName;
        TextView mTimeSent;
        ImageView mUserImage;
        ImageView mMessageTypeImage;

        MessengerViewHolder(View itemView, final OnItemClickListener customListener) {
            super(itemView);
            mUserImage = itemView.findViewById(R.id.messenger_user_image_id);
            mUserName = itemView.findViewById(R.id.messenger_user_name_id);
            mTimeSent = itemView.findViewById(R.id.messenger_time_sent_id);
            mMessageTypeImage = itemView.findViewById(R.id.messenger_ic_type_lost_item_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            customListener.onSelectMessage(position);
                        }
                    }
                }
            });

        }
    }


}
