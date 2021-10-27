package com.ep.linkedlist.view.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ep.linkedlist.model.ChatInfo;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiwon on 2016-09-20.
 */
@EBean
public class ChatListAdapter extends BaseAdapter {
    private static final String TAG = ChatListAdapter.class.getName();

    @RootContext
    Context context;

    List<ChatInfo> chatInfoList = new ArrayList<>();

    @Override
    public int getCount() {
        return chatInfoList.size();
    }

    @Override
    public ChatInfo getItem(int position) {
        return chatInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatListItem chatListItem;
        if (convertView == null) {
            chatListItem = ChatListItem_.build(context);
        } else {
            chatListItem = (ChatListItem) convertView;
        }

        ChatInfo chatInfo = getItem(position);
        chatListItem.bind(chatInfo);

        return chatListItem;
    }

    public void setChatInfoList(List<ChatInfo> chatInfoList) {
        this.chatInfoList.clear();
        this.chatInfoList.addAll(chatInfoList);
        notifyDataSetChanged();
    }
}