package com.ep.linkedlist.view.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ep.linkedlist.R;
import com.ep.linkedlist.util.FontUtils;

/**
 * Created by jiwon on 2017-02-12.
 */

public class LinkedListTextView extends TextView {

        public LinkedListTextView(Context context) {
            super(context);
            setType(context);
        }

        public LinkedListTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setType(context, attrs);
        }

        public LinkedListTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setType(context, attrs);
        }

        private void setType(Context context) {
            setIncludeFontPadding(false);
            this.setTypeface(FontUtils.getNotoSansTypeface(context, FontUtils.NotoSansType.REGULAR));
        }

        private void setType(Context context, AttributeSet attrs) {
            setIncludeFontPadding(false);
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NotoTextView, 0, 0);

            int fontWeight = typedArray.getInteger(R.styleable.NotoTextView_fontWeight, 3);
            switch (fontWeight) {
                case 0:
                    this.setTypeface(FontUtils.getNotoSansTypeface(context, FontUtils.NotoSansType.THIN));
                    break;
                case 1:
                    this.setTypeface(FontUtils.getNotoSansTypeface(context, FontUtils.NotoSansType.LIGHT));
                    break;
                case 2:
                    this.setTypeface(FontUtils.getNotoSansTypeface(context, FontUtils.NotoSansType.DEMI_LIGHT));
                    break;
                case 3:
                    this.setTypeface(FontUtils.getNotoSansTypeface(context, FontUtils.NotoSansType.REGULAR));
                    break;
                case 4:
                    this.setTypeface(FontUtils.getNotoSansTypeface(context, FontUtils.NotoSansType.MEDIUM));
                    break;
                case 5:
                    this.setTypeface(FontUtils.getNotoSansTypeface(context, FontUtils.NotoSansType.BOLD));
                    break;
                case 6:
                    this.setTypeface(FontUtils.getNotoSansTypeface(context, FontUtils.NotoSansType.BLACK));
                    break;
            }
        }
}
