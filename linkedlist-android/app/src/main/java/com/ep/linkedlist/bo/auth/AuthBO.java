package com.ep.linkedlist.bo.auth;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.ep.linkedlist.model.auth.AuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by jiwon on 2016-12-04.
 */
public class AuthBO {
    private static final String TAG = AuthBO.class.getName();

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FacebookAuthBO facebookAuthBO = new FacebookAuthBO();
    private GoogleAuthBO googleAuthBO = new GoogleAuthBO();
    private EmailAndPasswordAuthBO emailAndPasswordAuthBO = new EmailAndPasswordAuthBO();

    Activity activity;
    AuthStateListener authStateListener;

    public AuthBO(Activity activity, AuthStateListener authStateListener) {
        this.activity = activity;
        this.authStateListener = authStateListener;
    }

    public void init() {
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
        facebookAuthBO.init(activity);
        googleAuthBO.init(activity);
    }

    public void destory() {
        firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
        facebookAuthBO.destory();
        googleAuthBO.destory();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookAuthBO.onActivityResult(requestCode, resultCode, data);
        googleAuthBO.onActivityResult(requestCode, resultCode, data);
    }

    FirebaseAuth.AuthStateListener firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (authStateListener == null) {
                return;
            }

            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                authStateListener.onLogin(firebaseUser);
            } else {
                authStateListener.onLogout();
            }
        }
    };

    public void facebookLogin() {
        facebookAuthBO.login();
    }

    public void googleLogin() {
        googleAuthBO.login();
    }

    public void emailLogin(String email, String password) {
        emailAndPasswordAuthBO.login(email, password);
    }

    @Deprecated
    public void emailLogout() {
        emailAndPasswordAuthBO.logout();
}

    public void logout() {
        AuthProvider authProvider = AuthUtils.getAuthProvider();
        if (authProvider == AuthProvider.FACEBOOK) {
            facebookAuthBO.logout();
        } else if (authProvider == AuthProvider.GOOGLE) {
            googleAuthBO.logout();
        }
    }

    public interface AuthStateListener {
        public void onLogin(FirebaseUser firebaseUser);

        public void onLogout();
    }
}
