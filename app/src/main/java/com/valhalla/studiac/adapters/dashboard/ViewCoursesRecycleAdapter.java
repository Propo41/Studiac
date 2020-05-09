package com.valhalla.studiac.adapters.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.common.Course;

import java.util.ArrayList;

/*
 * adapter class for the recycler view to implement view courses ui
 */

public class ViewCoursesRecycleAdapter extends RecyclerView.Adapter<ViewCoursesRecycleAdapter.ViewCoursesViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<Course> mCourseItems;


    // to extract the data from the Array list (that we created in out MainActivity file) we create a constructor
    public ViewCoursesRecycleAdapter(ArrayList<Course> courseItems) {
        mCourseItems = courseItems;


    }

    // since each card view item has 2 types of interaction:
    // 1. clicking on the entire block
    // 2. clicking on the edit button
    // we have 2 methods in our custom interface
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onButtonClick(int position);

    }

    // this will be called from our MainActivity
    // we need to handle the onclick item from the ExampleViewHolder class
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public ViewCoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // we need to inflate the views and add those views to the view holder
        ViewCoursesViewHolder viewCoursesViewHolder;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_viewcourses, parent, false);
        viewCoursesViewHolder = new ViewCoursesViewHolder(view, mListener, viewType); // view holder

        // now we return the view holder object
        return viewCoursesViewHolder;

    }

    // binds the data from the array list to the view holder object
    @Override
    public void onBindViewHolder(@NonNull ViewCoursesViewHolder holder, int position) {

        if (mCourseItems.size() == 0) {
            // show a prompt image that no courses are added yet

        } else {
            Course course = mCourseItems.get(position);
            holder.mCourseNameTv.setText(course.getName());
            holder.mCourseCreditTv.setText("Course Credit: " + course.getCredit());
            holder.mCourseCodeTv.setText("Course Code: " + course.getCode());
        }

    }


    @Override
    public int getItemCount() {
        // we need to return how many items are there in our array list
        return mCourseItems.size();
    }


    // the adapter class needs a viewholder object
    // this view holder object will hold the views that is to be passed onto the recycler view
    // initially, onCreateViewHolder() will be called which will return the view holder object
    // containing the views
    static class ViewCoursesViewHolder extends RecyclerView.ViewHolder {
        TextView mCourseNameTv;
        TextView mCourseCreditTv;
        TextView mCourseCodeTv;
        FloatingActionButton mEditBv;
        int view_type;

        // contains the view ids of the views in the recycler view
        // we are passing our custom listener object as a constructor because the class is static,
        // and cannot be accessed from down below. It is recommended to keep the class static
        ViewCoursesViewHolder(View itemView, final OnItemClickListener customListener, int view_type) {
            super(itemView);
            mCourseNameTv = itemView.findViewById(R.id.messenger_user_name_id);
            mCourseCreditTv = itemView.findViewById(R.id.viewCourses_courseCredit_id);
            mCourseCodeTv = itemView.findViewById(R.id.bulletin_post_category_id);
            mEditBv = itemView.findViewById(R.id.bulletin_knock_user_id);

            // when a user clicks on an item, we will be referring that click to the
            // custom listener method onItemClick(int position)
            // we get the position of the click by calling getAdapterPosition()
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            customListener.onItemClick(position);

                        }
                    }
                }
            });


            mEditBv.setOnClickListener(new View.OnClickListener() {
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

            this.view_type = view_type;


        }
    }


}
