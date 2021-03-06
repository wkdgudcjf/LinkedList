package com.ep.linkedlist.backend.meeting.bo;

import com.ep.linkedlist.backend.firebase.util.FirebaseUtil;
import com.ep.linkedlist.backend.meeting.model.MeetingInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jiwon on 2016-10-02.
 */
@Service
public class MeetingBO {
    private final Logger logger = LoggerFactory.getLogger(MeetingBO.class);

    @Autowired
    Gson gson;

    @Autowired
    FirebaseDatabase firebaseDatabase;

    @Autowired
    FirebaseUtil firebaseUtil;

    private Map<String, Map<String, Object>> getMeetingMap(){
        Query meetingUsersQuery = firebaseDatabase.getReference("meeting/users");
        Query meetingInfoQuery = firebaseDatabase.getReference("meeting/info");

        return FirebaseUtil.join(
                meetingUsersQuery,
                meetingInfoQuery
        );
    }

    private Map<String, Map<String, Object>> getUsersMap(){
        Query usersInterestQuery = firebaseDatabase.getReference("users/interest");
        Query usersInfoQuery = firebaseDatabase.getReference("users/Info");
        Query usersDeviceQuery = firebaseDatabase.getReference("users/device");

        return FirebaseUtil.join(
                usersInterestQuery,
                usersInfoQuery,
                usersDeviceQuery
        );
    }

    private boolean isAvailableUserCount(MeetingInfo meetingInfo){
        long genderCount = meetingInfo.getManCount() + meetingInfo.getWomanCount();
        return meetingInfo.getWomanCount() < genderCount || meetingInfo.getManCount() < genderCount;
    }

    private Map<String, String> getPushUpdateData(Map<String, String> devices, String message){
        Map<String, String> updates = new HashMap<String, String>();
        for(Map.Entry<String, String> device : devices.entrySet()){
            String deviceToken = device.getKey();
            updates.put(deviceToken, message);
        }
        return updates;
    }

    public void invite(){

        Map<String, Map<String, Object>> meetingMap = getMeetingMap();

        logger.debug(gson.toJson(meetingMap));

        Map<String, Map<String, Object>> usersMap = getUsersMap();

        logger.debug(gson.toJson(usersMap));

        for (Map.Entry<String, Map<String, Object>> meeting : meetingMap.entrySet()) {
            String meetingKey = meeting.getKey();

            MeetingInfo meetingInfo = firebaseUtil.getObject(meeting.getValue(),"info",MeetingInfo.class);

            // ????????? ??? ????????? ??????
            if(isAvailableUserCount(meetingInfo) == false){
                continue;
            }

            // ?????? ??????
            String interest = meetingInfo.getInterest();

            // ?????????????????? ?????? ?????????????????? ??????
            for (Iterator<Map.Entry<String, Map<String, Object>>> iterator = usersMap.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, Map<String, Object>> users = iterator.next();

                String uid = users.getKey();
                logger.debug("uid : " + uid);
                Map<String, Object> value = users.getValue();
                boolean hasInterest = FirebaseUtil.getValue(value, "interest/" + interest) != null;
                logger.debug("inserest/" + interest + " : " + hasInterest);
                if (hasInterest == false) {
                    continue;
                }

                // ??? ????????? ?????? ???????????? ??????
                boolean canJoin = FirebaseUtil.getValue(meeting.getValue(), "users/" + uid) == null;
                logger.debug("users/" + uid + " : " + canJoin);
                if (canJoin == false) {
                    continue;
                }

                // ??????????????? ?????? ???????????? ????????? ??????
                Map<String, Object> updates = new HashMap<String, Object>();
                updates.put("meeting/info/" + meetingKey + "/manCount", meetingInfo.getManCount() + 1);
                updates.put("meeting/users/" + meetingKey + "/" + uid, "wait");
                updates.put("users/meeting/" + uid + "/" + meetingKey, "wait");

                // ?????? ????????? ??????
                Map<String, String> devices = FirebaseUtil.getValue(users.getValue(), "device");
                String mssage = meetingInfo.getMeetingName() + "??? ?????????????????????.";
                Map<String, String> push = getPushUpdateData(devices, mssage);
                updates.put("push", push);

                firebaseDatabase.getReference().updateChildren(updates);

                logger.debug("update");
                // ????????? ?????? ??????????????? ????????? ???????????? ??????
                iterator.remove();
            }

        }
    }
}
