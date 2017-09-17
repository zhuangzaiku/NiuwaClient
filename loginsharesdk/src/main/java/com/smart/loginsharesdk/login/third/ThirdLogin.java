package com.smart.loginsharesdk.login.third;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Copyright © 2016 Phoenix New Media Limited All Rights Reserved.
 * Created by fengjh on 16/2/15.
 */
public class ThirdLogin {

    private ArrayList<LoginApi> mLoginApi = new ArrayList<>();

    public LoginApi loginWeChat(Context context) {
        return login(context, "Wechat");
    }

    public ArrayList<LoginApi> getLoginApi() {
        return mLoginApi;
    }

    public void setLoginApi(LoginApi loginApi) {
        mLoginApi.add(loginApi);
    }

    public LoginApi loginSina(Context context) {
        return login(context, "SinaWeibo");
    }

    public LoginApi loginQQ(Context context) {
        return login(context, "QQ");
    }

    private LoginApi login(Context context, String platformName) {
        //初始化SDK
        ShareSDK.initSDK(context);

        LoginApi api = new LoginApi();
        setLoginApi(api);
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.login(context);
        return api;
    }

    public static void removeAuthorize(Context context, String platform) {
        ShareSDK.initSDK(context);
        Platform plat = ShareSDK.getPlatform(context, platform);
        if (plat != null && plat.isAuthValid()) {
            plat.removeAccount(true);
        }
    }
}
