package com.ep.linkedlist.model.meeting;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by jiwon on 2017-01-07.
 */
public class MeetingJoinUser {
    private String state;
    private long timestamp;
    private String photoURL;
    private String gender;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
