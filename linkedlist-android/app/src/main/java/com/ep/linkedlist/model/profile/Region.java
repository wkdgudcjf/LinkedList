package com.ep.linkedlist.model.profile;

import com.ep.linkedlist.R;

import java.util.HashMap;
import java.util.Map;

public enum Region {
    Seoul("서울", R.id.profile_button_region_seoul, R.id.meeting_create_button_seoul),
    Gyeonggi("경기", R.id.profile_button_region_gyeonggi, R.id.meeting_create_button_gyeonggi),
    Incheon("인천", R.id.profile_button_region_incheon, R.id.meeting_create_button_incheon),
    Daejeon("대전", R.id.profile_button_region_daejeon, R.id.meeting_create_button_daejeon),
    Chungbuk("충북", R.id.profile_button_region_chungbuk, R.id.meeting_create_button_chungbuk),
    Chungnam("충남", R.id.profile_button_region_chungnam, R.id.meeting_create_button_chungnam),
    Gangwon("강원", R.id.profile_button_region_gangwon, R.id.meeting_create_button_gangwon),
    Busan("부산", R.id.profile_button_region_busan, R.id.meeting_create_button_busan),
    Gyeongbuk("경북", R.id.profile_button_region_gyeongbuk, R.id.meeting_create_button_gyeongbuk),
    Gyeongnam("경남", R.id.profile_button_region_gyeongnam, R.id.meeting_create_button_gyeongnam),
    Daegu("대구", R.id.profile_button_region_daegu, R.id.meeting_create_button_daegu),
    Ulsan("울산", R.id.profile_button_region_ulsan, R.id.meeting_create_button_ulsan),
    Gwangju("광주", R.id.profile_button_region_gwangju, R.id.meeting_create_button_gwangju),
    Jeonbuk("전북", R.id.profile_button_region_jeonbuk, R.id.meeting_create_button_jeonbuk),
    Jeonnam("전남", R.id.profile_button_region_jeonnam, R.id.meeting_create_button_jeonnam),
    Jeju("제주", R.id.profile_button_region_jeju, R.id.meeting_create_button_jeju);

    private String value;
    private int buttonId_profile;
    private int buttonId_meeting_create;

    private static Map<String, Region> regionMap;

    static {
        regionMap = new HashMap<String, Region>();
        for(Region region : Region.values()){
            regionMap.put(region.getValue(), region);
        }
    }

    Region(String region, int buttonId_profile, int buttonId_meeting_create) {
        this.value = region;
        this.buttonId_profile = buttonId_profile;
        this.buttonId_meeting_create = buttonId_meeting_create;
    }

    public String getValue() { return value; }

    public int getButtonId_profile() { return buttonId_profile; }

    public int getButtonId_meeting_create() { return buttonId_meeting_create; }

    public static Region fromValue(String value) {
        return regionMap.get(value);
    }
}
