package com.valhalla.studiac.adapters.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Schedule;
import java.util.ArrayList;

public class ViewScheduleRecycleAdapter extends RecyclerView.Adapter<ViewScheduleRecycleAdapter.ViewRoutineViewHolder> {

    private ArrayList<Schedule> mSchedule;

    // view holder class
    static class ViewRoutineViewHolder extends RecyclerView.ViewHolder {
        TextView mCourse;
        TextView mStartTime;
        TextView mEndTime;

        ViewRoutineViewHolder(@NonNull View view) {
            super(view);
            mCourse = view.findViewById(R.id.common_add_schedule_item_name_id);
            mStartTime = view.findViewById(R.id.common_add_schedule_item_start_id);
            mEndTime = view.findViewById(R.id.common_add_schedule_item_end_id);
        }

    }

    public ViewScheduleRecycleAdapter(ArrayList<Schedule> schedule) {
        mSchedule = schedule;
    }

    @NonNull
    @Override
    public ViewRoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common_recycler_view_schedule, parent, false);
        return new ViewRoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewRoutineViewHolder holder, int position) {
        Schedule schedule = mSchedule.get(position);
        holder.mCourse.setText(schedule.getName());
        holder.mStartTime.setText(schedule.getStartTime());
        holder.mEndTime.setText(schedule.getEndTime());


    }

    @Override
    public int getItemCount() {
        return mSchedule.size();
    }


}
