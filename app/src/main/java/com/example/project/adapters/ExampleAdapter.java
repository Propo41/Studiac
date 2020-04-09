package com.example.project.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.utility.ExampleItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<ExampleItem> mExampleItems;


    // to extract the data from the Array list (that we created in out MainActivity file) we create a constructor
    public ExampleAdapter(ArrayList<ExampleItem> exampleItems) {
        mExampleItems = exampleItems;
    }

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
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // we need to inflate the views and add those views to the view holder
        ExampleViewHolder exampleViewHolder;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_viewcourses, parent, false);
        exampleViewHolder = new ExampleViewHolder(view, mListener, viewType); // view holder

        // now we return the view holder object
        return exampleViewHolder;

    }

    // binds the data from the array list to the view holder object
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        // we got the data of ArrayList<ExampleItem> from the constructor
        // now what this method does is that it returns the data at position "int position" from the array
        // to bind it to the viewholder
        ExampleItem currentItem = mExampleItems.get(position); // since is one extra row for the header, we are subtracting one
        holder.mCourseNameTv.setText(currentItem.getCourseName());
        holder.mCourseCreditTv.setText(currentItem.getCourseCredit());
        holder.mCourseCodeTv.setText(currentItem.getCourseCode());

    }


    @Override
    public int getItemCount() {
        // we need to return how many items are there in our array list
        return mExampleItems.size();


    }


    // the adapter class needs a viewholder object
    // this view holder object will hold the views that is to be passed onto the recycler view
    // initially, onCreateViewHolder() will be called which will return the view holder object
    // containing the views

    static class ExampleViewHolder extends RecyclerView.ViewHolder {
        TextView mCourseNameTv;
        TextView mCourseCreditTv;
        TextView mCourseCodeTv;
        FloatingActionButton mEditBv;
        int view_type;

        // contains the view ids of the views in the recycler view
        // we are passing our custom listener object as a constructor because the class is static,
        // and cannot be accessed from down below. It is recommended to keep the class static
        ExampleViewHolder(View itemView, final OnItemClickListener customListener, int view_type) {
            super(itemView);
            mCourseNameTv = itemView.findViewById(R.id.viewCourses_courseName_id);
            mCourseCreditTv = itemView.findViewById(R.id.viewCourses_courseCredit_id);
            mCourseCodeTv = itemView.findViewById(R.id.viewCourses_courseCode_id);
            mEditBv = itemView.findViewById(R.id.viewCourses_edit_button_id);

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
