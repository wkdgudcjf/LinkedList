package com.ep.linkedlist.view.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ep.linkedlist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by h.kim on 2016-10-15.
 */
public class MakeAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MakeAccountActivity";
    public ProgressDialog mProgressDialog;

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mPasswordCheckField;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeaccount);

        mEmailField = (EditText) findViewById(R.id.makeaccount_email_temp);
        mPasswordField = (EditText) findViewById(R.id.makeaccount_password_temp);
        mPasswordCheckField = (EditText) findViewById(R.id.makeaccount_passwordcheck);

        findViewById(R.id.join_start_normal).setOnClickListener(this);
        findViewById(R.id.join_back).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("이메일 입력해라.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("비번 입력해라.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
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

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                            Toast.makeText(MakeAccountActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.join_back :
                super.onBackPressed();
                break;
            case R.id.join_start_normal:
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;

            default:
                break;
        }
    }
}