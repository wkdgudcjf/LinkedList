/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.ep.linkedlist.backend.controller;

import com.ep.linkedlist.backend.firebase.push.bo.FcmBO;
import com.ep.linkedlist.backend.firebase.util.DefaultValueEventListener;
import com.ep.linkedlist.backend.meeting.model.MeetingInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class PushController {
    private final Logger logger = LoggerFactory.getLogger(PushController.class);

    private final String PUSH_TITLE = "우리들의 연결고리";

    @Autowired
    private FcmBO fcmBO;

    @Autowired
    FirebaseDatabase firebaseDatabase;

    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/push/meeting/message", method = RequestMethod.POST)
    public
    @ResponseBody
    String pushMeetingMessage(@RequestParam("meetingId") final String meetingId) throws IOException {
        logger.debug("pushMeetingMessage : " + meetingId);
        firebaseDatabase.getReference("meeting/info/" + meetingId).addListenerForSingleValueEvent(new DefaultValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MeetingInfo meetingInfo = dataSnapshot.getValue(MeetingInfo.class);
                final String meetingName = meetingInfo.getMeetingName();
                for(String uid : meetingInfo.getUsers().keySet()) {
                    firebaseDatabase.getReference("users/device/" + uid).addListenerForSingleValueEvent(new DefaultValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot device : dataSnapshot.getChildren()) {
                                String tokenId = device.getKey();
                                String body = String.format("'%s' 우연에 새로운 메시지가 도착했습니다.", meetingName);
                                String scheme = "LinkedList://chat?v=1&chatId=" + meetingId;
                                logger.debug("tokenId : " + tokenId);
                                try{
                                    fcmBO.sendFirebaseCloudMessage(tokenId, PUSH_TITLE, body, scheme);
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                }
                            }
                        }
                    });
                }
            }
        });
        return "SUCCESS";
    }



    @RequestMapping(value = "/push/meeting/join", method = RequestMethod.POST)
    public
    @ResponseBody
    String pushMeetingJoin(@RequestParam("meetingId") final String meetingId, @RequestParam("gender") final String gender) throws IOException {
        logger.debug("pushMeetingJoin : " + meetingId);
        firebaseDatabase.getReference("meeting/info/" + meetingId).addListenerForSingleValueEvent(new DefaultValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MeetingInfo meetingInfo = dataSnapshot.getValue(MeetingInfo.class);
                final String meetingName = meetingInfo.getMeetingName();
                for(String uid : meetingInfo.getUsers().keySet()) {
                    firebaseDatabase.getReference("users/profile/" + uid).addListenerForSingleValueEvent(new DefaultValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String genderUid = dataSnapshot.getKey();
                            String genderName = dataSnapshot.child("gender").getValue().toString().equals("남자") ? "MAN" : "WOMAN";
                            if(gender.equals(genderName)) {
                                return;
                            }

                            firebaseDatabase.getReference("users/device/" + genderUid).addListenerForSingleValueEvent(new DefaultValueEventListener(){
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot device : dataSnapshot.getChildren()) {
                                        String tokenId = device.getKey();
                                        String body = String.format("'%s' 우연에 새로운 이성이 참여했습니다.", meetingName);
                                        String scheme = "LinkedList://chat?v=1&chatId=" + meetingId;
                                        logger.debug("tokenId : " + tokenId);
                                        try{
                                            fcmBO.sendFirebaseCloudMessage(tokenId, PUSH_TITLE, body, scheme);
                                        } catch (Exception e) {
                                            logger.error(e.getMessage(), e);
                                        }
                                    }
                                }
                            });
                        }
                    });

                }
            }
        });
        return "SUCCESS";
    }
}