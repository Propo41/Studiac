package com.valhalla.studiac.adapters.messenger;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Message;
import com.valhalla.studiac.utility.Common;

import java.util.ArrayList;


public class MessageRecycleAdapter extends RecyclerView.Adapter<MessageRecycleAdapter.MessageViewHolder> {

    private static final int USER_1 = 1;
    private static final int USER_2 = 2;
    private ArrayList<Message> mMessages;
    private static int mUserImage;
    private static String mUserUid;
    private View mExpandedView = null;
    private int mExpandedPos = -1;


    public MessageRecycleAdapter(ArrayList<Message> messages, String userImage, String userUid, Context context) {
        this.mMessages = messages;
        mUserUid = userUid;
        mUserImage = getImageDrawableId(userImage, context);

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageViewHolder messageViewHolder;
        View view;
        if (viewType == USER_1) {
            // if the user is the current user
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_2, parent, false);
            messageViewHolder = new MessageViewHolder(view, USER_2); // view holder

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_1, parent, false);
            messageViewHolder = new MessageViewHolder(view, USER_1); // view holder

        }
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {

        holder.mMessage.setText(mMessages.get(position).getText());

        if (mExpandedPos != position) {
            holder.mTime.setVisibility(View.GONE);
        } else {
            holder.mTime.setVisibility(View.VISIBLE);

        }

        holder.mTime.setText(Common.getRelativeTime(mMessages.get(position).getTime()));
        if (!mMessages.get(position).getUserUid().equals(mUserUid)) {
            holder.mUserImage.setImageResource(mUserImage);
        }
        holder.mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExpandedView == null) {
                    mExpandedView = holder.mTime;
                    mExpandedView.setVisibility(View.VISIBLE);
                } else {
                    if (mExpandedView == holder.mTime) {
                        mExpandedView.setVisibility(View.GONE);
                        mExpandedView = null;
                    } else {
                        mExpandedView.setVisibility(View.GONE);
                        mExpandedView = holder.mTime;
                        mExpandedView.setVisibility(View.VISIBLE);
                    }

                }
                mExpandedPos = position;
            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private int getImageDrawableId(String resName, Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(resName, "drawable", Common.PACKAGE_NAME);
    }


    @Override
    public int getItemViewType(int position) {
        if (mMessages.get(position).getUserUid().equals(mUserUid)) {
            return USER_1;
        }
        return USER_2;

    }

    @Override
    public int getItemCount() {
        return mMessages.size();

    }


    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView mMessage;
        ImageView mUserImage;
        TextView mTime;

        MessageViewHolder(View itemView, int user) {
            super(itemView);
            if (user == USER_1) {
                mUserImage = itemView.findViewById(R.id.message_user1_image_id);
                mMessage = itemView.findViewById(R.id.message_user1_message_id);
                mTime = itemView.findViewById(R.id.message_user1_time);
            } else {
                // mUserImage = itemView.findViewById(R.id.message_user2_image_id);
                mMessage = itemView.findViewById(R.id.message_user2_message_id);
                mTime = itemView.findViewById(R.id.message_user2_time);
            }


        }


    }


}
