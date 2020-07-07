package com.valhalla.studiac.models;

public class Messenger {
    String user1Image;
    String user1Name;
    String user2Image;
    String user2Name;
    String user1Uid;
    String user2Uid;
    long time; // stores the time of the last message; keeps on changing constantly
    String lastMessage; // stores the last message sent

    public Messenger(){

    }

    public Messenger(String user1Image, String user1Name, String user2Image, String user2Name,
                     String user1Uid, String user2Uid, long time, String lastMessage) {
        this.user1Image = user1Image;
        this.user1Name = user1Name;
        this.user2Image = user2Image;
        this.user2Name = user2Name;
        this.user1Uid = user1Uid;
        this.user2Uid = user2Uid;
        this.time = time;
        this.lastMessage = lastMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUser1Image() {
        return user1Image;
    }

    public void setUser1Image(String user1Image) {
        this.user1Image = user1Image;
    }

    public String getUser1Name() {
        return user1Name;
    }

    public void setUser1Name(String user1Name) {
        this.user1Name = user1Name;
    }

    public String getUser2Image() {
        return user2Image;
    }

    public void setUser2Image(String user2Image) {
        this.user2Image = user2Image;
    }

    public String getUser2Name() {
        return user2Name;
    }

    public void setUser2Name(String user2Name) {
        this.user2Name = user2Name;
    }

    public String getUser1Uid() {
        return user1Uid;
    }

    public void setUser1Uid(String user1Uid) {
        this.user1Uid = user1Uid;
    }

    public String getUser2Uid() {
        return user2Uid;
    }

    public void setUser2Uid(String user2Uid) {
        this.user2Uid = user2Uid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
