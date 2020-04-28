package com.example.project.fragments.todo;

import android.content.Context;
import android.os.Bundle;
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
import com.example.project.adapters.todo.CurrentTasksRecycleAdapter;
import com.example.project.fragments.dialogs.AddTaskBottomSheetDialog;
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
    private final int HEADER = 0;

    public CurrentTasksFragment(ArrayList<Task> currentTasks) {
        mCurrentTasks = currentTasks;
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

    /*
     * used for the dragging effects
     * when the item is dropped, then customize the look
     * when the item is being dragged after long press, then customize the look
     */
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
                AddTaskBottomSheetDialog bottomSheetDialog = new AddTaskBottomSheetDialog();
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
                mCurrentTasks.remove(position);
                mAdapter.notifyItemRemoved(position);
                // mAdapter.notifyItemRangeChanged(position, mExampleItems.size());

            }
        });
    }


    // interface method of bottom sheet dialog
    @Override
    public void onAddPressed(Task task) {

        mCurrentTasks.add(task);
        //   mAdapter.getCurrentTaskItems().add(task);
        mAdapter.notifyItemInserted(mAdapter.getCurrentTaskItems().size() - 1);

    /*    if (task.getCategory().first.equals("Course")) {
            Course course = (Course) task.getCategory().second;
            course.getTodoTasks().add(task);
            mAdapter.getCurrentTaskItems().add(task);
            mAdapter.notifyItemInserted(mAdapter.getCurrentTaskItems().size()-1);

        }*/

        // if task.type == "Current Task", add it into current task stack
        // if task.type == "Current Week", add it to current week stack
        // if task.type == "Future", add it to future stack

    }

}
