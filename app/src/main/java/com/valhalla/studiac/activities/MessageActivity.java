package com.valhalla.studiac.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.messenger.MessageRecycleAdapter;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;

public class MessageActivity extends NavigationToolbarWhite {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_message);
        setupList();
    }

    /*
     * setups the recycler view on the current activity
     */
    private void setupList() {
        RecyclerView recyclerView = findViewById(R.id.message_recycle_view_id);
       /* recyclerView.addItemDecoration(new DividerItemDecoration(MessageActivity.this,
                DividerItemDecoration.VERTICAL));*/
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MessageActivity.this);
        MessageRecycleAdapter adapter = new MessageRecycleAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
