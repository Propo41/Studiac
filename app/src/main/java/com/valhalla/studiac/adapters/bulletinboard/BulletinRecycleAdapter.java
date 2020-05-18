package com.valhalla.studiac.adapters.bulletinboard;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.models.Post;
import com.valhalla.studiac.utility.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Locale.US;

/*
 * adapter class for the recycler view to implement bulleting board ui
 */

public class BulletinRecycleAdapter extends RecyclerView.Adapter<BulletinRecycleAdapter.BulletinBoardViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<Post> mPosts;


    public BulletinRecycleAdapter(ArrayList<Post> posts) {
        mPosts = posts;
    }


    public interface OnItemClickListener {
        void onButtonClick(int position);

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public BulletinBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BulletinBoardViewHolder bulletinBoardViewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_bulletin_board, parent, false);
        bulletinBoardViewHolder = new BulletinBoardViewHolder(view, mListener, view.getContext()); // view holder
        return bulletinBoardViewHolder;

    }

    private int getImageDrawableId(String resName, Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(resName, "drawable", Common.PACKAGE_NAME);
    }

    @Override
    public void onBindViewHolder(@NonNull BulletinBoardViewHolder holder, int position) {

        // holder.mUserImage.setImageResource(mPosts.get(position).getUserImageRes());
        holder.mUserImage.setImageResource(getImageDrawableId(mPosts.get(position).getAuthorImageRes(), holder.mContext));
        holder.mUserName.setText(mPosts.get(position).getAuthor());
        holder.mTopicName.setText(mPosts.get(position).getTopic());
        holder.mPostTime.setText(mPosts.get(position).getTimePosted());
        holder.mPostDescription.setText(mPosts.get(position).getBody());
        holder.mTopicImage.setImageResource(getTopicImage(position));

    }


    private int getTopicImage(int position) {
        String topic = mPosts.get(position).getTopic();

        switch (topic) {
            case Common.POST_TOPIC_1:
                return Common.TOPIC_NOTICE;
            case Common.POST_TOPIC_2:
                return Common.TOPIC_SEMINAR;
            case Common.POST_TOPIC_3:
                return Common.TOPIC_JOB_OPP;
            case Common.POST_TOPIC_4:
                return Common.TOPIC_CLUB_REC;
            case Common.POST_TOPIC_5:
                return Common.TOPIC_WORKSHOPS;
            case Common.POST_TOPIC_6:
                return Common.TOPIC_GIVEAWAY;
            case Common.POST_TOPIC_7:
                return Common.TOPIC_SALE_POST;
            case Common.POST_TOPIC_8:
                return Common.TOPIC_FIND_ROOMMATES;
            case Common.POST_TOPIC_9:
                return Common.TOPIC_LOST_ITEM;
            case Common.POST_TOPIC_10:
                return Common.TOPIC_BLOOD_DONA;
            case Common.POST_TOPIC_11:
                return Common.TOPIC_OTHERS;
            default:
                throw new IllegalStateException("Unexpected value: " + topic);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();

    }


    static class BulletinBoardViewHolder extends RecyclerView.ViewHolder {
        ImageView mUserImage;
        ImageView mTopicImage;
        TextView mUserName;
        TextView mTopicName;
        TextView mPostTime;
        TextView mPostDescription;
        FloatingActionButton mKnockUserButton;
        Context mContext;


        BulletinBoardViewHolder(View itemView, final OnItemClickListener customListener, Context context) {
            super(itemView);
            mContext = context;
            mUserImage = itemView.findViewById(R.id.bulletin_user_image_id);
            mUserName = itemView.findViewById(R.id.bulletin_user_name_id);
            mTopicName = itemView.findViewById(R.id.bulletin_post_topic_id);
            mPostTime = itemView.findViewById(R.id.bulletin_post_time_id);
            mPostDescription = itemView.findViewById(R.id.bulletin_post_description_id);
            mKnockUserButton = itemView.findViewById(R.id.bulletin_knock_user_id);
            mTopicImage = itemView.findViewById(R.id.bulletin_post_topic_img_id);


            mKnockUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            customListener.onButtonClick(position);
                        }
                    }
                }
            });

        }
    }


}
