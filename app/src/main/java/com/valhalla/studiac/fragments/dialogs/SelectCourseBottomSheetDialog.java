package com.valhalla.studiac.fragments.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.SelectCourseRecycleAdapter;
import com.valhalla.studiac.models.Course;

import java.util.ArrayList;

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


    private void handleClickEvents() {
        Button selectButton = mView.findViewById(R.id.dialog_course_picker_button_id);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getTargetFragment();
                if (fragment != null) {
                    fragment.onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_OK,
                            new Intent().putExtra("selectedCoursePosition", mCheckedPosition)
                    );

                    dismiss();
                }

            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = mView.findViewById(R.id.dialog_course_picker_recycler_view_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        DividerItemDecoration itemDecorator = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        Drawable decorator = ContextCompat.getDrawable(requireContext(), R.drawable.drawable_design);
        if(decorator!=null){
            itemDecorator.setDrawable(decorator);
            recyclerView.addItemDecoration(itemDecorator);
        }

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
