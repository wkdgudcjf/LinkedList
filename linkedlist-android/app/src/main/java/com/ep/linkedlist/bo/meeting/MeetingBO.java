package com.ep.linkedlist.bo.meeting;

import android.util.Log;

import com.ep.linkedlist.bo.pay.PayBO;
import com.ep.linkedlist.bo.profile.ProfileBO;
import com.ep.linkedlist.model.ChatInfo;
import com.ep.linkedlist.model.meeting.MeetingInfo;
import com.ep.linkedlist.model.meeting.MeetingJoinUser;
import com.ep.linkedlist.model.meeting.MeetingUserState;
import com.ep.linkedlist.model.profile.Gender;
import com.ep.linkedlist.model.profile.Profile;
import com.ep.linkedlist.service.rest.PushRestService;
import com.ep.linkedlist.util.CalendarFactory;
import com.ep.linkedlist.util.DefaultValueEventListener;
import com.ep.linkedlist.util.FirebaseUtils;
import com.ep.linkedlist.util.OnDataChangeListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiwon on 2016-09-19.
 */
@EBean
public class MeetingBO {
    private static final String TAG = MeetingBO.class.getName();

    @Bean
    PayBO payBO;

    @RestService
    PushRestService pushRestService;

    public void createMeeting(MeetingInfo meetingInfo) throws IllegalAccessException, IllegalStateException {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Log.d(TAG, "User is not logged in.");
            throw new IllegalAccessException("User is not logged in.");
        }

        payBO.setUser(firebaseUser);

        // 캐시 보유 확인
        if (payBO.hasFreeCoupon() == false || payBO.hasMeetingCash() == false) {
            throw new IllegalStateException();
        }

        // 방 개설
        insertMeeting(firebaseUser, meetingInfo);
        insertChating(firebaseUser, meetingInfo);

        // 과금
        payBO.createMeeting();
    }

    private void insertMeeting(FirebaseUser firebaseUser, MeetingInfo meetingInfo) {
        // get uid, meetingId
        String uid = firebaseUser.getUid();
        String meetingId = FirebaseDatabase.getInstance().getReference("meeting/info").push().getKey();
        meetingInfo.setId(meetingId);
        if (ProfileBO.getMyProfile().getGender().equals(Gender.MAN.getValue())) {
            meetingInfo.setManCount(1);
            meetingInfo.setWomanCount(0);
        } else {
            meetingInfo.setWomanCount(1);
            meetingInfo.setManCount(0);
        }


        HashMap<String, MeetingJoinUser> meetingJoinUserMap = new HashMap<String, MeetingJoinUser>();
        MeetingJoinUser meetingJoinUser = new MeetingJoinUser();
        meetingJoinUser.setState(MeetingUserState.ACCEPT.name());
        meetingJoinUser.setTimestamp(CalendarFactory.createInstance().getTimeInMillis());
        meetingJoinUser.setPhotoURL(ProfileBO.getMyProfile().getPhotoURI());
        meetingJoinUser.setGender(ProfileBO.getMyProfile().getGender());
        meetingJoinUserMap.put(firebaseUser.getUid(), meetingJoinUser);
        meetingInfo.setUsers(meetingJoinUserMap);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> updates = new HashMap<>();
        updates.put("meeting/info/" + meetingId, new ObjectMapper().convertValue(meetingInfo, Map.class));
        databaseReference.updateChildren(updates);
    }

    private void insertChating(FirebaseUser firebaseUser, MeetingInfo meetingInfo) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setId(meetingInfo.getId());
        chatInfo.setChatName(meetingInfo.getMeetingName());
        chatInfo.setUserCount(meetingInfo.getUserCount());
        chatInfo.setLastChatContent("새로운 채팅방에서 얘기를 나눠보세요!");
        chatInfo.setChatPhoto(meetingInfo.getPhotoUrl());
        chatInfo.setNoReadCount(0);
        chatInfo.setLastChatTimestamp(CalendarFactory.createInstance().getTimeInMillis());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> updates = new HashMap<>();
        updates.put("users/chat/" + firebaseUser.getUid() + "/" + chatInfo.getId(), new ObjectMapper().convertValue(chatInfo, Map.class));
        databaseReference.updateChildren(updates);
    }

    public void joinMeeting(final MeetingInfo meetingInfo, final FirebaseUtils.FirebaseResult firebaseResult) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            firebaseResult.onError();
        }

        String reference;
        Gender gender = Gender.fromValue(ProfileBO.getMyProfile().getGender());
        if (gender == Gender.MAN) {
            reference = "meeting/info/" + meetingInfo.getId() + "/manCount";
        } else {
            reference = "meeting/info/" + meetingInfo.getId() + "/womanCount";
        }
        FirebaseUtils.incrementCounter(reference, 1, new FirebaseUtils.FirebaseResult() {
            @Override
            public void onSuccess() {
                MeetingJoinUser meetingJoinUser = new MeetingJoinUser();
                meetingJoinUser.setState(MeetingUserState.ACCEPT.name());
                meetingJoinUser.setPhotoURL(ProfileBO.getMyProfile().getPhotoURI());
                meetingJoinUser.setGender(ProfileBO.getMyProfile().getGender());
                meetingJoinUser.setTimestamp(CalendarFactory.createInstance().getTimeInMillis());
                FirebaseDatabase.getInstance().getReference("meeting/info/" + meetingInfo.getId() + "/users/" + firebaseUser.getUid()).setValue(meetingJoinUser);
                insertChating(firebaseUser, meetingInfo);
                firebaseResult.onSuccess();
            }

            @Override
            public void onError() {
                firebaseResult.onError();
            }
        });
    }

    public void exitMeeting(final MeetingInfo meetingInfo, final FirebaseUtils.FirebaseResult firebaseResult) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if ((meetingInfo.getManCount() + meetingInfo.getWomanCount()) == 1) {
            FirebaseDatabase.getInstance().getReference("meeting/info/" + meetingInfo.getId()).removeValue();
            FirebaseDatabase.getInstance().getReference("chat/message/" + meetingInfo.getId()).removeValue();
            firebaseResult.onSuccess();
        } else {
            FirebaseUtils.incrementCounter("meeting/info/" + meetingInfo.getId() + "/" + (ProfileBO.getMyProfile().getGender().equals(Gender.MAN.getValue()) ? "manCount" : "womanCount"), -1, new FirebaseUtils.FirebaseResult() {
                @Override
                public void onSuccess() {
                    firebaseResult.onSuccess();
                    FirebaseDatabase.getInstance().getReference("meeting/info/" + meetingInfo.getId() + "/users/" + firebaseUser.getUid()).removeValue();
                }

                @Override
                public void onError() {
                    firebaseResult.onError();
                }
            });
        }
    }
    public void getWaitMeetingInfoList(final OnDataChangeListener<List<MeetingInfo>> listener) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        final ArrayList<MeetingInfo> meetingInfoList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("meeting/info").addListenerForSingleValueEvent(
                new DefaultValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                            String key = children.getKey();
                            MeetingInfo meetingInfo = children.getValue(MeetingInfo.class);
                            meetingInfo.setId(key);

                            // 대기중이거나 참여중이거나 거절한 목록 제외
                            boolean isJoined = false;
                            if (meetingInfo.getUsers() == null || meetingInfo.getUsers().get(firebaseUser.getUid()) == null) {
                                meetingInfoList.add(meetingInfo);
                            }
                        }

                        if (listener != null) {
                            listener.onDataChange(meetingInfoList);
                        }
                    }
                }
        );
    }

    public boolean enableJoinMeeting(MeetingInfo meetingInfo) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Profile profile = ProfileBO.getMyProfile();
        // 성별에 따른 인원수가 찬 방 삭제
        int genderCount = (int) meetingInfo.getUserCount() / 2;
        if (Gender.fromValue(profile.getGender()) == Gender.MAN) {
            if (meetingInfo.getManCount() >= genderCount) {
                return false;
            }
        } else {
            if (meetingInfo.getWomanCount() >= genderCount) {
                return false;
            }
        }

        // 대기중이거나 참여중이거나 거절한 목록 제외
        if (meetingInfo.getUsers() != null && meetingInfo.getUsers().get(firebaseUser.getUid()) != null) {
            return false;
        }
        // 참여 가능한 지역 아니면 제외


        // 인쳔, 경기, 서울은 통합
        if (meetingInfo.getRegion().equals("서울") || meetingInfo.getRegion().equals("인천") || meetingInfo.getRegion().equals("경기")) {
            if ((profile.getRegion().equals("서울") || profile.getRegion().equals("인천") || profile.getRegion().equals("경기")) == false) {
                return false;
            }
        } else {
            if (meetingInfo.getRegion().equals(profile.getRegion()) == false) {
                return false;
            }
        }

        return true;
    }

}