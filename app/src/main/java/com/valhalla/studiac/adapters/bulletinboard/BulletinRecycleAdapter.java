package com.valhalla.studiac.adapters.bulletinboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
 * adapter class for the recycler view to implement bulleting board ui
 */

public class BulletinRecycleAdapter extends RecyclerView.Adapter<BulletinRecycleAdapter.BulletinBoardViewHolder> {

    private OnItemClickListener mListener;

    public BulletinRecycleAdapter() {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bulletin_board, parent, false);
        bulletinBoardViewHolder = new BulletinBoardViewHolder(view, mListener); // view holder
        return bulletinBoardViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull BulletinBoardViewHolder holder, int position) {

       // CourseItems currentItem = mCourseItems.get(position); // since is one extra row for the header, we are subtracting oneholder.mCourseNameTv.setText(currentItem.getCourseName());
        holder.mUserName.setText("mofis");
        holder.mCategory.setText("Chocolate");
        holder.mPostTime.setText("Posted 24hrs ago");
        holder.mPostDescription.setText("Hey i wanna sell a kittyy. Anyone interested knock me up!");

    }


    @Override
    public int getItemCount() {
        //  return mCourseItems.size();
        return 10;

    }


    static class BulletinBoardViewHolder extends RecyclerView.ViewHolder {
        TextView mUserName;
        TextView mCategory;
        TextView mPostTime;
        TextView mPostDescription;
        FloatingActionButton mKnockUserButton;


        BulletinBoardViewHolder(View itemView, final OnItemClickListener customListener) {
            super(itemView);
            mUserName = itemView.findViewById(R.id.messenger_user_name_id);
            mCategory = itemView.findViewById(R.id.bulletin_post_category_id);
            mPostTime = itemView.findViewById(R.id.bulletin_post_time_id);
            mPostDescription = itemView.findViewById(R.id.messenger_post_description_id);
            mKnockUserButton = itemView.findViewById(R.id.bulletin_knock_user_id);

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
