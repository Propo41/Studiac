package com.valhalla.studiac.adapters.result_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.SpinnerAdapter;
import com.valhalla.studiac.holders.ImageItem;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Result;
import com.valhalla.studiac.models.Weight;

import java.util.ArrayList;


public class CalculateGPARecycleAdapter extends RecyclerView.Adapter<CalculateGPARecycleAdapter.CalculateGPAViewHolder> {

    private ArrayList<ImageItem> mSpinnerList;
    int mBugFixCounter;
    private Context mContext;
    private int mCourseTrackCount; // To keep track of courses when customSpinner list is being created
    public static String fixFirstItemBottomSheetCalculateSemesterGPA;
    public static String fixFirstItemBottomSheetCalculateSemesterGPA2;
    private Result mResult;
    private ArrayList<Weight> mWeightList;
    private ArrayList<Course> mCourseList;

    private boolean mChecker;

    public CalculateGPARecycleAdapter(Context mContext, Result result, ArrayList<ImageItem> mSpinnerList,
                                      ArrayList<Weight> weightList, ArrayList<Course> mCourseList) {
        this.mResult = result;
        this.mWeightList = weightList;
        this.mContext = mContext;
        this.mSpinnerList = mSpinnerList;
        this.mBugFixCounter = 0; // this is used to
        this.mCourseTrackCount = 0;
        this.mCourseList = mCourseList;
    }


    public class CalculateGPAViewHolder extends RecyclerView.ViewHolder {
        TextView mCourseCode;
        Spinner mCourseGradeSpinner;
        ArrayList<ImageItem> mInitializeSpinner;
        int mReturnPosition;

        CalculateGPAViewHolder(View itemView, ArrayList<ImageItem> mPossibleGradeList, final Context context) {
            super(itemView);
            mCourseCode = itemView.findViewById(R.id.item_calculate_semester_gpa_course_text);
            mCourseGradeSpinner = itemView.findViewById(R.id.item_calculate_semester_gpa_grade_spinner);
            SpinnerAdapter mSpinnerAdapter = new SpinnerAdapter(context, mPossibleGradeList, CalculateGPARecycleAdapter.class.getSimpleName());
            mCourseGradeSpinner.setAdapter(mSpinnerAdapter);
            mCourseGradeSpinner.setSelection(chooseDefaultSpinner());
            mInitializeSpinner = new ArrayList<>();
            for (int i = 0; i < mSpinnerAdapter.getCount(); i++) {
                mInitializeSpinner.add(new ImageItem("A+", R.drawable.result_tracker_ic_grade));
            }

            mCourseGradeSpinner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mChecker = true;
                    mReturnPosition = getAdapterPosition();
                    v.performClick();
                    return false;
                }
            });

            mCourseGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ImageItem clickedItem = (ImageItem) parent.getItemAtPosition(position);
                    String clickedCourseGrade = clickedItem.getName(); // here is the problem

                    mResult.getGradesObtained().put(mCourseList.get(mReturnPosition).getCode(), clickedCourseGrade);

                    if (mReturnPosition == 0 && mChecker && mBugFixCounter != 0) {
                        fixFirstItemBottomSheetCalculateSemesterGPA = mCourseList.get(mReturnPosition).getCode();
                        fixFirstItemBottomSheetCalculateSemesterGPA2 = clickedCourseGrade;
                        mChecker = false;
                        //  Toast.makeText(context, mInitializeSpinner.get(adapterPosition).getmCourseGradeText() + "ceb me", Toast.LENGTH_SHORT).show();
                    } else {
                        mBugFixCounter++;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    @NonNull
    @Override
    public CalculateGPAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottomsheet_semester_result, parent, false);
        return new CalculateGPAViewHolder(v, mSpinnerList, mContext);

    }


    @Override
    public void onBindViewHolder(@NonNull CalculateGPAViewHolder holder, int position) {
        holder.mCourseCode.setText(mCourseList.get(position).getCode());
        mChecker = false;
    }

    @Override
    public int getItemCount() {
        return mCourseList.size(); // returning the size of the number of courses in the hashmap
    }

    public int chooseDefaultSpinner() {
        int mReturnValue = 0;

        for (int i = 0; i < mWeightList.size(); i++) {
            String s = mResult.getGradesObtainedValue(mCourseList.get(mCourseTrackCount).getCode());
            if (s.equals(mWeightList.get(i).getGrade()))
                mReturnValue = i;
        }

        mCourseTrackCount++;
        return mReturnValue;
    }


}
