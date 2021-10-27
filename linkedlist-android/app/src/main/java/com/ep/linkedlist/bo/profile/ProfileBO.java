package com.ep.linkedlist.bo.profile;

import com.ep.linkedlist.model.profile.Profile;
import com.ep.linkedlist.util.DefaultValueEventListener;
import com.ep.linkedlist.util.OnDataChangeListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.EBean;

/**
 * Created by jiwon on 2017-01-01.
 */
@EBean
public class ProfileBO {
    private static Profile myProfile;

    private static String USER_PROFILE_REFERENCE = "users/profile/";

    public Task<Void> updateProfile(Profile profile) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        profile.setUid(firebaseUser.getUid());
        setMyProfile(profile);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(USER_PROFILE_REFERENCE + profile.getUid());
        return databaseReference.setValue(profile);
    }

    public void getProfile(final String uid, final OnDataChangeListener<Profile> listener) {
        FirebaseDatabase.getInstance().getReference(USER_PROFILE_REFERENCE + uid).addListenerForSingleValueEvent(new DefaultValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                if(profile!=null) {
                    profile.setUid(dataSnapshot.getKey());
                }
                if(listener!=null) {
                    listener.onDataChange(profile);
                }
            }
        });
    }

    public static void setMyProfile(Profile profile) {
        myProfile = profile;
    }

    public static Profile getMyProfile() {
        return myProfile;
    }
}
