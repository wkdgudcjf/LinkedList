package com.ep.linkedlist.view.list;


import android.support.v4.app.Fragment;

import com.ep.linkedlist.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by jiwon on 2016-09-19.
 */
@EFragment(R.layout.activity_list_empty)
public class ListFragmentEmpty extends Fragment {
    private static final String tag = ListFragmentEmpty.class.getName();

    @AfterViews
    void updateViews() {

    }
}
