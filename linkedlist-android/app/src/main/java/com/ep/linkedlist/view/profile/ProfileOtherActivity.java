package com.ep.linkedlist.view.profile;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ep.linkedlist.R;
import com.ep.linkedlist.bo.profile.ProfileBO;
import com.ep.linkedlist.model.profile.Profile;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by jiwon on 2017-01-13.
 */
@EActivity(R.layout.activity_profile_other)
public class ProfileOtherActivity extends Activity {

    @ViewById ImageView profileOtherPhotoImageView;
    @ViewById TextView profileOtherNicknameTextView;
    @ViewById TextView profileOtherRegionAgeSummaryTextView;
    @ViewById TextView profileOtherRegionAgeGenderTextView;
    @ViewById TextView profileOtherHeightBodyTypeTextView;
    @ViewById TextView profileOtherReligionTextView;
    @ViewById TextView profileOtherBloodTypeTextView;

    private static final String FORMAT_REGION_AGE_SUMMARY = "%s %d세";
    private static final String FORMAT_REGION_AGE_GENDER = "%s에 사는 %d살 %s에요.";
    private static final String FORMAT_HEIGHT_BODY_TYPE = "키는 %dcm, %s한 체형입니다.";
    private static final String FORMAT_RELIGION = "종교는 %s입니다.";
    private static final String FORMAT_BLOOD_TYPE = "혈액형은 %s입니다.";

    @AfterViews
    void afterViews(){
        String profileString = getIntent().getStringExtra("profile");
        Profile profile = new Gson().fromJson(profileString, Profile.class);
        if (StringUtils.isNotBlank(profile.getPhotoURI())) {
            Glide.with(this).load(profile.getPhotoURI()).into(profileOtherPhotoImageView);
        }
        else
        {
            if(profile.getUid().equals(ProfileBO.getMyProfile().getUid())) profileOtherPhotoImageView.setImageResource(R.drawable.user_default);
            else profileOtherPhotoImageView.setImageResource(R.drawable.ohter_user_default);
        }
        profileOtherNicknameTextView.setText(profile.getNickname());
        profileOtherRegionAgeSummaryTextView.setText(String.format(FORMAT_REGION_AGE_SUMMARY, profile.getRegion(), profile.getAge()));
        profileOtherRegionAgeGenderTextView.setText(String.format(FORMAT_REGION_AGE_GENDER, profile.getRegion(), profile.getAge(), profile.getGender()));
        profileOtherHeightBodyTypeTextView.setText(String.format(FORMAT_HEIGHT_BODY_TYPE, profile.getHeight(), profile.getBodyType()));
        profileOtherReligionTextView.setText(String.format(FORMAT_RELIGION, profile.getReligion()));
        profileOtherBloodTypeTextView.setText(String.format(FORMAT_BLOOD_TYPE, profile.getBloodType()));
    }

    @Click(R.id.profileOtherBackImageView)
    void clickBackButton(){
        finish();
    }
}
