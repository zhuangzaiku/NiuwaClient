package com.bru.toolkit.utils.screenrotate;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;

import com.bru.toolkit.utils.Logs;

/**
 * Class Desc: Class Desc
 * <p>
 * Creator : Bruce Ding
 * <p>
 * Email : brucedingdev@foxmail.com
 * <p>
 * Create Time: 2017/07/13 15:12
 */
public class ChangeOrientationHandler extends Handler {

    private String TAG = getClass().getSimpleName();
    private Activity activity;

    public ChangeOrientationHandler(Activity ac) {
        super();
        activity = ac;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == 888) {
            int orientation = msg.arg1;
            Logs.e(TAG, "orientation-->  " + orientation);
            if (orientation > 45 && orientation < 135) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                Logs.d(TAG, "横屏翻转: ");
            } else if (orientation > 135 && orientation < 225) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                Logs.d(TAG, "竖屏翻转: ");
            } else if (orientation > 225 && orientation < 315) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Logs.d(TAG, "横屏: ");
            } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Logs.d(TAG, "竖屏: ");
            }
        }
        super.handleMessage(msg);
    }
}
