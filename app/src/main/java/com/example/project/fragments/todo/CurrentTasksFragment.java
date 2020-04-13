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
import com.example.project.adapters.todo.CurrentTasksAdapter;
import com.example.project.utility.todo.CurrentTaskItems;
import java.util.ArrayList;
import java.util.Collections;

public class CurrentTasksFragment extends Fragment {

    private ArrayList<CurrentTaskItems> mCurrentTaskItems = new ArrayList<>();
    private CurrentTasksAdapter mAdapter;
    private View mView;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_todo_current, container, false);
        mContext = getActivity();
        initList();
        setupList();

        return mView;

    }



    private void setupList() {

        RecyclerView recyclerView = mView.findViewById(R.id.recycle_view_id);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new CurrentTasksAdapter(mCurrentTaskItems);
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
                Collections.swap(mCurrentTaskItems, position_dragged, position_target);
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

        // handle user events
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
                mCurrentTaskItems.remove(position);
                mAdapter.notifyItemRemoved(position);
                // mAdapter.notifyItemRangeChanged(position, mExampleItems.size());

            }
        });
    }

    private void initList() {

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


    }


}
