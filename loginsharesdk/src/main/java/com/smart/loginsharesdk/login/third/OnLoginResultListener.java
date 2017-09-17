package com.smart.loginsharesdk.login.third;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;

/**
 * Copyright © 2016 Phoenix New Media Limited All Rights Reserved.
 * Created by fengjh on 16/3/9.
 */
public interface OnLoginResultListener {

    void loginSuccess(UserInfo userInfo, Platform platform, HashMap<String, Object> res);

    void loginError(Throwable t);

    void loginCancel(Platform plat);
}
