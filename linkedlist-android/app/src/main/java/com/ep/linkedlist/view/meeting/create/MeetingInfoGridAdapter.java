package com.ep.linkedlist.view.meeting.create;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiwon on 2016-09-20.
 */
@EBean
public class MeetingInfoGridAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private static final String TAG = MeetingInfoGridAdapter.class.getName();

    @RootContext
    Context context;

    private String selectedInfo = "";
    private List<String> infoList = new ArrayList<>();
    private OnMeetingInfoClickListnener onMeetingInfoClickListnener;

    public void setInfoList(List<String> infoList){
        this.infoList = infoList;
        this.notifyDataSetChanged();
    }

    public void setGridView(GridView gridView){
        gridView.setOnItemClickListener(this);
    }

    public void setOnMeetingInfoClickListnener(OnMeetingInfoClickListnener listnener){
        this.onMeetingInfoClickListnener = listnener;
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public String getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedInfo = getItem(position);
        notifyDataSetChanged();
        if(onMeetingInfoClickListnener!=null){
            onMeetingInfoClickListnener.onClick(selectedInfo);
        }
        Log.d(TAG, selectedInfo);
    }

    public interface OnMeetingInfoClickListnener {
        public void onClick(String meetingInfo);
    }
}