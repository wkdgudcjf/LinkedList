package com.ep.linkedlist.bo.pay;

import com.google.firebase.auth.FirebaseUser;

import org.androidannotations.annotations.EBean;

/**
 * Created by Naver on 2016-09-30.
 */
@EBean
public class PayBO {
    private FirebaseUser firebaseUser;

    public void setUser(FirebaseUser user){
        firebaseUser = user;
    }

    public boolean hasFreeCoupon(){
        return true;
    }

    public boolean hasMeetingCash(){
        return true;
    }

    public void createMeeting(){
        return;
    }
}
