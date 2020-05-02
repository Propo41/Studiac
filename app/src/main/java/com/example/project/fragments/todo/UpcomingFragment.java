package com.example.project.fragments.todo;

import android.app.Activity;
import android.content.Intent;
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
import com.example.project.fragments.dialogs.TaskDescriptionDialog;
import com.example.project.utility.common.Common;
import com.example.project.utility.common.Course;
import com.example.project.utility.todo.TasksUtil;
import com.example.project.utility.todo.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UpcomingFragment extends Fragment implements AddTaskBottomSheetDialog.BottomSheetListener {

    private RecyclerView mRecyclerView;
    private UpcomingRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TasksUtil mUpcoming;
    private ArrayList<Task> mCurrentTasks;
    private ArrayList<Course> mCourses;
    private final int RESULT_DELETE_CLICKED = 1;

    public UpcomingFragment(TasksUtil upcoming, ArrayList<Task> currentTasks, ArrayList<Course> courses) {
        mUpcoming = upcoming;
        mCurrentTasks = currentTasks;
        mCourses = courses;
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
                AddTaskBottomSheetDialog bottomSheetDialog = new AddTaskBottomSheetDialog(mCourses);
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
                TaskDescriptionDialog dialog = new TaskDescriptionDialog((Task) mUpcoming.getTodoTasks().get(position), position);
                dialog.setTargetFragment(UpcomingFragment.this, RESULT_DELETE_CLICKED);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "currentUpcoming");


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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_DELETE_CLICKED && resultCode == Activity.RESULT_OK) {
            int pos = data.getExtras().getInt("pos");
            mUpcoming.getTodoTasks().remove(pos);
            mAdapter.notifyItemRemoved(pos);

        }
    }

    @Override
    public void onAddPressed(Task task) {
        String schedule = task.getSchedule();
        Integer countOfItems;
        Integer indexOfHeader;

        //  index = mDay.isVisited(Common.parseDate(task.getSchedule()));
        Pair<Integer, Integer> index = mUpcoming.isVisited(Common.parseDate(schedule));
        // if the header is not already present in the list
        if (index == null) {
            // add a new task with the header
            indexOfHeader = mUpcoming.insertNewTask(task, schedule);
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
