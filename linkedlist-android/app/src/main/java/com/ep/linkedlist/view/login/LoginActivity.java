package com.ep.linkedlist.view.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ep.linkedlist.R;
import com.ep.linkedlist.bo.auth.AuthBO;
import com.ep.linkedlist.bo.profile.ProfileBO;
import com.ep.linkedlist.model.profile.Profile;
import com.ep.linkedlist.util.DefaultValueEventListener;
import com.ep.linkedlist.view.MainPagerActivity_;
import com.ep.linkedlist.view.profile.RegistProfileActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = this.getClass().getName();
    public ProgressDialog mProgressDialog;

    private AuthBO authBO;
    private EditText mEmailField;
    private EditText mPasswordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main_mini);

        // Views
        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        // Buttons
        //findViewById(R.id.login_facebook_normal).setOnClickListener(this);
        //findViewById(R.id.login_google_normal).setOnClickListener(this);
        findViewById(R.id.login_facebook_mini).setOnClickListener(this);
        findViewById(R.id.login_google_mini).setOnClickListener(this);
        //findViewById(R.id.login_login_normal).setOnClickListener(this);
        //findViewById(R.id.login_idpwfind).setOnClickListener(this);
        findViewById(R.id.login_join_normal).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authBO = new AuthBO(this, authStateListener);
        authBO.init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        authBO.destory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authBO.onActivityResult(requestCode, resultCode, data);
    }

    AuthBO.AuthStateListener authStateListener = new AuthBO.AuthStateListener() {

        @Override
        public void onLogin(FirebaseUser firebaseUser) {
            hideProgressDialog();
            FirebaseDatabase.getInstance().getReference("users/profile/" + firebaseUser.getUid()).addListenerForSingleValueEvent(new DefaultValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    Intent intent = null;
                    if (profile == null) {
                        intent = new Intent(LoginActivity.this, RegistProfileActivity.class);
                    } else {
                        ProfileBO.setMyProfile(profile);
                        intent = new Intent(LoginActivity.this, MainPagerActivity_.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }

        @Override
        public void onLogout() {

        }
    };

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        authBO.emailLogin(email, password);
    }

    private void signOut() {
        authBO.emailLogout();;
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("이메일 입력하시지 말입니다");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("비밀번호 입력하시지 말입니다");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        authBO.emailLogin(email, password);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_facebook_mini:
                authBO.facebookLogin();
                break;
            case R.id.login_google_mini:
                authBO.googleLogin();
                break;
            case R.id.login_login_normal:
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.login_idpwfind:
                Intent intent1 = new Intent(this, IdPwFindActivity.class);
                startActivity(intent1);
                break;
            case R.id.login_join_normal:
                Intent intent2 = new Intent(this, MakeAccountActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
