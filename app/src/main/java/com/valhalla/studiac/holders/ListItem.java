package com.valhalla.studiac.holders;

import com.google.gson.annotations.SerializedName;

public class ListItem {
    @SerializedName("type")
    public String type = "ListItem";

    public ListItem() {

    }

    public String getTaskType() {
        return type;
    }

}
