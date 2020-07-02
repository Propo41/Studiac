package com.valhalla.studiac.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    String text;
    long time;
    String userUid;

    public Message(){

    }

    public Message(String text, long time, String userUid) {
        this.text = text;
        this.time = time;
        this.userUid = userUid;
    }


    protected Message(Parcel in) {
        text = in.readString();
        time = in.readLong();
        userUid = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeLong(time);
        parcel.writeString(userUid);
    }
}
