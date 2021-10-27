package com.ep.linkedlist.model.meeting;

import com.ep.linkedlist.R;

import java.util.HashMap;
import java.util.Map;

public enum SubCategory {
    // MainCategory - 여행
    MountainClimbing("등산", R.id.meeting_create_button_mountain_climbing),
    Walk("산책,트래킹", R.id.meeting_create_button_walk),
    Camping("캠핑", R.id.meeting_create_button_camping),
    Travel_Domestic("국내여행", R.id.meeting_create_button_travel_domestic),
    Travel_Overseas("해외여행", R.id.meeting_create_button_travel_overseas),
    Fishing("낚시", R.id.meeting_create_button_fishing),
    Paragliding("패러글라이딩", R.id.meeting_create_button_paragliding),

    // MainCategory - 운동
    Cycle("자전거", R.id.meeting_create_button_cycle),
    Bowling("볼링", R.id.meeting_create_button_bowling),
    Golf("골프", R.id.meeting_create_button_golf),
    Fitness("헬스", R.id.meeting_create_button_fitness),
    TableTenis("탁구", R.id.meeting_create_button_table_tenis),
    Boxing("복싱", R.id.meeting_create_button_boxing),
    Climbing("클라이밍", R.id.meeting_create_button_climbing),
    Pilates("필라테스", R.id.meeting_create_button_pilates),
    Swimming("수영", R.id.meeting_create_button_swimming),
    Basketball("농구", R.id.meeting_create_button_basketball),
    Yoga("요가", R.id.meeting_create_button_yoga),

    // MainCategory - 음악
    Piano("피아노", R.id.meeting_create_button_piano),
    Singing("노래", R.id.meeting_create_button_singing),
    Guitar("기타", R.id.meeting_create_button_guitar),
    Drum("드럼", R.id.meeting_create_button_drum),
    Hiphop("힙합", R.id.meeting_create_button_hiphop),
    Electronic("일렉", R.id.meeting_create_button_electronic),
    Indie("인디음악", R.id.meeting_create_button_indie),
    Violin("바이올린", R.id.meeting_create_button_violin),
    Composition("작곡", R.id.meeting_create_button_composition),
    Jazz("재즈", R.id.meeting_create_button_jazz),
    Band("밴드", R.id.meeting_create_button_band);

    private String value;
    private int buttonId_meetingCreate;

    private static Map<String, SubCategory> subCategoryMap;

    static {
        subCategoryMap = new HashMap<String, SubCategory>();
        for(SubCategory subCategory : SubCategory.values()){
            subCategoryMap.put(subCategory.getValue(), subCategory);
        }
    }

    SubCategory(String value, int buttonId_meetingCreate) {
        this.value = value;
        this.buttonId_meetingCreate = buttonId_meetingCreate;
    }

    public String getValue() { return this.value; }

    public int getButtonId_meetingCreate() { return this.buttonId_meetingCreate; }

    public static MainCategory getMainCategory(SubCategory subCategory) {
        MainCategory returnValue = null;
        for (MainCategory mainCategory : MainCategory.values()) {
            if (MainCategory.getSubCategoty(mainCategory).contains(subCategory)) {
                returnValue = mainCategory;
            }
        }

        return returnValue;
    }

    public static SubCategory fromValue(String value) { return subCategoryMap.get(value); }
}
