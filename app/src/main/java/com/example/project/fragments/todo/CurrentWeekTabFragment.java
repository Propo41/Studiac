package com.example.project.fragments.todo;

import android.os.Bundle;
import android.util.Log;
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
import com.example.project.adapters.todo.CurrentWeekRecycleAdapter;
import com.example.project.fragments.dialogs.AddTaskBottomSheetDialog;
import com.example.project.utility.todo.Day;
import com.example.project.utility.todo.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
 * The Fragment that is used for the tabs.
 * Each tab occupies this fragment.
 * The event handling also occurs from this fragment
 */
public class CurrentWeekTabFragment extends Fragment implements AddTaskBottomSheetDialog.BottomSheetListener {


    private Day mDay; // contains the task items for the current day of the week
    private CurrentWeekRecycleAdapter mAdapter; // we need to change it to ExampleAdapter object


    public CurrentWeekTabFragment(Day day ){
        mDay = day;

    }

    public CurrentWeekRecycleAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(CurrentWeekRecycleAdapter adapter) {
        mAdapter = adapter;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_todo_week_tab, container, false);
        handleEvents(view);

        return view;
    }



    private void handleEvents(View view) {
        // when user clicks on the large add button, open bottom sheet dialog
        FloatingActionButton button = view.findViewById(R.id.todo_ADD_week_button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskBottomSheetDialog bottomSheetDialog = new AddTaskBottomSheetDialog();
                bottomSheetDialog.setTargetFragment(CurrentWeekTabFragment.this, 1);
                if (getFragmentManager() != null) {
                    bottomSheetDialog.show(getFragmentManager(), "currentWeek");
                }
            }
        });
    }


    @Override
    public void onAddPressed(Task task) {
        Object object = task.getCategory().second;
        Integer countOfItems;
        Integer indexOfHeader;
        if(object!=null){
            Pair<Integer, Integer> index = mDay.isVisited(object);
            // if the header is not already present in the list
            if (index == null){
                // add a new task with the header
                indexOfHeader = mDay.insertNewTask(task, object);
                mAdapter.notifyItemRangeInserted(indexOfHeader, 2);

            }else{
                indexOfHeader = index.first;
                countOfItems = index.second;
                // add a task below the current header after all the items
                mDay.insertTask(task, indexOfHeader, countOfItems);
              //  Log.i("index header: ", indexOfHeader + "");
              //  Log.i("item count: ", countOfItems + "");
                mAdapter.notifyItemRangeInserted(indexOfHeader+countOfItems+1, 1);

            }
        }
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       setupList(view);
       super.onViewCreated(view, savedInstanceState);
    }



    private void setupList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.todo_week_recycle_view_id);
     /*   recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));*/
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        if(mDay.getTodoTasks() == null){
            Log.i("setuplist: ", "Nothing here yet. Add a task");
        }else{
            mAdapter = new CurrentWeekRecycleAdapter(mDay.getTodoTasks());
            mAdapter.setOnItemClickListener(new CurrentWeekRecycleAdapter.OnItemClickListener() {
                @Override
                public void onButtonClick(int position) {
                    // @TODO: add logic for adding task to current tasks
                    Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemClick(int position) {
                    //mDays.getCurrentWeekItems().get(position).changeText("clicked!!");
                    //mAdapter.notifyItemChanged(position);
                    // @TODO: open dialog 24
                }
            });

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);

        }


    }

}
