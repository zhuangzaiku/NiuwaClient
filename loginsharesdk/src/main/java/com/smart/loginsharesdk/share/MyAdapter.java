package com.smart.loginsharesdk.share;

import android.view.View;

import cn.sharesdk.framework.authorize.AuthorizeAdapter;

/**
 * Created by fengjh on 2014/10/16.
 */
public class MyAdapter extends AuthorizeAdapter {
    @Override
    public void onCreate() {
        super.onCreate();
        //隐藏标题
        getTitleLayout().setVisibility(View.GONE);
    }
}
