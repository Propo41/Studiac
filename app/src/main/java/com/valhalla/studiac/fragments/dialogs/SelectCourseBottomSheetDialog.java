package com.valhalla.studiac.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.SelectCourseRecycleAdapter;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.models.Course;

import java.util.ArrayList;
import java.util.Objects;

public class SelectCourseBottomSheetDialog extends BottomSheetDialogFragment {

    private ArrayList<Course> mCourses;
    private View mView;
    private SelectCourseRecycleAdapter mAdapter;
    private int mCheckedPosition = 0;

    public SelectCourseBottomSheetDialog(ArrayList<Course> courses) {
        mCourses = courses;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_common_select_course, container, false);
        mView = view;
        setupRecyclerView();
        handleClickEvents();

        return view;
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


/*
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.setupDialog(dialog, style);
    }*/

    /*@NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_common_select_course, null);
        mView = view;

        setupRecyclerView();
        handleClickEvents();
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setLayout(Common.getEquivalentPx(300), ConstraintLayout.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setGravity(Gravity.CENTER);

        return alertDialog;
    }*/


    private void handleClickEvents() {
        Button selectButton = mView.findViewById(R.id.dialog_course_picker_button_id);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        new Intent().putExtra("selectedCoursePosition", mCheckedPosition)
                );

                dismiss();
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = mView.findViewById(R.id.dialog_course_picker_recycler_view_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        DividerItemDecoration itemDecorator = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.drawable_design));
        recyclerView.addItemDecoration(itemDecorator);

        mAdapter = new SelectCourseRecycleAdapter(mCourses);
        handleToggleEvents();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void handleToggleEvents() {
        mAdapter.setOnItemClickListener(new SelectCourseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemChecked(int position) {
                mCheckedPosition = position;
            }
        });

    }

}
