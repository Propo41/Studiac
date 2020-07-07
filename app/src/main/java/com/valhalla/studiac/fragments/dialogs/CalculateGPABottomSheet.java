package com.valhalla.studiac.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.result_tracker.CalculateGPARecycleAdapter;
import com.valhalla.studiac.holders.ImageItem;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Result;
import com.valhalla.studiac.models.Weight;

import java.util.ArrayList;

import static com.valhalla.studiac.adapters.result_tracker.CalculateGPARecycleAdapter.fixFirstItemBottomSheetCalculateSemesterGPA;
import static com.valhalla.studiac.adapters.result_tracker.CalculateGPARecycleAdapter.fixFirstItemBottomSheetCalculateSemesterGPA2;


public class CalculateGPABottomSheet extends BottomSheetDialogFragment {

    private TextView mGPAText;
    private ArrayList<ImageItem> mSpinnerItems;
    private calculateGPAButtonListener mListener;
    private Result mResult;
    private ArrayList<Course> mCourseList;
    private ArrayList<Weight> mWeightList;
    private ArrayList<Result> mResultList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheet_calculate_semester_gpa, container, false);

        initializeViews(v);
        populateSpinner();
        setupList(v);

        return v;
    }


    /**
     * @param result result of the current semester
     *               //@param gradeWeights grade weights instance of the university
     */
    public CalculateGPABottomSheet(Result result, ArrayList<Course> courseList,
                                   ArrayList<Weight> weightList, ArrayList<Result> mResultList) {
        mResult = result;
        mWeightList = weightList;
        mCourseList = courseList;
        this.mResultList = mResultList;
        for (int i = 0; i < mCourseList.size(); i++) {
            String str = result.getGradesObtainedValue(mCourseList.get(i).getCode());
            if (str == null) {
                mResult.setGradesObtainedValue(mCourseList.get(i).getCode(), "A");
            }
            //System.out.println("At btm sheet last sem constructor  "  + courseNames[i] +  "  " +  result.getGradesObtainedValue(courseNames[i]));
        }


        if (mCourseList.size() != 0) {
            fixFirstItemBottomSheetCalculateSemesterGPA = mCourseList.get(0).getCode();
            fixFirstItemBottomSheetCalculateSemesterGPA2 = result.getGradesObtainedValue(mCourseList.get(0).getCode());
            //System.out.println(fixFirstItemBottomSheetCalculateSemesterGPA + "  X  " + fixFirstItemBottomSheetCalculateSemesterGPA2);
        }
    }


    @Override
    public int getTheme() {
        return R.style.BaseBottomSheetDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }


    private void initializeViews(View v) {
        Button mCalculateButton = v.findViewById(R.id.calculate_semester_gpa_bottomsheet_calculatebtn);
        mGPAText = v.findViewById(R.id.calculate_semester_gpa_bottomsheet_gpa_text);

        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fixFirstItemBottomSheetCalculateSemesterGPA = courseNames[0];
                //fixFirstItemBottomSheetCalculateSemesterGPA2 = courseGrades[0];
                //  updateValuesList();
                Double gpa = mListener.calculateSemesterGpa();
                mResult.setGpa(gpa);
                //  System.out.println( "kpo" + gpa);
                mGPAText.setVisibility(View.VISIBLE);
                mGPAText.setText(getString(R.string.PlaceHolder_GPA, gpa.toString()));
            }
        });
    }


    private void setupList(View v) {
        RecyclerView mRecycleView = v.findViewById(R.id.calculate_semester_gpa_bottomsheet_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        CalculateGPARecycleAdapter mAdapter = new CalculateGPARecycleAdapter(getContext(), mResult, mSpinnerItems, mWeightList, mCourseList);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mAdapter);

    }


    public interface calculateGPAButtonListener {
        double calculateSemesterGpa();
    }

    private void populateSpinner() {
        mSpinnerItems = new ArrayList<>();
        for (int i = 0; i < mWeightList.size(); i++) {
            mSpinnerItems.add(new ImageItem(mWeightList.get(i).getGrade(), R.drawable.result_tracker_ic_grade));
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        mResult.getGradesObtained().put(fixFirstItemBottomSheetCalculateSemesterGPA, fixFirstItemBottomSheetCalculateSemesterGPA2);
        super.onDismiss(dialog);
        mResult.getGradesObtained().put(fixFirstItemBottomSheetCalculateSemesterGPA, fixFirstItemBottomSheetCalculateSemesterGPA2);
        mResult.setGradesObtainedValue(fixFirstItemBottomSheetCalculateSemesterGPA, fixFirstItemBottomSheetCalculateSemesterGPA2);
        System.out.println("On Dismiss Btm sheet " + fixFirstItemBottomSheetCalculateSemesterGPA + "  X  " + fixFirstItemBottomSheetCalculateSemesterGPA2);
        for (int i = 0; i < mResultList.get(mResultList.size() - 1).getGradesObtained().size(); i++) {
            String str = mResultList.get(mResultList.size() - 1).getGradesObtainedValue(mCourseList.get(i).getCode());
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (calculateGPAButtonListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }


}
