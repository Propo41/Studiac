package com.valhalla.studiac.adapters.bulletinboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Post;
import com.valhalla.studiac.utility.Common;

import java.util.ArrayList;

/*
 * adapter class for the recycler view to implement bulleting board ui
 */

public class BulletinMyPostsRecycleAdapter extends RecyclerView.Adapter<BulletinMyPostsRecycleAdapter.BulletinBoardViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<Post> mPosts;


    public BulletinMyPostsRecycleAdapter(ArrayList<Post> posts) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_bulletin_board_my_posts, parent, false);
        bulletinBoardViewHolder = new BulletinBoardViewHolder(view, mListener); // view holder
        return bulletinBoardViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull BulletinBoardViewHolder holder, int position) {
        holder.mCategoryName.setText(mPosts.get(position).getCategory());
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
        ImageView mTopicImage;
        TextView mTopicName;
        TextView mCategoryName;
        TextView mPostTime;
        TextView mPostDescription;
        FloatingActionButton mDeletePostButton;


        BulletinBoardViewHolder(View itemView, final OnItemClickListener customListener) {
            super(itemView);
            mCategoryName = itemView.findViewById(R.id.bulletin_my_posts_category_id);
            mTopicName = itemView.findViewById(R.id.bulletin_my_posts_topic_id);
            mPostTime = itemView.findViewById(R.id.bulletin_my_posts_time_id);
            mPostDescription = itemView.findViewById(R.id.bulletin_my_posts_description_id);
            mDeletePostButton = itemView.findViewById(R.id.bulletin_my_posts_delete_button_id);
            mTopicImage = itemView.findViewById(R.id.bulletin_my_posts_topic_img_id);

            mDeletePostButton.setOnClickListener(new View.OnClickListener() {
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
