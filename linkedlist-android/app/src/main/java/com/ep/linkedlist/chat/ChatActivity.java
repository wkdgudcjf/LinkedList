package com.ep.linkedlist.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ep.linkedlist.R;
import com.ep.linkedlist.bo.chat.ChatBO;
import com.ep.linkedlist.chat.adapter.ChatFirebaseAdapter;
import com.ep.linkedlist.chat.adapter.ChatTopUserListAdapter;
import com.ep.linkedlist.chat.adapter.ClickListenerChatFirebase;
import com.ep.linkedlist.chat.model.ChatModel;
import com.ep.linkedlist.model.ChatInfo;
import com.ep.linkedlist.model.meeting.MeetingInfo;
import com.ep.linkedlist.model.profile.Profile;
import com.ep.linkedlist.service.rest.PushRestService;
import com.ep.linkedlist.util.CalendarFactory;
import com.ep.linkedlist.util.DefaultValueEventListener;
import com.ep.linkedlist.util.FirebaseUtils;
import com.ep.linkedlist.view.profile.ProfileOtherActivity_;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_chat)
public class ChatActivity extends AppCompatActivity implements ClickListenerChatFirebase {
    private final static String TAG = ChatActivity.class.getName();

    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;

    @RestService
    PushRestService pushRestService;
    String chatMessageReference;
    String chatInfoReference;
    String meetingInfoReference;
    ChatInfo chatInfo;
    MeetingInfo meetingInfo;
    final Map<String, Profile> meetingJoinUserProfileMap = new HashMap<String, Profile>();

    private FirebaseUser firebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private ChatFirebaseAdapter firebaseAdapter;
    @ViewById
    TextView chatNameTextView;

    @ViewById
    RecyclerView messageRecyclerView;

    @ViewById
    EditText chatEditText;

    @Bean
    ChatBO chatBO;

    @ViewById
    RecyclerView chatTopUserListRecyclerView;

    @ViewById
    ImageView chatMenuImageView;

    @Bean
    ChatTopUserListAdapter chatTopUserListAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private PopupMenu popupMenu;

    @AfterViews
    void afterViews() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        chatTopUserListRecyclerView.setAdapter(chatTopUserListAdapter);
        chatTopUserListAdapter.setOnItemClickListener(onItemClickListener);
        createPopupMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String chatId = getIntent().getStringExtra("chatId");
        chatMessageReference = "chat/message/" + chatId;
        chatInfoReference = "users/chat/" + firebaseUser.getUid() + "/" + chatId;
        meetingInfoReference = "meeting/info/" + chatId;
        mFirebaseDatabaseReference.child(chatInfoReference).addValueEventListener(chatInfoListener);
        mFirebaseDatabaseReference.child(meetingInfoReference).addValueEventListener(meetingInfoListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseDatabaseReference.child(chatInfoReference).removeEventListener(chatInfoListener);
        mFirebaseDatabaseReference.child(meetingInfoReference).removeEventListener(meetingInfoListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAdapter.cleanup();
    }

    private DefaultValueEventListener chatInfoListener = new DefaultValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            chatInfo = dataSnapshot.getValue(ChatInfo.class);
            if (chatInfo == null) {
                return;
            }
            chatInfo.setId(dataSnapshot.getKey());
            chatNameTextView.setText(chatInfo.getChatName());
        }
    };

    private DefaultValueEventListener meetingInfoListener = new DefaultValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            meetingInfo = dataSnapshot.getValue(MeetingInfo.class);
            if (meetingInfo == null) {
                return;
            }
            meetingInfo.setId(dataSnapshot.getKey());
            initMeetingJoinUserProfileMap();
        }
    };

    private void initMeetingJoinUserProfileMap(){
        final int meetingJoinUserCount = meetingInfo.getUsers().values().size();
        for(String uid : meetingInfo.getUsers().keySet()) {
            mFirebaseDatabaseReference.child("users/profile/" + uid).addListenerForSingleValueEvent(new DefaultValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    profile.setUid(dataSnapshot.getKey());
                    meetingJoinUserProfileMap.put(profile.getUid(), profile);
                    if(meetingJoinUserProfileMap.size() == meetingJoinUserCount) {
                        updateTopChatUserList();
                        requestMessage();
                    }
                }
            });
        }
    }

    public void updateTopChatUserList() {
        List<Profile> meetingJoinUserProfileList = new ArrayList<Profile>(meetingJoinUserProfileMap.values());
        chatTopUserListAdapter.setMeetingJoinUserList(meetingJoinUserProfileList);
    }

    @Override
    public void clickUserPhoto(String uid) {
        showUserProfile(uid);
    }

    /**
     * Ler collections chatmodel Firebase
     */
    private void requestMessage() {
        // 참여한 이후 대화
        Query query = mFirebaseDatabaseReference.child(chatMessageReference).orderByChild("timestamp").startAt(meetingInfo.getUsers().get(firebaseUser.getUid()).getTimestamp());
        firebaseAdapter = new ChatFirebaseAdapter(query, this, meetingJoinUserProfileMap);
        firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    messageRecyclerView.scrollToPosition(positionStart);
                }
                mFirebaseDatabaseReference.child("users/chat/" + firebaseUser.getUid() + "/" + chatInfo.getId() + "/noReadCount").setValue(0);
            }
        });
        messageRecyclerView.setLayoutManager(mLinearLayoutManager);
        messageRecyclerView.setAdapter(firebaseAdapter);
        messageRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        Log.d(TAG,"onInterceptTouchEvent");
                        InputMethodManager imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(chatEditText.getWindowToken(), 0);
                        return false;
                    }
                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                        Log.d(TAG, "onTouchEvent");
                    }
                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                        Log.d(TAG, "onRequestDisallowInterceptTouchEvent");
                    }
                });
    }
    /**
     * 다른 사용자 프로필 확인
     * @param uid
     */
    void showUserProfile(String uid) {
        mFirebaseDatabaseReference.child("users/profile/" + uid).addListenerForSingleValueEvent(new DefaultValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                if (profile == null) {
                    Toast.makeText(ChatActivity.this, "정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String profileString = new Gson().toJson(profile);
                Intent intent = new Intent(ChatActivity.this, ProfileOtherActivity_.class);
                intent.putExtra("profile", profileString);
                startActivity(intent);
            }
        });
    }

    @Click(R.id.chatSendButton)
    void clickSendMessage() {
        if (StringUtils.isBlank(chatEditText.getText().toString())) {
            return;
        }
        ChatModel chatModel = new ChatModel(firebaseUser.getUid(), chatEditText.getText().toString(), CalendarFactory.createInstance().getTimeInMillis());
        chatBO.updateChatInfo(meetingInfo, chatModel.getMessage(), chatModel.getTimestamp());
        mFirebaseDatabaseReference.child(chatMessageReference).push().setValue(chatModel);
        chatEditText.setText(null);
        pushMeetingMessage();
    }

    @Background
    void pushMeetingMessage() {
        try {
            pushRestService.pushMeetingMessage(meetingInfo.getId());
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    @Click(R.id.chatBackImageView)
    void clickBackButton() {
        finish();
    }

    @Click(R.id.chatMenuImageView)
    void clickMenuButton() {
        popupMenu.show();
    }

    ChatTopUserListAdapter.OnItemClickListener onItemClickListener = new ChatTopUserListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Profile profile) {
            showUserProfile(profile.getUid());
        }
    };

    void createPopupMenu(){
        popupMenu = new PopupMenu(this, chatMenuImageView);
        getMenuInflater().inflate(R.menu.menu_chat, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.chatExitButton :
                        ChatActivity.this.clickChatExitButton();
                        break;
                }
                return false;
            }
        });
    }

    void clickChatExitButton() {
        mFirebaseDatabaseReference.child(chatInfoReference).removeEventListener(chatInfoListener);
        mFirebaseDatabaseReference.child(meetingInfoReference).removeEventListener(meetingInfoListener);

        chatBO.exitChat(chatInfo, meetingInfo, new FirebaseUtils.FirebaseResult() {
            @Override
            public void onSuccess() {
                finish();
            }

            @Override
            public void onError() {
                Toast.makeText(ChatActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
