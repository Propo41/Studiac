package com.example.project.fragments.todo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.todo.CurrentTasksAdapter;
import com.example.project.fragments.dialogs.AddTaskMainBSDialog;
import com.example.project.utility.common.Course;
import com.example.project.utility.todo.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class CurrentTasksFragment extends Fragment implements AddTaskMainBSDialog.BottomSheetListener{

   // private ArrayList<CurrentTaskItems> mCurrentTaskItems = new ArrayList<>();
    private CurrentTasksAdapter mAdapter;
    private View mView;
    private Context mContext;
    private ArrayList<Task> mTasks;

    public CurrentTasksFragment(ArrayList<Task> tasks){
        mTasks = tasks;
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
        mAdapter = new CurrentTasksAdapter(mTasks);
        recyclerView.setLayoutManager(layoutManager);
        handleEvents();
        ItemTouchHelper helper = handleDragEvents();
        recyclerView.setAdapter(mAdapter);
        helper.attachToRecyclerView(recyclerView);

    }

    private ItemTouchHelper handleDragEvents() {

        // used for the dragging effects
        // when the item is dropped, then customize the look
        // when the item is being dragged after long press, then customize the look
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged = viewHolder.getAdapterPosition();
                int position_target = target.getAdapterPosition();
                Collections.swap(mTasks, position_dragged, position_target);
                mAdapter.notifyItemMoved(position_dragged, position_target);
                return false;
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
                AddTaskMainBSDialog bottomSheetDialog = new AddTaskMainBSDialog();
                bottomSheetDialog.setTargetFragment(CurrentTasksFragment.this, 1);
                if (getFragmentManager() != null) {
                    bottomSheetDialog.show(getFragmentManager(), "currentTasks");
                }
            }
        });

        // handle events on the adapters
        mAdapter.setOnItemClickListener(new CurrentTasksAdapter.OnItemClickListener() {

            // when the mini add button is clicked
            @Override
            public void onAddClicked(int position) {
                // @TODO: open dialog 28
            }

            // when the entire task is clicked
            // show additional details of the item
            @Override
            public void onItemClick(int position) {
                // @TODO: open dialog 24
            }

            // when the task is checked
            // remove the task that's clicked
            @Override
            public void onCheckClicked(int position) {
                mTasks.remove(position);
                mAdapter.notifyItemRemoved(position);
                // mAdapter.notifyItemRangeChanged(position, mExampleItems.size());

            }
        });
    }

    private void initList() {

     /*   mCurrentTaskItems.add(new CurrentTaskItems("this is android"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is oreo"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is marshmallow"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is chocolate"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is kitkat"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is strawberry"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is milkshake"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is android"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is oreo"));

        mCurrentTaskItems.add(new CurrentTaskItems("this is android"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is oreo"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is marshmallow"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is chocolate"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is kitkat"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is strawberry"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is milkshake"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is android"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is oreo"));

        mCurrentTaskItems.add(new CurrentTaskItems("this is android"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is oreo"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is marshmallow"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is chocolate"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is kitkat"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is strawberry"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is milkshake"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is android"));
        mCurrentTaskItems.add(new CurrentTaskItems("this is oreo"));*/


    }


    // interface method of bottom sheet dialog
    @Override
    public void onAddPressed(Task task) {

        if (task.getCategory().first.equals("Course")) {
            Course course = (Course) task.getCategory().second;
            course.getTodoTasks().add(task);
            mAdapter.getCurrentTaskItems().add(task);
            mAdapter.notifyItemInserted(mAdapter.getCurrentTaskItems().size()-1);

        }

        // if task.type == "Current Task", add it into current task stack
        // if task.type == "Current Week", add it to current week stack
        // if task.type == "Future", add it to future stack

    }

}
