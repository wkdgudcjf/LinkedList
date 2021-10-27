package com.ep.linkedlist.view.home;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.ep.linkedlist.R;
import com.ep.linkedlist.bo.meeting.MeetingBO;
import com.ep.linkedlist.bo.messaging.DeviceBO;
import com.ep.linkedlist.bo.profile.ProfileBO;
import com.ep.linkedlist.chat.ChatActivity_;
import com.ep.linkedlist.model.meeting.MeetingInfo;
import com.ep.linkedlist.model.profile.Gender;
import com.ep.linkedlist.service.rest.PushRestService;
import com.ep.linkedlist.util.DefaultValueEventListener;
import com.ep.linkedlist.util.FirebaseUtils;
import com.ep.linkedlist.view.meeting.MeetingListAdapter;
import com.ep.linkedlist.view.meeting.create.MeetingCreateActivity_;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jiwon on 2016-09-19.
 */
@EFragment(R.layout.activity_home)
public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.getName();

    private String MEETING_INFO_REFERENCE = "meeting/info";

    /**
     * 프로필 등록 전까지 임시 구현
     */
    @Bean
    DeviceBO deviceBO;

    @Bean
    MeetingBO meetingBO;

    @Bean
    MeetingListAdapter meetingListAdapter;

    @ViewById
    ListView meetingListView;

    @RestService
    PushRestService pushRestService;

    int count_n = 0;

    @AfterViews
    void afterViews(){
        deviceBO.register();
        meetingListView.setAdapter(meetingListAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference(MEETING_INFO_REFERENCE).addValueEventListener(defaultValueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference(MEETING_INFO_REFERENCE).removeEventListener(defaultValueEventListener);
    }

    DefaultValueEventListener defaultValueEventListener = new DefaultValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<MeetingInfo> meetingInfoList = new ArrayList<>();
            for (DataSnapshot children : dataSnapshot.getChildren()) {
                String key = children.getKey();
                MeetingInfo meetingInfo = children.getValue(MeetingInfo.class);
                meetingInfo.setId(key);
                if (meetingBO.enableJoinMeeting(meetingInfo)) {
                    meetingInfoList.add(meetingInfo);
                }
            }
            Collections.reverse(meetingInfoList);
            meetingListAdapter.setMeetingInfoList(meetingInfoList);
        }
    };

    @Click(R.id.moveCreateMeetingButton)
    void moveCreateMeeting() {
        startActivity(new Intent(getContext(), MeetingCreateActivity_.class));
    }

    @ItemClick(R.id.meetingListView)
    void clickMeetingItem(final MeetingInfo meetingInfo) {
        if(meetingInfo.getUserCount() == 4)
        {
            int mancount=(int)meetingInfo.getManCount();
            int womancount=(int)meetingInfo.getWomanCount();
            if((mancount >= 2) && (ProfileBO.getMyProfile().getGender().equals(Gender.MAN.getValue()))) {
                Toast.makeText(getContext(), "남자정원초과", Toast.LENGTH_SHORT).show();
                count_n = 1;
            }
            else if((womancount >= 2) && (ProfileBO.getMyProfile().getGender().equals(Gender.WOMAN.getValue()))) {
                Toast.makeText(getContext(), "여자정원초과", Toast.LENGTH_SHORT).show();
                count_n = 1;
            }
        }
        else if(meetingInfo.getUserCount() == 6)
        {
            int mancount=(int)meetingInfo.getManCount();
            int womancount=(int)meetingInfo.getWomanCount();
            if((mancount >= 3) && (ProfileBO.getMyProfile().getGender().equals(Gender.MAN.getValue()))) {
                Toast.makeText(getContext(), "남자정원초과", Toast.LENGTH_SHORT).show();
                count_n = 1;
            }
            else if((womancount >= 3) && (ProfileBO.getMyProfile().getGender().equals(Gender.WOMAN.getValue()))) {
                Toast.makeText(getContext(), "여자정원초과", Toast.LENGTH_SHORT).show();
                count_n = 1;
            }
        }
        else if(meetingInfo.getUserCount() == 8)
        {
            int mancount=(int)meetingInfo.getManCount();
            int womancount=(int)meetingInfo.getWomanCount();
            if((mancount >= 4) && (ProfileBO.getMyProfile().getGender().equals(Gender.MAN.getValue()))) {
                Toast.makeText(getContext(), "남자정원초과", Toast.LENGTH_SHORT).show();
                count_n = 1;
            }
            else if((womancount >= 4) && (ProfileBO.getMyProfile().getGender().equals(Gender.WOMAN.getValue()))) {
                Toast.makeText(getContext(), "여자정원초과", Toast.LENGTH_SHORT).show();
                count_n = 1;
            }
        }
        else
        {
            Toast.makeText(getContext(), "joinMeeting Error", Toast.LENGTH_SHORT).show();
        }
        if(count_n == 0) {
            meetingBO.joinMeeting(meetingInfo, new FirebaseUtils.FirebaseResult() {
                @Override
                public void onSuccess() {


                    pushMeetingJoin(meetingInfo);
                    Intent intent = new Intent(getContext(), ChatActivity_.class);
                    intent.putExtra("chatId", meetingInfo.getId());
                    startActivity(intent);
                }

                @Override
                public void onError() {
                    Toast.makeText(getContext(), "참여에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Background
    void pushMeetingJoin(MeetingInfo meetingInfo) {
        try {
            Gender gender = Gender.fromValue(ProfileBO.getMyProfile().getGender());
            pushRestService.pushMeetingJoin(meetingInfo.getId(), gender.name());
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }
}
