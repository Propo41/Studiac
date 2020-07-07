package com.valhalla.studiac.holders;

/**
 * A holder class that holds the text and an associated image.
 * @uses: used in custom spinners, settings activity
 * extends ListItem to help display contents in the recycler list of settings Activity
 */
public class ImageItem extends ListItem {
    private String mName;
    private int mImageId;

    public ImageItem(String name, int imageId) {
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
