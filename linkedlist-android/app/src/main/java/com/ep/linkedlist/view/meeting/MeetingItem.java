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

        if(meetingInfo.getDetailInterest().equals("수영")) backg.setBackgroundResource(R.drawable.meeting_swimming);
        else if(meetingInfo.getDetailInterest().equals("필라테스")) backg.setBackgroundResource(R.drawable.meeting_pilates);
        else if(meetingInfo.getDetailInterest().equals("요가")) backg.setBackgroundResource(R.drawable.meeting_yoga);
        else if(meetingInfo.getDetailInterest().equals("골프")) backg.setBackgroundResource(R.drawable.meeting_golf);
        else if(meetingInfo.getDetailInterest().equals("자전거")) backg.setBackgroundResource(R.drawable.meeting_bicycle);
        else if(meetingInfo.getDetailInterest().equals("농구")) backg.setBackgroundResource(R.drawable.meeting_basketball);
        else if(meetingInfo.getDetailInterest().equals("클라이밍")) backg.setBackgroundResource(R.drawable.meeting_climming);
        else if(meetingInfo.getDetailInterest().equals("헬스")) backg.setBackgroundResource(R.drawable.meeting_health);
        else if(meetingInfo.getDetailInterest().equals("복싱")) backg.setBackgroundResource(R.drawable.meeting_boxing);
        else if(meetingInfo.getDetailInterest().equals("볼링")) backg.setBackgroundResource(R.drawable.meeting_bowling);
        else if(meetingInfo.getDetailInterest().equals("탁구")) backg.setBackgroundResource(R.drawable.meeting_tabletennis);

        else if(meetingInfo.getDetailInterest().equals("캠핑")) backg.setBackgroundResource(R.drawable.meeting_camping);
        else if(meetingInfo.getDetailInterest().equals("낚시")) backg.setBackgroundResource(R.drawable.meeting_fishing);
        else if(meetingInfo.getDetailInterest().equals("국내여행")) backg.setBackgroundResource(R.drawable.meeting_domestictravel);
        else if(meetingInfo.getDetailInterest().equals("등산")) backg.setBackgroundResource(R.drawable.meeting_mountainclimbing);
        else if(meetingInfo.getDetailInterest().equals("산책,트래킹")) backg.setBackgroundResource(R.drawable.meeting_trekking);
        else if(meetingInfo.getDetailInterest().equals("패러글라이딩")) backg.setBackgroundResource(R.drawable.meeting_paragliding);
        else if(meetingInfo.getDetailInterest().equals("해외여행")) backg.setBackgroundResource(R.drawable.meeting_overseastravel);

        else if(meetingInfo.getDetailInterest().equals("인디음악")) backg.setBackgroundResource(R.drawable.meeting_indimusic);
        else if(meetingInfo.getDetailInterest().equals("밴드")) backg.setBackgroundResource(R.drawable.meeting_band);
        else if(meetingInfo.getDetailInterest().equals("작곡")) backg.setBackgroundResource(R.drawable.meeting_composition);
        else if(meetingInfo.getDetailInterest().equals("일렉")) backg.setBackgroundResource(R.drawable.meeting_electronicmusic);
        else if(meetingInfo.getDetailInterest().equals("드럼")) backg.setBackgroundResource(R.drawable.meeting_drum);
        else if(meetingInfo.getDetailInterest().equals("바이올린")) backg.setBackgroundResource(R.drawable.meeting_violin);
        else if(meetingInfo.getDetailInterest().equals("재즈")) backg.setBackgroundResource(R.drawable.meeting_jazz);
        else if(meetingInfo.getDetailInterest().equals("기타")) backg.setBackgroundResource(R.drawable.meeting_guitar);
        else if(meetingInfo.getDetailInterest().equals("노래")) backg.setBackgroundResource(R.drawable.meeting_singing);
        else if(meetingInfo.getDetailInterest().equals("피아노")) backg.setBackgroundResource(R.drawable.meeting_piano);
        else if(meetingInfo.getDetailInterest().equals("힙합")) backg.setBackgroundResource(R.drawable.meeting_hiphop);
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