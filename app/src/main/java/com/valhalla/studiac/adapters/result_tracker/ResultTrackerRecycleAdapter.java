package com.valhalla.studiac.adapters.result_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Result;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.valhalla.studiac.activities.ResultTrackerActivity.mLastSemesterGPA;

public class ResultTrackerRecycleAdapter extends RecyclerView.Adapter<ResultTrackerRecycleAdapter.ResultTrackerViewHolder> {

    private ArrayList<Result> mResultList;
    private AdapterResultTrackerOnItemClickListener mListener;
    private AdapterResultTrackerOnItemClickListener2 mListener2;
    private Context context;
    private int mLastSemesterCoursesTaken;
    private static int TYPE_HEADER = 0;
    private static int TYPE_BODY = 1;
    private Double mGpa;


    public ResultTrackerRecycleAdapter(ArrayList<Result> mResultList, Context context, int mLastSemesterCoursesTaken, Double gpa) {
        this.mResultList = mResultList;
        this.context = context;
        this.mLastSemesterCoursesTaken = mLastSemesterCoursesTaken;
        mGpa = gpa;
        Toast.makeText(context, mResultList.size() + "", Toast.LENGTH_SHORT).show();
    }

    public interface AdapterResultTrackerOnItemClickListener {
        void onEditClick(int position);
    }

    public interface AdapterResultTrackerOnItemClickListener2 {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AdapterResultTrackerOnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemClickListener2(AdapterResultTrackerOnItemClickListener2 listener) {
        mListener2 = listener;
    }

    public static class ResultTrackerViewHolder extends RecyclerView.ViewHolder {
        TextView mSemesterNumber;
        TextView mGPA;
        TextView mCoursesTaken;
        ImageView mBackgroundImage;
        FloatingActionButton button;
        TextView mCGPAText;

        ResultTrackerViewHolder(View itemView, final AdapterResultTrackerOnItemClickListener listener,
                                final AdapterResultTrackerOnItemClickListener2 listener2, int viewType) {
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
                        listener.onEditClick(getAdapterPosition() - 1);
                    }
                });


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener2.onItemClick(position - 1);
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
            evh = new ResultTrackerViewHolder(v, mListener, mListener2, viewType);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_result_tracker, parent, false);
            evh = new ResultTrackerViewHolder(v, mListener, mListener2, viewType);
        }

        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultTrackerViewHolder holder, int position) {
        if (position == 0) {
            if (mGpa == 0.0) {
                holder.mCGPAText.setText(context.getString(R.string.PlaceHolder_CGPA, "N/A"));
            } else {
                holder.mCGPAText.setText(context.getString(R.string.PlaceHolder_CGPA, mGpa.toString()));
            }

        } else {
            position -= 1;
            Result currentItem = mResultList.get(position);
            position++;
            holder.mSemesterNumber.setText("Semester " + position);
            String s = currentItem.getGpa() + "";
            s = s.trim();
            if (mResultList.size() != position) {
                if (s.equals("0.0") || s.equals(null)) {
                    holder.mBackgroundImage.setImageResource(R.drawable.shape_background_faded_vivid_blue);
                    holder.button.setVisibility(View.VISIBLE);
                    holder.mGPA.setText("GPA : N / A ");
                    holder.mCoursesTaken.setText("Courses Taken : N / A ");
                } else {
                    holder.mBackgroundImage.setImageResource(R.drawable.shape_background_vividblue_four);
                    holder.button.setVisibility(View.INVISIBLE);
                    holder.mGPA.setText("GPA : " + currentItem.getGpa());
                    holder.mCoursesTaken.setText("Courses Taken : " + currentItem.getCoursesTaken());
                }
            } else {
                holder.mBackgroundImage.setImageResource(R.drawable.shape_background_vividblue_four);
                holder.button.setVisibility(View.INVISIBLE);
                if (mResultList.get(mResultList.size() - 1).getGpa() == 0 || mResultList.get(mResultList.size() - 1).getGpa() == null) {
                    holder.mGPA.setText("GPA : N / A ");
                    holder.mCoursesTaken.setText("Courses Taken : N / A ");
                } else {
                    holder.mGPA.setText("GPA : " + mLastSemesterGPA);
                    holder.mCoursesTaken.setText("Courses Taken : " + mLastSemesterCoursesTaken);
                }
            }

        }


    }

    @Override
    public int getItemCount() {
        return mResultList.size() + 1;
    }

}
