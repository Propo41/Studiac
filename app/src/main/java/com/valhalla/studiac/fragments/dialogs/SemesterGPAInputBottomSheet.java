package com.valhalla.studiac.fragments.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.ErrorStyle;

public class SemesterGPAInputBottomSheet extends BottomSheetDialogFragment {

    private  BottomSheetListener mBottomSheetListener;
    private EditText mGPAInput;
    private EditText mTotalCredit;
    private EditText mCoursesTaken;
    private ErrorStyle errorStyle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_semester_gpa_only,container,false);
        Button mEnterButton = v.findViewById(R.id.semester_result_info_bottomsheet_enterbtn);
        errorStyle = ErrorStyle.getInstance(getContext());
        mGPAInput = v.findViewById(R.id.semester_result_info_bottomsheet_gpa_edit_text);
        mTotalCredit = v.findViewById(R.id.semester_result_info_bottomsheet_total_credits_edit_text);
        mCoursesTaken = v.findViewById(R.id.semester_result_info_bottomsheet_courses_taken);
        errorStyle.resetError(mGPAInput);
        errorStyle.resetError(mTotalCredit);
        errorStyle.resetError(mCoursesTaken);

        mEnterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(mGPAInput.getText().toString().equals("")){
                    errorStyle.setError(getString(R.string.Error_EmptyField),mGPAInput);
                }else if(mCoursesTaken.getText().toString().equals("")){
                    errorStyle.setError(getString(R.string.Error_EmptyField),mCoursesTaken);
                }else if(mTotalCredit.getText().toString().equals("")){
                    errorStyle.setError(getString(R.string.Error_EmptyField),mTotalCredit);
                }else{
                    mBottomSheetListener.onButtonCLicked(Double.parseDouble(mGPAInput.getText().toString()), Double.parseDouble(mTotalCredit.getText().toString()), Integer.parseInt(mCoursesTaken.getText().toString()));
                    dismiss();
                }

            }

        });
        return v;
    }


    public interface BottomSheetListener{
        void onButtonCLicked(double mGPAEntered, double mTotalCredit, Integer mCoursesTaken);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mBottomSheetListener = (BottomSheetListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }

    }

    @Override
    public int getTheme() {
        return R.style.BaseBottomSheetDialog;
    }

}
