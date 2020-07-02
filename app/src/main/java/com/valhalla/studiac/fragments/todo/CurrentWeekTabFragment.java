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

import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.todo.CurrentWeekRecycleAdapter;
import com.valhalla.studiac.fragments.dialogs.AddTaskBottomSheetDialog;
import com.valhalla.studiac.fragments.dialogs.TaskDescriptionDialog;
import com.valhalla.studiac.holders.ListItem;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.TasksUtil;
import com.valhalla.studiac.models.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.utility.CustomPair;

import org.w3c.dom.Text;

import java.util.ArrayList;

/*
 * The Fragment that is used for the tabs.
 * Each tab occupies this fragment.
 * The event handling also occurs from this fragment
 */
public class CurrentWeekTabFragment extends Fragment implements AddTaskBottomSheetDialog.BottomSheetListener {


    private TasksUtil mDay; // contains the task items for the current day of the week
    private CurrentWeekRecycleAdapter mAdapter; // we need to change it to ExampleAdapter object
    private ArrayList<ListItem> mCurrentTasks;
    private ArrayList<Course> mCourses;
    private final int RESULT_DELETE_CLICKED = 1;
    private final int RESULT_ADD_CLICKED = 2;
    private TextView mPandaText;
    private ImageView mPandaImage;


    public CurrentWeekTabFragment(TasksUtil day, ArrayList<ListItem> currentTasks, ArrayList<Course> courses) {
        mDay = day;
        mCurrentTasks = currentTasks;
        mCourses = courses;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo_week_tab, container, false);
        mPandaImage = view.findViewById(R.id.todo_week_panda_image_id);
        mPandaText = view.findViewById(R.id.todo_week_panda_text_id);

        handleEvents(view);


        return view;
    }


    private void handleEvents(View view) {
        // when user clicks on the large add button, open bottom sheet dialog
        FloatingActionButton button = view.findViewById(R.id.todo_ADD_week_button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskBottomSheetDialog bottomSheetDialog = new AddTaskBottomSheetDialog(mCourses);
                bottomSheetDialog.setTargetFragment(CurrentWeekTabFragment.this, RESULT_ADD_CLICKED);
                bottomSheetDialog.show(getParentFragmentManager(), "currentWeek");
            }
        });
    }


    @Override
    public void onAddPressed(Task task) {
        String category = task.getCategory();
        Integer countOfItems;
        Integer indexOfHeader;
        CustomPair index = mDay.isVisited(category);

        // if the header is not already present in the list
        if (index == null) {
            // add a new task with the header
            indexOfHeader = mDay.insertNewTask(task, category);
            mAdapter.notifyItemRangeInserted(indexOfHeader, 2);
        } else {
            indexOfHeader = index.first;
            countOfItems = index.second;
            // add a task below the current header after all the items
            mDay.insertTask(task, indexOfHeader, countOfItems);
            mAdapter.notifyItemRangeInserted(indexOfHeader + countOfItems + 1, 1);
        }

        if (mDay.getTodoTasks().size() > 0) {
            mPandaImage.setVisibility(View.INVISIBLE);
            mPandaText.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (mDay.getTodoTasks().size() == 0) {
            mPandaImage.setVisibility(View.VISIBLE);
            mPandaText.setVisibility(View.VISIBLE);
        }
        setupList(view);
        super.onViewCreated(view, savedInstanceState);
    }


    private void setupList(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.todo_week_recycle_view_id);
     /*   recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));*/
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new CurrentWeekRecycleAdapter(mDay.getTodoTasks(), getContext());
        handleListEvents();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);


    }

    private void handleListEvents() {
        mAdapter.setOnItemClickListener(new CurrentWeekRecycleAdapter.OnItemClickListener() {
            @Override
            public void onButtonClick(int position) {
                // the mTasksUtil.getTodoTasks() contains several object types. But we need
                // only the Task object. So, we cast it into Task. We know for sure that the item
                // clicked must be a Task item, since we can't interact with headers
                mCurrentTasks.add((Task) mDay.getTodoTasks().get(position));
                Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(int position) {
                TaskDescriptionDialog dialog = new TaskDescriptionDialog((Task) mDay.getTodoTasks().get(position), position);
                dialog.setTargetFragment(CurrentWeekTabFragment.this, RESULT_DELETE_CLICKED);
                dialog.show(getParentFragmentManager(), "currentWeek");

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // when delete button is clicked
        if (requestCode == RESULT_DELETE_CLICKED && resultCode == Activity.RESULT_OK) {
            int pos = data.getExtras().getInt("pos");
            int count = mDay.removeTask(pos);
            if (mDay.getTodoTasks().size() == 0) {
                mPandaText.setVisibility(View.VISIBLE);
                mPandaImage.setVisibility(View.VISIBLE);
            }
            mAdapter.notifyItemRangeRemoved(pos - 1, count);

        }
    }

}
