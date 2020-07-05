package com.valhalla.studiac.fragments.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.todo.UpcomingRecycleAdapter;
import com.valhalla.studiac.fragments.dialogs.AddTaskBottomSheetDialog;
import com.valhalla.studiac.fragments.dialogs.TaskDescriptionDialog;
import com.valhalla.studiac.holders.ListItem;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Task;
import com.valhalla.studiac.models.TasksUtil;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.utility.CustomPair;

import java.util.ArrayList;

public class UpcomingFragment extends Fragment implements AddTaskBottomSheetDialog.BottomSheetListener {

    private UpcomingRecycleAdapter mAdapter;
    private TasksUtil mUpcoming;
    private ArrayList<ListItem> mCurrentTasks;
    private ArrayList<Course> mCourses;
    private final int RESULT_DELETE_CLICKED = 1;
    private TextView mPandaText;
    private ImageView mPandaImage;
    //  private DatabaseReference mDatabaseReference;


    public UpcomingFragment(TasksUtil upcoming, ArrayList<ListItem> currentTasks, ArrayList<Course> courses) {
        mUpcoming = upcoming;
        mCurrentTasks = currentTasks;
        mCourses = courses;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_upcoming, container, false);
        mPandaImage = view.findViewById(R.id.todo_upcoming_panda_image_id);
        mPandaText = view.findViewById(R.id.todo_upcoming_panda_text_id);

        if (mUpcoming.getTodoTasks().size() == 0) {
            mPandaImage.setVisibility(View.VISIBLE);
            mPandaText.setVisibility(View.VISIBLE);
        }
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
                bottomSheetDialog.show(getParentFragmentManager(), "upcoming");

            }
        });
    }

    private void setupList(View view) {
        RecyclerView mRecyclerView = view.findViewById(R.id.todo_upcoming_recycler_id);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new UpcomingRecycleAdapter(mUpcoming, getContext());

        mAdapter.setOnItemClickListener(new UpcomingRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TaskDescriptionDialog dialog = new TaskDescriptionDialog((Task) mUpcoming.getTodoTasks().get(position), position);
                dialog.setTargetFragment(UpcomingFragment.this, RESULT_DELETE_CLICKED);
                dialog.show(getParentFragmentManager(), "currentUpcoming");
            }

            @Override
            public void onButtonClick(int position) {
                mCurrentTasks.add(mUpcoming.getTodoTasks().get(position));
                Toast.makeText(getActivity(), "Task added to current tasks", Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /*
     * result returned from the pop up dialog
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_DELETE_CLICKED && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    int pos = data.getExtras().getInt("pos");
                    int count = mUpcoming.removeTask(pos);
                    mAdapter.notifyItemRangeRemoved(pos, count);

                    if (mUpcoming.getTodoTasks().size() == 0) {
                        mPandaText.setVisibility(View.VISIBLE);
                        mPandaImage.setVisibility(View.VISIBLE);
                    }

                }
            }


        }
    }

    @Override
    public void onAddPressed(Task task) {
        String schedule = Common.parseDate(task.getSchedule());
        Integer countOfItems;
        Integer indexOfHeader;

        //  index = mDay.isVisited(Common.parseDate(task.getSchedule()));
        CustomPair index = mUpcoming.isVisited(schedule);
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

        if (mUpcoming.getTodoTasks().size() > 0) {
            mPandaText.setVisibility(View.INVISIBLE);
            mPandaImage.setVisibility(View.INVISIBLE);
        }
        //  mUpcoming.showList();
        // mUpcoming.showMap();

    }

}
