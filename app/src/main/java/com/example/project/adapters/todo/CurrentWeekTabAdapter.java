package com.example.project.adapters.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.utility.todo.CurrentWeekItems;

import java.util.ArrayList;

/*
 * adapter to show contents of each tab in (current-week)
 * each tab is a corresponding day. The contents for each day is passed along through the constructor
 */

public class CurrentWeekTabAdapter extends RecyclerView.Adapter<CurrentWeekTabAdapter.ExampleViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<CurrentWeekItems> mCurrentWeekItems;

    // creating macros to identify the header and the list
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 1;


    // to extract the data from the Array list (that we created in out MainActivity file) we create a constructor
    public CurrentWeekTabAdapter(ArrayList<CurrentWeekItems> currentWeekItems){
        mCurrentWeekItems = currentWeekItems;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onButtonClick(int position);

    }

    // this will be called from our MainActivity
    // we need to handle the onclick item from the ExampleViewHolder class
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        //if position = 0, then show the header, else show the list
        if(mCurrentWeekItems.get(position).isSection()){
            return TYPE_HEADER; // The macro that we defined
        }else{
            return TYPE_LIST;
        }
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // we need to inflate the views and add those views to the view holder
        ExampleViewHolder exampleViewHolder;


        if(viewType == TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_week_header, parent, false);
            exampleViewHolder = new ExampleViewHolder(view, mListener, viewType); // view holder
        }else{

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_week_body, parent, false);
            exampleViewHolder = new ExampleViewHolder(view, mListener, viewType); // view holder
        }

        // now we return the view holder object
        return exampleViewHolder;

    }

    // binds the data from the array list to the view holder object
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        // we got the data of ArrayList<ExampleItem> from the constructor
        // now what this method does is that it returns the data at position "int position" from the array
        // to bind it to the viewholder

        if(holder.view_type == TYPE_HEADER){
            holder.mHeaderTextView.setText("ALGORITHM 1230");

        }else{
            CurrentWeekItems currentItem = mCurrentWeekItems.get(position-1); // since is one extra row for the header, we are subtracting one
            holder.mTask.setText(currentItem.getText1());
           // ShapeDrawable gradientDrawable = (ShapeDrawable) holder.mImageView.getBackground();
            //gradientDrawable.setTint(Color.BLUE);
        }


    }



    @Override
    public int getItemCount() {
        // we need to return how many items are there in our array list
        return mCurrentWeekItems.size();



    }


    // the adapter class needs a viewholder object
    // this view holder object will hold the views that is to be passed onto the recycler view
    // initially, onCreateViewHolder() will be called which will return the view holder object
    // containing the views

     static class ExampleViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mTask;
        int view_type;
        TextView mHeaderTextView;

        // contains the view ids of the views in the recycler view
        // we are passing our custom listener object as a constructor because the class is static,
        // and cannot be accessed from down below. It is recommended to keep the class static
        ExampleViewHolder(View itemView, final OnItemClickListener customListener, int view_type){
            super(itemView);
            if(view_type == TYPE_LIST){
               // mImageView = itemView.findViewById(R.id.img_id);
              //  mTextView = itemView.findViewById(R.id.text1_id);
                mTask = itemView.findViewById(R.id.item_id);
                mImageView = itemView.findViewById(R.id.color_image_id);


                // when a user clicks on an item, we will be referring that click to the
                // custom listener method onItemClick(int position)
                // we get the position of the click by calling getAdapterPosition()
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(customListener!= null){
                            int position = getAdapterPosition();
                            if(position!= RecyclerView.NO_POSITION){
                                customListener.onItemClick(position);
                            }
                        }
                    }
                });

                this.view_type = view_type;

            }else if(view_type == TYPE_HEADER){
                mHeaderTextView = itemView.findViewById(R.id.header_id);
                this.view_type = view_type;

            }

        }
    }




}
