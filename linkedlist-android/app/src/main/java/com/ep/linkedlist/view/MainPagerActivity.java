package com.ep.linkedlist.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ep.linkedlist.R;
import com.ep.linkedlist.view.chat.ChatFragment_;
import com.ep.linkedlist.view.home.HomeFragment_;
import com.ep.linkedlist.view.list.ListFragmentEmpty_;
import com.ep.linkedlist.view.profile.ProfileFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by jiwon on 2016-10-30.
 */
@EActivity(R.layout.activity_main_pager)
public class MainPagerActivity extends FragmentActivity {
    private static final String TAG = MainPagerActivity.class.getName();

    private static final int PAGE_HOME = 0;
    private static final int PAGE_LIST = 1;
    private static final int PAGE_CHAT = 2;
    private static final int PAGE_PROFILE = 3;
    @ViewById
    ImageView mainMenuHome;

    @ViewById
    ImageView mainMenuList;

    @ViewById
    ImageView mainMenuChat;

    @ViewById
    ImageView mainMenuUser;
    @ViewById
    ViewPager mainViewPager;

    PagerAdapter pagerAdapter;

    @AfterViews
    void afterViews() {
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(pagerAdapter);
        mainViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            updateTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void updateTab(int position) {
        mainMenuHome.setImageResource(R.drawable.main_menu_home);
        mainMenuList.setImageResource(R.drawable.main_menu_list);
        mainMenuChat.setImageResource(R.drawable.main_menu_chat);
        mainMenuUser.setImageResource(R.drawable.main_menu_user);
        if (position == PAGE_HOME)
        {
            mainMenuHome.setImageResource(R.drawable.main_menu_home_select);
        }
        else if (position == PAGE_LIST)
        {
            mainMenuList.setImageResource(R.drawable.main_menu_list_select);
        }
        else if (position == PAGE_CHAT)
        {
            mainMenuChat.setImageResource(R.drawable.main_menu_chat_select);
        }
        else if (position == PAGE_PROFILE)
        {
            mainMenuUser.setImageResource(R.drawable.main_menu_user_select);
        }
    }

    @Click({
            R.id.mainMenuHome,
            R.id.mainMenuHome_Entire,
            R.id.mainMenuList,
            R.id.mainMenuList_Entire,
            R.id.mainMenuChat,
            R.id.mainMenuChat_Entire,
            R.id.mainMenuUser,
            R.id.mainMenuUser_Entire
    })
    void clickMenu(View view) {
        switch (view.getId()) {
            case R.id.mainMenuHome:
            case R.id.mainMenuHome_Entire:
                mainViewPager.setCurrentItem(PAGE_HOME, true);
                break;
            case R.id.mainMenuList:
            case R.id.mainMenuList_Entire:
                mainViewPager.setCurrentItem(PAGE_LIST, true);
                break;
            case R.id.mainMenuChat:
            case R.id.mainMenuChat_Entire:
                mainViewPager.setCurrentItem(PAGE_CHAT, true);
                break;
            case R.id.mainMenuUser:
            case R.id.mainMenuUser_Entire:
                mainViewPager.setCurrentItem(PAGE_PROFILE, true);
                break;
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == PAGE_HOME) {
                fragment = HomeFragment_.builder().build();
            } else if (position == PAGE_LIST) {
                fragment = ListFragmentEmpty_.builder().build();
            } else if (position == PAGE_CHAT) {
                fragment = ChatFragment_.builder().build();
            } else if (position == PAGE_PROFILE) {
                fragment = ProfileFragment_.builder().build();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
