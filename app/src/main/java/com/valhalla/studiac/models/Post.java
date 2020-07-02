package com.valhalla.studiac.models;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Post {
    private String mBody;
    private String mCategory;
    private String mTopic;
    private String mUid;
    private String mAuthor;
    private String mAuthorImageRes;
    private long mTimePosted;

    public Post() {

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("body", mBody);
        result.put("category", mCategory);
        result.put("topic", mTopic);
        result.put("uid", mUid);
        result.put("author", mAuthor);
        result.put("authorImageRes", mAuthorImageRes);
        result.put("timePosted", mTimePosted);

        return result;
    }

    public String getAuthorImageRes() {
        return mAuthorImageRes;
    }

    public void setAuthorImageRes(String authorImageRes) {
        mAuthorImageRes = authorImageRes;
    }

    public Post(String body, String category, String topic, String uid, String author, String authorImage, long timePosted) {
        mBody = body;
        mCategory = category;
        mTopic = topic;
        mUid = uid;
        mAuthor = author;
        mAuthorImageRes = authorImage;
        this.mTimePosted = timePosted;
    }


    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }



    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String getTopic() {
        return mTopic;
    }

    public void setTopic(String topic) {
        mTopic = topic;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public long getTimePosted() {
        return mTimePosted;
    }

    public void setTimePosted(long timePosted) {
        this.mTimePosted = timePosted;
    }


}
