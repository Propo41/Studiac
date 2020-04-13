package com.example.project.adapters.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.utility.todo.CurrentTaskItems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class CurrentTasksAdapter extends RecyclerView.Adapter<CurrentTasksAdapter.CurrentTasksViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<CurrentTaskItems> mCurrentTaskItems;

    // creating macros to identify the header and the list
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 1;


    // view holder class for the adapter.
    // it creates objects at runtime for the adapter
    static class CurrentTasksViewHolder extends RecyclerView.ViewHolder {
        TextView mTask;
        CheckBox mCheckBox;
        FloatingActionButton mAddTask;
        int view_type;
        TextView mHeaderTextView;

        CurrentTasksViewHolder(View itemView, final OnItemClickListener customListener, int view_type) {
            super(itemView);
            mHeaderTextView = itemView.findViewById(R.id.header_tv_id);
            mAddTask = itemView.findViewById(R.id.header_bv_id);

            if (view_type == TYPE_LIST) {
                mTask = itemView.findViewById(R.id.task_id);
                mCheckBox = itemView.findViewById(R.id.todo_checkBox_id);

                // when a user clicks on a checkbox, call the method: onItemClick from the interface
                mCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                customListener.onCheckClicked(position);
                            }
                        }
                    }
                });

                // when a user clicks on the item, call the method: onItemClick from the interface
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

                // when the user clicks on the mini add button on the header
            } else if (view_type == TYPE_HEADER) {
                mHeaderTextView = itemView.findViewById(R.id.header_tv_id);
                mAddTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                customListener.onAddClicked(position);
                            }
                        }
                    }
                });


            }

            this.view_type = view_type;


        }
    }


    // to extract the data from the Array list (that we created in out MainActivity file) we create a constructor
    public CurrentTasksAdapter(ArrayList<CurrentTaskItems> currentTaskItems) {
        mCurrentTaskItems = currentTaskItems;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onCheckClicked(int position);

        void onAddClicked(int position);
    }

    // this will be called from our MainActivity
    // we need to handle the onclick item from the ExampleViewHolder class
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        //if position = 0, then show the header, else show the list
        if (position == 0) {
            return TYPE_HEADER; // The macro that we defined
        } else {
            return TYPE_LIST;
        }
    }


    @NonNull
    @Override
    public CurrentTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // we need to inflate the views and add those views to the view holder
        CurrentTasksViewHolder currentTasksViewHolder;

        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_current_header, parent, false);
            currentTasksViewHolder = new CurrentTasksViewHolder(view, mListener, viewType); // view holder
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_current_body, parent, false);
            currentTasksViewHolder = new CurrentTasksViewHolder(view, mListener, viewType); // view holder
        }

        return currentTasksViewHolder;

    }

    // binds the data from the array list to the view holder object
    // this is called frequently to recycle the views
    @Override
    public void onBindViewHolder(@NonNull CurrentTasksViewHolder holder, int position) {

        // we got the data of ArrayList<ExampleItem> from the constructor
        // now what this method does is that it returns the data at position "int position" from the array
        // to bind it to the viewholder
        if (holder.view_type == TYPE_HEADER) {
            holder.mHeaderTextView.setText(R.string.todo_currentTasks_headerText);

        } else {
            CurrentTaskItems currentItem = mCurrentTaskItems.get(position - 1); // since is one extra row for the header, we are subtracting one
            holder.mTask.setText(currentItem.getText1());
            // don't change this ever. Used to reset the checkbox of the recycled item
            holder.mCheckBox.setChecked(false);
        }


    }


    @Override
    public int getItemCount() {
        // we need to return how many items are there in our array list
        return mCurrentTaskItems.size();

    }





}
