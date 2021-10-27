package com.ep.linkedlist.model.meeting;

import com.ep.linkedlist.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ep.linkedlist.model.meeting.SubCategory.*;

public enum MainCategory {
    Travel("여행", R.id.meeting_create_button_travel),
    Exercise("운동", R.id.meeting_create_button_exercise),
    Music("음악", R.id.meeting_create_button_music);

    private String value;
    private int buttonId_meetingCreate;

    private static Map<String, MainCategory> mainCategoryMap;
    private static Map<MainCategory, ArrayList<SubCategory>> subCategories;

    static {
        mainCategoryMap = new HashMap<String, MainCategory>();
        for(MainCategory mainCategory : MainCategory.values()){
            mainCategoryMap.put(mainCategory.getValue(), mainCategory);
        }

        ArrayList<SubCategory> subCategories_travel = new ArrayList<>();
        subCategories_travel.add(MountainClimbing);
        subCategories_travel.add(Walk);
        subCategories_travel.add(Camping);
        subCategories_travel.add(Travel_Domestic);
        subCategories_travel.add(Travel_Overseas);
        subCategories_travel.add(Fishing);
        subCategories_travel.add(Paragliding);

        ArrayList<SubCategory> subCategories_exercise = new ArrayList<>();
        subCategories_exercise.add(Cycle);
        subCategories_exercise.add(Bowling);
        subCategories_exercise.add(Golf);
        subCategories_exercise.add(Fitness);
        subCategories_exercise.add(TableTenis);
        subCategories_exercise.add(Boxing);
        subCategories_exercise.add(Climbing);
        subCategories_exercise.add(Pilates);
        subCategories_exercise.add(Swimming);
        subCategories_exercise.add(Basketball);
        subCategories_exercise.add(Yoga);

        ArrayList<SubCategory> subCategories_music = new ArrayList<>();
        subCategories_music.add(Piano);
        subCategories_music.add(Singing);
        subCategories_music.add(Guitar);
        subCategories_music.add(Drum);
        subCategories_music.add(Hiphop);
        subCategories_music.add(Electronic);
        subCategories_music.add(Indie);
        subCategories_music.add(Violin);
        subCategories_music.add(Composition);
        subCategories_music.add(Jazz);
        subCategories_music.add(Band);

        subCategories = new HashMap<MainCategory, ArrayList<SubCategory>>();
        subCategories.put(Travel, subCategories_travel);
        subCategories.put(Exercise, subCategories_exercise);
        subCategories.put(Music, subCategories_music);
    }

    MainCategory(String value, int buttonId_meetingCreate) {
        this.value = value;
        this.buttonId_meetingCreate = buttonId_meetingCreate;
    }

    public String getValue() { return value; }

    public int getButtonId_meetingCreate() { return buttonId_meetingCreate; }

    public static ArrayList<SubCategory> getSubCategoty(MainCategory mainCategory) { return subCategories.get(mainCategory); }

    public static MainCategory fromValue(String value) {
        return mainCategoryMap.get(value);
    }
}