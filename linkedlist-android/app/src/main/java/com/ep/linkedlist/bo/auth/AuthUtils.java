package com.ep.linkedlist.bo.auth;

import android.content.Context;
import android.content.Intent;

import com.ep.linkedlist.model.auth.AuthProvider;
import com.ep.linkedlist.view.login.LoginActivity;
import com.ep.linkedlist.view.profile.RegistProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * Created by jiwon on 2016-10-02.
 */
public class AuthUtils {
    private final static String TAG = AuthUtils.class.getName();

    public static boolean isLogin() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static AuthProvider getAuthProvider() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        }

        if (user.getProviders().contains("facebook.com")) {
            return AuthProvider.FACEBOOK;
        } else if (user.getProviders().contains("google.com")) {
            return AuthProvider.GOOGLE;
        }
        return null;
    }
}
