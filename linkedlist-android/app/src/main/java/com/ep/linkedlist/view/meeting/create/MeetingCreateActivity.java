package com.ep.linkedlist.view.meeting.create;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ep.linkedlist.R;
import com.ep.linkedlist.auth.CustomRadioButton;
import com.ep.linkedlist.bo.meeting.MeetingBO;
import com.ep.linkedlist.chat.ChatActivity_;
import com.ep.linkedlist.model.Meta;
import com.ep.linkedlist.model.meeting.MainCategory;
import com.ep.linkedlist.model.meeting.MeetingInfo;
import com.ep.linkedlist.model.meeting.SubCategory;
import com.ep.linkedlist.model.profile.Region;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_meeting_create)
public class MeetingCreateActivity extends AppCompatActivity {
    private static final String TAG = MeetingCreateActivity.class.getName();

    private static final int TotalStep = 5;
    private static final int ButtonOffsetY = 34 + 10; // DP
    private static final int MarginBottom = 22; // DP

    private static final long UserCount_2to2 = 4;
    private static final long UserCount_3to3 = 6;
    private static final long UserCount_4to4 = 8;

    Meta meta;
    MeetingInfo meetingInfo = new MeetingInfo();

    boolean[] isDropDownButtonClicked = new boolean[TotalStep];
    ViewGroup.LayoutParams step1_layout_param;
    ViewGroup.MarginLayoutParams step1_margin_param;
    ViewGroup.LayoutParams step1_button_param;
    ViewGroup.LayoutParams step4_layout_param;

    boolean initialized = false;

    // For Step 1
    ArrayList<ImageButton> regionButtons = new ArrayList<>();
    ArrayList<LinearLayout> regionButtonParents = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MeetingCreate Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    // For Step 2
    static class UserCountButtonInfo {
        private int buttonId;
        private int normalImageId;
        private int pressedImageId;

        UserCountButtonInfo(int buttonId, int normalImageId, int pressedImageId) {
            this.buttonId = buttonId;
            this.normalImageId = normalImageId;
            this.pressedImageId = pressedImageId;
        }

        public int getButtonId() {
            return this.buttonId;
        }

        public int getNormalImage() {
            return this.normalImageId;
        }

        public int getPressedImage() {
            return this.pressedImageId;
        }

        public void setButtonId(int buttonId) {
            this.buttonId = buttonId;
        }

        public void setNormalImage(int normalImageId) {
            this.normalImageId = normalImageId;
        }

        public void setPressedImage(int pressedImageId) {
            this.pressedImageId = pressedImageId;
        }
    }

    static Map<Long, UserCountButtonInfo> userCountToIds = new HashMap<Long, UserCountButtonInfo>();

    // For Step 3, 4
    ArrayList<ImageButton> mainCategoryButtons = new ArrayList<ImageButton>();
    ArrayList<LinearLayout> subCategoryButtonParents = new ArrayList<LinearLayout>();
    Map<String, ImageButton> subCategoryButtons = new HashMap<String, ImageButton>();
    static Map<MainCategory, Integer> subCategoryDropDownedBackgrounds = new HashMap<MainCategory, Integer>();

    CustomRadioButton selectRegionButtons = new CustomRadioButton();
    CustomRadioButton selectUserCountButtons = new CustomRadioButton();
    CustomRadioButton selectMainCategoryButtons = new CustomRadioButton();
    CustomRadioButton selectSubCategoryButtons = new CustomRadioButton();

    @Bean
    MeetingBO meetingBO;

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static {
        userCountToIds.put(UserCount_2to2, new UserCountButtonInfo(R.id.meeting_create_button_2to2,
                R.drawable.meeting_create_step2_2to2_normal, R.drawable.meeting_create_step2_2to2_pressed));
        userCountToIds.put(UserCount_3to3, new UserCountButtonInfo(R.id.meeting_create_button_3to3,
                R.drawable.meeting_create_step2_3to3_normal, R.drawable.meeting_create_step2_3to3_pressed));
        userCountToIds.put(UserCount_4to4, new UserCountButtonInfo(R.id.meeting_create_button_4to4,
                R.drawable.meeting_create_step2_4to4_normal, R.drawable.meeting_create_step2_4to4_pressed));

        subCategoryDropDownedBackgrounds.put(MainCategory.Travel, R.drawable.meeting_create_step4_background_3by3);
        subCategoryDropDownedBackgrounds.put(MainCategory.Exercise, R.drawable.meeting_create_step4_background_3by4);
        subCategoryDropDownedBackgrounds.put(MainCategory.Music, R.drawable.meeting_create_step4_background_3by4);
    }

    @AfterViews
    void init() {
        requestMeta();

        // 드롭다운 메뉴 버튼 플래그 초기화
        for (int i = 0; i < TotalStep; ++i) {
            isDropDownButtonClicked[i] = false;
        }

        // 지역 선택 버튼 Row 레이아웃
        if (initialized == false) {
            regionButtonParents.add((LinearLayout) findViewById(R.id.meeting_create_step1_region_1));
            regionButtonParents.add((LinearLayout) findViewById(R.id.meeting_create_step1_region_2));
            regionButtonParents.add((LinearLayout) findViewById(R.id.meeting_create_step1_region_3));
            regionButtonParents.add((LinearLayout) findViewById(R.id.meeting_create_step1_region_4));
            regionButtonParents.add((LinearLayout) findViewById(R.id.meeting_create_step1_region_5));
            regionButtonParents.add((LinearLayout) findViewById(R.id.meeting_create_step1_region_6));
        }

        // 서브 카테고리 선택 버튼 Row 레이아웃
        if (initialized == false) {
            subCategoryButtonParents.add((LinearLayout) findViewById(R.id.meeting_create_step4_subCategory_1));
            subCategoryButtonParents.add((LinearLayout) findViewById(R.id.meeting_create_step4_subCategory_2));
            subCategoryButtonParents.add((LinearLayout) findViewById(R.id.meeting_create_step4_subCategory_3));
            subCategoryButtonParents.add((LinearLayout) findViewById(R.id.meeting_create_step4_subCategory_4));
            subCategoryButtonParents.add((LinearLayout) findViewById(R.id.meeting_create_step4_subCategory_5));

            subCategoryButtonParents.get(0).removeAllViews();
        }

        // 레이아웃 파라미터 저장
        if (initialized == false) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.meeting_create_step1_region_1);
            step1_layout_param = new ViewGroup.LayoutParams(linearLayout.getLayoutParams());
            step1_margin_param = new ViewGroup.MarginLayoutParams((ViewGroup.MarginLayoutParams) linearLayout.getLayoutParams());

            ImageButton imageButton = (ImageButton) findViewById(R.id.meeting_create_button_seoul);
            step1_button_param = new ViewGroup.LayoutParams(imageButton.getLayoutParams());

            linearLayout = (LinearLayout) findViewById(R.id.meeting_create_step4_subCategory_1);
            step4_layout_param = new ViewGroup.LayoutParams(linearLayout.getLayoutParams());
        }

        // 지역 선택 버튼 매핑
        if (initialized == false) {
            selectRegionButtons.RegistButton(Region.Seoul.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Seoul.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_seoul_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_seoul_pressed));
            selectRegionButtons.RegistButton(Region.Gyeonggi.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Gyeonggi.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_gyeonggi_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_gyeonggi_pressed));
            selectRegionButtons.RegistButton(Region.Incheon.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Incheon.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_incheon_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_incheon_pressed));
            selectRegionButtons.RegistButton(Region.Daejeon.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Daejeon.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_daejeon_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_daejeon_pressed));
            selectRegionButtons.RegistButton(Region.Chungbuk.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Chungbuk.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_chungbuk_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_chungbuk_pressed));
            selectRegionButtons.RegistButton(Region.Chungnam.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Chungnam.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_chungnam_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_chungnam_pressed));
            selectRegionButtons.RegistButton(Region.Gangwon.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Gangwon.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_gangwon_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_gangwon_pressed));
            selectRegionButtons.RegistButton(Region.Busan.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Busan.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_busan_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_busan_pressed));
            selectRegionButtons.RegistButton(Region.Gyeongbuk.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Gyeongbuk.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_gyeongbuk_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_gyeongbuk_pressed));
            selectRegionButtons.RegistButton(Region.Gyeongnam.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Gyeongnam.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_gyeongnam_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_gyeongnam_pressed));
            selectRegionButtons.RegistButton(Region.Daegu.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Daegu.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_daegu_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_daegu_pressed));
            selectRegionButtons.RegistButton(Region.Ulsan.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Ulsan.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_ulsan_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_ulsan_pressed));
            selectRegionButtons.RegistButton(Region.Gwangju.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Gwangju.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_gwangju_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_gwangju_pressed));
            selectRegionButtons.RegistButton(Region.Jeonbuk.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Jeonbuk.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_jeonbuk_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_jeonbuk_pressed));
            selectRegionButtons.RegistButton(Region.Jeonnam.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Jeonnam.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_jeonnam_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_jeonnam_pressed));
            selectRegionButtons.RegistButton(Region.Jeju.getButtonId_meeting_create(), (ImageButton) findViewById(Region.Jeju.getButtonId_meeting_create()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_jeju_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step1_button_jeju_pressed));
        }

        // 미팅 유저 숫자 선택 버튼 매핑
        if (initialized == false) {
            selectUserCountButtons.RegistButton(R.id.meeting_create_button_2to2, (ImageButton) findViewById(R.id.meeting_create_button_2to2),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step2_2to2_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step2_2to2_pressed));
            selectUserCountButtons.RegistButton(R.id.meeting_create_button_2to2, (ImageButton) findViewById(R.id.meeting_create_button_3to3),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step2_3to3_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step2_3to3_pressed));
            selectUserCountButtons.RegistButton(R.id.meeting_create_button_2to2, (ImageButton) findViewById(R.id.meeting_create_button_4to4),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step2_4to4_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step2_4to4_pressed));
        }

        // 메인 카테고리 선택 버튼 매핑
        if (initialized == false) {
            selectMainCategoryButtons.RegistButton(MainCategory.Travel.getButtonId_meetingCreate(), (ImageButton) findViewById(MainCategory.Travel.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step3_travel_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step3_travel_pressed));
            selectMainCategoryButtons.RegistButton(MainCategory.Exercise.getButtonId_meetingCreate(), (ImageButton) findViewById(MainCategory.Exercise.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step3_exercise_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step3_exercise_pressed));
            selectMainCategoryButtons.RegistButton(MainCategory.Music.getButtonId_meetingCreate(), (ImageButton) findViewById(MainCategory.Music.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step3_music_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step3_music_pressed));
        }

        // 서브 카테고리 선택 버튼 매핑
        if (initialized == false) {
            selectSubCategoryButtons.RegistButton(SubCategory.MountainClimbing.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.MountainClimbing.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_mountain_climbing_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_mountain_climbing_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Walk.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Walk.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_walk_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_walk_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Camping.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Camping.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_camping_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_camping_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Travel_Domestic.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Travel_Domestic.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_travel_domestic_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_travel_domestic_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Travel_Overseas.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Travel_Overseas.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_travel_overseas_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_travel_overseas_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Fishing.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Fishing.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_fishing_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_fishing_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Paragliding.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Paragliding.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_paragliding_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_paragliding_pressed));

            selectSubCategoryButtons.RegistButton(SubCategory.Cycle.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Cycle.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_cycle_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_cycle_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Bowling.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Bowling.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_bowling_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_bowling_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Golf.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Golf.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_golf_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_golf_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Fitness.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Fitness.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_fitness_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_fitness_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.TableTenis.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.TableTenis.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_pingpong_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_pingpong_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Boxing.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Boxing.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_boxing_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_boxing_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Climbing.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Climbing.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_climbing_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_climbing_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Pilates.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Pilates.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_pilates_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_pilates_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Swimming.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Swimming.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_swimming_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_swimming_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Basketball.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Basketball.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_basketball_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_basketball_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Yoga.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Yoga.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_yoga_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_yoga_pressed));

            selectSubCategoryButtons.RegistButton(SubCategory.Piano.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Piano.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_piano_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_piano_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Singing.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Singing.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_singing_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_singing_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Guitar.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Guitar.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_guitar_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_guitar_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Drum.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Drum.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_drum_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_drum_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Hiphop.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Hiphop.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_hiphop_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_hiphop_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Electronic.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Electronic.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_electronic_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_electronic_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Indie.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Indie.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_indie_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_indie_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Violin.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Violin.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_violin_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_violin_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Composition.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Composition.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_composition_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_composition_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Jazz.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Jazz.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_jazz_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_jazz_pressed));
            selectSubCategoryButtons.RegistButton(SubCategory.Band.getButtonId_meetingCreate(), (ImageButton) findViewById(SubCategory.Band.getButtonId_meetingCreate()),
                    ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_band_normal), ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_button_band_pressed));
        }

        // 미팅 이름 입력 EditText 이벤트 추가
        if (initialized == false) {
            final EditText editTextMeetingName = (EditText) findViewById(R.id.meeting_create_edittext_meeting_name);
            FrameLayout touchInterceptor = (FrameLayout) findViewById(R.id.meeting_create_touch_intercaptor);

            touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (editTextMeetingName.isFocused()) {
                            final Rect outRect = new Rect();
                            editTextMeetingName.getGlobalVisibleRect(outRect);

                            if (outRect.contains((int) event.getRawX(), (int) event.getRawY()) == false) {
                                editTextMeetingName.clearFocus();

                                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                        }
                    }

                    return false;
                }
            });

            editTextMeetingName.setImeOptions(EditorInfo.IME_ACTION_DONE);
            editTextMeetingName.addTextChangedListener(new TextWatcher() {
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
                    editTextMeetingName.removeTextChangedListener(this);

                    /*
                    if (s.toString().length() > 0) {
                        Pattern pattern = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐ㄱ-ㅣ가-힣ᆢᆞ ]+$");
                        if (pattern.matcher(s).matches() == false) {
                            editTextMeetingName.setText(beforeText);
                            editTextMeetingName.setSelection(editTextMeetingName.length());
                        }
                    }
                    */

                    editTextMeetingName.addTextChangedListener(this);

                    meetingInfo.setMeetingName(s.toString());
                    validateCreateMeeting();
                }
            });
            editTextMeetingName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        meetingInfo.setMeetingName(editTextMeetingName.getText().toString());
                        validateCreateMeeting();

                        View keyboard = getCurrentFocus();
                        if (keyboard != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        return true;
                    }

                    return false;
                }
            });
        }

        // 우연 만들기 버튼 상태별 리소스
        if (initialized == false) {
            {
                Drawable normalImage, pressedImage, disableImage;
                normalImage = ContextCompat.getDrawable(this, R.drawable.meeting_create_button_submit_enable);
                pressedImage = ContextCompat.getDrawable(this, R.drawable.meeting_create_button_submit_pressed);
                disableImage = ContextCompat.getDrawable(this, R.drawable.meeting_create_button_submit_disabled);

                StateListDrawable states = new StateListDrawable();
                states.addState(new int[]{-android.R.attr.state_enabled}, disableImage);
                states.addState(new int[]{android.R.attr.state_pressed}, pressedImage);
                states.addState(new int[]{android.R.attr.state_enabled}, normalImage);

                ImageButton meetingCreateButton = (ImageButton) findViewById(R.id.meeting_create_button_submit);
                meetingCreateButton.setImageDrawable(states);
            }
        }

        initialized = true;
        validateCreateMeeting();
    }

    void requestMeta() {
        FirebaseDatabase.getInstance().getReference("meta").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Meta meta = dataSnapshot.getValue(Meta.class);
                updateView(meta);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void updateView(Meta meta) {
        // 서버로 보낸 요청이 도착했을 때 Meta 정보 저장
        this.meta = meta;
        Log.d(TAG, gson.toJson(meta));

        // Process Step 1
        {
            regionButtons.clear();
            List<String> regionList = new ArrayList<>(meta.getRegion().keySet());
            Iterator<String> iterator = regionList.iterator();

            while (iterator.hasNext()) {
                String regionName = iterator.next();

                ImageButton newButton = new ImageButton(this);
                newButton.setLayoutParams(step1_button_param);
                newButton.setAdjustViewBounds(true);
                newButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                newButton.setId(Region.fromValue(regionName).getButtonId_meeting_create());
                newButton.setImageDrawable(selectRegionButtons.GetNormalImage(Region.fromValue(regionName).getButtonId_meeting_create()));
                newButton.setBackground(null);
                newButton.setTag(regionName);
                newButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        onRegionButtonClicked(view);
                    }
                });

                LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.f);
                newButton.setLayoutParams(linearLayoutParam);

                ViewGroup.MarginLayoutParams viewGroupLayoutParam = (ViewGroup.MarginLayoutParams) newButton.getLayoutParams();
                newButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                viewGroupLayoutParam.width = newButton.getMeasuredWidth();
                viewGroupLayoutParam.height = newButton.getMeasuredHeight();
                newButton.setLayoutParams(viewGroupLayoutParam);

                regionButtons.add(newButton);
            }

            // 빈 공간 메우기용 더미 버튼 삽입
            while (regionButtons.size() % 3 != 0) {
                ImageButton newButton = new ImageButton(this);
                newButton.setLayoutParams(step1_button_param);
                newButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.meeting_create_button_empty));
                newButton.setAdjustViewBounds(true);
                newButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                newButton.setBackground(null);

                LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.f);
                newButton.setLayoutParams(linearLayoutParam);

                ViewGroup.MarginLayoutParams viewGroupLayoutParam = (ViewGroup.MarginLayoutParams) newButton.getLayoutParams();
                newButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                viewGroupLayoutParam.width = newButton.getMeasuredWidth();
                viewGroupLayoutParam.height = newButton.getMeasuredHeight();
                newButton.setLayoutParams(viewGroupLayoutParam);

                regionButtons.add(newButton);
            }
        }

        // Process Step 3
        {

        }

        // Process Step 4
        {
            subCategoryButtons.clear();

            for (MainCategory mainCategory : MainCategory.values()) {
                Map<String, String> subCategories = meta.getInterestCategory().get(mainCategory.getValue());
                for (String subCategory : subCategories.keySet()) {
                    ImageButton newButton = new ImageButton(this);
                    newButton.setLayoutParams(step1_button_param);
                    newButton.setAdjustViewBounds(true);
                    newButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    newButton.setId(SubCategory.fromValue(subCategory).getButtonId_meetingCreate());
                    newButton.setImageDrawable(selectSubCategoryButtons.GetNormalImage(SubCategory.fromValue(subCategory).getButtonId_meetingCreate()));
                    newButton.setBackground(null);
                    newButton.setTag(subCategory);
                    newButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            onSubCategoryButtonClicked(view);
                        }
                    });

                    LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.f);
                    newButton.setLayoutParams(linearLayoutParam);

                    ViewGroup.MarginLayoutParams viewGroupLayoutParam = (ViewGroup.MarginLayoutParams) newButton.getLayoutParams();
                    newButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    viewGroupLayoutParam.width = newButton.getMeasuredWidth();
                    viewGroupLayoutParam.height = newButton.getMeasuredHeight();
                    newButton.setLayoutParams(viewGroupLayoutParam);

                    subCategoryButtons.put(subCategory, newButton);
                }
            }
        }
    }
    @Click(R.id.meeting_create_back_button)
    void onBackButtonClicked(View view) {
        super.onBackPressed();
    }

    @Click(R.id.meeting_create_button_seoul)
    void onRegionButtonClicked(View view) {
        // 이전에 선택된 버튼이 있었다면 선택되지 않은 상태로
        if (meetingInfo.getRegion() != null) {
            ImageButton oldSelectedButton = (ImageButton) findViewById(Region.fromValue(meetingInfo.getRegion()).getButtonId_meeting_create());
            oldSelectedButton.setImageDrawable(selectRegionButtons.GetNormalImage(Region.fromValue(meetingInfo.getRegion()).getButtonId_meeting_create()));
        }

        // 이번에 눌린 버튼은 선택된 상태로
        final String region = view.getTag().toString();
        ImageButton newSelectedButton = (ImageButton) findViewById(Region.fromValue(region).getButtonId_meeting_create());
        newSelectedButton.setImageDrawable(selectRegionButtons.GetPressedImage(Region.fromValue(region).getButtonId_meeting_create()));

        // 눌린 버튼의 데이터로 갱신
        meetingInfo.setRegion(region);

        validateCreateMeeting();
    }
    @Click(R.id.meeting_create_button_gyeonggi)
    void onRegionButtonClicked2(View view) {
        // 이전에 선택된 버튼이 있었다면 선택되지 않은 상태로
        if (meetingInfo.getRegion() != null) {
            ImageButton oldSelectedButton = (ImageButton) findViewById(Region.fromValue(meetingInfo.getRegion()).getButtonId_meeting_create());
            oldSelectedButton.setImageDrawable(selectRegionButtons.GetNormalImage(Region.fromValue(meetingInfo.getRegion()).getButtonId_meeting_create()));
        }

        // 이번에 눌린 버튼은 선택된 상태로
        final String region = view.getTag().toString();
        ImageButton newSelectedButton = (ImageButton) findViewById(Region.fromValue(region).getButtonId_meeting_create());
        newSelectedButton.setImageDrawable(selectRegionButtons.GetPressedImage(Region.fromValue(region).getButtonId_meeting_create()));

        // 눌린 버튼의 데이터로 갱신
        meetingInfo.setRegion(region);

        validateCreateMeeting();
    }
    @Click(R.id.meeting_create_button_incheon)
    void onRegionButtonClicked3(View view) {
        // 이전에 선택된 버튼이 있었다면 선택되지 않은 상태로
        if (meetingInfo.getRegion() != null) {
            ImageButton oldSelectedButton = (ImageButton) findViewById(Region.fromValue(meetingInfo.getRegion()).getButtonId_meeting_create());
            oldSelectedButton.setImageDrawable(selectRegionButtons.GetNormalImage(Region.fromValue(meetingInfo.getRegion()).getButtonId_meeting_create()));
        }

        // 이번에 눌린 버튼은 선택된 상태로
        final String region = view.getTag().toString();
        ImageButton newSelectedButton = (ImageButton) findViewById(Region.fromValue(region).getButtonId_meeting_create());
        newSelectedButton.setImageDrawable(selectRegionButtons.GetPressedImage(Region.fromValue(region).getButtonId_meeting_create()));

        // 눌린 버튼의 데이터로 갱신
        meetingInfo.setRegion(region);

        validateCreateMeeting();
    }
    @Click(R.id.meeting_create_button_2to2)
    void onUserCountSelectButtonClicked2(View view) {
        // 이전에 선택된 버튼이 있었다면 선택되지 않은 상태로
        final long oldSelectedUserCount = meetingInfo.getUserCount();
        if (oldSelectedUserCount != 0) {
            ImageButton oldSelectedButton = (ImageButton) findViewById(userCountToIds.get(oldSelectedUserCount).getButtonId());
            oldSelectedButton.setImageDrawable(ContextCompat.getDrawable(this, userCountToIds.get(oldSelectedUserCount).getNormalImage()));
        }

        // 이번에 눌린 버튼은 선택된 상태로
        final long selectedUserCount = Long.parseLong(view.getTag().toString());
        ImageButton newSelectedButton = (ImageButton) findViewById(userCountToIds.get(selectedUserCount).getButtonId());
        newSelectedButton.setImageDrawable(ContextCompat.getDrawable(this, userCountToIds.get(selectedUserCount).getPressedImage()));

        // 눌린 버튼의 데이터로 갱신
        meetingInfo.setUserCount(selectedUserCount);

        validateCreateMeeting();
    }
    @Click(R.id.meeting_create_button_3to3)
    void onUserCountSelectButtonClicked3(View view) {
        // 이전에 선택된 버튼이 있었다면 선택되지 않은 상태로
        final long oldSelectedUserCount = meetingInfo.getUserCount();
        if (oldSelectedUserCount != 0) {
            ImageButton oldSelectedButton = (ImageButton) findViewById(userCountToIds.get(oldSelectedUserCount).getButtonId());
            oldSelectedButton.setImageDrawable(ContextCompat.getDrawable(this, userCountToIds.get(oldSelectedUserCount).getNormalImage()));
        }

        // 이번에 눌린 버튼은 선택된 상태로
        final long selectedUserCount = Long.parseLong(view.getTag().toString());
        ImageButton newSelectedButton = (ImageButton) findViewById(userCountToIds.get(selectedUserCount).getButtonId());
        newSelectedButton.setImageDrawable(ContextCompat.getDrawable(this, userCountToIds.get(selectedUserCount).getPressedImage()));

        // 눌린 버튼의 데이터로 갱신
        meetingInfo.setUserCount(selectedUserCount);

        validateCreateMeeting();
    }
    @Click(R.id.meeting_create_button_4to4)
    void onUserCountSelectButtonClicked4(View view) {
        // 이전에 선택된 버튼이 있었다면 선택되지 않은 상태로
        final long oldSelectedUserCount = meetingInfo.getUserCount();
        if (oldSelectedUserCount != 0) {
            ImageButton oldSelectedButton = (ImageButton) findViewById(userCountToIds.get(oldSelectedUserCount).getButtonId());
            oldSelectedButton.setImageDrawable(ContextCompat.getDrawable(this, userCountToIds.get(oldSelectedUserCount).getNormalImage()));
        }

        // 이번에 눌린 버튼은 선택된 상태로
        final long selectedUserCount = Long.parseLong(view.getTag().toString());
        ImageButton newSelectedButton = (ImageButton) findViewById(userCountToIds.get(selectedUserCount).getButtonId());
        newSelectedButton.setImageDrawable(ContextCompat.getDrawable(this, userCountToIds.get(selectedUserCount).getPressedImage()));

        // 눌린 버튼의 데이터로 갱신
        meetingInfo.setUserCount(selectedUserCount);

        validateCreateMeeting();
    }
    @Click(R.id.meeting_create_button_travel)
    void onMainCategoryButtonClicked(View view) {
        // 이전에 선택된 버튼이 있었다면 선택되지 않은 상태로
        final String oldMainCategory = meetingInfo.getInterest();
        if (oldMainCategory != null) {
            ImageButton oldSelectedButton = (ImageButton) findViewById(MainCategory.fromValue(oldMainCategory).getButtonId_meetingCreate());
            oldSelectedButton.setImageDrawable(selectMainCategoryButtons.GetNormalImage(MainCategory.fromValue(oldMainCategory).getButtonId_meetingCreate()));
        }
        // 이전에 선택된 버튼이 없었다면 배경 disable ---> enable
        else {
            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.meeting_create_step_4);
            frameLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_background));

            frameLayout = (FrameLayout)findViewById(R.id.meeting_create_step_5);
            frameLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.meeting_create_step5_background));

            ImageButton dropDownButton = (ImageButton)findViewById(R.id.meeting_create_step4_dropdown);
            dropDownButton.setVisibility(View.VISIBLE);

            EditText editText = (EditText)findViewById(R.id.meeting_create_edittext_meeting_name);
            editText.setEnabled(true);
        }

        // 이번에 눌린 버튼은 선택된 상태로
        final String selectedMainCategory = view.getTag().toString();
        ImageButton newSelectedButton = (ImageButton) findViewById(MainCategory.fromValue(selectedMainCategory).getButtonId_meetingCreate());
        newSelectedButton.setImageDrawable(selectMainCategoryButtons.GetPressedImage(MainCategory.fromValue(selectedMainCategory).getButtonId_meetingCreate()));

        // 눌린 버튼의 데이터로 갱신
        meetingInfo.setInterest(selectedMainCategory);

        if (meetingInfo.getDetailInterest() != null &&
                MainCategory.getSubCategoty(MainCategory.fromValue(selectedMainCategory)).contains(SubCategory.fromValue(meetingInfo.getDetailInterest())) == false) {
            // 서브 카테고리가 선택되어 있고, 새로 선택된 메인 카테고리에 포함되지 않을 때
            // 해당 서브 카테고리는 선택되지 않음으로 바꿈
            ImageButton selectedSubCategoryButton = (ImageButton) findViewById(SubCategory.fromValue(meetingInfo.getDetailInterest()).getButtonId_meetingCreate());
            selectedSubCategoryButton.setImageDrawable(selectSubCategoryButtons.GetNormalImage(SubCategory.fromValue(meetingInfo.getDetailInterest()).getButtonId_meetingCreate()));
            meetingInfo.setDetailInterest(null);
        }

        // 눌려진 메인 카테고리에 종속되는 서브 카테고리 버튼 세팅(Step 4)
        isDropDownButtonClicked[4] = !isDropDownButtonClicked[4];
        onDropDownMenuClicked(findViewById(R.id.meeting_create_step4_dropdown));

        validateCreateMeeting();
    }

    @Click(R.id.meeting_create_button_exercise)
    void onMainCategoryButtonClicked2(View view) {
        // 이전에 선택된 버튼이 있었다면 선택되지 않은 상태로
        final String oldMainCategory = meetingInfo.getInterest();
        if (oldMainCategory != null) {
            ImageButton oldSelectedButton = (ImageButton) findViewById(MainCategory.fromValue(oldMainCategory).getButtonId_meetingCreate());
            oldSelectedButton.setImageDrawable(selectMainCategoryButtons.GetNormalImage(MainCategory.fromValue(oldMainCategory).getButtonId_meetingCreate()));
        }
        // 이전에 선택된 버튼이 없었다면 배경 disable ---> enable
        else {
            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.meeting_create_step_4);
            frameLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_background));

            frameLayout = (FrameLayout)findViewById(R.id.meeting_create_step_5);
            frameLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.meeting_create_step5_background));

            ImageButton dropDownButton = (ImageButton)findViewById(R.id.meeting_create_step4_dropdown);
            dropDownButton.setVisibility(View.VISIBLE);

            EditText editText = (EditText)findViewById(R.id.meeting_create_edittext_meeting_name);
            editText.setEnabled(true);
        }

        // 이번에 눌린 버튼은 선택된 상태로
        final String selectedMainCategory = view.getTag().toString();
        ImageButton newSelectedButton = (ImageButton) findViewById(MainCategory.fromValue(selectedMainCategory).getButtonId_meetingCreate());
        newSelectedButton.setImageDrawable(selectMainCategoryButtons.GetPressedImage(MainCategory.fromValue(selectedMainCategory).getButtonId_meetingCreate()));

        // 눌린 버튼의 데이터로 갱신
        meetingInfo.setInterest(selectedMainCategory);

        if (meetingInfo.getDetailInterest() != null &&
                MainCategory.getSubCategoty(MainCategory.fromValue(selectedMainCategory)).contains(SubCategory.fromValue(meetingInfo.getDetailInterest())) == false) {
            // 서브 카테고리가 선택되어 있고, 새로 선택된 메인 카테고리에 포함되지 않을 때
            // 해당 서브 카테고리는 선택되지 않음으로 바꿈
            ImageButton selectedSubCategoryButton = (ImageButton) findViewById(SubCategory.fromValue(meetingInfo.getDetailInterest()).getButtonId_meetingCreate());
            selectedSubCategoryButton.setImageDrawable(selectSubCategoryButtons.GetNormalImage(SubCategory.fromValue(meetingInfo.getDetailInterest()).getButtonId_meetingCreate()));
            meetingInfo.setDetailInterest(null);
        }

        // 눌려진 메인 카테고리에 종속되는 서브 카테고리 버튼 세팅(Step 4)
        isDropDownButtonClicked[4] = !isDropDownButtonClicked[4];
        onDropDownMenuClicked(findViewById(R.id.meeting_create_step4_dropdown));

        validateCreateMeeting();
    }
    @Click(R.id.meeting_create_button_music)
    void onMainCategoryButtonClicked3(View view) {
        // 이전에 선택된 버튼이 있었다면 선택되지 않은 상태로
        final String oldMainCategory = meetingInfo.getInterest();
        if (oldMainCategory != null) {
            ImageButton oldSelectedButton = (ImageButton) findViewById(MainCategory.fromValue(oldMainCategory).getButtonId_meetingCreate());
            oldSelectedButton.setImageDrawable(selectMainCategoryButtons.GetNormalImage(MainCategory.fromValue(oldMainCategory).getButtonId_meetingCreate()));
        }
        // 이전에 선택된 버튼이 없었다면 배경 disable ---> enable
        else {
            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.meeting_create_step_4);
            frameLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_background));

            frameLayout = (FrameLayout)findViewById(R.id.meeting_create_step_5);
            frameLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.meeting_create_step5_background));

            ImageButton dropDownButton = (ImageButton)findViewById(R.id.meeting_create_step4_dropdown);
            dropDownButton.setVisibility(View.VISIBLE);

            EditText editText = (EditText)findViewById(R.id.meeting_create_edittext_meeting_name);
            editText.setEnabled(true);
        }

        // 이번에 눌린 버튼은 선택된 상태로
        final String selectedMainCategory = view.getTag().toString();
        ImageButton newSelectedButton = (ImageButton) findViewById(MainCategory.fromValue(selectedMainCategory).getButtonId_meetingCreate());
        newSelectedButton.setImageDrawable(selectMainCategoryButtons.GetPressedImage(MainCategory.fromValue(selectedMainCategory).getButtonId_meetingCreate()));

        // 눌린 버튼의 데이터로 갱신
        meetingInfo.setInterest(selectedMainCategory);

        if (meetingInfo.getDetailInterest() != null &&
                MainCategory.getSubCategoty(MainCategory.fromValue(selectedMainCategory)).contains(SubCategory.fromValue(meetingInfo.getDetailInterest())) == false) {
            // 서브 카테고리가 선택되어 있고, 새로 선택된 메인 카테고리에 포함되지 않을 때
            // 해당 서브 카테고리는 선택되지 않음으로 바꿈
            ImageButton selectedSubCategoryButton = (ImageButton) findViewById(SubCategory.fromValue(meetingInfo.getDetailInterest()).getButtonId_meetingCreate());
            selectedSubCategoryButton.setImageDrawable(selectSubCategoryButtons.GetNormalImage(SubCategory.fromValue(meetingInfo.getDetailInterest()).getButtonId_meetingCreate()));
            meetingInfo.setDetailInterest(null);
        }

        // 눌려진 메인 카테고리에 종속되는 서브 카테고리 버튼 세팅(Step 4)
        isDropDownButtonClicked[4] = !isDropDownButtonClicked[4];
        onDropDownMenuClicked(findViewById(R.id.meeting_create_step4_dropdown));

        validateCreateMeeting();
    }
    void onSubCategoryButtonClicked(View view) {
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.meeting_create_step_4);
        if (frameLayout.getVisibility() == View.INVISIBLE) {
            // 비활성화 상태라면 아무 일도 안 함
            return;
        }

        // 이전에 선택된 버튼이 있었다면 선택되지 않은 상태로
        final String oldSubCategory = meetingInfo.getDetailInterest();
        if (oldSubCategory != null) {
            Log.d(TAG, oldSubCategory);
            ImageButton oldSelectedButton = (ImageButton) findViewById(SubCategory.fromValue(oldSubCategory).getButtonId_meetingCreate());
            oldSelectedButton.setImageDrawable(selectSubCategoryButtons.GetNormalImage(SubCategory.fromValue(oldSubCategory).getButtonId_meetingCreate()));
        }

        // 이번에 눌린 버튼은 선택된 상태로
        final String selectedSubCategory = view.getTag().toString();
        ImageButton newSelectedButton = (ImageButton) findViewById(SubCategory.fromValue(selectedSubCategory).getButtonId_meetingCreate());
        newSelectedButton.setImageDrawable(selectSubCategoryButtons.GetPressedImage(SubCategory.fromValue(selectedSubCategory).getButtonId_meetingCreate()));

        // 눌린 버튼의 데이터로 갱신
        meetingInfo.setDetailInterest(selectedSubCategory);

        validateCreateMeeting();
    }
    @Click(R.id.meeting_create_step1_dropdown)
    void onDropDownMenuClicked(View view) {
        // 드롭다운 화살표 버튼이 눌려졌을 때

        final int parameter = Integer.parseInt(view.getTag().toString());
        final int offsetY = (int) (ButtonOffsetY * getResources().getDisplayMetrics().density);
        final int marginBottom = (int) (MarginBottom * getResources().getDisplayMetrics().density);

        isDropDownButtonClicked[parameter] = !isDropDownButtonClicked[parameter];

        if (parameter == 1) {
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.meeting_create_step_1);
            int newBackgroundId = isDropDownButtonClicked[parameter]
                    ? R.drawable.meeting_create_step1_background_3by6
                    : R.drawable.meeting_create_step1_background;
            frameLayout.setBackground(ContextCompat.getDrawable(this, newBackgroundId));

            if (isDropDownButtonClicked[parameter]) {
                for (int i = 0; i < regionButtonParents.size(); ++i) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) regionButtonParents.get(i).getLayoutParams();
                    if (i == regionButtonParents.size() - 1) {
                        layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin + (offsetY * i),
                                step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);
                    } else {
                        layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin + (offsetY * i),
                                step1_margin_param.rightMargin, step1_margin_param.bottomMargin);
                    }

                    regionButtonParents.get(i).setLayoutParams(layoutParams);
                    regionButtonParents.get(i).removeAllViews();
                }

                int iterCount = 0;
                Iterator<ImageButton> iter = regionButtons.iterator();
                while (iter.hasNext()) {
                    regionButtonParents.get(iterCount / 3).addView(iter.next());
                    ++iterCount;
                }
            } else {
                for (int i = 0; i < regionButtonParents.size(); ++i) {
                    if (meetingInfo.getRegion() == null) {
                        // 아직 선택된 지역이 없다면 처음 3개의 지역을 그냥 보여 줌
                        if (i > 0) {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) regionButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin);

                            regionButtonParents.get(i).setLayoutParams(layoutParams);
                            regionButtonParents.get(i).removeAllViews();
                        } else {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);
                            regionButtonParents.get(i).setLayoutParams(layoutParams);
                        }
                    } else {
                        // 선택된 지역이 있다면 해당 지역이 포함된 Row를 보여 줌
                        List<String> regionList = new ArrayList<>(meta.getRegion().keySet());
                        final int selectedIndex = regionList.indexOf(meetingInfo.getRegion());

                        if (i != selectedIndex / 3) {
                            regionButtonParents.get(i).removeAllViews();

                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) regionButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin);
                            regionButtonParents.get(i).setLayoutParams(layoutParams);
                        } else {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) regionButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);
                            regionButtonParents.get(i).setLayoutParams(layoutParams);
                        }
                    }
                }
            }
        } else if (parameter == 4) {
            if (meetingInfo.getInterest() == null) {
                return;
            }

            // 드롭다운 메뉴 버튼을 눌러서 메뉴가 펴질 때
            if (isDropDownButtonClicked[parameter]) {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.meeting_create_step_4);
                final int newBackgroundId = subCategoryDropDownedBackgrounds.get(MainCategory.fromValue(meetingInfo.getInterest()));
                frameLayout.setBackground(ContextCompat.getDrawable(this, newBackgroundId));

                for (int i = 0; i < subCategoryButtonParents.size(); ++i) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                    layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin, step1_margin_param.rightMargin, step1_margin_param.bottomMargin);

                    subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                    subCategoryButtonParents.get(i).removeAllViews();
                }

                ArrayList<SubCategory> subCategories = MainCategory.getSubCategoty(MainCategory.fromValue(meetingInfo.getInterest()));
                for (int i = 0; i < subCategories.size(); ++i) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i / 3).getLayoutParams();
                    if (i == subCategories.size() - 1) {
                        layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin + (offsetY * (i / 3)),
                                step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);
                    } else {
                        layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin + (offsetY * (i / 3)),
                                step1_margin_param.rightMargin, step1_margin_param.bottomMargin);
                    }
                    subCategoryButtonParents.get(i / 3).setLayoutParams(layoutParams);

                    ImageButton imageButton = subCategoryButtons.get(subCategories.get(i).getValue());
                    subCategoryButtonParents.get(i / 3).addView(imageButton);
                }

                for (int i = (subCategories.size() / 3) + 1; i < subCategoryButtonParents.size(); ++i) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                    layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                            step1_margin_param.rightMargin, step1_margin_param.bottomMargin);
                    subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                }

                // 빈 공간 메우기용 더미 버튼 삽입
                int subCatogorySize = subCategories.size();
                while (subCatogorySize % 3 != 0) {
                    ImageButton newButton = new ImageButton(this);
                    newButton.setLayoutParams(step1_button_param);
                    newButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.meeting_create_button_empty));
                    newButton.setAdjustViewBounds(true);
                    newButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    newButton.setBackground(null);

                    LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.f);
                    newButton.setLayoutParams(linearLayoutParam);

                    ViewGroup.MarginLayoutParams viewGroupLayoutParam = (ViewGroup.MarginLayoutParams) newButton.getLayoutParams();
                    newButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    viewGroupLayoutParam.width = newButton.getMeasuredWidth();
                    viewGroupLayoutParam.height = newButton.getMeasuredHeight();
                    newButton.setLayoutParams(viewGroupLayoutParam);

                    subCategoryButtonParents.get(subCategories.size() / 3).addView(newButton);
                    ++subCatogorySize;
                }
            } else {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.meeting_create_step_4);
                frameLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_background));

                ArrayList<SubCategory> subCategories = MainCategory.getSubCategoty(MainCategory.fromValue(meetingInfo.getInterest()));
                for (int i = 0; i < subCategoryButtonParents.size(); ++i) {
                    if (meetingInfo.getDetailInterest() == null) {
                        // 아직 선택된 서브 카테고리가 없다면 처음 3개의 서브 카테고리를 그냥 보여 줌
                        if (i > 0) {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin);

                            subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                            subCategoryButtonParents.get(i).removeAllViews();
                        } else {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);

                            subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                            subCategoryButtonParents.get(i).removeAllViews();

                            for (int j = 0; j < 3; ++j) {
                                if (j >= subCategories.size()) {
                                    ImageButton newButton = new ImageButton(this);
                                    newButton.setLayoutParams(step1_button_param);
                                    newButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.meeting_create_button_empty));
                                    newButton.setAdjustViewBounds(true);
                                    newButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    newButton.setBackground(null);

                                    LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.f);
                                    newButton.setLayoutParams(linearLayoutParam);

                                    ViewGroup.MarginLayoutParams viewGroupLayoutParam = (ViewGroup.MarginLayoutParams) newButton.getLayoutParams();
                                    newButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                                    viewGroupLayoutParam.width = newButton.getMeasuredWidth();
                                    viewGroupLayoutParam.height = newButton.getMeasuredHeight();
                                    newButton.setLayoutParams(viewGroupLayoutParam);

                                    subCategoryButtonParents.get(i).addView(newButton);
                                } else {
                                    ImageButton newButton = subCategoryButtons.get(subCategories.get(j).getValue());
                                    subCategoryButtonParents.get(i).addView(newButton);
                                }
                            }
                        }
                    } else {
                        // 선택된 서브 카테고리가 있다면 해당 서브 카테고리가 포함된 Row를 보여 줌
                        final int selectedIndex = subCategories.indexOf(SubCategory.fromValue(meetingInfo.getDetailInterest()));
                        if (i != selectedIndex / 3) {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin);

                            subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                            subCategoryButtonParents.get(i).removeAllViews();
                        } else if (i > selectedIndex / 3) {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin);
                            subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                        } else {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);
                            subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                        }
                    }
                }
            }
        } else {
            Log.w(TAG, "드롭다운 메뉴가 존재하지 않는 스텝");
        }
    }
    @Click(R.id.meeting_create_step4_dropdown)
    void onDropDownMenuClicked2(View view) {
        // 드롭다운 화살표 버튼이 눌려졌을 때

        final int parameter = Integer.parseInt(view.getTag().toString());
        final int offsetY = (int) (ButtonOffsetY * getResources().getDisplayMetrics().density);
        final int marginBottom = (int) (MarginBottom * getResources().getDisplayMetrics().density);

        isDropDownButtonClicked[parameter] = !isDropDownButtonClicked[parameter];

        if (parameter == 1) {
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.meeting_create_step_1);
            int newBackgroundId = isDropDownButtonClicked[parameter]
                    ? R.drawable.meeting_create_step1_background_3by6
                    : R.drawable.meeting_create_step1_background;
            frameLayout.setBackground(ContextCompat.getDrawable(this, newBackgroundId));

            if (isDropDownButtonClicked[parameter]) {
                for (int i = 0; i < regionButtonParents.size(); ++i) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) regionButtonParents.get(i).getLayoutParams();
                    if (i == regionButtonParents.size() - 1) {
                        layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin + (offsetY * i),
                                step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);
                    } else {
                        layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin + (offsetY * i),
                                step1_margin_param.rightMargin, step1_margin_param.bottomMargin);
                    }

                    regionButtonParents.get(i).setLayoutParams(layoutParams);
                    regionButtonParents.get(i).removeAllViews();
                }

                int iterCount = 0;
                Iterator<ImageButton> iter = regionButtons.iterator();
                while (iter.hasNext()) {
                    regionButtonParents.get(iterCount / 3).addView(iter.next());
                    ++iterCount;
                }
            } else {
                for (int i = 0; i < regionButtonParents.size(); ++i) {
                    if (meetingInfo.getRegion() == null) {
                        // 아직 선택된 지역이 없다면 처음 3개의 지역을 그냥 보여 줌
                        if (i > 0) {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) regionButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin);

                            regionButtonParents.get(i).setLayoutParams(layoutParams);
                            regionButtonParents.get(i).removeAllViews();
                        } else {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);
                            regionButtonParents.get(i).setLayoutParams(layoutParams);
                        }
                    } else {
                        // 선택된 지역이 있다면 해당 지역이 포함된 Row를 보여 줌
                        List<String> regionList = new ArrayList<>(meta.getRegion().keySet());
                        final int selectedIndex = regionList.indexOf(meetingInfo.getRegion());

                        if (i != selectedIndex / 3) {
                            regionButtonParents.get(i).removeAllViews();

                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) regionButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin);
                            regionButtonParents.get(i).setLayoutParams(layoutParams);
                        } else {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) regionButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);
                            regionButtonParents.get(i).setLayoutParams(layoutParams);
                        }
                    }
                }
            }
        } else if (parameter == 4) {
            if (meetingInfo.getInterest() == null) {
                return;
            }

            // 드롭다운 메뉴 버튼을 눌러서 메뉴가 펴질 때
            if (isDropDownButtonClicked[parameter]) {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.meeting_create_step_4);
                final int newBackgroundId = subCategoryDropDownedBackgrounds.get(MainCategory.fromValue(meetingInfo.getInterest()));
                frameLayout.setBackground(ContextCompat.getDrawable(this, newBackgroundId));

                for (int i = 0; i < subCategoryButtonParents.size(); ++i) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                    layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin, step1_margin_param.rightMargin, step1_margin_param.bottomMargin);

                    subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                    subCategoryButtonParents.get(i).removeAllViews();
                }

                ArrayList<SubCategory> subCategories = MainCategory.getSubCategoty(MainCategory.fromValue(meetingInfo.getInterest()));
                for (int i = 0; i < subCategories.size(); ++i) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i / 3).getLayoutParams();
                    if (i == subCategories.size() - 1) {
                        layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin + (offsetY * (i / 3)),
                                step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);
                    } else {
                        layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin + (offsetY * (i / 3)),
                                step1_margin_param.rightMargin, step1_margin_param.bottomMargin);
                    }
                    subCategoryButtonParents.get(i / 3).setLayoutParams(layoutParams);

                    ImageButton imageButton = subCategoryButtons.get(subCategories.get(i).getValue());
                    subCategoryButtonParents.get(i / 3).addView(imageButton);
                }

                for (int i = (subCategories.size() / 3) + 1; i < subCategoryButtonParents.size(); ++i) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                    layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                            step1_margin_param.rightMargin, step1_margin_param.bottomMargin);
                    subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                }

                // 빈 공간 메우기용 더미 버튼 삽입
                int subCatogorySize = subCategories.size();
                while (subCatogorySize % 3 != 0) {
                    ImageButton newButton = new ImageButton(this);
                    newButton.setLayoutParams(step1_button_param);
                    newButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.meeting_create_button_empty));
                    newButton.setAdjustViewBounds(true);
                    newButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    newButton.setBackground(null);

                    LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.f);
                    newButton.setLayoutParams(linearLayoutParam);

                    ViewGroup.MarginLayoutParams viewGroupLayoutParam = (ViewGroup.MarginLayoutParams) newButton.getLayoutParams();
                    newButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    viewGroupLayoutParam.width = newButton.getMeasuredWidth();
                    viewGroupLayoutParam.height = newButton.getMeasuredHeight();
                    newButton.setLayoutParams(viewGroupLayoutParam);

                    subCategoryButtonParents.get(subCategories.size() / 3).addView(newButton);
                    ++subCatogorySize;
                }
            } else {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.meeting_create_step_4);
                frameLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.meeting_create_step4_background));

                ArrayList<SubCategory> subCategories = MainCategory.getSubCategoty(MainCategory.fromValue(meetingInfo.getInterest()));
                for (int i = 0; i < subCategoryButtonParents.size(); ++i) {
                    if (meetingInfo.getDetailInterest() == null) {
                        // 아직 선택된 서브 카테고리가 없다면 처음 3개의 서브 카테고리를 그냥 보여 줌
                        if (i > 0) {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin);

                            subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                            subCategoryButtonParents.get(i).removeAllViews();
                        } else {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);

                            subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                            subCategoryButtonParents.get(i).removeAllViews();

                            for (int j = 0; j < 3; ++j) {
                                if (j >= subCategories.size()) {
                                    ImageButton newButton = new ImageButton(this);
                                    newButton.setLayoutParams(step1_button_param);
                                    newButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.meeting_create_button_empty));
                                    newButton.setAdjustViewBounds(true);
                                    newButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    newButton.setBackground(null);

                                    LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.f);
                                    newButton.setLayoutParams(linearLayoutParam);

                                    ViewGroup.MarginLayoutParams viewGroupLayoutParam = (ViewGroup.MarginLayoutParams) newButton.getLayoutParams();
                                    newButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                                    viewGroupLayoutParam.width = newButton.getMeasuredWidth();
                                    viewGroupLayoutParam.height = newButton.getMeasuredHeight();
                                    newButton.setLayoutParams(viewGroupLayoutParam);

                                    subCategoryButtonParents.get(i).addView(newButton);
                                } else {
                                    ImageButton newButton = subCategoryButtons.get(subCategories.get(j).getValue());
                                    subCategoryButtonParents.get(i).addView(newButton);
                                }
                            }
                        }
                    } else {
                        // 선택된 서브 카테고리가 있다면 해당 서브 카테고리가 포함된 Row를 보여 줌
                        final int selectedIndex = subCategories.indexOf(SubCategory.fromValue(meetingInfo.getDetailInterest()));
                        if (i != selectedIndex / 3) {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin);

                            subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                            subCategoryButtonParents.get(i).removeAllViews();
                        } else if (i > selectedIndex / 3) {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin);
                            subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                        } else {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) subCategoryButtonParents.get(i).getLayoutParams();
                            layoutParams.setMargins(step1_margin_param.leftMargin, step1_margin_param.topMargin,
                                    step1_margin_param.rightMargin, step1_margin_param.bottomMargin + marginBottom);
                            subCategoryButtonParents.get(i).setLayoutParams(layoutParams);
                        }
                    }
                }
            }
        } else {
            Log.w(TAG, "드롭다운 메뉴가 존재하지 않는 스텝");
        }
    }
    @Background
    @Click(R.id.meeting_create_button_submit)
    void createMeeting() {
        if (validateCreateMeeting() == false) {
            return;
        }

        meetingInfo.setPhotoUrl(meta.getInterestCategory().get(meetingInfo.getInterest()).get(meetingInfo.getDetailInterest()));

        try {
            meetingBO.createMeeting(meetingInfo);

            Intent intent = new Intent(this, ChatActivity_.class);
            intent.putExtra("chatId", meetingInfo.getId());
            startActivity(intent);

            finish();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    boolean validateCreateMeeting() {
        boolean result = true;

        if (meetingInfo.getRegion() == null) {
            result = false;
        }

        if (meetingInfo.getUserCount() == 0) {
            result = false;
        }

        if (meetingInfo.getInterest() == null) {
            result = false;
        }

        if (meetingInfo.getDetailInterest() == null) {
            result = false;
        }

        if (meetingInfo.getMeetingName() == null ||
            meetingInfo.getMeetingName().isEmpty()) {
            result = false;
        }

        ImageButton meetingCreateButton = (ImageButton) findViewById(R.id.meeting_create_button_submit);
        meetingCreateButton.setEnabled(result);

        return result;
    }
}
