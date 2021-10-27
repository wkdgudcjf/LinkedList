package com.ep.linkedlist.view.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ep.linkedlist.R;

/**
 * Created by h.kim on 2016-10-15.
 */
public class IdPwFindActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mIdFindField;
    private EditText mPwFindField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idpwfind);

        mIdFindField = (EditText) findViewById(R.id.find_inputboxbg_edittext1);
        mPwFindField = (EditText) findViewById(R.id.find_inputboxbg_edittext2);

        findViewById(R.id.find_back).setOnClickListener(this);
        findViewById(R.id.find_ok_normal1).setOnClickListener(this);
        findViewById(R.id.find_ok_normal2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.find_back:
               super.onBackPressed();
                break;
            case R.id.find_ok_normal1:
                Toast.makeText(IdPwFindActivity.this, R.string.id_find_failed,
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.find_ok_normal2:
                Toast.makeText(IdPwFindActivity.this, R.string.pw_find_failed,
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}