package com.example.project.utility;

public class CardItem {

    private String mText;
    private String mTitle;



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
