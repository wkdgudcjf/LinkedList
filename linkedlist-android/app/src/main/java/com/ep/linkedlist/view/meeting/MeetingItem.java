package com.ep.linkedlist.view.meeting;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ep.linkedlist.R;
import com.ep.linkedlist.model.meeting.MeetingInfo;
import com.ep.linkedlist.model.meeting.MeetingJoinUser;
import com.ep.linkedlist.model.profile.Gender;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jiwon on 2016-09-20.
 */
@EViewGroup(R.layout.item_meeting_list)
public class MeetingItem extends RelativeLayout {
    Context context;

    @ViewById
    TextView itemMeetingName;

    @ViewById
    TextView itemMeetingUserCount;

    @ViewById
    LinearLayout itemMeetingUserPhotos;

    @ViewById
    RelativeLayout backg;

    ArrayList<CircleImageView> personImageList = new ArrayList<CircleImageView>();

    public MeetingItem(Context context) {
        super(context);
        this.context = context;
    }

    public void bind(MeetingInfo meetingInfo) {
        itemMeetingName.setText(meetingInfo.getMeetingName());
        itemMeetingUserCount.setText(meetingInfo.getManCount() + meetingInfo.getWomanCount() + "/" + meetingInfo.getUserCount());

        ArrayList<CircleImageView> circleImageViews = new ArrayList<>();

        for (int i = 0; i < itemMeetingUserPhotos.getChildCount(); i++)
        {
            CircleImageView circleImageView = (CircleImageView) itemMeetingUserPhotos.getChildAt(i);

            if (i < itemMeetingUserPhotos.getChildCount() - meetingInfo.getUserCount())
            {
                circleImageView.setVisibility(INVISIBLE);
            } else {
                circleImageView.setVisibility(VISIBLE);
                circleImageViews.add(circleImageView);
            }
        }

        CircleImageView circleImageView_8a = (CircleImageView) itemMeetingUserPhotos.getChildAt(7);
        CircleImageView circleImageView_8b = (CircleImageView) itemMeetingUserPhotos.getChildAt(6);
        CircleImageView circleImageView_8c = (CircleImageView) itemMeetingUserPhotos.getChildAt(5);
        CircleImageView circleImageView_8d = (CircleImageView) itemMeetingUserPhotos.getChildAt(4);
        CircleImageView circleImageView_8e = (CircleImageView) itemMeetingUserPhotos.getChildAt(3);
        CircleImageView circleImageView_8f = (CircleImageView) itemMeetingUserPhotos.getChildAt(2);
        CircleImageView circleImageView_8g = (CircleImageView) itemMeetingUserPhotos.getChildAt(1);
        CircleImageView circleImageView_8h = (CircleImageView) itemMeetingUserPhotos.getChildAt(0);

        personImageList.add(circleImageView_8a);
        personImageList.add(circleImageView_8b);
        personImageList.add(circleImageView_8c);
        personImageList.add(circleImageView_8d);
        personImageList.add(circleImageView_8e);
        personImageList.add(circleImageView_8f);
        personImageList.add(circleImageView_8g);
        personImageList.add(circleImageView_8h);

        if(meetingInfo.getUserCount() == 4)
        {
            circleImageView_8a.setBackgroundResource(R.drawable.boy);
            circleImageView_8b.setBackgroundResource(R.drawable.boy);
            circleImageView_8c.setBackgroundResource(R.drawable.girl);
            circleImageView_8d.setBackgroundResource(R.drawable.girl);
        }
        else if(meetingInfo.getUserCount() == 6)
        {
            circleImageView_8a.setBackgroundResource(R.drawable.boy);
            circleImageView_8b.setBackgroundResource(R.drawable.boy);
            circleImageView_8c.setBackgroundResource(R.drawable.boy);
            circleImageView_8d.setBackgroundResource(R.drawable.girl);
            circleImageView_8e.setBackgroundResource(R.drawable.girl);
            circleImageView_8f.setBackgroundResource(R.drawable.girl);
        }
        else if(meetingInfo.getUserCount() == 8)
        {
            circleImageView_8a.setBackgroundResource(R.drawable.boy);
            circleImageView_8b.setBackgroundResource(R.drawable.boy);
            circleImageView_8c.setBackgroundResource(R.drawable.boy);
            circleImageView_8d.setBackgroundResource(R.drawable.boy);
            circleImageView_8e.setBackgroundResource(R.drawable.girl);
            circleImageView_8f.setBackgroundResource(R.drawable.girl);
            circleImageView_8g.setBackgroundResource(R.drawable.girl);
            circleImageView_8h.setBackgroundResource(R.drawable.girl);
        }

        int mancount=0;
        int womancount=(int)meetingInfo.getUserCount()/2;

        Iterator<String> iterator = meetingInfo.getUsers().keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            MeetingJoinUser user = meetingInfo.getUsers().get(key);
            if(user.getGender().equals(Gender.MAN.getValue())) {
                if (StringUtils.isNotBlank(user.getPhotoURL())) {
                    Glide.with(getContext())
                            .load(user.getPhotoURL())
                            .into(personImageList.get(mancount++));
                }
                else {
                    personImageList.get(mancount++).setImageResource(R.drawable.profile_default_image);
                }

            }
            else {
                if (StringUtils.isNotBlank(user.getPhotoURL())) {
                    Glide.with(getContext())
                            .load(user.getPhotoURL())
                            .into(personImageList.get(mancount++));
                }
                else {
                    personImageList.get(womancount++).setImageResource(R.drawable.profile_default_image);
                }
            }
        }

        if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_swimming);
        else if(meetingInfo.getDetailInterest().equals("????????????")) backg.setBackgroundResource(R.drawable.meeting_pilates);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_yoga);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_golf);
        else if(meetingInfo.getDetailInterest().equals("?????????")) backg.setBackgroundResource(R.drawable.meeting_bicycle);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_basketball);
        else if(meetingInfo.getDetailInterest().equals("????????????")) backg.setBackgroundResource(R.drawable.meeting_climming);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_health);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_boxing);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_bowling);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_tabletennis);

        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_camping);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_fishing);
        else if(meetingInfo.getDetailInterest().equals("????????????")) backg.setBackgroundResource(R.drawable.meeting_domestictravel);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_mountainclimbing);
        else if(meetingInfo.getDetailInterest().equals("??????,?????????")) backg.setBackgroundResource(R.drawable.meeting_trekking);
        else if(meetingInfo.getDetailInterest().equals("??????????????????")) backg.setBackgroundResource(R.drawable.meeting_paragliding);
        else if(meetingInfo.getDetailInterest().equals("????????????")) backg.setBackgroundResource(R.drawable.meeting_overseastravel);

        else if(meetingInfo.getDetailInterest().equals("????????????")) backg.setBackgroundResource(R.drawable.meeting_indimusic);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_band);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_composition);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_electronicmusic);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_drum);
        else if(meetingInfo.getDetailInterest().equals("????????????")) backg.setBackgroundResource(R.drawable.meeting_violin);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_jazz);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_guitar);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_singing);
        else if(meetingInfo.getDetailInterest().equals("?????????")) backg.setBackgroundResource(R.drawable.meeting_piano);
        else if(meetingInfo.getDetailInterest().equals("??????")) backg.setBackgroundResource(R.drawable.meeting_hiphop);
        else
        {
            // except process
        }

//        int index = 0;
//        if (meetingInfo.getUsers() == null) return;
//        for (Map.Entry<String, MeetingJoinUser> meetingJoinUserEntry : meetingInfo.getUsers().entrySet()) {
//            Glide.with(context)
//                    .load(meetingJoinUserEntry.getValue().getPhotoUrl())
//                    .into(circleImageViews.get(index));
//            index++;
//        }
    }
}