package com.ep.linkedlist.view.meeting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ep.linkedlist.bo.meeting.MeetingBO;
import com.ep.linkedlist.model.meeting.MeetingInfo;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiwon on 2016-09-20.
 */
@EBean
public class MeetingListAdapter extends BaseAdapter {
    private static final String TAG = MeetingListAdapter.class.getName();

    @RootContext
    Context context;

    @Bean
    MeetingBO meetingBO;

    List<MeetingInfo> meetingInfoList = new ArrayList<>();

    @Override
    public int getCount() {
        return meetingInfoList.size();
    }

    @Override
    public MeetingInfo getItem(int position) {
        return meetingInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MeetingItem meetingItem;
        if (convertView == null) {
            meetingItem = MeetingItem_.build(context);
        } else {
            meetingItem = (MeetingItem) convertView;
        }

        MeetingInfo meetingInfo = getItem(position);

        meetingItem.bind(meetingInfo);

        return meetingItem;
    }

    public void setMeetingInfoList(List<MeetingInfo> meetingInfoList) {
        this.meetingInfoList.clear();
        this.meetingInfoList.addAll(meetingInfoList);
        notifyDataSetChanged();
    }
}