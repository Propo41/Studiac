package com.valhalla.studiac.models;

public class Pojo {
    private double mGPA;
    private boolean mWeightUpdated;

    public Pojo(double mGPA) {
        this.mGPA = mGPA;
    }

    public Pojo(boolean mWeightUpdated){
        this.mWeightUpdated = mWeightUpdated;
    }

    public double getGPA() {
        return mGPA;
    }

    public void setGPA(double mGPA) {
        this.mGPA = mGPA;
    }

    public boolean isWeightUpdated() {
        return mWeightUpdated;
    }

    public void setWeightUpdated(boolean mWeightUpdated) {
        this.mWeightUpdated = mWeightUpdated;
    }

}
