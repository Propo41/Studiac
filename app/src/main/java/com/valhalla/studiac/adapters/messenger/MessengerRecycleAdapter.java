package com.valhalla.studiac.adapters.messenger;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Message;
import com.valhalla.studiac.models.Messenger;
import com.valhalla.studiac.utility.Common;

import java.util.ArrayList;

/*
 * adapter class for the recycler view to implement bulleting board ui
 */

public class MessengerRecycleAdapter extends RecyclerView.Adapter<MessengerRecycleAdapter.MessengerViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<Messenger> messengerList;
    private FirebaseUser mUser;
    private Context mContext;

    public MessengerRecycleAdapter(ArrayList<Messenger> messengerList) {
        this.messengerList = messengerList;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public interface OnItemClickListener {
        void onSelectMessage(int position);

     //   boolean onLongClick(int position);

        void onProfileClick(int position, View view);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public MessengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessengerViewHolder messengerViewHolder;
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messenger, parent, false);
        messengerViewHolder = new MessengerViewHolder(view, mListener); // view holder
        return messengerViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MessengerViewHolder holder, int position) {
        // if the current user is user 1 then show user 2 details and vice versa
        if (mUser.getUid().equals(messengerList.get(position).getUser2Uid())) {
            int imgId = getImageDrawableId((messengerList.get(position).getUser1Image()), mContext);
            holder.mUserImage.setImageResource(imgId);
            holder.mUserName.setText(messengerList.get(position).getUser1Name());
        } else {
            int imgId = getImageDrawableId((messengerList.get(position).getUser2Image()), mContext);
            holder.mUserImage.setImageResource(imgId);
            holder.mUserName.setText(messengerList.get(position).getUser2Name());
        }
        holder.mTimeSent.setText(Common.getRelativeTime(messengerList.get(position).getTime()));
        holder.mDescription.setText(messengerList.get(position).getLastMessage());


    }


    private int getImageDrawableId(String resName, Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(resName, "drawable", Common.PACKAGE_NAME);
    }


    @Override
    public int getItemCount() {
        return messengerList.size();

    }


    static class MessengerViewHolder extends RecyclerView.ViewHolder {
        TextView mUserName;
        TextView mTimeSent;
        ImageView mUserImage;
        TextView mDescription;

        MessengerViewHolder(View itemView, final OnItemClickListener customListener) {
            super(itemView);
            mUserImage = itemView.findViewById(R.id.messenger_user_image_id);
            mUserName = itemView.findViewById(R.id.messenger_user_name_id);
            mTimeSent = itemView.findViewById(R.id.messenger_time_sent_id);
            mDescription = itemView.findViewById(R.id.messenger_post_description_id);

           /* // upon long press of an item
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (customListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            return customListener.onLongClick(position);
                        }
                    }
                    return false;
                }
            });*/

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


            mUserImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            customListener.onProfileClick(position, mUserImage);
                        }
                    }
                }
            });

        }
    }


}
