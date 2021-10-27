package com.ep.linkedlist.model.profile;

import com.ep.linkedlist.R;

import java.util.HashMap;
import java.util.Map;

public enum Gender {
    MAN("남자", R.id.profile_button_gender_male),
    WOMAN("여자", R.id.profile_button_gender_female);

    private String value;
    private int buttonId;

    private static Map<String, Gender> genderMap;

    static {
        genderMap = new HashMap<String, Gender>();
        for(Gender gender : Gender.values()){
            genderMap.put(gender.getValue(), gender);
        }
    }

    Gender(String gender, int buttonId){
        this.value = gender;
        this.buttonId = buttonId;
    }

    public String getValue(){
        return value;
    }

    public int getButtonId_profile() { return buttonId; }

    public static Gender fromValue(String value) {
        return genderMap.get(value);
    }
}
