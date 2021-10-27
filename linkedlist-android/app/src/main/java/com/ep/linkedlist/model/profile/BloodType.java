package com.ep.linkedlist.model.profile;

import com.ep.linkedlist.R;

import java.util.HashMap;
import java.util.Map;

public enum BloodType {
    A("A형", R.id.profile_button_bloodType_A),
    AB("AB형", R.id.profile_button_bloodType_AB),
    B("B형", R.id.profile_button_bloodType_B),
    O("O형", R.id.profile_button_bloodType_O);

    private String value;
    private int buttonId;

    private static Map<String, BloodType> bloodTypeMap;

    static {
        bloodTypeMap = new HashMap<String, BloodType>();
        for(BloodType bloodType : BloodType.values()){
            bloodTypeMap.put(bloodType.getValue(), bloodType);
        }
    }

    BloodType(String bloodType, int buttonId) {
        this.value = bloodType;
        this.buttonId = buttonId;
    }

    public String getValue() { return value; }

    public int getButtonId_profile() { return buttonId; }

    public static BloodType fromValue(String value) {
        return bloodTypeMap.get(value);
    }
}
