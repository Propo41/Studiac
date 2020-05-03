package com.example.project.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.todo.CurrentTasksRecycleAdapter;
import com.example.project.utility.common.Common;
import com.example.project.utility.common.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CommonAddScheduleRecycleAdapter extends RecyclerView.Adapter<CommonAddScheduleRecycleAdapter.CommonAddScheduleViewHolder> {

    private ArrayList<Schedule> mSchedules;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    static class CommonAddScheduleViewHolder extends RecyclerView.ViewHolder {
        FloatingActionButton mRemoveButtonView;
        TextView mDayView;
        TextView mStartView;
        TextView mEndView;

        CommonAddScheduleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mDayView = itemView.findViewById(R.id.common_add_schedule_item_name_id);
            mStartView = itemView.findViewById(R.id.common_add_schedule_item_start_id);
            mEndView = itemView.findViewById(R.id.common_add_schedule_item_end_id);
            mRemoveButtonView = itemView.findViewById(R.id.common_add_schedule_item_remove_id);


            mRemoveButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRemoveClick(position);
                        }
                    }
                }
            });
        }


    }

    public CommonAddScheduleRecycleAdapter(ArrayList<Schedule> schedules) {
        mSchedules = schedules;

    }


    public interface OnItemClickListener {
        void onRemoveClick(int position);
    }



    @NonNull
    @Override
    public CommonAddScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CommonAddScheduleViewHolder commonAddScheduleViewHolder;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common_recycler_view_schedule, parent, false);
        commonAddScheduleViewHolder = new CommonAddScheduleViewHolder(view, mListener); // view holder

        return commonAddScheduleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommonAddScheduleViewHolder holder, int position) {
        holder.mDayView.setText(mSchedules.get(position).getName());
        holder.mStartView.setText(Common.convertTo12hFormat(mSchedules.get(position).getStartTime()));
        holder.mEndView.setText(Common.convertTo12hFormat(mSchedules.get(position).getEndTime()));
        holder.mRemoveButtonView.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return mSchedules.size();
    }


}


