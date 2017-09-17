package com.smart.loginsharesdk.share.onekeyshare;

/**
 * Created by fengjh on 2014/10/17.
 */
public class Type {
    //微信、易信平台分享内容的类型
    public static final int SHARE_TEXT = 1;
    public static final int SHARE_IMAGE = 2;
    public static final int SHARE_WEBPAGE = 3;
    public static final int SHARE_MUSIC = 4;
    public static final int SHARE_VIDEO = 5;
    public static final int SHARE_APPS = 6;
    public static final int SHARE_FILE = 7;
    public static final int SHARE_EMOJI = 8;
    public static final int SHARE_TEXT_OTHER = 9;
    //微信、易信平台分享图片来源的类型
    public static final int IMAGE_LOCAL = 1;
    public static final int IMAGE_BITMAP = 2;
    public static final int IMAGE_NETWORK = 3;
    //微信平台应用分享的类型
    public static final int APPS_FILE = 1;
    public static final int APPS_SCRIPT = 2;
    //平台绑定与解绑结果的类型
    public static final int BIND_SUCCESS = 1;
    public static final int BIND_ERROR = 2;
    public static final int BIND_CANCEL = 3;
    public static final int UNBIND_SECCESS = 4;
    //分享结果的类型
    public static final int SHARE_SUCCESS = 1;
    public static final int SHARE_ERROR = 2;
    public static final int SHARE_CANCEL = 3;
    //平台无效，没有安装相应的客户端
    public static final int PLATFORM_NOT_VALID = 11;
}
