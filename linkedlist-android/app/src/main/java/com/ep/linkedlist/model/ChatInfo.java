package com.ep.linkedlist.model;

/**
 * Created by jiwon on 2016-11-06.
 */
public class ChatInfo {
    String id;
    String chatPhoto;
    String chatName;
    long userCount;
    String lastChatContent;
    long lastChatTimestamp;
    long noReadCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatPhoto() {
        return chatPhoto;
    }

    public void setChatPhoto(String chatPhoto) {
        this.chatPhoto = chatPhoto;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getLastChatContent() {
        return lastChatContent;
    }

    public void setLastChatContent(String lastChatContent) {
        this.lastChatContent = lastChatContent;
    }

    public long getLastChatTimestamp() {
        return lastChatTimestamp;
    }

    public void setLastChatTimestamp(long lastChatTimestamp) {
        this.lastChatTimestamp = lastChatTimestamp;
    }

    public long getNoReadCount() {
        return noReadCount;
    }

    public void setNoReadCount(long noReadCount) {
        this.noReadCount = noReadCount;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }
}
