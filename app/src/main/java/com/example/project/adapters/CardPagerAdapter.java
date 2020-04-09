package com.example.project.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.project.R;
import com.example.project.utility.CardItem;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {

        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter_viewroutine, container, false);

        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    /*
    binds the text views with the texts that is passed through the CardItem object
     */

    private void bind(CardItem item, View view) {
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        titleTextView.setText(item.getTitle());

      /*  titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Title clicked!!!!!");
            }
        });*/


      /* // TableLayout tableLayout = view.findViewById(R.id.table_layout_id);
        Context context = view.getContext();

        for (int i = 0; i < 4; i++) {
            TableRow table_row = new TableRow(context);

            TextView nameView = new TextView(context);
            nameView.setText("Course " + i+1);
            nameView.setGravity(Gravity.START);
            table_row.addView(nameView);

            TextView name2View = new TextView(context);
            name2View.setText("TimeStart " + i+1);
            name2View.setGravity(Gravity.CENTER);

            table_row.addView(name2View);


            TextView name3View = new TextView(context);
            name3View.setText("TimeEnd " + i+1);
            name3View.setGravity(Gravity.END);

            table_row.addView(name3View);

            //tableLayout.addView(table_row);

        }*/


        // TextView contentTextView = view.findViewById(R.id.coursename_id);
        // contentTextView.setText(item.getText());
    }

}




