package com.example.project.fragments.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.todo.UpcomingRecycleAdapter;
import com.example.project.fragments.dialogs.AddTaskBottomSheetDialog;
import com.example.project.utility.todo.TasksUtil;
import com.example.project.utility.todo.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UpcomingFragment extends Fragment implements AddTaskBottomSheetDialog.BottomSheetListener {

    private ArrayList<TasksUtil> mItems;
    private RecyclerView mRecyclerView;
    private UpcomingRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TasksUtil mUpcoming;
    private ArrayList<Task> mCurrentTasks;

    public UpcomingFragment(TasksUtil upcoming, ArrayList<Task> currentTasks) {
        mUpcoming = upcoming;
        mCurrentTasks = currentTasks;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_upcoming, container, false);
        setupList(view);
        handleEvents(view);

        return view;
    }


    private void handleEvents(View view) {
        // when user clicks on the large add button, open bottom sheet dialog
        FloatingActionButton button = view.findViewById(R.id.todo_ADD_upcoming_button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskBottomSheetDialog bottomSheetDialog = new AddTaskBottomSheetDialog();
                bottomSheetDialog.setTargetFragment(UpcomingFragment.this, 1);
                if (getFragmentManager() != null) {
                    bottomSheetDialog.show(getFragmentManager(), "upcoming");
                }
            }
        });
    }

    private void setupList(View view) {
        mRecyclerView = view.findViewById(R.id.todo_upcoming_recycler_id);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new UpcomingRecycleAdapter(mUpcoming);

        mAdapter.setOnItemClickListener(new UpcomingRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // @TODO: open dialog 24
            }

            @Override
            public void onButtonClick(int position) {
                // @TODO: add logic for adding the item to current task
                mCurrentTasks.add((Task) mUpcoming.getTodoTasks().get(position));
                Toast.makeText(getActivity(), "Task added", Toast.LENGTH_SHORT).show();

            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onAddPressed(Task task) {
        Object object = task.getSchedule();
        Integer countOfItems;
        Integer indexOfHeader;
        if (object != null) {
            Pair<Integer, Integer> index = mUpcoming.isVisited(object);
            // if the header is not already present in the list
            if (index == null) {
                // add a new task with the header
                indexOfHeader = mUpcoming.insertNewTask(task, object);
                mAdapter.notifyItemRangeInserted(indexOfHeader, 2);

            } else {
                indexOfHeader = index.first;
                countOfItems = index.second;
                // add a task below the current header after all the items
                mUpcoming.insertTask(task, indexOfHeader, countOfItems);
                //  Log.i("index header: ", indexOfHeader + "");
                //  Log.i("item count: ", countOfItems + "");
                mAdapter.notifyItemRangeInserted(indexOfHeader + countOfItems + 1, 1);

            }
        }
    }
}
