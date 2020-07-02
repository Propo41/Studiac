package com.valhalla.studiac.utility;

public class CustomPair {
    public Integer first;
    public Integer second;

    CustomPair(){}

    public CustomPair(Integer first, Integer second) {
        this.first = first;
        this.second = second;
    }


    /**
     * Checks the two objects for equality by checking if first==second
     */
    public boolean isEqual(Integer first, Integer second) {
        return first.equals(second);
    }


}



