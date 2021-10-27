package com.ep.linkedlist.chat.adapter;

import android.content.Context;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ep.linkedlist.R;
import com.ep.linkedlist.model.profile.Profile;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ep.linkedlist.R.id.chatMessagePhotoImageView;

/**
 * Created by jiwon on 2016-09-20.
 */
@EViewGroup(R.layout.item_chat_top_user_list)
public class ChatTopUserListItem extends RelativeLayout {

    @ViewById
    CircleImageView chatTopUserListItem;

    private Profile profile;

    public ChatTopUserListItem(Context context) {
        super(context);
    }

    public void bind(Profile profile) {
        this.profile = profile;
        if (StringUtils.isNotBlank(profile.getPhotoURI())) {
            Glide.with(this.getContext())
                    .load(profile.getPhotoURI())
                    .into(chatTopUserListItem);
        }
        else {
            chatTopUserListItem.setImageResource(R.drawable.profile_default_image);
        }
    }

    public Profile getProfile() {
        return profile;
    }
}