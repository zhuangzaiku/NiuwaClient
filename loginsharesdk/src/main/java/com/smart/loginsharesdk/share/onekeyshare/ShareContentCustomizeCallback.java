package com.smart.loginsharesdk.share.onekeyshare;

/**
 * Created by fengjh on 2014/9/29.
 */
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;

public interface ShareContentCustomizeCallback {

    public void onShare(Platform platform, ShareParams paramsToShare);

}