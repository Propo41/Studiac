package com.valhalla.studiac.adapters.result_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Pojo;
import com.valhalla.studiac.models.Result;

import java.util.ArrayList;


public class ResultTrackerRecycleAdapter extends RecyclerView.Adapter<ResultTrackerRecycleAdapter.ResultTrackerViewHolder> {

    private ArrayList<Result> mResultList;
    private OnItemClickListener mListener;
    private Context context;
    private final static int TYPE_HEADER = 0;
    private final static int TYPE_BODY = 1;
    private Pojo GPA;


    public ResultTrackerRecycleAdapter(ArrayList<Result> mResultList, Context context, Pojo mGPA) {
        this.mResultList = mResultList;
        this.context = context;
        this.GPA = mGPA;
    }

    public interface OnItemClickListener {
        void onEditClick(int position);

        void onItemClick(int position);

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ResultTrackerViewHolder extends RecyclerView.ViewHolder {
        TextView mSemesterNumber;
        TextView mGPA;
        TextView mCoursesTaken;
        ImageView mBackgroundImage;
        FloatingActionButton button;
        TextView mCGPAText;

        ResultTrackerViewHolder(View itemView, final OnItemClickListener listener, int viewType) {
            super(itemView);

            if (viewType == TYPE_BODY) {
                mSemesterNumber = itemView.findViewById(R.id.activity_result_tracker_item_semester_text);
                mGPA = itemView.findViewById(R.id.activity_result_tracker_item_gpa_text);
                mCoursesTaken = itemView.findViewById(R.id.activity_result_tracker_item_courses_taken_text);
                mBackgroundImage = itemView.findViewById(R.id.activity_result_tracker_item_image_view);
                button = itemView.findViewById(R.id.activity_result_tracker_item_add_gpa_button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onEditClick(getAdapterPosition() - 1);
                            }
                        }
                    }
                });


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onItemClick(getAdapterPosition() - 1);
                            }
                        }
                    }
                });
            } else {
                mCGPAText = itemView.findViewById(R.id.gpaText2);

            }


        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_BODY;

    }

    @NonNull
    @Override
    public ResultTrackerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ResultTrackerViewHolder evh;
        if (viewType == TYPE_BODY) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_body_result_tracker, parent, false);
            evh = new ResultTrackerViewHolder(v, mListener, viewType);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_result_tracker, parent, false);
            evh = new ResultTrackerViewHolder(v, mListener, viewType);
        }

        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultTrackerViewHolder holder, int position) {
        if (position == 0) {
            holder.mCGPAText.setText(context.getString(R.string.PlaceHolder_CGPA, GPA.getGPA() + ""));
        } else {
            position -= 1;
            Result currentItem = mResultList.get(position);
            position++;
            holder.mSemesterNumber.setText(context.getString(R.string.PlaceHolder_Semester, position + ""));

            if (mResultList.size() != position) {
                if (!currentItem.getIsGpaCalculated()) {
                    holder.mBackgroundImage.setImageResource(R.drawable.shape_background_faded_vivid_blue);
                    holder.button.setVisibility(View.VISIBLE);
                    holder.mGPA.setText(context.getString(R.string.PlaceHolder_GPA, "N/A"));
                    holder.mCoursesTaken.setText(context.getString(R.string.PlaceHolder_CoursesTaken, "N/A"));
                } else {
                    holder.mBackgroundImage.setImageResource(R.drawable.shape_background_vividblue_four);
                    holder.button.setVisibility(View.INVISIBLE);
                    holder.mGPA.setText(context.getString(R.string.PlaceHolder_GPA, currentItem.getGpa() + ""));
                    holder.mCoursesTaken.setText(context.getString(R.string.PlaceHolder_CoursesTaken,
                            currentItem.getCoursesTaken() + ""));

                }
            } else {
                // if it's the last item in the list
                // change the colour of the last semester
                holder.mBackgroundImage.setImageResource(R.drawable.shape_background_purple);
                holder.button.setVisibility(View.INVISIBLE);
                if (!currentItem.getIsGpaCalculated()) {
                    holder.mGPA.setText(context.getString(R.string.PlaceHolder_GPA, "N/A"));
                    holder.mCoursesTaken.setText(context.getString(R.string.PlaceHolder_CoursesTaken, "N/A"));
                } else {
                    holder.mGPA.setText(context.getString(R.string.PlaceHolder_GPA, currentItem.getGpa() + ""));
                    holder.mCoursesTaken.setText(context.getString(R.string.PlaceHolder_CoursesTaken,
                            currentItem.getCoursesTaken() + ""));
                }
            }

        }


    }

    @Override
    public int getItemCount() {
        return mResultList.size() + 1;
    }


}
