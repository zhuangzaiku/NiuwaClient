package com.smart.loginsharesdk.share.onekeyshare;

/**
 * Created by fengjh on 2014/9/29.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.smart.loginsharesdk.utils.ILog;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.UIHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import static com.mob.tools.utils.R.getStringRes;

/**
 * 快捷分享的入口
 */
public class OnekeyShare extends FakeActivity implements
        PlatformActionListener, Callback {
    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;

    private int notifyIcon;
    private String notifyTitle;
    private boolean silent;
    private PlatformActionListener callback;
    private ShareContentCustomizeCallback customizeCallback;
    private boolean disableSSO;
    private Context mContext;


    /**
     * 构造函数，用于数据初始化
     */
    public OnekeyShare(Context context) {
        callback = this;
        mContext = context;
        ShareSDK.initSDK(context);
    }

    /**
     * 分享时Notification的图标和文字
     *
     * @param icon  通知的图标
     * @param title 通知的标题
     */
    public void setNotification(int icon, String title) {
        notifyIcon = icon;
        notifyTitle = title;
    }

    /**
     * 设置自定义的外部回调
     *
     * @param callback 设置自定义的外部回调接口
     */
    public void setCallback(PlatformActionListener callback) {
        this.callback = callback;
    }

    /**
     * 返回操作回调
     *
     * @return 操作回调
     */
    public PlatformActionListener getCallback() {
        return callback;
    }

    /**
     * 设置用于分享过程中，根据不同平台自定义分享内容的回调
     *
     * @param callback 设置不同平台自定义分享内容的回调
     */
    public void setShareContentCustomizeCallback(ShareContentCustomizeCallback callback) {
        customizeCallback = callback;
    }

    /**
     * 返回自定义分享内容的回调
     *
     * @return 自定义分享内容的回调
     */
    public ShareContentCustomizeCallback getShareContentCustomizeCallback() {
        return customizeCallback;
    }


    /**
     * 设置一个总开关，用于在分享前若需要授权，则禁用sso功能
     */
    public void disableSSOWhenAuthorize() {
        disableSSO = true;
    }

    /**
     * 设置一个总开关，用于在分享前若不需要授权，则启用用sso功能
     */
    public void enableSSOWhenAuthorize() {
        disableSSO = false;
    }

    /**
     * 循环执行分享
     */
    public void share(HashMap<Platform, HashMap<String, Object>> shareData) {
        ILog.d("Onekey", "====share");
        boolean started = false;
        for (Entry<Platform, HashMap<String, Object>> ent : shareData.entrySet()) {
            Platform plat = ent.getKey();
            plat.SSOSetting(disableSSO);
            String name = plat.getName();
            ILog.d("Onekey", "====platform======name=" + name);

            boolean isWechat = "WechatMoments".equals(name) || "Wechat".equals(name)
                    || "WechatFavorite".equals(name);
            ILog.d("onekey", "====isValid=" + plat.isClientValid());
            if (isWechat && !plat.isClientValid()) {
                Message msg = new Message();
                msg.what = MSG_TOAST;
                int resId = getStringRes(getContext(), "wechat_client_inavailable");
                msg.obj = activity.getString(resId);
                //微信平台名称
                msg.obj = activity.getString(resId);
                UIHandler.sendMessage(msg, this);
                continue;
            }

            HashMap<String, Object> data = ent.getValue();
            int shareType = Platform.SHARE_TEXT;
            String imagePath = String.valueOf(data.get("imagePath"));
            ILog.d("Onekey", "====plat=imagePath---" + imagePath);
            ILog.d("Onekey", "====plat=(new File(imagePath)).exists()---" + (new File(imagePath)).exists());

            if (imagePath != null && (new File(imagePath)).exists()) {
                shareType = Platform.SHARE_IMAGE;
                ILog.d("Onekey", "====plat=SHARE_IMAGE---");
                if (imagePath.endsWith(".gif")) {
                    shareType = Platform.SHARE_EMOJI;
                    ILog.d("Onekey", "====plat=SHARE_EMOJI---");
                } else if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
                    shareType = Platform.SHARE_WEBPAGE;
                    ILog.d("Onekey", "====plat=SHARE_WEBPAGE---");
                }
            } else {
                Bitmap viewToShare = (Bitmap) data.get("viewToShare");
                if (viewToShare != null && !viewToShare.isRecycled()) {
                    shareType = Platform.SHARE_IMAGE;
                    ILog.d("Onekey", "====plat=viewToShare=SHARE_IMAGE---");
                    if (data.containsKey("url")) {
                        Object url = data.get("url");
                        if (url != null && !TextUtils.isEmpty(url.toString())) {
                            shareType = Platform.SHARE_WEBPAGE;
                            ILog.d("Onekey", "====plat=viewToShare=SHARE_WEBPAGE---");
                        }
                    }
                } else {
                    Object imageUrl = data.get("imageUrl");
                    if (imageUrl != null && !TextUtils.isEmpty(String.valueOf(imageUrl))) {
                        shareType = Platform.SHARE_IMAGE;
                        ILog.d("Onekey", "====plat=imageUrl=SHARE_IMAGE---");
                        if (String.valueOf(imageUrl).endsWith(".gif")) {
                            shareType = Platform.SHARE_EMOJI;
                            ILog.d("Onekey", "====plat=imageUrl=SHARE_EMOJI---");
                        } else if (data.containsKey("url")) {
                            Object url = data.get("url");
                            if (url != null && !TextUtils.isEmpty(url.toString())) {
                                shareType = Platform.SHARE_WEBPAGE;
                                ILog.d("Onekey", "====plat=imageUrl=SHARE_WEBPAGE---");
                            }
                        }
                    }
                }
            }
            ILog.d("Onekey", "====plat=shareType=" + shareType);
            data.put("shareType", shareType);
            ILog.d("Onekey", "====plat=started---" + started);
            if (!started) {
                started = true;
                if (equals(callback)) {
                    int resId = getStringRes(getContext(), "sharing");
                    if (resId > 0) {
                        showNotification(2000, getContext().getString(resId));
                    }
                }
                finish();
            }
            ILog.d("Onekey", "====plat=share---");
            plat.setPlatformActionListener(callback);
            ShareCore shareCore = new ShareCore();
            shareCore.setShareContentCustomizeCallback(customizeCallback);
            //需要分享的全面内容
            ILog.d("Onekey", "====plat=data---" + data.toString());
            shareCore.share(plat, data);
        }
    }

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        ILog.d("Onekey", "====onComplete=res=" + res.toString());
        ILog.d("Onekey", "====onComplete=platform=username=" + platform.getDb().getUserName());
        ILog.d("Onekey", "====onComplete=platform=nickname=" + platform.getDb().get("nickname"));
        ILog.d("Onekey", "====onComplete=platform=UserIcon=" + platform.getDb().getUserIcon());
        ILog.d("Onekey", "====onComplete=platform=UserId=" + platform.getDb().getUserId());
        ILog.d("Onekey", "====onComplete=platform=platformName=" + platform.getName());
        map.put("platform", platform);
        map.put("platformname", platform.getName());
        map.put("username", platform.getDb().getUserName());
        map.put("usericon", platform.getDb().getUserIcon());
        map.put("userid", platform.getDb().getUserId());
//        userInfo.put("userinfo",map);
        ILog.d("Onekey", "====onComplete=platform=userInfo=" + map.toString());
        ILog.d("Onekey", "====onComplete=platform=执行UIHandler=");
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
        ILog.d("Onekey", "====onComplete=platform=执行UIHandler完成=");
//        Log.d("Onekey","====onComplete=platform=userInfo====="+userInfo.toString());
        ILog.d("Onekey", "====onComplete=platform=userInfo=====" + map.toString());
//        callUserInfo.setUserInfo(map);
        ILog.d("Onekey", "====onComplete=platform====userInfo=====" + map.toString());
    }

    public void onError(Platform platform, int action, Throwable t) {
        t.printStackTrace();

        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
        // 分享失败的统计
        ShareSDK.logDemoEvent(4, platform);
    }

    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg) {
        if (msg.obj instanceof Platform) {
            Platform platform = (Platform) msg.obj;
            String name = getPlatformName(platform);
            boolean isWechat = "WechatMoments".equals(platform.getName()) || "Wechat".equals(platform.getName());
            switch (msg.what) {
                case MSG_TOAST: {
                    String text = String.valueOf(msg.obj);
                    Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                break;
                case MSG_ACTION_CCALLBACK: {
                    switch (msg.arg1) {
                        case 1: {
                            // 成功
                            if (!isWechat) {
                                Intent intent = new Intent();
                                intent.setAction("com.ifeng.share.bind");
                                Bundle bundle = new Bundle();
                                bundle.putInt("bind", Type.BIND_SUCCESS);
                                bundle.putString("plat", name);
                                intent.putExtras(bundle);
                                getContext().sendBroadcast(intent);
                            }
                            //分享成功
                            if (shareStatusListener != null)
                                shareStatusListener.shareSuccess(platform);
                        }
                        break;
                        case 2: {
                            // 失败
                            String expName = msg.obj.getClass().getSimpleName();
                            if ("WechatClientNotExistException".equals(expName)
                                    || "WechatTimelineNotSupportedException".equals(expName)
                                    || "WechatFavoriteNotSupportedException".equals(expName)) {
                                int resId = getStringRes(getContext(), "wechat_client_inavailable");
                                if (resId > 0) {
                                    Toast toast = Toast.makeText(getContext(), getContext().getString(resId), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            } else if ("QQClientNotExistException".equals(expName)) {
                                int resId = getStringRes(getContext(), "qq_client_inavailable");
                                if (resId > 0) {
                                    Toast toast = Toast.makeText(getContext(), getContext().getString(resId), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            } else {
                                //分享失败
                                if (shareStatusListener != null)
                                    shareStatusListener.shareError(platform);
                            }
                        }
                        break;
                        case 3: {
                            // 取消
                            int resId = getStringRes(getContext(), "share_canceled");
                            if (resId > 0) {
                                //分享取消
                                if (shareStatusListener != null)
                                    shareStatusListener.shareCancel(platform);
                            }
                        }
                        break;
                    }
                }
                break;
                case MSG_CANCEL_NOTIFY: {
                    NotificationManager nm = (NotificationManager) msg.obj;
                    if (nm != null) {
                        nm.cancel(msg.arg1);
                    }
                }
                break;
            }
        }
        return false;
    }

    private String getPlatformName(Platform plat) {
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

    // 在状态栏提示分享操作
    private void showNotification(long cancelTime, String text) {
        try {
            Context app = getContext().getApplicationContext();
            NotificationManager nm = (NotificationManager) app
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            final int id = Integer.MAX_VALUE / 13 + 1;
            nm.cancel(id);
            Notification notification = new NotificationCompat.Builder(mContext)
                    // 设置显示在状态栏的通知提示信息
                    .setTicker("下载完成")
                            // 设置消息标题
                    .setContentTitle(text)
                            // 设置消息图标
                    .setSmallIcon(notifyIcon)
                            // 设置该通知自动消失
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.FLAG_AUTO_CANCEL)
                    .build();
            nm.notify(id, notification);

            if (cancelTime > 0) {
                Message msg = new Message();
                msg.what = MSG_CANCEL_NOTIFY;
                msg.obj = nm;
                msg.arg1 = id;
                ILog.d("Onekey", "====plat=msgmsg=" + msg);
                UIHandler.sendMessageDelayed(msg, cancelTime, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnShareStatusListener shareStatusListener;

    public void setOnShareStatusListener(OnShareStatusListener shareStatusListener) {
        this.shareStatusListener = shareStatusListener;
    }

    public interface OnShareStatusListener {
        public void shareSuccess(Platform platform);

        public void shareError(Platform platform);

        public void shareCancel(Platform platform);
    }
}
