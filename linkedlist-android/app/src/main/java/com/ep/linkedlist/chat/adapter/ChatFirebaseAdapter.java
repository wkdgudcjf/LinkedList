package com.ep.linkedlist.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ep.linkedlist.R;
import com.ep.linkedlist.chat.model.ChatModel;
import com.ep.linkedlist.model.profile.Profile;
import com.ep.linkedlist.util.CalendarFactory;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by Alessandro Barreto on 23/06/2016.
 */
public class ChatFirebaseAdapter extends FirebaseRecyclerAdapter<ChatModel,ChatFirebaseAdapter.MyChatViewHolder> {

    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;
    private static final int RIGHT_MSG_WITH_USER = 2;
    private static final int LEFT_MSG_WITH_USER = 3;
    private static final int RIGHT_MSG_WITH_DATE = 4;
    private static final int LEFT_MSG_WITH_DATE = 5;

    private ClickListenerChatFirebase mClickListenerChatFirebase;

    private FirebaseUser firebaseUser;
    private Map<String, Profile> meetingJoinUserProfileMap;



    public ChatFirebaseAdapter(Query ref, ClickListenerChatFirebase mClickListenerChatFirebase, Map<String, Profile> meetingJoinUserProfileMap) {
        super(ChatModel.class, R.layout.item_message_left, MyChatViewHolder.class, ref);
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.mClickListenerChatFirebase = mClickListenerChatFirebase;
        this.meetingJoinUserProfileMap = meetingJoinUserProfileMap;
    }

    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new MyChatViewHolder(view);
        } else if (viewType == LEFT_MSG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new MyChatViewHolder(view);
        } else if (viewType == RIGHT_MSG_WITH_USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right_with_user, parent, false);
            return new MyChatViewHolder(view);
        } else if (viewType == LEFT_MSG_WITH_USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left_with_user, parent, false);
            return new MyChatViewHolder(view);
        }else if (viewType == RIGHT_MSG_WITH_DATE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right_with_date, parent, false);
            return new MyChatViewHolder(view);
        } else if (viewType == LEFT_MSG_WITH_DATE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left_with_date, parent, false);
            return new MyChatViewHolder(view);
        } else {
            return onCreateViewHolder(parent, LEFT_MSG);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel model = getItem(position);
        if(position == 0) {
            if (model.getUid().equals(firebaseUser.getUid())) {
                return RIGHT_MSG_WITH_DATE;
            } else {
                return LEFT_MSG_WITH_DATE;
            }
        } else if(isNotEqualDay(model.getTimestamp(), getItem(position - 1).getTimestamp())) {
            if (model.getUid().equals(firebaseUser.getUid())) {
                return RIGHT_MSG_WITH_DATE;
            } else {
                return LEFT_MSG_WITH_DATE;
            }
        } else if (isNotEqualUser(model, getItem(position - 1))) {
            if (model.getUid().equals(firebaseUser.getUid())) {
                return RIGHT_MSG_WITH_USER;
            } else {
                return LEFT_MSG_WITH_USER;
            }
        } else if(isNotEqualMin(model.getTimestamp(), getItem(position - 1).getTimestamp())) {
            if (model.getUid().equals(firebaseUser.getUid())) {
                return RIGHT_MSG_WITH_USER;
            } else {
                return LEFT_MSG_WITH_USER;
            }
        } else {
            if (model.getUid().equals(firebaseUser.getUid())) {
                return RIGHT_MSG;
            } else {
                return LEFT_MSG;
            }
        }
    }

    @Override
    protected void populateViewHolder(MyChatViewHolder viewHolder, ChatModel model, int position) {
        Profile profile = meetingJoinUserProfileMap.get(model.getUid());
        if(profile==null) {
            viewHolder.setPhoto("");
            viewHolder.setNickname("우 연");
        } else {
            viewHolder.setPhoto(profile.getPhotoURI());
            viewHolder.setNickname(profile.getNickname());
        }
        viewHolder.setDate(model.getTimestamp());
        viewHolder.setTime(model.getTimestamp());
        viewHolder.setMessage(model.getMessage());
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView chatMessageDateTextView;
        ImageView chatMessagePhotoImageView;
        TextView chatMessageNicknameTextView;
        RelativeLayout layout;
        TextView chatMessageTimeTextView;
        TextView chatMessageContentTextView;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            chatMessageDateTextView = (TextView) itemView.findViewById(R.id.chatMessageDateTextView);
            chatMessagePhotoImageView = (ImageView) itemView.findViewById(R.id.chatMessagePhotoImageView);
            chatMessageNicknameTextView = (TextView) itemView.findViewById(R.id.chatMessageNicknameTextView);
            chatMessageTimeTextView = (TextView) itemView.findViewById(R.id.chatMessageTimeTextView);
            chatMessageContentTextView = (TextView) itemView.findViewById(R.id.chatMessageContentTextView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ChatModel model = getItem(position);
            switch (view.getId())
            {
                case R.id.chatMessagePhotoImageView:
                    mClickListenerChatFirebase.clickUserPhoto(model.getUid());
                    break;
            }
        }

        public void setDate(long timestamp) {
            if (chatMessageDateTextView == null)return;
            Calendar calendar = CalendarFactory.createInstance(timestamp);
            chatMessageDateTextView.setText(DateFormatUtils.format(calendar, "yyyy.MM.dd"));
        }

        public void setPhoto(String photoUrl){
            if (chatMessagePhotoImageView == null)return;
            if (StringUtils.isNotBlank(photoUrl)) {
                Glide.with(chatMessagePhotoImageView.getContext())
                        .load(photoUrl).centerCrop()
                        .transform(new CircleTransform(chatMessagePhotoImageView.getContext()))
                        .override(40,40)
                        .into(chatMessagePhotoImageView);
            }
           else {
                chatMessagePhotoImageView.setImageResource(R.drawable.profile_default_image);
            }
            chatMessagePhotoImageView.setOnClickListener(this);
        }

        public void setNickname(String nickname){
            if (chatMessageNicknameTextView == null)return;
            chatMessageNicknameTextView.setText(nickname);
        }

        public void setTime(long timestamp){
            if (chatMessageTimeTextView == null)return;
            Calendar calendar = CalendarFactory.createInstance(timestamp);
            chatMessageTimeTextView.setText(DateFormatUtils.format(calendar, "HH:mm"));
        }

        public void setMessage(String message){
            if (chatMessageContentTextView == null)return;
            chatMessageContentTextView.setText(message);
        }

    }

    private boolean isNotEqualDay(long timestamp1, long timestamp2){
        Calendar calendar1 = CalendarFactory.createInstance(timestamp1);
        Calendar calendar2 = CalendarFactory.createInstance(timestamp2);

        return calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR)
                || calendar1.get(Calendar.DAY_OF_YEAR) != calendar2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isNotEqualMin(long timestamp1, long timestamp2) {
        Calendar calendar1 = CalendarFactory.createInstance(timestamp1);
        Calendar calendar2 = CalendarFactory.createInstance(timestamp2);

        return calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR)
                || calendar1.get(Calendar.DAY_OF_YEAR) != calendar2.get(Calendar.DAY_OF_YEAR)
                || calendar1.get(Calendar.HOUR_OF_DAY) != calendar2.get(Calendar.HOUR_OF_DAY)
                || calendar1.get(Calendar.MINUTE) != calendar2.get(Calendar.MINUTE);
    }

    private boolean isNotEqualUser(ChatModel chatModel1, ChatModel chatModel2) {
        return StringUtils.equals(chatModel1.getUid(), chatModel2.getUid()) == false;
    }
}
