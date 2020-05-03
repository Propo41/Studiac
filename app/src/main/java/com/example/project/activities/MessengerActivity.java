package com.example.project.activities;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import com.example.project.R;
import com.example.project.adapters.messenger.MessengerRecycleAdapter;
import com.example.project.toolbars.NavigationToolbarBlue;
import com.example.project.toolbars.NavigationToolbarWhite;

public class MessengerActivity extends NavigationToolbarWhite {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_messenger);
        setupList();
    }


    /*
     * setups the recycler view on the current activity
     */
    private void setupList() {
        RecyclerView recyclerView = findViewById(R.id.messenger_recycle_view_id);
        // adding separator between the items
        recyclerView.addItemDecoration(new DividerItemDecoration(MessengerActivity.this,
                DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MessengerActivity.this);
        MessengerRecycleAdapter adapter = new MessengerRecycleAdapter();
        adapter.setOnItemClickListener(new MessengerRecycleAdapter.OnItemClickListener() {
            @Override
            public void onSelectMessage(int position) {
                // todo: open activity 16
                startActivity(new Intent(MessengerActivity.this, MessageActivity.class));

            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
