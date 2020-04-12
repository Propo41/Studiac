package com.example.project.utility;

import java.util.ArrayList;

public class CardItem {

    private String mText;
    private String mTitle;
    // an array list of course objects that has member variables: CourseName, CourseStartTime, CourseEndTime
    // sorted day wise, ie: index 0 is Sunday, index 1 is Monday .....

    public CardItem(String title, String text) {
        mTitle = title;
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public String getTitle() {
        return mTitle;
    }
}
