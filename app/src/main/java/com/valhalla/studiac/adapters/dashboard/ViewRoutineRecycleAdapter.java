package com.valhalla.studiac.adapters.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Schedule;
import com.valhalla.studiac.utility.Common;

import java.util.ArrayList;

public class ViewRoutineRecycleAdapter extends RecyclerView.Adapter<ViewRoutineRecycleAdapter.ViewRoutineViewHolder> {

    private ArrayList<Schedule> mSchedule;
    private OnItemClickListener mListener;



    ViewRoutineRecycleAdapter(ArrayList<Schedule> schedule) {
        mSchedule = schedule;
    }

    public interface OnItemClickListener {
        void onCourseClick(int position);

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    // view holder class
    static class ViewRoutineViewHolder extends RecyclerView.ViewHolder {
        TextView mCourse;
        TextView mStartTime;
        TextView mEndTime;

        ViewRoutineViewHolder(@NonNull View view, final OnItemClickListener listener) {
            super(view);
            mCourse = view.findViewById(R.id.common_add_schedule_item_name_id);
            mStartTime = view.findViewById(R.id.common_add_schedule_item_start_id);
            mEndTime = view.findViewById(R.id.common_add_schedule_item_end_id);

            mCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCourseClick(position);
                        }
                    }
                }
            });
        }

    }


    @NonNull
    @Override
    public ViewRoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common_recycler_view_schedule, parent, false);
        return new ViewRoutineViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewRoutineViewHolder holder, int position) {
        Schedule schedule = mSchedule.get(position);

        holder.mCourse.setText(schedule.getName());
        holder.mStartTime.setText(schedule.getStartTime());
        holder.mEndTime.setText(schedule.getEndTime());
        String type = schedule.getType();

        if (type.equals(Common.COURSE_TYPE_2)) {
            holder.mCourse.setBackgroundResource(R.drawable.shape_background_view_routine_1_purple);
        }
    }

    @Override
    public int getItemCount() {
        return mSchedule.size();
    }


}
