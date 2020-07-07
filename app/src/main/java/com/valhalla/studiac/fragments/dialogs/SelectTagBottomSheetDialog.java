package com.valhalla.studiac.fragments.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.SelectTagRecycleAdapter;

import java.util.ArrayList;


public class SelectTagBottomSheetDialog extends BottomSheetDialogFragment {

    private View mView;
    private SelectTagRecycleAdapter mAdapter;
    private ArrayList<String> mTagList;
    private int mCheckedPosition;

    public SelectTagBottomSheetDialog() {
        mTagList = new ArrayList<>();
        mTagList.add("Quiz");
        mTagList.add("Assignment");
        mTagList.add("Mid");
        mTagList.add("Lab Quiz");
        mTagList.add("Lab Assignment");
        mTagList.add("Presentation");
        mTagList.add("Case Work");
        mTagList.add("Project");
        mTagList.add("Final");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_todo_select_tag, container, false);
        mView = view;
        setupRecyclerView();
        Button button = view.findViewById(R.id.selectButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pass the selected item to the parent activity
                Fragment fragment = getTargetFragment();
                if (fragment != null) {
                    fragment.onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_OK,
                            new Intent().putExtra("selectedType", mTagList.get(mCheckedPosition))
                    );
                    dismiss();
                }

            }
        });

        return view;
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = mView.findViewById(R.id.dialog_select_tag_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        mAdapter = new SelectTagRecycleAdapter(mTagList, getContext());
        handleToggleEvents();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void handleToggleEvents() {
        mAdapter.setOnItemClickListener(new SelectTagRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemChecked(int position) {
                mCheckedPosition = position;
            }
        });

    }


}
