package com.example.project.fragments.todo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.todo.CurrentTasksRecycleAdapter;
import com.example.project.fragments.dialogs.AddQuickTaskBottomSheetDialog;
import com.example.project.fragments.dialogs.AddTaskBottomSheetDialog;
import com.example.project.fragments.dialogs.SelectCourseDialog;
import com.example.project.fragments.dialogs.TaskDescriptionDialog;
import com.example.project.utility.common.Course;
import com.example.project.utility.todo.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class CurrentTasksFragment extends Fragment implements AddTaskBottomSheetDialog.BottomSheetListener {

    // private ArrayList<CurrentTaskItems> mCurrentTaskItems = new ArrayList<>();
    private CurrentTasksRecycleAdapter mAdapter;
    private View mView;
    private Context mContext;
    private ArrayList<Task> mCurrentTasks;
    private ArrayList<Course> mCourses;
    private final int RESULT_DELETE_CLICKED = 1;
    private final int ITEM_DESCRIPTION = 2;
    private final int ADD_QUICK_TASK = 3;

    private final int HEADER = 0;

    public CurrentTasksFragment(ArrayList<Task> currentTasks, ArrayList<Course> courses) {
        mCurrentTasks = currentTasks;
        mCourses = courses;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_todo_current, container, false);
        mContext = getActivity();

        //initList();
        setupList();

        return mView;
    }

    private void setupList() {

        RecyclerView recyclerView = mView.findViewById(R.id.todo_current_recycle_view_id);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new CurrentTasksRecycleAdapter(mCurrentTasks);
        recyclerView.setLayoutManager(layoutManager);
        handleEvents();
        ItemTouchHelper helper = handleDragEvents();
        recyclerView.setAdapter(mAdapter);
        helper.attachToRecyclerView(recyclerView);

    }

    private ItemTouchHelper handleDragEvents() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged = viewHolder.getAdapterPosition();
                int position_target = target.getAdapterPosition();
                if (position_target == 0) {
                    return false;
                }
                if (position_dragged == 0) {
                    return false;
                }
                Collections.swap(mCurrentTasks, position_dragged, position_target);
                mAdapter.notifyItemMoved(position_dragged, position_target);
                return false;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                // if it's a header, then don't drag it
                return viewHolder.getItemViewType() == 0 ? HEADER : super.getMovementFlags(recyclerView, viewHolder);

            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            // when the item is dropped, then customize the look
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.colorBaseWhite));
            }

            // when the item is being dragged after long press, then customize the look
            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    if (viewHolder != null) {
                        viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.colorLightAsh));

                    }
                }
            }


        });
    }

    private void handleEvents() {
        // when user clicks on the large add button
        FloatingActionButton button = mView.findViewById(R.id.todo_ADD_current_button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskBottomSheetDialog bottomSheetDialog = new AddTaskBottomSheetDialog(mCourses);
                bottomSheetDialog.setTargetFragment(CurrentTasksFragment.this, 1);
                if (getFragmentManager() != null) {
                    bottomSheetDialog.show(getFragmentManager(), "currentTasks");
                }
            }
        });

        // handle events on the adapters
        mAdapter.setOnItemClickListener(new CurrentTasksRecycleAdapter.OnItemClickListener() {

            // when the mini add button is clicked
            @Override
            public void onAddClicked(int position) {
                AddQuickTaskBottomSheetDialog dialog = new AddQuickTaskBottomSheetDialog();
                dialog.setTargetFragment(CurrentTasksFragment.this, ADD_QUICK_TASK);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "currentTasks");

            }

            // when the entire task is clicked
            // show additional details of the item
            @Override
            public void onItemClick(int position) {
                TaskDescriptionDialog dialog = new TaskDescriptionDialog(mCurrentTasks.get(position), position);
                dialog.setTargetFragment(CurrentTasksFragment.this, ITEM_DESCRIPTION);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "currentTasks");
            }

            // when the task is checked
            // remove the task that's clicked
            @Override
            public void onCheckClicked(int position) {
                mCurrentTasks.remove(position);
                mAdapter.notifyItemRemoved(position);
                // mAdapter.notifyItemRangeChanged(position, mExampleItems.size());

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ITEM_DESCRIPTION && resultCode == Activity.RESULT_OK) {
            int pos = data.getExtras().getInt("pos");
            mCurrentTasks.remove(pos);
            mAdapter.notifyItemRemoved(pos);

        }else if(requestCode == ADD_QUICK_TASK && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            mCurrentTasks.add(new Task(bundle.getString("description"), bundle.getString("additionalNotes")));
            mAdapter.notifyItemInserted(mCurrentTasks.size());
        }
    }

    // interface method of bottom sheet dialog
    @Override
    public void onAddPressed(Task task) {
        mCurrentTasks.add(task);
        mAdapter.notifyItemInserted(mAdapter.getCurrentTaskItems().size() - 1);

    }

}
