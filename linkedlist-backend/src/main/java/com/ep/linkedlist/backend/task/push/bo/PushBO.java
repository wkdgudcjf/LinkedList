package com.ep.linkedlist.backend.task.push.bo;

import com.ep.linkedlist.backend.firebase.push.bo.FcmBO;
import com.ep.linkedlist.backend.firebase.util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by jiwon on 2016-10-02.
 */
@Service
public class PushBO {
    @Autowired
    FcmBO fcmBO;

    @Autowired
    FirebaseDatabase firebaseDatabase;

    @Autowired
    FirebaseUtil firebaseUtil;

    public void sendMessage() {
        DataSnapshot dataSnapshot = FirebaseUtil.await(firebaseDatabase.getReference("push"));
        for (DataSnapshot push : dataSnapshot.getChildren()) {
            String deviceToken = push.getKey();
            String message = push.getValue(String.class);
//            try {
//                fcmBO.sendFirebaseCloudMessage(deviceToken, "초대 메시지", message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}
