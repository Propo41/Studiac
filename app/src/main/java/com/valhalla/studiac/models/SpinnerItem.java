package com.valhalla.studiac.models;

import java.util.ArrayList;

public class SpinnerItem {
    private String mName;
    private int mImageId;

    public SpinnerItem(String name, int imageId) {
        mName = name;
        mImageId = imageId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int imageId) {
        mImageId = imageId;
    }
}
