package com.ep.linkedlist.bo.chat;

import android.util.Log;

import com.ep.linkedlist.bo.meeting.MeetingBO;
import com.ep.linkedlist.model.meeting.MeetingInfo;
import com.ep.linkedlist.model.ChatInfo;
import com.ep.linkedlist.model.meeting.MeetingJoinUser;
import com.ep.linkedlist.util.DefaultValueEventListener;
import com.ep.linkedlist.util.FirebaseUtils;
import com.ep.linkedlist.util.OnDataChangeListener;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jiwon on 2016-09-19.
 */
@EBean
public class ChatBO {
    private static final String TAG = ChatBO.class.getName();

    @Bean
    MeetingBO meetingBO;

    public void updateChatInfo(final MeetingInfo meetingInfo, final String message, final long timestamp){
        String chatId = meetingInfo.getId();
        for(String uid : meetingInfo.getUsers().keySet()) {
            transactionChatInfo("users/chat/" + uid + "/" + chatId, message, timestamp);
        }
    }

    private void transactionChatInfo(final String reference, final String message, final long timestamp) {
        FirebaseDatabase.getInstance().getReference(reference).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                ChatInfo chatInfo = currentData.getValue(ChatInfo.class);
                if (chatInfo != null) {
                    Log.d(TAG, "transactionChatInfo : " + reference);
                    chatInfo.setLastChatContent(message);
                    chatInfo.setLastChatTimestamp(timestamp);
                    chatInfo.setNoReadCount(chatInfo.getNoReadCount() + 1);
                    currentData.setValue(chatInfo);
                } else {
                    Log.d(TAG, "transactionChatInfo fail : " + reference);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "transactionChatInfo:onComplete:" + databaseError);
            }
        });
    }

    public void exitChat(final ChatInfo chatInfo, final MeetingInfo meetingInfo, final FirebaseUtils.FirebaseResult firebaseResult) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        meetingBO.exitMeeting(meetingInfo, new FirebaseUtils.FirebaseResult() {
            @Override
            public void onSuccess() {
                FirebaseDatabase.getInstance().getReference("users/chat/" + firebaseUser.getUid() + "/" + chatInfo.getId()).removeValue();
                firebaseResult.onSuccess();
            }

            @Override
            public void onError() {
                firebaseResult.onError();
            }
        });
    }
}