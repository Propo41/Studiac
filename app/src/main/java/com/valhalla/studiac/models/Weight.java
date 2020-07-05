package com.valhalla.studiac.models;

public class Weight {
    private String mGrade;
    private double mWeight;


    public Weight() {
    }

    public Weight(String mGrade, double mWeight) {
        this.mGrade = mGrade;
        this.mWeight = mWeight;
    }

    public String getGrade() {
        return mGrade;
    }

    public void setGrade(String mGrade) {
        this.mGrade = mGrade;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double mWeight) {
        this.mWeight = mWeight;
    }
}
