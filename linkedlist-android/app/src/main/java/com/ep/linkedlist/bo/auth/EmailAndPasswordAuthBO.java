package com.ep.linkedlist.bo.auth;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by jiwon on 2016-11-20.
 */
public class EmailAndPasswordAuthBO {
    private final static String TAG = EmailAndPasswordAuthBO.class.getName();

    private FirebaseAuth firebaseAuth;

    Context context;

    public void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                }
            }
        });
    }

    public void logout(){
        firebaseAuth.signOut();
    }

    public void init(Context context){
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
    }
}
