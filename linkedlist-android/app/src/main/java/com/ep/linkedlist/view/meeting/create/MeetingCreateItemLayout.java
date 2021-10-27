package com.ep.linkedlist.view.meeting.create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ep.linkedlist.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by jiwon on 2016-10-22.
 */
@EViewGroup(R.layout.item_meeting_info)
public class MeetingCreateItemLayout extends LinearLayout {
    private static final String TAG = MeetingCreateItemLayout.class.getName();

    private static final int COLIMN_COUNT = 3;

    Context context;

    @ViewById
    TextView meetingInfoRegionTextView;

    @ViewById
    TableLayout meetingInfoRegionTableLayout;

    private String selectedInfo;

    public MeetingCreateItemLayout(Context context) {
        super(context);
        this.context = context;
    }

    public void bind(String infoName, List<String> infoList){
        meetingInfoRegionTextView.setText(infoName);
        updateTable(infoList, meetingInfoRegionTableLayout);
    }

    public void updateTable(List<String> infoList, TableLayout view){
        view.removeAllViews();

        int columnCount = 0;
        TableRow row = null;
        TextView column = null;
        for (String info : infoList) {
            if (columnCount == 0) {
                row = (TableRow) LayoutInflater.from(context).inflate(R.layout.item_meeting_info_table_row, null);
                view.addView(row);
            }
            column = (TextView) LayoutInflater.from(context).inflate(R.layout.item_meeting_info_table_item, null);
            column.setText(info);
            row.addView(column);
            column.setOnClickListener(itemClickLisnter);
            columnCount = (columnCount + 1) % COLIMN_COUNT;
        }
    }

    public void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private boolean isCollapse = true;
    @Click(R.id.meetingInfoRegionTextView)
    void onTextViewClick(){
        if(isCollapse){
            expand(meetingInfoRegionTableLayout);
            isCollapse = false;
        } else {
            collapse(meetingInfoRegionTableLayout);
            isCollapse = true;
        }
    }

    View.OnClickListener itemClickLisnter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TableLayout tableLayout = MeetingCreateItemLayout.this.meetingInfoRegionTableLayout;
            int tableRowCount = tableLayout.getChildCount();
            for (int rowIndex = 0; rowIndex < tableRowCount; rowIndex++) {
                TableRow tableRow = (TableRow) tableLayout.getChildAt(rowIndex);
                int tableColumnCount = tableRow.getChildCount();
                for (int columnIndex = 0; columnIndex < tableColumnCount; columnIndex++) {
                    TextView textView = (TextView)tableRow.getChildAt(columnIndex);
                    textView.setSelected(false);
                }
            }
            v.setSelected(true);
            selectedInfo = ((TextView)v).getText().toString();
            if(onItemClickListener!=null){
                onItemClickListener.onClick(v);
            }
        }
    };

    public String getSelectedInfo(){
        return selectedInfo;
    }

    private OnClickListener onItemClickListener;
    public void setOnItemClickListener(View.OnClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
