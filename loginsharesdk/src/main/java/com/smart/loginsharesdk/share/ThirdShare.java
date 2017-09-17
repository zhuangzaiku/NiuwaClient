package com.smart.loginsharesdk.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.smart.loginsharesdk.R;
import com.smart.loginsharesdk.share.onekeyshare.EditPage;
import com.smart.loginsharesdk.share.onekeyshare.OnekeyShare;
import com.smart.loginsharesdk.share.onekeyshare.ShareCore;
import com.smart.loginsharesdk.share.onekeyshare.Type;
import com.smart.loginsharesdk.utils.ILog;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.UIHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Copyright © 2016 Phoenix New Media Limited All Rights Reserved.
 * Created by fengjh on 16/2/15.
 */
public class ThirdShare implements PlatformActionListener, Handler.Callback {

    private Context context;
    private String imagePath;
    private String imageUrl;
    private String text;
    private int imageType = 0;
    private boolean disableSSO = true;
    private String curPlatName;
    private String bypassApproval;
    private String title;
    private String titleUrl;
    private String site;
    private String siteUrl;
    private float latitude;
    private float longitude;
    private int appType = 0;
    private int type = 0;
    private String musicUrl;
    private String url;
    private String filePath;
    private String extInfo;
    private Bitmap bitmap;
    private HashMap<String, Object> reqData;

    private int tryOnCount = 0;


    public ThirdShare(Context context) {
        this.context = context;
        ShareSDK.initSDK(context);
        reqData = new HashMap<String, Object>();
        if (this.bypassApproval != null && !"".equals(this.bypassApproval)) {
            boolean bypass = true;
            if ("true".equals(this.bypassApproval)) {
                bypass = true;
            } else if ("false".equals(this.bypassApproval)) {
                bypass = false;
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("BypassApproval", bypass);
            if (curPlatName != null && !"".equals(curPlatName)) {
                if (curPlatName.equals(Wechat.NAME)) {
                    ShareSDK.setPlatformDevInfo("Wechat", map);
                } else if (curPlatName.equals(WechatMoments.NAME)) {
                    ShareSDK.setPlatformDevInfo("WechatMoments", map);
                }
            } else {
                ShareSDK.setPlatformDevInfo("Wechat", map);
                ShareSDK.setPlatformDevInfo("WechatMoments", map);
            }
        }
    }

    /**
     * 分享本地图片
     *
     * @param imagePath 本地图片的路径
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        reqData.put("imagePath", imagePath);
    }

    /**
     * 分享网络图片
     *
     * @param imageUrl 网络图片的网址
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        reqData.put("imageUrl", imageUrl);
    }

    /**
     * 分享Bitmap对象的图片
     *
     * @param bitmap 图片的Bitmap对象
     */
    public void setImageData(Bitmap bitmap) {
        this.bitmap = bitmap;
        reqData.put("bitmap", bitmap);
    }

    /**
     * 分享文字内容
     *
     * @param text 需要分享的文字内容
     */
    public void setText(String text) {
        this.text = text;
        reqData.put("text", text);
    }

    /**
     * 微信、易信分享时设置分享的图片的类型，如：本地图片、网络图片、Bitmap对象
     *
     * @param imageType 微信、易信图片分享时分享的图片来源类型
     */
    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    /**
     * 微信、易信分享时，设置要分享内容的类型，如：文字、图片、视频、音乐等
     *
     * @param type 微信、易信分享时代分享内容的类型
     */
    public void setShareType(int type) {
        this.type = type;
    }

    /**
     * title标题，印象笔记、邮箱、短息、微信（好友、朋友圈、收藏）、易信（好友、朋友圈）、人人网、QQ空间，否则可以不提供
     *
     * @param title 分享的标题
     */
    public void setTitle(String title) {
        this.title = title;
        reqData.put("title", title);
    }

    /**
     * titleUrl标题链接，人人网、QQ空间，否则可以不提供
     *
     * @param titleUrl 分享标题的链接地址
     */
    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
        reqData.put("titleUrl", titleUrl);
    }

    /**
     * 设置分享此内容的网站名称，QQ空间
     *
     * @param site site是分享此内容的网站名称，QQ空间
     */
    public void setSite(String site) {
        this.site = site;
        reqData.put("site", site);
    }

    /**
     * 设置分享此内容的网站地址，QQ空间
     *
     * @param siteUrl siteUrl是分享此内容的网站地址，QQ空间
     */
    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
        reqData.put("siteUrl", siteUrl);
    }

    /**
     * 设置分享地纬度，新浪微博、腾讯微博
     *
     * @param latitude 分享地纬度，新浪微博、腾讯微博
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
        reqData.put("latitude", latitude);
    }

    /**
     * 设置分享地经度，新浪微博、腾讯微博
     *
     * @param longitude 分享地经度，新浪微博、腾讯微博
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
        reqData.put("longitude", longitude);
    }

    /**
     * 设置音乐的网址，微信（好友、朋友圈、收藏）、易信（好友、朋友圈），否则可以不提供
     *
     * @param musicUrl MusicUrl音乐的网址
     */
    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
        reqData.put("musicUrl", musicUrl);
    }

    /**
     * 分享的网页的地址
     * url,微信（好友、朋友圈、收藏）、易信（好友、朋友圈），否则可以不提供
     *
     * @param url 分享的网页的地址
     */
    public void setUrl(String url) {
        this.url = url;
        reqData.put("url", url);
    }

    /**
     * 设置带分享应用程序的本地路径，微信（好友）、易信（好友），否则可以不提供
     *
     * @param filePath filePath带分享应用程序的本地路径
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
        reqData.put("filePath", filePath);
    }

    /**
     * 设置分享文件、应用程序、脚本的附加信息，微信（好友）平台
     *
     * @param extInfo 分享文件、应用程序、脚本的附加信息
     */
    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
        reqData.put("extInfo", extInfo);
    }

    /**
     * 设置是否禁用SSO授权方式，禁用SSO授权方式，分享前需要授权，值设置为true，启用SSO授权方式，分享前不需要授权，值设置为false
     *
     * @param disableSSO 默认值为true，
     */
    public void setDisableSSO(boolean disableSSO) {
        this.disableSSO = disableSSO;
    }

    /**
     * 指定微信好友、微信朋友圈、易信好友、易信朋友圈是否绕过审核，默认都是绕过审核的
     *
     * @param curPlatName    平台名称
     * @param bypassApproval 可以设置"false"、"true"
     */
    public void setBypassApproval(String curPlatName, String bypassApproval) {
        this.curPlatName = curPlatName;
        this.bypassApproval = bypassApproval;
    }

    /**
     * 指定微信、易信平台（好友、朋友圈）是否绕过审核，默认都是绕过审核的
     *
     * @param bypassApproval 可以设置"false"、"true"
     */
    public void setBypassApproval(String bypassApproval) {
        this.bypassApproval = bypassApproval;
    }


    /**
     * 分享到新浪微博
     */
    public void share2SinaWeibo(boolean silent) {
        final Platform Sina = ShareSDK.getPlatform(SinaWeibo.NAME);
        if (silent) {
//            oneKeyShare(Sina);
            reqData.put("platform", Sina.getName());
//            shareData(context, reqData);
            HashMap<Platform, HashMap<String, Object>> map = new HashMap<Platform, HashMap<String, Object>>();
            map.put(Sina, reqData);
            OnekeyShare onekeyShare = new OnekeyShare(context);
            onekeyShare.setNotification(R.drawable.notification_icon, "新浪微博正在分享...");
            onekeyShare.share(map);
            onekeyShare.setOnShareStatusListener(new OnekeyShare.OnShareStatusListener() {
                @Override
                public void shareSuccess(Platform platform) {
                    if (shareStatusListener != null) {
                        shareStatusListener.shareSuccess(platform);
                    }
                }

                @Override
                public void shareError(Platform platform) {
                    if (shareStatusListener != null) {
                        shareStatusListener.shareError(platform);
                    }
                }

                @Override
                public void shareCancel(Platform platform) {
                    if (shareStatusListener != null) {
                        shareStatusListener.shareCancel(platform);
                    }
                }
            });
            return;
        }
        ILog.d("IShare2Platform", "====Sina=valid=" + Sina.isAuthValid());
        if (!Sina.isAuthValid()) {
            Sina.SSOSetting(false);
            Sina.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    if (Sina instanceof CustomPlatform || ShareCore.isUseClientToShare(Sina.getName())) {
                        oneKeyShare(Sina);
                        return;
                    }
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            Sina.showUser(null);
            return;
        }
        if (Sina instanceof CustomPlatform || ShareCore.isUseClientToShare(Sina.getName())) {
            oneKeyShare(Sina);
            return;
        }
        reqData.put("platform", Sina.getName());
        shareData(context, reqData);
    }

    /**
     * 分享到QQ好友
     */
    public void share2QQ() {
        final Platform qq = ShareSDK.getPlatform(QQ.NAME);
        ILog.d("IShare2Platform", "====qq=valid=" + qq.isAuthValid());
        if (!qq.isAuthValid()) {
            qq.SSOSetting(false);
            qq.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    if (qq instanceof CustomPlatform || ShareCore.isUseClientToShare(qq.getName())) {
                        oneKeyShare(qq);
                        return;
                    }
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            qq.showUser(null);
            return;
        }
        if (qq instanceof CustomPlatform || ShareCore.isUseClientToShare(qq.getName())) {
            ILog.d("IShare2Platform", "====qq=oneKeyShare=");
            oneKeyShare(qq);
            return;
        }
        ILog.d("IShare2Platform", "====qq=shareData=");
    }

    /**
     * 分享到QQ空间
     */
    public void share2QZone() {
        final Platform qZone = ShareSDK.getPlatform(QZone.NAME);
        ILog.d("IShare2Platform", "====qZone=valid=" + qZone.isAuthValid());
        if (!qZone.isAuthValid()) {
            qZone.SSOSetting(false);
            qZone.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    if (qZone instanceof CustomPlatform || ShareCore.isUseClientToShare(qZone.getName())) {
                        oneKeyShare(qZone);
                        return;
                    }
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            qZone.showUser(null);
            return;
        }
        if (qZone instanceof CustomPlatform || ShareCore.isUseClientToShare(qZone.getName())) {
            ILog.d("IShare2Platform", "====qZone=oneKeyShare=");
            oneKeyShare(qZone);
            return;
        }
        ILog.d("IShare2Platform", "====qZone=shareData=");
    }

    /**
     * 分享到微信好友
     */
    public void share2Wechat() {
        Platform weChat = ShareSDK.getPlatform("Wechat");
        if (weChat instanceof CustomPlatform || ShareCore.isUseClientToShare(weChat.getName())) {
            oneKeyShare(weChat);
            return;
        }
    }

    /**
     * 分享到微信朋友圈
     */
    public void share2WechatMoments() {
        Platform WeChatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
        if (WeChatMoments instanceof CustomPlatform || ShareCore.isUseClientToShare(WeChatMoments.getName())) {
            oneKeyShare(WeChatMoments);
            return;
        }
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> stringObjectHashMap) {
        tryOnCount = 0;
        Message msg = new Message();
        msg.what = Type.SHARE_SUCCESS;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
//        ShareSDK.logDemoEvent(1,platform);
    }


    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        Log.e("IShare2Platform", "onError==", throwable);
        tryOnCount = tryOnCount + 1;
        Message msg = new Message();
        msg.what = Type.SHARE_ERROR;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
        // 分享失败的统计
        ShareSDK.logDemoEvent(4, platform);
    }


    @Override
    public void onCancel(Platform platform, int action) {
        Log.e("IShare2Platform", "onCancel==action=="+action);
        tryOnCount = 0;
        Message msg = new Message();
        msg.what = Type.SHARE_CANCEL;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
        ShareSDK.logDemoEvent(5, platform);
    }

    @Override
    public boolean handleMessage(Message message) {
        Platform platform = (Platform) message.obj;
        String name = getPlatformName(platform);
        boolean isWechat = "WechatMoments".equals(platform.getName()) || "Wechat".equals(platform.getName());
        switch (message.what) {
            case Type.SHARE_SUCCESS:
                if (!isWechat) {
                    Intent intent = new Intent();
                    intent.setAction("com.ifeng.share.bind");
                    Bundle bundle = new Bundle();
                    bundle.putInt("bind", Type.BIND_SUCCESS);
                    bundle.putString("plat", name);
                    intent.putExtras(bundle);
                    context.sendBroadcast(intent);
                }
                //分享成功
                if (shareStatusListener != null)
                    shareStatusListener.shareSuccess(platform);
                break;
            case Type.SHARE_ERROR:
                //分享失败
                if (tryOnCount == 1) {
                    platform.removeAccount(true);
                    platform.SSOSetting(false);
                    platform.setPlatformActionListener(new PlatformActionListener() {
                        @Override
                        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                            String pName = platform.getName();
                            if ("SinaWeibo".equals(pName)) {
                                Platform Sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                                if (Sina instanceof CustomPlatform || ShareCore.isUseClientToShare(Sina.getName())) {
                                    oneKeyShare(Sina);
                                    return;
                                }
                            } else if ("QZone".equals(pName)) {
                                Platform qZone = ShareSDK.getPlatform(QZone.NAME);
                                if (qZone instanceof CustomPlatform || ShareCore.isUseClientToShare(qZone.getName())) {
                                    oneKeyShare(qZone);
                                    return;
                                }
                            } else if ("QQ".equals(pName)) {
                                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                                if (qq instanceof CustomPlatform || ShareCore.isUseClientToShare(qq.getName())) {
                                    oneKeyShare(qq);
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onError(Platform platform, int i, Throwable throwable) {

                        }

                        @Override
                        public void onCancel(Platform platform, int i) {

                        }
                    });
                    platform.showUser(null);
                } else {
                    if (shareStatusListener != null)
                        shareStatusListener.shareError(platform);
                }
                break;
            case Type.SHARE_CANCEL:
                //分享取消
                if (shareStatusListener != null)
                    shareStatusListener.shareCancel(platform);
                break;
            case Type.PLATFORM_NOT_VALID:
                String InValidname = getInValidPlatformName(platform);
                Toast toast = Toast.makeText(context, "亲，您未安装" + InValidname + ",无法进行" + InValidname + "分享的(⊙o⊙)", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                break;
        }
        return false;
    }

    private static String getPlatformName(Platform plat) {
        String name = plat.getName();
        if ("SinaWeibo".equals(name)) {
            return "新浪微博";
        } else if ("QZone".equals(name)) {
            return "QQ空间";
        } else if ("QQ".equals(name)) {
            return "QQ好友";
        } else if ("Wechat".equals(name)) {
            return "微信好友";
        } else if ("WechatMoments".equals(name)) {
            return "微信朋友圈";
        }
        return "";
    }

    private String getInValidPlatformName(Platform plat) {
        String name = plat.getName();
        if ("SinaWeibo".equals(name)) {
            return "新浪微博";
        } else if ("QZone".equals(name) || "QQ".equals(name)) {
            return "QQ";
        } else if ("Wechat".equals(name) || "WechatMoments".equals(name)) {
            return "微信";
        }
        return "";
    }

    //直接分享
    private void oneKeyShare(Platform platform) {
//        ShareSDK.logDemoEvent(1,platform);
        Platform.ShareParams sp = null;
        String name = platform.getName();
        boolean isWechat = "WechatMoments".equals(name) || "Wechat".equals(name)
                || "WechatFavorite".equals(name);
        platform.setPlatformActionListener(this);
        if (isWechat) {
            if (isWechat && platform.isClientValid()) {
                if (type != 0) {
                    sp = getShareParams(type, reqData);
                    if (disableSSO) {
                        //禁用SSO授权方式
                        platform.SSOSetting(true);
                    } else {
                        //开启SSO授权方式
                        platform.SSOSetting(false);
                    }
                    platform.share(sp);
                    return;
                }
            } else {
                //没有安装客户端
                Message msg = new Message();
                msg.what = Type.PLATFORM_NOT_VALID;
                msg.obj = platform;
                UIHandler.sendMessage(msg, this);
                return;
            }
        } else {
            HashMap<Platform, HashMap<String, Object>> req
                    = new HashMap<Platform, HashMap<String, Object>>();
            req.put(platform, reqData);
            share(req);
            return;
        }
    }

    private void setWXYXComplete(Platform.ShareParams params, HashMap<String, Object> data) {
        params.setText(data.get("text").toString());
        params.setTitle(data.get("title").toString());
    }

    private Platform.ShareParams getShareParams(int type, HashMap<String, Object> data) {
        Platform.ShareParams params = new Platform.ShareParams();
        //图文混合分享
//        if (type == LoginType.SHARE_TEXT_OTHER) {
        setWXYXComplete(params, data);
//        }
        //否则单独分享某类信息
        switch (type) {
            case Type.SHARE_TEXT: {
                params.setText(data.get("text").toString());
                params.setTitle(data.get("title").toString());
                params.setShareType(Platform.SHARE_TEXT);
            }
            break;
            case Type.SHARE_IMAGE: {
                if (imageType != 0) {
                    params.setShareType(Platform.SHARE_IMAGE);
                    setImageType(params, imageType, data);
                }
            }
            break;
            case Type.SHARE_EMOJI: {
                if (imageType != 0) {
                    params.setShareType(Platform.SHARE_EMOJI);
                    setImageType(params, imageType, data);
                }
            }
            break;
            case Type.SHARE_MUSIC: {
                if (data.containsKey("musicUrl") && data.get("musicUrl") != null && data.containsKey("url") && data.get("url") != null) {
                    params.setShareType(Platform.SHARE_MUSIC);
                    params.setMusicUrl(data.get("musicUrl").toString());
                    params.setUrl(data.get("url").toString());
                    setImageType(params, imageType, data);
                }
            }
            break;
            case Type.SHARE_VIDEO: {
                if (data.containsKey("url") && data.get("url") != null) {
                    params.setShareType(Platform.SHARE_VIDEO);
                    params.setUrl(data.get("url").toString());
                    setImageType(params, imageType, data);
                }
            }
            break;
            case Type.SHARE_WEBPAGE: {
                if (imageType != 0) {
                    params.setShareType(Platform.SHARE_WEBPAGE);
                    if (data.containsKey("url") && data.get("url") != null) {
                        params.setUrl(data.get("url").toString());
                        setImageType(params, imageType, data);
                    }
                }
            }
            break;
            case Type.SHARE_APPS: {
                //只有微信好友可以分享
                if (appType != 0) {
                    params.setShareType(Platform.SHARE_APPS);
                    switch (appType) {
                        case Type.APPS_FILE:
                            //携带文件
                            if (data.containsKey("filePath") && data.get("filePath") != null && data.containsKey("extInfo") && data.get("extInfo") != null) {
                                params.setFilePath(data.get("filePath").toString());
                                params.setExtInfo(data.get("extInfo").toString());
                                setImageType(params, imageType, data);
                            }
                            break;
                        case Type.APPS_SCRIPT:
                            //携带脚本
                            if (data.containsKey("extInfo") && data.get("extInfo") != null) {
                                params.setExtInfo(data.get("extInfo").toString());
                                setImageType(params, imageType, data);
                            }
                            break;
                    }
                }
            }
            break;
            case Type.SHARE_FILE: {
                //只有微信好友、微信收藏可以分享文件
                if (data.containsKey("filePath") && data.get("filePath") != null) {
                    params.setFilePath(data.get("filePath").toString());
                    setImageType(params, imageType, data);
                }
            }
            break;
        }
        return params;
    }

    //设置图片的类型
    private void setImageType(Platform.ShareParams params, int imageType, HashMap<String, Object> data) {
        switch (imageType) {
            case Type.IMAGE_LOCAL:
                //本地图片
                if (data.containsKey("imagePath") && data.get("imagePath") != null) {
                    params.setImagePath(data.get("imagePath").toString());
                }
                break;
            case Type.IMAGE_BITMAP:
                //Bitmap对象
                if (data.containsKey("bitmap") && data.get("bitmap") != null) {
                    params.setImageData((Bitmap) data.get("bitmap"));
                }
                break;
            case Type.IMAGE_NETWORK:
                //网络图片
                if (data.containsKey("imageUrl") && data.get("imageUrl") != null) {
                    params.setImageUrl(data.get("imageUrl").toString());
                }
                break;
            default:
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_share_image);
                params.setImageData(bitmap);
                break;
        }
    }

    //跳转到内容页面在进行分享
    private void shareData(Context context, HashMap<String, Object> reqData) {

        EditPage page = new EditPage();
        page.setShareData(reqData);
        page.showForResult(context, null, new FakeActivity() {
            @Override
            public void onResult(HashMap<String, Object> data) {
                if (data != null && data.containsKey("editRes")) {
                    HashMap<Platform, HashMap<String, Object>> editRes = (HashMap<Platform, HashMap<String, Object>>) data.get("editRes");
                    share(editRes);
                }
            }
        });
    }

    private void share(HashMap<Platform, HashMap<String, Object>> shareData) {
        for (Map.Entry<Platform, HashMap<String, Object>> ent : shareData.entrySet()) {
            Platform platform = ent.getKey();
            platform.SSOSetting(disableSSO);
            HashMap<String, Object> data = ent.getValue();
            int shareType = Platform.SHARE_TEXT;
            String imagePath = String.valueOf(data.get("imagePath"));
            if (imagePath != null && (new File(imagePath)).exists()) {
                shareType = Platform.SHARE_IMAGE;
                if (imagePath.endsWith(".git")) {
                    shareType = Platform.SHARE_EMOJI;
                } else if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
                    shareType = Platform.SHARE_WEBPAGE;
                }
            } else {
                Object imageUrl = data.get("imageUrl");
                if (imageUrl != null && !TextUtils.isEmpty(String.valueOf(imageUrl))) {
                    shareType = Platform.SHARE_IMAGE;
                    if (String.valueOf(imageUrl).endsWith(".gif")) {
                        shareType = Platform.SHARE_EMOJI;
                    } else if (data.containsKey("url")) {
                        Object url = data.get("url");
                        if (url != null && !TextUtils.isEmpty(url.toString())) {
                            shareType = Platform.SHARE_WEBPAGE;
                        }
                    }
                }
            }
            data.put("shareType", shareType);
            Platform.ShareParams params = new Platform.ShareParams(data);
            platform.setPlatformActionListener(this);
            platform.share(params);
        }
    }

    private OnShareStatusListener shareStatusListener;

    public void setOnShareStatusListener(OnShareStatusListener shareStatusListener) {
        this.shareStatusListener = shareStatusListener;
    }

    public static boolean getAuthorizeStatus(Context context, String platform) {
        boolean status = false;

        return status;
    }

    public static void setAuthorizeStatus(Context context, String platform, boolean status) {
        ILog.d("IPartake", "====setAuthorizeStatus");
        ShareSDK.initSDK(context);
        Platform plat = ShareSDK.getPlatform(platform);
        String text = getPlatformName(plat);

        if (plat != null) {
            if (status) {
                //绑定
                if (!plat.isAuthValid()) {
                    plat.SSOSetting(false);
                    plat.setPlatformActionListener(new PlatformActionListener() {
                        @Override
                        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                        }

                        @Override
                        public void onError(Platform platform, int i, Throwable throwable) {

                        }

                        @Override
                        public void onCancel(Platform platform, int i) {

                        }
                    });
                    plat.showUser(null);
                }

            } else {
                //解绑
                ILog.d("IPartake", "====setAuthorizeStatus=解绑");
                if (plat.isAuthValid()) {
                    plat.removeAccount(true);
                    Toast.makeText(context, text + "解绑成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction("com.ifeng.share.bind");
                    Bundle bundle = new Bundle();
                    bundle.putInt("bind", Type.UNBIND_SECCESS);
                    bundle.putString("plat", text);
                    intent.putExtras(bundle);
                    context.sendBroadcast(intent);
                    return;
                }
            }
        }
    }

}