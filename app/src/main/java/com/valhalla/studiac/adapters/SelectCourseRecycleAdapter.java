package com.valhalla.studiac.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Course;

import java.util.ArrayList;

/*
 * adapter class for the recycler view to implement select course
 */

public class SelectCourseRecycleAdapter extends RecyclerView.Adapter<SelectCourseRecycleAdapter.SelectCourseViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<Course> mCourseItems;
    RadioButton mCheckedButton = null;


    public SelectCourseRecycleAdapter(ArrayList<Course> courseItems) {
        mCourseItems = courseItems;
    }

    public interface OnItemClickListener {
        void onItemChecked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public SelectCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelectCourseViewHolder selectCourseViewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_select_course, parent, false);
        selectCourseViewHolder = new SelectCourseViewHolder(view); // view holder

        return selectCourseViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final SelectCourseViewHolder holder, final int position) {
        if (position == 0) {
            holder.mRadioButton.setChecked(true);
            mCheckedButton = holder.mRadioButton;
        }
        Course course = mCourseItems.get(position); // since is one extra row for the header, we are subtracting one
        holder.mRadioButton.setText(course.getCode());

        holder.mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // if any of the radio button is not selected then check the selected button
                if (mCheckedButton != null) {
                    mCheckedButton.setChecked(false);
                }
                mCheckedButton = (RadioButton) buttonView;

                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemChecked(position);
                    }
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return mCourseItems.size();
    }

    static class SelectCourseViewHolder extends RecyclerView.ViewHolder {
        RadioButton mRadioButton;

        SelectCourseViewHolder(View itemView) {
            super(itemView);
            mRadioButton = itemView.findViewById(R.id.item_dialog_select_course_radio_button_id);

        }
    }


}
