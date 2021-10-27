package com.ep.linkedlist.model.profile;

import com.ep.linkedlist.R;

import java.util.HashMap;
import java.util.Map;

public enum BodyType {
    Skinny("스키니", R.id.profile_button_bodyType_skinny),
    Slim("슬림한", R.id.profile_button_bodyType_slim),
    Normal("보통", R.id.profile_button_bodyType_normal),
    Chubby("통통한", R.id.profile_button_bodyType_chubby);

    private String value;
    private int buttonId;

    private static Map<String, BodyType> bodyTypeMap;

    static {
        bodyTypeMap = new HashMap<String, BodyType>();
        for(BodyType bodyType : BodyType.values()){
            bodyTypeMap.put(bodyType.getValue(), bodyType);
        }
    }

    BodyType(String bodyType, int buttonId) {
        this.value = bodyType;
        this.buttonId = buttonId;
    }

    public String getValue() { return value; }

    public int getButtonId_profile() { return buttonId; }

    public static BodyType fromValue(String value) {
        return bodyTypeMap.get(value);
    }
}
