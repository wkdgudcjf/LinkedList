package com.ep.linkedlist.view.chat;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ep.linkedlist.R;
import com.ep.linkedlist.model.ChatInfo;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jiwon on 2016-09-20.
 */
@EViewGroup(R.layout.item_chat_list)
public class ChatListItem extends FrameLayout {
    Context context;

    @ViewById CircleImageView itemChatListPhotoCircleImageView;

    @ViewById TextView itemChatListNameTextView;
    @ViewById TextView itemChatListUserCountTextView;
    @ViewById TextView itemChatListLastContentTextView;
    @ViewById TextView itemChatListTimeTextView;
    @ViewById RelativeLayout itemChatListNoReadCountRelativeLayout;
    @ViewById TextView itemChatListNoReadCountTextView;


    public ChatListItem(Context context) {
        super(context);
        this.context = context;
    }

    public void bind(ChatInfo chatInfo) {
        if (StringUtils.isNotBlank(chatInfo.getChatPhoto())) {
            Glide.with(context)
                    .load(chatInfo.getChatPhoto())
                    .into(itemChatListPhotoCircleImageView);
        }
        else {
            itemChatListPhotoCircleImageView.setImageResource(R.drawable.profile_default_image);
        }

        itemChatListNameTextView.setText(chatInfo.getChatName());
        itemChatListUserCountTextView.setText(String.format("%d:%d", chatInfo.getUserCount() / 2, chatInfo.getUserCount() / 2));
        itemChatListLastContentTextView.setText(chatInfo.getLastChatContent());
        itemChatListTimeTextView.setText(DateFormatUtils.format(chatInfo.getLastChatTimestamp(), "a hh:mm"));
        if(chatInfo.getNoReadCount() == 0) {
            itemChatListNoReadCountRelativeLayout.setVisibility(INVISIBLE);
        } else {
            itemChatListNoReadCountRelativeLayout.setVisibility(VISIBLE);
            itemChatListNoReadCountTextView.setText(Long.toString(chatInfo.getNoReadCount()));
        }
    }
}