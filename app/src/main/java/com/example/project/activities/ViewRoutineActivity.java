package com.example.project.activities;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.project.R;
import com.example.project.adapters.CardPagerAdapter;
import com.example.project.toolbars.NavigationToolbarWhite;
import com.example.project.utility.ShadowTransformer;
import com.example.project.utility.CardItem;

public class ViewRoutineActivity extends NavigationToolbarWhite {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_viewroutine);
        createCardView();

    }

    private void createCardView() {

        ViewPager viewPager = findViewById(R.id.viewPager);

        CardPagerAdapter cardAdapter = new CardPagerAdapter();
        cardAdapter.addCardItem(new CardItem("Sunday", "Course names"));
        cardAdapter.addCardItem(new CardItem("Monday", "Course names"));
        cardAdapter.addCardItem(new CardItem("Tuesday", "Course names"));
        cardAdapter.addCardItem(new CardItem("Thursday", "Course names"));
        cardAdapter.addCardItem(new CardItem("Friday", "Course names"));
        cardAdapter.addCardItem(new CardItem("Saturday", "Course names"));


        ShadowTransformer cardShadowTransformer = new ShadowTransformer(viewPager, cardAdapter);
        cardShadowTransformer.enableScaling(true);
        viewPager.setAdapter(cardAdapter); // only show it on fragments
        viewPager.setPageTransformer(false, cardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);


    }


}
