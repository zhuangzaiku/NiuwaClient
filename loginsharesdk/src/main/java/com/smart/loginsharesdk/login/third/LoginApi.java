package com.smart.loginsharesdk.login.third;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;

import static com.mob.tools.utils.R.getStringRes;

public class LoginApi implements Callback {
    private static final int MSG_AUTH_CANCEL = 1;
    private static final int MSG_AUTH_ERROR = 2;
    private static final int MSG_AUTH_COMPLETE = 3;
    private Toast mToast;
    private String platform;
    private Context context;
    private Handler handler;
    private OnLoginResultListener mOnLoginResultListener;

    public LoginApi() {
        handler = new Handler(Looper.getMainLooper(), this);
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setOnLoginResultListener(OnLoginResultListener onLoginResultListener) {
        mOnLoginResultListener = onLoginResultListener;
    }


    public void makeToast(String s) {
        if (mToast == null) {
            mToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(s);
        }
//        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public void login(Context context) {
        this.context = context.getApplicationContext();
        if (platform == null) {
            return;
        }

        Platform plat = ShareSDK.getPlatform(platform);
        if (plat == null) {
            return;
        }
        if (plat != null && plat.isAuthValid()) {
            plat.removeAccount(true);
        }
//        http://bbs.mob.com/forum.php?mod=viewthread&tid=20881&highlight=40125
//        {"errcode":40125,"errmsg":"invalid appsecret, view more at http:\/\/t.cn\/RAEkdVq, hints: [ req_id: _Gl1nA0807ns85 ]"}
        Log.i("LoginApi", "LoginApi==isAuthValid==" + plat.isAuthValid());
        //使用SSO授权，通过客户单授权
        plat.SSOSetting(false);
        plat.setPlatformActionListener(new PlatformActionListener() {
            public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
                Log.i("LoginApi", "LoginApi=onComplete=action==" + action);
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_COMPLETE;
                    msg.arg2 = action;
                    msg.obj = new Object[]{plat, res};
                    handler.sendMessage(msg);
                }
            }

            public void onError(Platform plat, int action, Throwable t) {
                Log.i("LoginApi", "LoginApi=onError=action==" + action);
                Log.i("LoginApi", "LoginApi=onError=action=t=" + t);
                t.printStackTrace();
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_ERROR;
                    msg.arg2 = action;
                    msg.obj = t;
                    handler.sendMessage(msg);
                }
            }

            public void onCancel(Platform plat, int action) {
                Log.i("LoginApi", "LoginApi=onCancel=action==" + action);
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_CANCEL;
                    msg.arg2 = action;
                    msg.obj = plat;
                    handler.sendMessage(msg);
                }
            }
        });
        plat.showUser(null);
//        plat.authorize();
    }

    /**
     * 处理操作结果
     */
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                // 取消
                Platform plat = (Platform) msg.obj;
                if (mOnLoginResultListener != null) {
                    mOnLoginResultListener.loginCancel(plat);
                }
            }
            break;
            case MSG_AUTH_ERROR: {
                // 失败
                String expName = msg.obj.getClass().getSimpleName();
                if ("WechatClientNotExistException".equals(expName)
                        || "WechatTimelineNotSupportedException".equals(expName)
                        || "WechatFavoriteNotSupportedException".equals(expName)) {
                    int resId = getStringRes(context, "wechat_client_inavailable");
                    if (resId > 0) {
                        makeToast(context.getString(resId));
                    }
                } else if ("QQClientNotExistException".equals(expName)) {
                    int resId = getStringRes(context, "qq_client_inavailable");
                    if (resId > 0) {
                        makeToast(context.getString(resId));
                    }
                } else {
                    //分享失败
                    Throwable t = (Throwable) msg.obj;
                    if (mOnLoginResultListener != null) {
                        mOnLoginResultListener.loginError(t);
                    }
                }
            }
            break;
            case MSG_AUTH_COMPLETE: {
                // 成功
                Object[] objs = (Object[]) msg.obj;
                Platform plat = (Platform) objs[0];
                @SuppressWarnings("unchecked")
                HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
                UserInfo userInfo = new UserInfo();
                PlatformDb db = plat.getDb();
                userInfo.setUserId(db.getUserId());
                userInfo.setUserName(db.getUserName());
                userInfo.setUserIcon(db.getUserIcon());
                if("QQ".equals(platform)){
                    if(null != res.get("figureurl_qq_2"))
                    userInfo.setUserIcon((String)res.get("figureurl_qq_2"));
                }
                userInfo.setUserGender(db.getUserGender());
                userInfo.setExpiresIn("" + db.getExpiresIn());
                userInfo.setExpiresTime("" + db.getExpiresTime());
                userInfo.setUserToken(db.getToken());
                if (mOnLoginResultListener != null) {
                    mOnLoginResultListener.loginSuccess(userInfo, plat, res);
                }

            }
            break;
        }
        return false;
    }

}
