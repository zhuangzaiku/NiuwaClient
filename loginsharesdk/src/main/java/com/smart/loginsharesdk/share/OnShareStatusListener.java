package com.smart.loginsharesdk.share;

import cn.sharesdk.framework.Platform;

/**
 * Created by fengjh on 2014/12/8.
 */
public interface OnShareStatusListener {

    public void shareSuccess(Platform platform);

    public void shareError(Platform platform);

    public void shareCancel(Platform platform);
}
