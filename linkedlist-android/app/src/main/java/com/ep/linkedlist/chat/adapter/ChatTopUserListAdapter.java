package com.ep.linkedlist.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ep.linkedlist.model.meeting.MeetingJoinUser;
import com.ep.linkedlist.model.profile.Profile;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiwon on 2017-01-21.
 */
@EBean
public class ChatTopUserListAdapter extends RecyclerView.Adapter<ChatTopUserListAdapter.ChatTopUserListItemViewHoler> {
    private final static String TAG = ChatTopUserListAdapter.class.getName();

    @RootContext
    Context context;

    private OnItemClickListener onItemClickListener;

    List<Profile> profileList = new ArrayList<Profile>();

    public void setMeetingJoinUserList(List<Profile> profileList) {
        this.profileList.clear();
        this.profileList.addAll(profileList);
        for(Profile profile : profileList) {
            Log.d(TAG, "setMeetingJoinUserList : " + profile);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ChatTopUserListItemViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatTopUserListItem chatTopUserListItem = ChatTopUserListItem_.build(context);
        return new ChatTopUserListItemViewHoler(chatTopUserListItem, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ChatTopUserListItemViewHoler holder, int position) {
        holder.getChatTopUserListItem().bind(profileList.get(position));
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public final static class ChatTopUserListItemViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ChatTopUserListItem chatTopUserListItem;
        private OnItemClickListener onItemClickListener;

        public ChatTopUserListItemViewHoler(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.chatTopUserListItem = (ChatTopUserListItem) itemView;
            this.onItemClickListener = onItemClickListener;
            chatTopUserListItem.setOnClickListener(this);
        }

        public ChatTopUserListItem getChatTopUserListItem() {
            return chatTopUserListItem;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(chatTopUserListItem.getProfile());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Profile profile);
    }
}
