package com.ep.linkedlist.view.chat;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.ep.linkedlist.R;
import com.ep.linkedlist.bo.chat.ChatBO;
import com.ep.linkedlist.chat.ChatActivity_;
import com.ep.linkedlist.model.ChatInfo;
import com.ep.linkedlist.util.DefaultValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jiwon on 2016-09-19.
 */
@EFragment(R.layout.activity_chat_list)
public class ChatFragment extends Fragment {

    @Bean
    ChatBO chatBO;

    @Bean
    ChatListAdapter chatListAdapter;

    FirebaseUser firebaseUser;

    @ViewById
    ListView chatListView;

    @ViewById
    ImageView chatListEmptyImageView;

    @AfterViews
    void afterViews(){
        chatListView.setAdapter(chatListAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("users/chat/"+ firebaseUser.getUid()).orderByChild("lastChatTimestamp").addValueEventListener(defaultValueEventListener);
        chatListAdapter.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference("users/chat/"+ firebaseUser.getUid()).orderByChild("lastChatTimestamp").removeEventListener(defaultValueEventListener);
        chatListAdapter.unregisterDataSetObserver(dataSetObserver);
    }

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            if(chatListAdapter.getCount() > 0) {
                chatListEmptyImageView.setVisibility(View.GONE);
                chatListView.setVisibility(View.VISIBLE);
            } else {
                chatListEmptyImageView.setVisibility(View.VISIBLE);
                chatListView.setVisibility(View.GONE);
            }
        }
    };

    private DefaultValueEventListener defaultValueEventListener = new DefaultValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<ChatInfo> chatInfoList = new ArrayList<>();
            for (DataSnapshot children : dataSnapshot.getChildren()) {
                String key = children.getKey();
                ChatInfo chatInfo = children.getValue(ChatInfo.class);
                chatInfo.setId(key);
                chatInfoList.add(chatInfo);
            }
            Collections.reverse(chatInfoList);
            chatListAdapter.setChatInfoList(chatInfoList);
        }
    };

    @ItemClick(R.id.chatListView)
    void chatItemClicked(ChatInfo chatInfo) {
        Intent intent = new Intent(getContext(), ChatActivity_.class);
        intent.putExtra("chatId", chatInfo.getId());
        startActivity(intent);
    }
}
