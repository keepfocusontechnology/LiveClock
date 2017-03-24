package com.mr_qual1ty.clock.timerclock;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by mr_qual1ty on 2017/3/23.
 */

public class DensityUtils {


    public static float mDensity;
    public static int mScreenWidth;
    public static int mScreenHeigth;

    private static Context mAppContext;

    private DensityUtils() {

    }

    public static void setAppContext(Context context) {
        mAppContext = context;
        mDensity = mAppContext.getResources().getDisplayMetrics().density;
        mScreenWidth = mAppContext.getResources().getDisplayMetrics().widthPixels;
        mScreenHeigth = mAppContext.getResources().getDisplayMetrics().heightPixels;
        if (mScreenWidth > mScreenHeigth) {
            mScreenWidth = mAppContext.getResources().getDisplayMetrics().heightPixels;
            mScreenHeigth = mAppContext.getResources().getDisplayMetrics().widthPixels;
        }
    }

    /**
     * dp converter to px
     *
     * @param dpValue before computing dp
     * @return after computing px
     */
    public static int dip2px(float dpValue) {
        Log.e("tag", "mAppContext = " + mAppContext);
        float scale = mAppContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px converter to dp
     *
     * @param pxValue before computing px
     * @return after computing dp
     */
    public static int px2dip(float pxValue) {
        float scale = mAppContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px converter to sp, To ensure that the same text size
     *
     * @param pxValue before computing px
     * @return after computing sp
     */
    public static int px2sp(float pxValue) {
        float fontScale = mAppContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp converter to px, To ensure that the same text size
     *
     * @param spValue before computing sp
     * @return after computing px
     */
    public static int sp2px(float spValue) {
        float fontScale = mAppContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * Gets the screen width and height, the unit is px
     *
     * @return point.x : width ,point.y : height
     */
    public static Point getScreenMetrics() {
        DisplayMetrics dm = mAppContext.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);

    }

    /**
     * Get screen aspect ratio
     *
     * @return screen aspect ratio
     */
    public static float getScreenRate() {
        Point P = getScreenMetrics();
        float H = P.y;
        float W = P.x;
        return (H / W);
    }

}
