package com.example.project.utility.todo;



public class CurrentWeekItems {

    private String mText1;
    private boolean mSection;

    public CurrentWeekItems(String text1){
        mSection = false;
        mText1 = text1;
    }

    public CurrentWeekItems(String text1, boolean section){
        mSection = section;
        mText1 = text1;
    }

    public void setSection(boolean section){
        mSection = section;
    }

    public boolean isSection(){
        return mSection;
    }

    public String getText1() {
        return mText1;
    }


    public void changeText(String text){
        mText1 = text;
    }



}
