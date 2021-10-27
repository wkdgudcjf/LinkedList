package com.ep.linkedlist.bo.messaging;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.androidannotations.annotations.EBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiwon on 2016-09-19.
 */
@EBean
public class DeviceBO {
    private static final String TAG = DeviceBO.class.getName();

    public void register() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String deviceToken = FirebaseInstanceId.getInstance().getToken();

        if (user != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put(deviceToken, true);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/device/" + user.getUid());
            databaseReference.updateChildren(updates);
            Log.d(TAG, "User device token is updated.");
            Log.d(TAG, "uid : " + user.getUid());
            Log.d(TAG, "deviceToken : " + deviceToken);
        }
    }
}
