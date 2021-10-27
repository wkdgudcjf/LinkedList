package com.ep.linkedlist.backend.meeting.model;

import com.ep.linkedlist.backend.meeting.model.MeetingJoinUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiwon on 2016-10-02.
 */
public class MeetingInfo {
    @JsonIgnore
    String id;
    long userCount;
    long manCount;
    long womanCount;
    String region;
    String interest;
    String detailInterest;
    String meetingName;
    Map<String, MeetingJoinUser> users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getManCount() {
        return manCount;
    }

    public void setManCount(long manCount) {
        this.manCount = manCount;
    }

    public long getWomanCount() {
        return womanCount;
    }

    public void setWomanCount(long womanCount) {
        this.womanCount = womanCount;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getDetailInterest() {
        return detailInterest;
    }

    public void setDetailInterest(String detailInterest) {
        this.detailInterest = detailInterest;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public Map<String, MeetingJoinUser> getUsers() {
        return users;
    }

    public void setUsers(Map<String, MeetingJoinUser> users) {
        this.users = users;
    }
}
