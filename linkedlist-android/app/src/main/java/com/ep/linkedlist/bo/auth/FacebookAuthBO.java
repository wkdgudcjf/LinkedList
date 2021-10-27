package com.ep.linkedlist.bo.auth;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ep.linkedlist.view.login.LoginActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * h
 * Created by jiwon on 2016-10-02.
 */
@EBean
public class FacebookAuthBO {
    private final static String TAG = FacebookAuthBO.class.getName();

    private FirebaseAuth firebaseAuth;
    private CallbackManager facebookCallbackManager;
    LoginButton facebookLoginButton;

    @RootContext
    Context context;

    public void login() {
        facebookLoginButton.performClick();
    }

    public void logout() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
    }

    public void init(Context context) {
        this.context = context;

        FacebookSdk.sdkInitialize(context);
        firebaseAuth = FirebaseAuth.getInstance();
        facebookCallbackManager = CallbackManager.Factory.create();

        facebookLoginButton = new LoginButton(context);
        facebookLoginButton.registerCallback(facebookCallbackManager, facebookCallback);
    }

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "onSuccess");
            ((LoginActivity)context).showProgressDialog();
            handleFacebookAccessToken(loginResult.getAccessToken());
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d(TAG, "onError");
        }
    };

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void destory() {

    }
}
