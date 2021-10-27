package com.ep.linkedlist.model;

import java.util.Map;

/**
 * Created by jiwon on 2016-10-09.
 */
public class Meta {
    private Map<String, Map<String, String>> interestCategory;
    private Map<String, Boolean> meetingUserCount;
    private Map<String, Boolean> region;

    public Map<String, Map<String, String>> getInterestCategory() {
        return interestCategory;
    }

    public void setInterestCategory(Map<String, Map<String, String>> interestCategory) {
        this.interestCategory = interestCategory;
    }

    public Map<String, Boolean> getMeetingUserCount() {
        return meetingUserCount;
    }

    public void setMeetingUserCount(Map<String, Boolean> meetingUserCount) {
        this.meetingUserCount = meetingUserCount;
    }

    public Map<String, Boolean> getRegion() {
        return region;
    }

    public void setRegion(Map<String, Boolean> region) {
        this.region = region;
    }
}
