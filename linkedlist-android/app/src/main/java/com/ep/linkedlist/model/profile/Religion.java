package com.ep.linkedlist.model.profile;

import com.ep.linkedlist.R;

import java.util.HashMap;
import java.util.Map;

public enum Religion {
    None("무교", R.id.profile_button_religion_none),
    Christianity("기독교", R.id.profile_button_religion_christianity),
    Buddhism("불교", R.id.profile_button_religion_buddhism),
    Catholicism("천주교", R.id.profile_button_religion_catholicism);

    private String value;
    private int buttonId;

    private static Map<String, Religion> religionMap;

    static {
        religionMap = new HashMap<String, Religion>();
        for(Religion religion : Religion.values()){
            religionMap.put(religion.getValue(), religion);
        }
    }

    Religion(String religion, int buttonId) {
        this.value = religion;
        this.buttonId = buttonId;
    }

    public String getValue() { return value; }

    public int getButtonId_profile() { return buttonId; }

    public static Religion fromValue(String value) {
        return religionMap.get(value);
    }
}
