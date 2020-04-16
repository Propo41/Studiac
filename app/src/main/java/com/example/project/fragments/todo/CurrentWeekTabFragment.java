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
import com.example.project.adapters.todo.CurrentWeekRecycleAdapter;
import com.example.project.utility.todo.Days;

/*
 * The Fragment that is used for the tabs.
 * Each tab occupies this fragment.
 * The event handling also occurs from this fragment
 */
public class CurrentWeekTabFragment extends Fragment {


    private Days mDays;
    private CurrentWeekRecycleAdapter mAdapter; // we need to change it to ExampleAdapter object

    public CurrentWeekTabFragment(Days days){
        mDays = days;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_week_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       /* Bundle args = getArguments();
        if (args != null) {
            ((TextView) view.findViewById(R.id.fragment_text_id))
                    .setText(args.getString("message"));
        }*/
       setupList(view);
       super.onViewCreated(view, savedInstanceState);
    }


    private void setupList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.todo_week_recycle_view_id);
     /*   recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));*/
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new CurrentWeekRecycleAdapter(mDays.getTaskItems());

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
