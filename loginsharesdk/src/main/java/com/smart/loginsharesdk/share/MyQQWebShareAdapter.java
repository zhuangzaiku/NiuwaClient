package com.smart.loginsharesdk.share;

import android.view.View;
import android.view.Window;

import cn.sharesdk.tencent.qq.QQWebShareAdapter;

/**
 * Created by fengjh on 2014/10/29.
 */
public class MyQQWebShareAdapter extends QQWebShareAdapter {

    @Override
    public void onCreate() {
        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate();
        getTitleLayout().setVisibility(View.GONE);
    }
}
