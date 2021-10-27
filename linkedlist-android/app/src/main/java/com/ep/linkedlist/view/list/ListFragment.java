package com.ep.linkedlist.view.list;


import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.ep.linkedlist.R;
import com.ep.linkedlist.bo.meeting.MeetingBO;
import com.ep.linkedlist.model.meeting.MeetingInfo;
import com.ep.linkedlist.view.meeting.MeetingItem;
import com.ep.linkedlist.view.meeting.MeetingItem_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiwon on 2016-09-19.
 */
@EFragment(R.layout.activity_list)
public class ListFragment extends Fragment {
    private static final String tag = ListFragment.class.getName();

    @ViewById
    LinearLayout meetingListInWait;

    @ViewById
    LinearLayout meetingListInStart;

    @Bean
    MeetingBO meetingBO;

    @AfterViews
    void updateViews() {

    }
}
