package com.ep.linkedlist.view.profile;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ep.linkedlist.R;
import com.ep.linkedlist.auth.CustomRadioButton;
import com.ep.linkedlist.bo.profile.ProfileBO;
import com.ep.linkedlist.model.profile.BloodType;
import com.ep.linkedlist.model.profile.BodyType;
import com.ep.linkedlist.model.profile.Gender;
import com.ep.linkedlist.model.profile.Profile;
import com.ep.linkedlist.model.profile.Region;
import com.ep.linkedlist.model.profile.Religion;
import com.ep.linkedlist.util.Pair;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.regex.Pattern;

public class ModifyProfileActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = this.getClass().getName();

    private int m_currentStep = 0;
    private int m_lastStep = 1;
    private int m_registProfileViewIds[] = {
            R.layout.activity_regist_profile_step1,
            R.layout.activity_regist_profile_step2
            // R.layout.activity_regist_profile_step3,
            // R.layout.activity_regist_profile_step4,
            // R.layout.activity_regist_profile_step5
    };

    private Profile profile = ProfileBO.getMyProfile();

    static private final int MaxNameLength = 6;
    static private final int MaxNicknameLength = 12;
    static private final int MinAge = 1;
    static private final int MaxAge = 99;
    static private final int DefaultAge = 25;

    static private final int MinHeight = 1;
    static private final int MaxHeight = 250;
    static private final int DefaultMaleHeight = 175;
    static private final int DefaultFemaleHeight = 160;

    static private final int DatePickerDefaultYear = 1990;
    static private final int DatePickerDefaultMonth = 0;
    static private final int DatePickerDefaultDay = 1;

    static private CustomRadioButton m_genderButton = new CustomRadioButton();
    static private CustomRadioButton m_bloodTypeButton = new CustomRadioButton();
    static private CustomRadioButton m_bodyTypeButton = new CustomRadioButton();
    static private CustomRadioButton m_religionButton = new CustomRadioButton();
    static private CustomRadioButton m_regionButton = new CustomRadioButton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(m_registProfileViewIds[m_currentStep]);

        final EditText editTextName = (EditText)findViewById(R.id.profile_editText_name);
        editTextName.addTextChangedListener(new TextWatcher() {
            String beforeText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // To do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                editTextName.removeTextChangedListener(this);

                if (s.toString().length() > 0) {
                    Pattern pattern = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐ㄱ-ㅣ가-힣ᆢᆞ]+$");
                    if (pattern.matcher(s).matches() == false) {
                        editTextName.setText(beforeText);
                        editTextName.setSelection(editTextName.length());
                    }
                }

                editTextName.addTextChangedListener(this);
                onProfileDataChanged();
            }
        });

        final EditText editTextNickname = (EditText)findViewById(R.id.profile_editText_nickname);
        editTextNickname.addTextChangedListener(new TextWatcher() {
            String beforeText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // To do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                editTextNickname.removeTextChangedListener(this);

                if (s.toString().length() > 0) {
                    Pattern pattern = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐ㄱ-ㅣ가-힣ᆢᆞ]+$");
                    if (pattern.matcher(s).matches() == false) {
                        editTextNickname.setText(beforeText);
                        editTextNickname.setSelection(editTextNickname.length());
                    }
                }

                editTextNickname.addTextChangedListener(this);
                onProfileDataChanged();
            }
        });

        final TextView editTextAge = (TextView)findViewById(R.id.profile_editText_age);
        editTextAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = openDatePicker();
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setTitle("생년월일 선택");
                datePickerDialog.show();
            }
        });

        TextView textViewHeightAge = (TextView)findViewById(R.id.profile_editText_age);
        textViewHeightAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumberPickerAge();
            }
        });

        TextView textViewHeightHeight = (TextView)findViewById(R.id.profile_editText_height);
        textViewHeightHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumberPickerHeight();
            }
        });

        onEnterStep(m_currentStep);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                try {
                    processPrevStep();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 다음 단계 버튼 이벤트 처리
            case R.id.profile_next_step_button:
                try {
                    processNextStep();
                }
                catch (IllegalAccessException exception) {
                    Log.d(ModifyProfileActivity.class.getName(), "Save user profile data has been failed.");
                }
                return;

            // 이전 단계 버튼 이벤트 처리
            case R.id.profile_prev_step_button:
                try {
                    processPrevStep();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return;

            default:
                // 데이터 변경에 대한 처리가 필요한 클릭 이벤트이므로 로직 계속 진행
                break;
        }

        switch (view.getId()) {
            // 성별 선택 이벤트 처리 (아무 처리하지 않음, 변경 불가)
            case R.id.profile_button_gender_male: break;
            case R.id.profile_button_gender_female: break;

            // 나이 입력 이벤트 처리
            case R.id.profile_editText_age: openNumberPickerAge(); break;
            case R.id.profile_editText_height: openNumberPickerHeight(); break;

            // 혈액형 선택 이벤트 처리
            case R.id.profile_button_bloodType_AB: onBloodTypeButtonClick(BloodType.AB); break;
            case R.id.profile_button_bloodType_A: onBloodTypeButtonClick(BloodType.A); break;
            case R.id.profile_button_bloodType_B: onBloodTypeButtonClick(BloodType.B); break;
            case R.id.profile_button_bloodType_O: onBloodTypeButtonClick(BloodType.O); break;

            // 체형 선택 이벤트 처리
            case R.id.profile_button_bodyType_skinny: onBodyTypeButtonClick(BodyType.Skinny); break;
            case R.id.profile_button_bodyType_slim: onBodyTypeButtonClick(BodyType.Slim); break;
            case R.id.profile_button_bodyType_normal: onBodyTypeButtonClick(BodyType.Normal); break;
            case R.id.profile_button_bodyType_chubby: onBodyTypeButtonClick(BodyType.Chubby); break;

            // 종교 선택 이벤트 처리
            case R.id.profile_button_religion_none: onReligionButtonClick(Religion.None); break;
            case R.id.profile_button_religion_christianity: onReligionButtonClick(Religion.Christianity); break;
            case R.id.profile_button_religion_buddhism: onReligionButtonClick(Religion.Buddhism); break;
            case R.id.profile_button_religion_catholicism: onReligionButtonClick(Religion.Catholicism); break;

            // 지역 선택 이벤트 처리
            case R.id.profile_button_region_seoul: onRegionButtonClick(Region.Seoul); break;
            case R.id.profile_button_region_gyeonggi: onRegionButtonClick(Region.Gyeonggi); break;
            case R.id.profile_button_region_incheon: onRegionButtonClick(Region.Incheon); break;
            case R.id.profile_button_region_daejeon: onRegionButtonClick(Region.Daejeon); break;
            case R.id.profile_button_region_chungbuk: onRegionButtonClick(Region.Chungbuk); break;
            case R.id.profile_button_region_chungnam: onRegionButtonClick(Region.Chungnam); break;
            case R.id.profile_button_region_gangwon: onRegionButtonClick(Region.Gangwon); break;
            case R.id.profile_button_region_busan: onRegionButtonClick(Region.Busan); break;
            case R.id.profile_button_region_gyeongbuk: onRegionButtonClick(Region.Gyeongbuk); break;
            case R.id.profile_button_region_gyeongnam: onRegionButtonClick(Region.Gyeongnam); break;
            case R.id.profile_button_region_daegu: onRegionButtonClick(Region.Daegu); break;
            case R.id.profile_button_region_ulsan: onRegionButtonClick(Region.Ulsan); break;
            case R.id.profile_button_region_gwangju: onRegionButtonClick(Region.Gwangju); break;
            case R.id.profile_button_region_jeonbuk: onRegionButtonClick(Region.Jeonbuk); break;
            case R.id.profile_button_region_jeonnam: onRegionButtonClick(Region.Jeonnam); break;
            case R.id.profile_button_region_jeju: onRegionButtonClick(Region.Jeju); break;
        }

        onProfileDataChanged();
    }

    private void InitRadioButtons() {
        // 성별 선택 버튼 라디오 버튼화
        m_genderButton.RegistButton(R.id.profile_button_gender_male, (ImageButton)findViewById(R.id.profile_button_gender_male),
                ContextCompat.getDrawable(this, R.drawable.profile_gender_male_normal), ContextCompat.getDrawable(this, R.drawable.profile_gender_male_pressed));
        m_genderButton.RegistButton(R.id.profile_button_gender_female, (ImageButton)findViewById(R.id.profile_button_gender_female),
                ContextCompat.getDrawable(this, R.drawable.profile_gender_female_normal), ContextCompat.getDrawable(this, R.drawable.profile_gender_female_pressed));

        // 혈액형 선택 라디오 버튼화
        m_bloodTypeButton.RegistButton(R.id.profile_button_bloodType_A, (ImageButton)findViewById(R.id.profile_button_bloodType_A),
                ContextCompat.getDrawable(this, R.drawable.profile_bloodtype_a_normal), ContextCompat.getDrawable(this, R.drawable.profile_bloodtype_a_pressed));
        m_bloodTypeButton.RegistButton(R.id.profile_button_bloodType_B, (ImageButton)findViewById(R.id.profile_button_bloodType_B),
                ContextCompat.getDrawable(this, R.drawable.profile_bloodtype_b_normal), ContextCompat.getDrawable(this, R.drawable.profile_bloodtype_b_pressed));
        m_bloodTypeButton.RegistButton(R.id.profile_button_bloodType_O, (ImageButton)findViewById(R.id.profile_button_bloodType_O),
                ContextCompat.getDrawable(this, R.drawable.profile_bloodtype_o_normal), ContextCompat.getDrawable(this, R.drawable.profile_bloodtype_o_pressed));
        m_bloodTypeButton.RegistButton(R.id.profile_button_bloodType_AB, (ImageButton)findViewById(R.id.profile_button_bloodType_AB),
                ContextCompat.getDrawable(this, R.drawable.profile_bloodtype_ab_normal), ContextCompat.getDrawable(this, R.drawable.profile_bloodtype_ab_pressed));

        // 체형 선택 라디오 버튼화
        m_bodyTypeButton.RegistButton(R.id.profile_button_bodyType_skinny, (ImageButton)findViewById(R.id.profile_button_bodyType_skinny),
                ContextCompat.getDrawable(this, R.drawable.profile_bodytype_skinny_normal), ContextCompat.getDrawable(this, R.drawable.profile_bodytype_skinny_pressed));
        m_bodyTypeButton.RegistButton(R.id.profile_button_bodyType_slim, (ImageButton)findViewById(R.id.profile_button_bodyType_slim),
                ContextCompat.getDrawable(this, R.drawable.profile_bodytype_slim_normal), ContextCompat.getDrawable(this, R.drawable.profile_bodytype_slim_pressed));
        m_bodyTypeButton.RegistButton(R.id.profile_button_bodyType_normal, (ImageButton)findViewById(R.id.profile_button_bodyType_normal),
                ContextCompat.getDrawable(this, R.drawable.profile_bodytype_normal_normal), ContextCompat.getDrawable(this, R.drawable.profile_bodytype_normal_pressed));
        m_bodyTypeButton.RegistButton(R.id.profile_button_bodyType_chubby, (ImageButton)findViewById(R.id.profile_button_bodyType_chubby),
                ContextCompat.getDrawable(this, R.drawable.profile_bodytype_chubby_normal), ContextCompat.getDrawable(this, R.drawable.profile_bodytype_chubby_pressed));

        // 종교 선택 라디오 버튼화
        m_religionButton.RegistButton(R.id.profile_button_religion_none, (ImageButton)findViewById(R.id.profile_button_religion_none),
                ContextCompat.getDrawable(this, R.drawable.profile_religion_none_normal), ContextCompat.getDrawable(this, R.drawable.profile_religion_none_pressed));
        m_religionButton.RegistButton(R.id.profile_button_religion_christianity, (ImageButton)findViewById(R.id.profile_button_religion_christianity),
                ContextCompat.getDrawable(this, R.drawable.profile_religion_christianity_normal), ContextCompat.getDrawable(this, R.drawable.profile_religion_christianity_pressed));
        m_religionButton.RegistButton(R.id.profile_button_religion_buddhism, (ImageButton)findViewById(R.id.profile_button_religion_buddhism),
                ContextCompat.getDrawable(this, R.drawable.profile_religion_buddhism_normal), ContextCompat.getDrawable(this, R.drawable.profile_religion_buddhism_pressed));
        m_religionButton.RegistButton(R.id.profile_button_religion_catholicism, (ImageButton)findViewById(R.id.profile_button_religion_catholicism),
                ContextCompat.getDrawable(this, R.drawable.profile_religion_catholic_normal), ContextCompat.getDrawable(this, R.drawable.profile_religion_catholic_pressed));

        // 지역 선택 라디오 버튼화
        m_regionButton.RegistButton(R.id.profile_button_region_seoul, (ImageButton)findViewById(R.id.profile_button_region_seoul),
                ContextCompat.getDrawable(this, R.drawable.profile_region_seoul_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_seoul_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_gyeonggi, (ImageButton)findViewById(R.id.profile_button_region_gyeonggi),
                ContextCompat.getDrawable(this, R.drawable.profile_region_gyeonggi_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_gyeonggi_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_incheon, (ImageButton)findViewById(R.id.profile_button_region_incheon),
                ContextCompat.getDrawable(this, R.drawable.profile_region_incheon_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_incheon_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_daejeon, (ImageButton)findViewById(R.id.profile_button_region_daejeon),
                ContextCompat.getDrawable(this, R.drawable.profile_region_daejeon_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_daejeon_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_chungbuk, (ImageButton)findViewById(R.id.profile_button_region_chungbuk),
                ContextCompat.getDrawable(this, R.drawable.profile_region_chungbuk_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_chungbuk_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_chungnam, (ImageButton)findViewById(R.id.profile_button_region_chungnam),
                ContextCompat.getDrawable(this, R.drawable.profile_region_chungnam_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_chungnam_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_gangwon, (ImageButton)findViewById(R.id.profile_button_region_gangwon),
                ContextCompat.getDrawable(this, R.drawable.profile_region_gangwon_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_gangwon_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_busan, (ImageButton)findViewById(R.id.profile_button_region_busan),
                ContextCompat.getDrawable(this, R.drawable.profile_region_busan_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_busan_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_gyeongbuk, (ImageButton)findViewById(R.id.profile_button_region_gyeongbuk),
                ContextCompat.getDrawable(this, R.drawable.profile_region_gyeongbuk_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_gyeongbuk_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_gyeongnam, (ImageButton)findViewById(R.id.profile_button_region_gyeongnam),
                ContextCompat.getDrawable(this, R.drawable.profile_region_gyeongnam_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_gyeongnam_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_daegu, (ImageButton)findViewById(R.id.profile_button_region_daegu),
                ContextCompat.getDrawable(this, R.drawable.profile_region_daegu_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_daegu_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_ulsan, (ImageButton)findViewById(R.id.profile_button_region_ulsan),
                ContextCompat.getDrawable(this, R.drawable.profile_region_ulsan_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_ulsan_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_gwangju, (ImageButton)findViewById(R.id.profile_button_region_gwangju),
                ContextCompat.getDrawable(this, R.drawable.profile_region_gwangju_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_gwangju_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_jeonbuk, (ImageButton)findViewById(R.id.profile_button_region_jeonbuk),
                ContextCompat.getDrawable(this, R.drawable.profile_region_jeonbuk_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_jeonbuk_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_jeonnam, (ImageButton)findViewById(R.id.profile_button_region_jeonnam),
                ContextCompat.getDrawable(this, R.drawable.profile_region_jeonnam_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_jeonnam_pressed));
        m_regionButton.RegistButton(R.id.profile_button_region_jeju, (ImageButton)findViewById(R.id.profile_button_region_jeju),
                ContextCompat.getDrawable(this, R.drawable.profile_region_jeju_normal), ContextCompat.getDrawable(this, R.drawable.profile_region_jeju_pressed));
    }

    private void onGenderButtonClick(Gender gender) {
        m_genderButton.OnClick(gender.getButtonId_profile());
        profile.setGender(gender.getValue());
    }

    private void onBloodTypeButtonClick(BloodType bloodType) {
        m_bloodTypeButton.OnClick(bloodType.getButtonId_profile());
        profile.setBloodType(bloodType.getValue());
    }

    private void onBodyTypeButtonClick(BodyType bodyType) {
        m_bodyTypeButton.OnClick(bodyType.getButtonId_profile());
        profile.setBodyType(bodyType.getValue());
    }

    private void onReligionButtonClick(Religion religion) {
        m_religionButton.OnClick(religion.getButtonId_profile());
        profile.setReligion(religion.getValue());
    }

    private void onRegionButtonClick(Region region) {
        m_regionButton.OnClick(region.getButtonId_profile());
        profile.setRegion(region.getValue());
    }

    private void processNextStep() throws IllegalAccessException {
        Pair<Boolean, String> validateResult = ValidateNextStep();
        if (validateResult.getFirst() == false) {
            Toast toast = Toast.makeText(this, validateResult.getSecond().toString(), Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        if (m_currentStep >= m_lastStep) {
            ProfileBO profileBO = new ProfileBO();
            profileBO.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()==false){
                        Log.d(TAG, "업데이트 실패.");
                        return;
                    }
                    finish();
                }
            });
            return;
        }

        onLeaveStep(m_currentStep);
        ++m_currentStep;

        setContentView(m_registProfileViewIds[m_currentStep]);
        onEnterStep(m_currentStep);
    }

    private void processPrevStep() throws IllegalAccessException {
        if (m_currentStep <= 0) {
            finish();
            return;
        }

        onLeaveStep(m_currentStep);
        --m_currentStep;

        setContentView(m_registProfileViewIds[m_currentStep]);
        onEnterStep(m_currentStep);
    }

    private void onLeaveStep(int step) {
        // 스텝을 떠날 때 해야할 일을 수행한다
        if (step == 0) {
            EditText name = (EditText)findViewById(R.id.profile_editText_name);
            if (name != null) {
                profile.setName(name.getText().toString());
            }

            EditText nickname = (EditText)findViewById(R.id.profile_editText_nickname);
            if (nickname != null) {
                profile.setNickname(nickname.getText().toString());
            }
        }
        else if (step == 1) {
            // No Task
        }
        else if (step == 2) {
            // No Task
        }
        else if (step == 3) {
            // No Task
        }
        else if (step == 4) {
            // No Task
        }
        else if (step == 5) {
            // No Task
        }
    }

    private void onEnterStep(int step) {
        // 다음 단계, 등록 완료 버튼 pressed, clicked 상태 추가
        {
            Drawable normalImage, pressedImage, disableImage;
            switch (m_currentStep) {
                case 0:
                    normalImage = ContextCompat.getDrawable(this, R.drawable.profile_move_to_step2_normal);
                    pressedImage = ContextCompat.getDrawable(this, R.drawable.profile_move_to_step2_pressed);
                    disableImage = ContextCompat.getDrawable(this, R.drawable.profile_move_to_step2_disabled);
                    break;

                case 1:
                    normalImage = ContextCompat.getDrawable(this, R.drawable.profile_move_to_step3_normal);
                    pressedImage = ContextCompat.getDrawable(this, R.drawable.profile_move_to_step3_pressed);
                    disableImage = ContextCompat.getDrawable(this, R.drawable.profile_move_to_step3_disabled);
                    break;

                default:
                    normalImage = pressedImage = disableImage = null;
                    Log.w(TAG, "ModifyProfileActivity/onEnterStep : Wrong Step.");
                    break;
            }

            StateListDrawable states = new StateListDrawable();
            states.addState(new int[] { -android.R.attr.state_enabled }, disableImage);
            states.addState(new int[] { android.R.attr.state_pressed }, pressedImage);
            states.addState(new int[] { android.R.attr.state_enabled }, normalImage);

            ImageButton goToNextStepButton = (ImageButton) findViewById(R.id.profile_next_step_button);
            goToNextStepButton.setImageDrawable(states);
        }

        InitRadioButtons();

        // 스텝 진입 시 세팅해야 할 일을 수행한다
        if (step == 0) {
            // 저장된 유저 프로필 데이터가 있다면 해당 값으로 세팅한다
            if (profile.getName().isEmpty() == false) {
                EditText editText = (EditText)findViewById(R.id.profile_editText_name);
                if (editText != null) {
                    editText.setText(profile.getName());
                }

                // 프로필 변경 액티비티는 백 버튼이 있음
                ImageView imageView = (ImageView)findViewById(R.id.profile_imageView_header_step_1);
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.profile_header_step_1));
            }

            if (profile.getNickname().isEmpty() == false) {
                EditText editText = (EditText)findViewById(R.id.profile_editText_nickname);
                if (editText != null) {
                    editText.setText(profile.getNickname());
                }
            }

            if (profile.getGender() != null) {
                m_genderButton.OnClick(Gender.fromValue(profile.getGender()).getButtonId_profile());
            }

            if (profile.getAge() > 0) {
                TextView textView = (TextView)findViewById(R.id.profile_editText_age);
                if (textView != null) {
                    textView.setText("" + profile.getAge());
                }
            }

            if (profile.getBloodType() != null) {
                m_bloodTypeButton.OnClick(BloodType.fromValue(profile.getBloodType()).getButtonId_profile());
            }

            if (profile.getHeight() > 0) {
                TextView textView = (TextView)findViewById(R.id.profile_editText_height);
                if (textView != null) {
                    textView.setText("" + profile.getHeight());
                }
            }

            if (profile.getBodyType() != null) {
                m_bodyTypeButton.OnClick(BodyType.fromValue(profile.getBodyType()).getButtonId_profile());
            }
        }
        else if (step == 1) {
            if (profile.getReligion() != null) {
                m_religionButton.OnClick(Religion.fromValue(profile.getReligion()).getButtonId_profile());
            }

            if (profile.getRegion() != null) {
                m_regionButton.OnClick(Region.fromValue(profile.getRegion()).getButtonId_profile());
            }

        }
        else if (step == 2) {
            final String buttonText = getResources().getText(R.string.profile_button_company_authorization1)
                    + "     " + getResources().getText(R.string.profile_button_company_authorization2);
            ((Button)findViewById(R.id.profile_button_company_authorization)).setText(buttonText);
        }
        else if (step == 3) {
            // No task
        }
        else if (step == 4) {
            // No task
        }
        else if (step == 5) {
            // No task
        }

        ImageButton goToNextStepButton = (ImageButton)findViewById(R.id.profile_next_step_button);
        goToNextStepButton.setEnabled(ValidateNextStep().getFirst());
    }

    private void onProfileDataChanged() {
        // 프로필 데이터가 변경되었을 때 다음 스텝 버튼이 활성화될 것인지 비활성화될 것인지 결정
        final boolean newButtonState = ValidateNextStep().getFirst();
        ImageButton goToNextStepButton = (ImageButton)findViewById(R.id.profile_next_step_button);
        goToNextStepButton.setEnabled(newButtonState);
    }

    private Pair<Boolean, String> ValidateNextStep() {
        // 아직 빌드 컨피그가 먹히지 않는다. 빌드 설정을 좀 더 알아보고 적용해보자.
        /*
        if (BuildConfig.DEBUG) {
            return new Pair(new Boolean(true), "");
        }
        */

        if (m_currentStep == 0) {
            // 이름 유효성 검사
            final TextView inputName = (EditText)findViewById(R.id.profile_editText_name);
            if (TextUtils.isEmpty(inputName.getText())) {
                return new Pair(new Boolean(false), "이름을 입력하세요.");
            }
            else if (Pattern.matches("^[가-힣]*$", inputName.getText()) == false ) {
                return new Pair(new Boolean(false), "이름을 올바르게 입력하세요.");
            }
            else if (inputName.getText().length() > MaxNameLength) {
                return new Pair(new Boolean(false), "이름은 " + MaxNameLength + "자를 넘길 수 없습니다.");
            }

            // 닉네임 유효성 검사
            final TextView inputNickName = (EditText)findViewById(R.id.profile_editText_nickname);
            if (TextUtils.isEmpty(inputNickName.getText())) {
                return new Pair(new Boolean(false), "닉네임을 입력하세요.");
            }
            else if (Pattern.matches("^[a-zA-Z0-9가-힣]*$", inputNickName.getText()) == false) {
                return new Pair(new Boolean(false), "닉네임을 올바르게 입력하세요.");
            }
            else if (inputNickName.getText().length() > MaxNicknameLength) {
                return new Pair(new Boolean(false), "닉네임은 " + MaxNicknameLength + "자를 넘길 수 없습니다.");
            }

            // 성별 유효성 검사
            if (profile.getGender() == null) {
                return new Pair(new Boolean(false), "성별을 선택하세요.");
            }

            // 나이 유효성 검사
            if (profile.getAge() <= 0 ||
                profile.getAge() > MaxAge) {
                return new Pair(new Boolean(false), "나이를 다시 입력하세요.");
            }

            // 키 유효성 검사
            if (profile.getHeight() <= 0 ||
                profile.getHeight() > MaxHeight) {
                return new Pair(new Boolean(false), "키를 다시 입력하세요.");
            }

            // 혈액형 유효성 검사
            if (profile.getBloodType() == null) {
                return new Pair(new Boolean(false), "혈액형을 선택하세요.");
            }

            // 체형 유효성 검사
            if (profile.getBodyType() == null) {
                return new Pair(new Boolean(false), "체형을 선택하세요.");
            }
        }
        else if (m_currentStep == 1) {
            // 종교 유효성 검사
            if (profile.getReligion() == null) {
                return new Pair(new Boolean(false), "종교를 선택하세요.");
            }

            // 지역 유효성 검사
            if (profile.getRegion() == null) {
                return new Pair(new Boolean(false), "지역을 선택하세요.");
            }
        }
        else if (m_currentStep == 2) {

        }
        else if (m_currentStep == 3) {

        }
        else if (m_currentStep == 4) {

        }
        else if (m_currentStep == 5) {

        }

        return new Pair(new Boolean(true), "");
    }

    private DatePickerDialog openDatePicker() {
        return new DatePickerDialog(ModifyProfileActivity.this, android.R.style.Theme_Holo_Light_Dialog, dateSetListener,
                DatePickerDefaultYear, DatePickerDefaultMonth, DatePickerDefaultDay);
    }

    private void openNumberPickerAge() {
        final NumberPicker numberPicker = new NumberPicker(ModifyProfileActivity.this);
        numberPicker.setMinValue(MinAge);
        numberPicker.setMaxValue(MaxAge);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                for (int i = 0; i < numberPicker.getChildCount(); ++i) {
                    View child = numberPicker.getChildAt(i);
                    if (child instanceof EditText) {
                        ((EditText) child).setTextSize(50.f);
                    }

                    if (child instanceof TextView) {
                        ((EditText) child).setTextSize(50.f);
                    }
                }
            }
        });

        // 선택된 나이가 없으면 기본 나이로 설정
        final int settedAge = profile.getAge() != 0 ? profile.getAge() : DefaultAge;
        numberPicker.setValue(settedAge);

        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyProfileActivity.this).setView(numberPicker);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                TextView textView = (TextView)findViewById(R.id.profile_editText_age);
                textView.setText("" + numberPicker.getValue());
                textView.setTextColor(Color.BLACK);

                profile.setAge(numberPicker.getValue());
                onProfileDataChanged();
            }
        });
        builder.setTitle("나이 선택");
        builder.show();
    }

    private void openNumberPickerHeight() {
        final NumberPicker numberPicker = new NumberPicker(ModifyProfileActivity.this);
        numberPicker.setMinValue(MinHeight);
        numberPicker.setMaxValue(MaxHeight);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                for (int i = 0; i < numberPicker.getChildCount(); ++i) {
                    View child = numberPicker.getChildAt(i);
                    if (child instanceof EditText) {
                        ((EditText) child).setTextSize(50.f);
                    }

                    if (child instanceof TextView) {
                        ((EditText) child).setTextSize(50.f);
                    }
                }
            }
        });

        // 남자를 선택했거나, 성별을 선택하지 않은 상태라면 남자 기본 키로 설정
        final int defaultHeight = Gender.fromValue(profile.getGender()) == Gender.WOMAN ? DefaultFemaleHeight : DefaultMaleHeight;
        numberPicker.setValue(defaultHeight);

        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyProfileActivity.this).setView(numberPicker);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                TextView textView = (TextView)findViewById(R.id.profile_editText_height);
                textView.setText("" + numberPicker.getValue());
                textView.setTextColor(Color.BLACK);

                profile.setHeight(numberPicker.getValue());
                onProfileDataChanged();
            }
        });
        builder.setTitle("키 선택");
        builder.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            final int currentYear = calendar.get(Calendar.YEAR);
            final int age = currentYear - year + 1;

            profile.setAge(age);
            profile.setBirthYear(year);
            profile.setBirthMonth(monthOfYear + 1);
            profile.setBirthDay(dayOfMonth);
            onProfileDataChanged();

            TextView textView = (TextView)findViewById(R.id.profile_editText_age);
            textView.setText("" + age);
            textView.setTextColor(Color.BLACK);
        }
    };
}