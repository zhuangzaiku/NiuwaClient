package com.bru.toolkit.utils.screenrotate;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.OrientationEventListener;

/**
 * Class Desc:
 * 1.使用方法：在Activity中创建对象，获取Activity对象，
 * 在Activity的onPause方法中调用此类onPause，去除监听
 * 在Activity的onResume方法中调用此类onResume，添加监听
 * 2.为防止在屏幕旋转中反复调用onCreate方法，需要在Activity的menifest配置中添加如下代码：
 * android:screenOrientation="sensor"
 * android:configChanges="orientation|screenSize|smallestScreenSize|keyboard|keyboardHidden|navigation"
 * <p>
 * Creator : Bruce Ding
 * <p>
 * Email : brucedingdev@foxmail.com
 * <p>
 * Create Time: 2017/07/13 15:16
 */
public class OrientationSensorListener extends OrientationEventListener {

    private static final int _DATA_X = 0;
    private static final int _DATA_Y = 1;
    private static final int _DATA_Z = 2;
    private OnOrientationChanged orientationListener;

    public static final int ORIENTATION_UNKNOWN = -1;

    private Handler rotateHandler;

    @Override
    public void onOrientationChanged(int orientation) {
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            return;  //手机平放时，检测不到有效的角度
        }
        //只检测是否有四个角度的改变
        if (orientation > 350 || orientation < 10) { //0度
            orientation = 0;
        } else if (orientation > 80 && orientation < 100) { //90度
            orientation = 90;
        } else if (orientation > 170 && orientation < 190) { //180度
            orientation = 180;
        } else if (orientation > 260 && orientation < 280) { //270度
            orientation = 270;
        } else {
        }
//        if (rotateHandler != null) {
//            rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
//        }
        if (null != orientationListener){
            orientationListener.orientationChanged(orientation);
        }
    }

    public OrientationSensorListener(Activity act, OnOrientationChanged orientationListener) {
        super(act);
        this.orientationListener = orientationListener;
        rotateHandler = new ChangeOrientationHandler(act);
        enable();
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        int orientation = ORIENTATION_UNKNOWN;
        float X = -values[_DATA_X];
        float Y = -values[_DATA_Y];
        float Z = -values[_DATA_Z];
        float magnitude = X * X + Y * Y;
        // Don't trust the angle if the magnitude is small compared to the y value
        if (magnitude * 4 >= Z * Z) {
            float OneEightyOverPi = 57.29577957855f;
            float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
            orientation = 90 - (int) Math.round(angle);
            // normalize to 0 - 359 range
            while (orientation >= 360) {
                orientation -= 360;
            }
            while (orientation < 0) {
                orientation += 360;
            }
        }

        if (rotateHandler != null) {
            rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
        }

    }

    public void onResume() {
        enable();
//        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void onPause() {
        disable();
//        sm.unregisterListener(this);
    }

}