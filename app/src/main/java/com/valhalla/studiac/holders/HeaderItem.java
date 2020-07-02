package com.valhalla.studiac.holders;

import com.google.gson.annotations.SerializedName;

public class HeaderItem extends ListItem {
    @SerializedName("header")
    private String mHeader;

    public HeaderItem() {
        mHeader = "header";
        type = "HeaderItem";

    }

    public HeaderItem(String header) {
        mHeader = header;
        type = "HeaderItem";

    }

    public String getHeader() {
        return mHeader;
    }

    public void setHeader(String header) {
        mHeader = header;
    }
}
