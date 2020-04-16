package com.example.project.adapters.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;
import com.example.project.utility.todo.Days;
import com.example.project.utility.todo.TaskItems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<Days> mItems;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 1;



    public UpcomingAdapter(ArrayList<Days> items) {
        mItems = items;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onButtonClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_LIST;
        }
    }


    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        UpcomingViewHolder upcomingViewHolder;

        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_upcoming_header, parent, false);
            upcomingViewHolder = new UpcomingViewHolder(view, mListener, viewType); // view holder
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_upcoming_body, parent, false);
            upcomingViewHolder = new UpcomingViewHolder(view, mListener, viewType); // view holder
        }
        return upcomingViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, int position) {


        // @TODO: random values are inserted. Add real values with real propoer classes
        if (holder.view_type == TYPE_HEADER) {
            holder.mHeader.setText("APRIL 2020");
        } else {
            holder.mDate.setText("16");
            holder.mDay.setText("Tue");
            holder.mCourseName.setText("ALGO 1200");
            holder.mTask.setText("online on merge sort");

        }
    }

    @Override
    public int getItemCount() {
        // we need to return how many items are there in our array list
        // return mTaskItems.size();
        return 15;


    }

    static class UpcomingViewHolder extends RecyclerView.ViewHolder {
        TextView mHeader;
        TextView mTask;
        TextView mDate;
        TextView mDay;
        TextView mCourseName;
        FloatingActionButton mAdd;
        int view_type;

        UpcomingViewHolder(View itemView, final OnItemClickListener customListener, int view_type) {
            super(itemView);

            if (view_type == TYPE_LIST) {
                mTask = itemView.findViewById(R.id.todo_upcoming_task_id);
                mCourseName = itemView.findViewById(R.id.todo_upcoming_course_id);
                mDate = itemView.findViewById(R.id.todo_upcoming_date_id);
                mDay = itemView.findViewById(R.id.todo_upcoming_day_id);
                mAdd = itemView.findViewById(R.id.todo_upcoming_add_id);

                mAdd.setOnClickListener(new View.OnClickListener() {
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

            } else{
                mHeader = itemView.findViewById(R.id.todo_upcoming_header_id);
            }

            this.view_type = view_type;
        }
    }


}
