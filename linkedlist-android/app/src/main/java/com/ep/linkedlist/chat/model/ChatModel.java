package com.ep.linkedlist.chat.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Alessandro Barreto on 17/06/2016.
 */
public class ChatModel {
    @JsonIgnore
    private String id;
    private String uid;
    private String message;
    private long timestamp;

    public ChatModel() {

    }

    public ChatModel(String uid, String message, long timeStamp) {
        this.uid = uid;
        this.message = message;
        this.timestamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
