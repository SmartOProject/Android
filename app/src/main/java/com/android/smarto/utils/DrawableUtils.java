package com.android.smarto.utils;

import android.graphics.drawable.Drawable;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

public class DrawableUtils {
    private static final int[] EMPTY_STATE = new int[] {};

    public static void clearState(Drawable drawable) {
        if (drawable != null) {
            drawable.setState(EMPTY_STATE);
        }
    }
}
