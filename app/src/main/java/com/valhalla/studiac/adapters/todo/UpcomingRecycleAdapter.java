package com.valhalla.studiac.adapters.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.holders.HeaderItem;
import com.valhalla.studiac.models.Task;
import com.valhalla.studiac.models.TasksUtil;
import com.valhalla.studiac.utility.Common;

import java.text.ParseException;

public class UpcomingRecycleAdapter extends RecyclerView.Adapter<UpcomingRecycleAdapter.UpcomingViewHolder> {

    private OnItemClickListener mListener;
    private TasksUtil mItems;
    private Context mContext;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 1;


    public UpcomingRecycleAdapter(TasksUtil items, Context context) {
        mItems = items;
        mContext = context;
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
        if (mItems.getTodoTasks().get(position) instanceof HeaderItem) {
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
        if (holder.view_type == TYPE_HEADER) {
            holder.mHeader.setText(((HeaderItem) mItems.getTodoTasks().get(position)).getHeader());
        } else {
            Task task = (Task) mItems.getTodoTasks().get(position);
            String date = task.getSchedule();
            String[] dateContents = date.split("-");
            holder.mDate.setText(dateContents[0]);
            try {
                holder.mDay.setText(Common.convertDateToDay(task.getSchedule()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String category = task.getCategory();
            holder.mTaskName.setText(category);
            holder.mTaskDescription.setText(task.getDescription());
            Integer colour = Common.TASK_TAG_COLOURS.get(task.getTaskType());
            if (colour != null) {
                holder.mRoot.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), colour, null));
            }else{
                // colour to show if anything goes wrong
                holder.mRoot.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorAshHint, null));

            }

        }
    }

    @Override
    public int getItemCount() {
        // we need to return how many items are there in our array list
        // return mTaskItems.size();
        return mItems.getTodoTasks().size();
    }

    static class UpcomingViewHolder extends RecyclerView.ViewHolder {
        TextView mHeader;
        TextView mTaskDescription;
        TextView mDate;
        TextView mDay;
        TextView mTaskName;
        FloatingActionButton mAdd;
        ConstraintLayout mRoot;
        int view_type;

        UpcomingViewHolder(View itemView, final OnItemClickListener customListener, int view_type) {
            super(itemView);

            if (view_type == TYPE_LIST) {
                mTaskDescription = itemView.findViewById(R.id.todo_upcoming_task_id);
                mTaskName = itemView.findViewById(R.id.todo_upcoming_course_id);
                mDate = itemView.findViewById(R.id.todo_upcoming_date_id);
                mDay = itemView.findViewById(R.id.todo_upcoming_day_id);
                mAdd = itemView.findViewById(R.id.todo_upcoming_add_id);
                mRoot = itemView.findViewById(R.id.todo_upcoming_root_id);

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

            } else {
                mHeader = itemView.findViewById(R.id.todo_upcoming_header_id);
            }

            this.view_type = view_type;
        }
    }


}
