package com.example.project.fragments.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.todo.UpcomingAdapter;
import com.example.project.utility.todo.Days;

import java.util.ArrayList;

public class UpcomingFragment extends Fragment {

    private  ArrayList<Days> mItems;
    private RecyclerView mRecyclerView;
    private UpcomingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public UpcomingFragment(ArrayList<Days> months)
    {
        mItems = months;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_todo_upcoming, container, false);
        setupList(view);
        return view;
    }

    private void setupList(View view) {
        mRecyclerView = view.findViewById(R.id.todo_upcoming_recycler_id);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new UpcomingAdapter(mItems);

        mAdapter.setOnItemClickListener(new UpcomingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // @TODO: open dialog 24
            }

            @Override
            public void onButtonClick(int position) {
                // @TODO: add logic for adding the item to current task
                Toast.makeText(getActivity(), "Task added to current task", Toast.LENGTH_SHORT).show();

            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }
}
