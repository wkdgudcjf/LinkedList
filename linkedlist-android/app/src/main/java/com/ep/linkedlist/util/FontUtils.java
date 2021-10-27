package com.ep.linkedlist.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by jiwon on 2017-02-16.
 */

public class FontUtils {
    public enum NotoSansType {
        THIN, LIGHT, DEMI_LIGHT, REGULAR, MEDIUM, BOLD, BLACK;
    }

    private static Typeface NOTO_SANS_THIN;
    private static Typeface NOTO_SANS_LIGHT;
    private static Typeface NOTO_SANS_DEMI_LIGHT;
    private static Typeface NOTO_SANS_REGULAR;
    private static Typeface NOTO_SANS_MEDIUM;
    private static Typeface NOTO_SANS_BOLD;
    private static Typeface NOTO_SANS_BLACK;

    public static Typeface getNotoSansTypeface(Context context, NotoSansType notoSansType) {
        if(NotoSansType.THIN == notoSansType) {
            if(NOTO_SANS_THIN == null) {
                NOTO_SANS_THIN = Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Thin-Hestia.otf");
            }
            return NOTO_SANS_THIN;
        } else if(NotoSansType.LIGHT == notoSansType) {
            if(NOTO_SANS_LIGHT == null) {
                NOTO_SANS_LIGHT = Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Light-Hestia.otf");
            }
            return NOTO_SANS_LIGHT;
        } else if(NotoSansType.DEMI_LIGHT == notoSansType) {
            if(NOTO_SANS_DEMI_LIGHT == null) {
                NOTO_SANS_DEMI_LIGHT = Typeface.createFromAsset(context.getAssets(), "NotoSansKR-DemiLight-Hestia.otf");
            }
            return NOTO_SANS_DEMI_LIGHT;
        } else if(NotoSansType.REGULAR == notoSansType) {
            if(NOTO_SANS_REGULAR == null) {
                NOTO_SANS_REGULAR = Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Regular-Hestia.otf");
            }
            return NOTO_SANS_REGULAR;
        } else if(NotoSansType.MEDIUM == notoSansType) {
            if(NOTO_SANS_MEDIUM == null) {
                NOTO_SANS_MEDIUM = Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Medium-Hestia.otf");
            }
            return NOTO_SANS_MEDIUM;
        } else if(NotoSansType.BOLD == notoSansType) {
            if(NOTO_SANS_BOLD == null) {
                NOTO_SANS_BOLD = Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Bold-Hestia.otf");
            }
            return NOTO_SANS_BOLD;
        } else if(NotoSansType.BLACK == notoSansType) {
            if(NOTO_SANS_BLACK == null) {
                NOTO_SANS_BLACK = Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Black-Hestia.otf");
            }
            return NOTO_SANS_BLACK;
        } else {
            return getNotoSansTypeface(context, NotoSansType.REGULAR);
        }
    }
}
