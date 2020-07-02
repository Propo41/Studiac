package com.valhalla.studiac.adapters.todo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.holders.HeaderItem;
import com.valhalla.studiac.holders.ListItem;
import com.valhalla.studiac.models.Task;
import com.valhalla.studiac.utility.Common;

import java.util.ArrayList;

/*
 * adapter to convert contents into recycler view to show contents of each tab in (current-week)
 * each tab is a corresponding day. The contents for each day is passed along through the constructor
 */

public class CurrentWeekRecycleAdapter extends RecyclerView.Adapter<CurrentWeekRecycleAdapter.CurrentWeekTabViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<ListItem> mTaskItems;
    private Context mContext;

    //  mTaskItems only contains a series of String(header) and Task(body) instances
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 1;

    static class CurrentWeekTabViewHolder extends RecyclerView.ViewHolder {
        CardView mImageView;
        TextView mTask;
        FloatingActionButton mAddTask;
        int view_type;
        TextView mHeaderTextView;


        CurrentWeekTabViewHolder(View itemView, final OnItemClickListener customListener, int view_type) {
            super(itemView);
            if (view_type == TYPE_LIST) {
                mTask = itemView.findViewById(R.id.todo_week_item_id);
                mImageView = itemView.findViewById(R.id.todo_week_color_id);
                mAddTask = itemView.findViewById(R.id.todo_upcoming_add_id);

                // when the add button is clicked
                mAddTask.setOnClickListener(new View.OnClickListener() {
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

                // when the item is clicked
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
                mHeaderTextView = itemView.findViewById(R.id.todo_week_header_id);
            }

            this.view_type = view_type;


        }
    }


    public CurrentWeekRecycleAdapter(ArrayList<ListItem> taskItems, Context context) {
        mTaskItems = taskItems;
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

        if (mTaskItems.get(position) instanceof HeaderItem) {
            return TYPE_HEADER;
        } else {
            return TYPE_LIST;
        }
    }


    @NonNull
    @Override
    public CurrentWeekTabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CurrentWeekTabViewHolder currentWeekTabViewHolder;
        if (viewType == TYPE_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_week_body, parent, false);
            currentWeekTabViewHolder = new CurrentWeekTabViewHolder(view, mListener, viewType); // view holder
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_week_header, parent, false);
            currentWeekTabViewHolder = new CurrentWeekTabViewHolder(view, mListener, viewType); // view holder
        }
        return currentWeekTabViewHolder;

    }

    // binds the data from the array list to the view holder object
    @Override
    public void onBindViewHolder(@NonNull CurrentWeekTabViewHolder holder, int position) {
        if (holder.view_type == TYPE_HEADER) {
            holder.mHeaderTextView.setText(((HeaderItem) mTaskItems.get(position)).getHeader());
        } else {
            Task task = (Task) mTaskItems.get(position); // since is one extra row for the header, we are subtracting one
            holder.mTask.setText(task.getDescription());

            Integer colour = Common.TASK_TAG_COLOURS.get(task.getTaskType());
            if (colour != null) {
                holder.mImageView.setCardBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), colour, null));
                // ShapeDrawable gradientDrawable = (ShapeDrawable) holder.mImageView.getBackground();
                //gradientDrawable.setTint(Color.BLUE);
            }else{
                // colour to show if anything goes wrong
                holder.mImageView.setCardBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorAshHint, null));

            }

        }


    }

    @Override
    public int getItemCount() {
        if (mTaskItems.size() == 1) {
            return 0;
        }
        return mTaskItems.size();
    }


}
