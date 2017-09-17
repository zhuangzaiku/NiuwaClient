package tv.niuwa.live.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.smart.androidutils.utils.LogUtils;
import com.smart.androidutils.utils.NetworkUtils;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.loginsharesdk.login.third.LoginApi;
import com.smart.loginsharesdk.login.third.OnLoginResultListener;
import com.smart.loginsharesdk.login.third.ThirdLogin;
import com.smart.loginsharesdk.login.third.UserInfo;
import tv.niuwa.live.MainActivity;
import tv.niuwa.live.R;
import tv.niuwa.live.XieyiActivity;
import tv.niuwa.live.core.BaseSiSiEditActivity;
import tv.niuwa.live.event.BroadCastEvent;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.view.SFProgrssDialog;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import de.greenrobot.event.EventBus;


public class LoginActivity extends BaseSiSiEditActivity implements OnLoginResultListener {

    private static String TAG = LoginActivity.class.getName();
    public static String FINISH_EVENT="LoginActivity finish";

    private final int THIRD_LOGIN_SUCCESS = 0;
    private final int THIRD_LOGIN_ERROR = 1;
    private final int THIRD_LOGIN_CANCEL = 2;

//    @Bind(R.id.text_top_title)
//    TextView mTextTopTitle;
    @Bind(R.id.player_surface)
    SurfaceView mPlayerSurface;
    @Bind(R.id.login_bg_image)
    ImageView mLoginBgImage;
//    @Bind(R.id.image_top_back)
//    ImageView mImageTopBack;
//    @OnClick(R.id.image_top_back)
//    public void back(View view) {
//        LoginActivity.this.finish();
//    }

    private ThirdLogin mThirdLogin;
    private SFProgrssDialog mProgressDialog;

    @OnClick(R.id.tv_xieyi)
    public void openXieyi(){
        //TODO 跳转协议页面
        XieyiActivity.newInstance(LoginActivity.this);
    }
    @OnClick(R.id.tv_loginbyphone)
    public void loginByPhone(){
        //TODO 跳转手机注册
        LoginByPhoneActivity.newInstance(this);
    }


    @OnClick(R.id.linear_login_weichat_container)
    public void loginWeChat(View view) {
        if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
            if (mThirdLogin != null) {
                mProgressDialog = SFProgrssDialog.show(this,"请稍后...");
                mThirdLogin.loginWeChat(LoginActivity.this).setOnLoginResultListener(this);
            }
        } else {
            toast("网络不给力~~");
        }
    }

    @OnClick(R.id.linear_login_qq_container)
    public void loginQQ(View view) {
        if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
            if (mThirdLogin != null) {
                mProgressDialog = SFProgrssDialog.show(this,"请稍后...");
                mThirdLogin.loginQQ(LoginActivity.this).setOnLoginResultListener(this);
            }
        } else {
            toast("网络不给力~~");
        }
    }

    @OnClick(R.id.linear_login_sina_container)
    public void loginSina(View view) {
        if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
            if (mThirdLogin != null) {
                mProgressDialog = SFProgrssDialog.show(this,"请稍后...");
                mThirdLogin.loginSina(LoginActivity.this).setOnLoginResultListener(this);
            }
        } else {
            toast("网络不给力~~");
        }
    }



    SurfaceHolder mSurfaceHolder;
    KSYMediaPlayer ksyMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mTextTopTitle.setText("登录");
//        mImageTopBack.setVisibility(View.GONE);
        mThirdLogin = new ThirdLogin();
        mSurfaceHolder = mPlayerSurface.getHolder();
        mSurfaceHolder.addCallback(mSurfaceCallback);
        mPlayerSurface.setKeepScreenOn(true);
        ksyMediaPlayer = new KSYMediaPlayer.Builder(this).build();
        ksyMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        ksyMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        ksyMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        ksyMediaPlayer.setOnInfoListener(mOnInfoListener);
        ksyMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangeListener);
        ksyMediaPlayer.setOnErrorListener(mOnErrorListener);
        ksyMediaPlayer.setOnSeekCompleteListener(mOnSeekCompletedListener);
        ksyMediaPlayer.setScreenOnWhilePlaying(true);
        ksyMediaPlayer.setBufferTimeMax(1);
        ksyMediaPlayer.setTimeout(5, 30);
        ksyMediaPlayer.setPlayerMute(0);
        ksyMediaPlayer.setVolume(0,0);
        ksyMediaPlayer.setLooping(true);
        try {
            ksyMediaPlayer.setDataSource(Api.LOGIN_BG);
            ksyMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ksyMediaPlayer != null) {
            ksyMediaPlayer.release();
            ksyMediaPlayer = null;
        }
        mPlayerSurface = null;
        EventBus.getDefault().unregister(this);
    }

    private final SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (ksyMediaPlayer != null && ksyMediaPlayer.isPlaying())
                ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (ksyMediaPlayer != null)
                ksyMediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "surfaceDestroyed");
            if (ksyMediaPlayer != null) {
                ksyMediaPlayer.setDisplay(null);
            }
        }
    };
    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {

//            mVideoWidth = ksyMediaPlayer.getVideoWidth();
//            mVideoHeight = ksyMediaPlayer.getVideoHeight();

            // Set Video Scaling Mode
            ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

            //start player
            ksyMediaPlayer.start();


        }
    };
    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {

        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangeListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {

        }
    };

    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompletedListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            Log.e(TAG, "onSeekComplete...............");
        }
    };

    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
            //    toast("OnCompletionListener, play complete.");
//            videoPlayEnd();
        }
    };

    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            switch (what) {
                case KSYMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Log.e(TAG, "OnErrorListener, Error Unknown:" + what + ",extra:" + extra);
                    break;
                default:
                    Log.e(TAG, "OnErrorListener, Error:" + what + ",extra:" + extra);
            }

//            videoPlayEnd();

            return false;
        }
    };

    public IMediaPlayer.OnInfoListener mOnInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            switch (i) {
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Log.d(TAG, "Buffering Start.");
                    break;
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    Log.d(TAG, "Buffering End.");
                    break;
                case KSYMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:

                    break;
                case KSYMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    mLoginBgImage.setVisibility(View.GONE);

                    break;
                case KSYMediaPlayer.MEDIA_INFO_SUGGEST_RELOAD:

                    break;
                case KSYMediaPlayer.MEDIA_INFO_RELOADED:

                    return false;
            }
            return false;
        }
    };

    @Override
    public int getLayoutResource() {
        return R.layout.activity_login2;
    }

    @Override
    public void loginSuccess(UserInfo userInfo, Platform platform, HashMap<String, Object> res) {
        LogUtils.i(TAG, "loginSuccess");
        String platformName = platform.getName();
        String plat = "";
        if ("QQ".equals(platformName)) {
            plat = "QQ";
        } else if ("SinaWeibo".equals(platformName)) {
            plat = "SinaWeibo";
        } else if ("Wechat".equals(platformName)) {
            plat = "Wechat";
        }
        //第三方登录成功后,对接自己的接口,上报第三方用户数据
        //暂且提示登录成功
        JSONObject params = new JSONObject();
        params.put("from", plat);
        params.put("name", userInfo.getUserName());
        params.put("head_img", userInfo.getUserIcon());
        params.put("sex", userInfo.getUserGender());
        params.put("access_token", userInfo.getUserToken());
        params.put("expires_date", userInfo.getExpiresTime());
        params.put("openid", userInfo.getUserId());
        Api.thirdLogin(LoginActivity.this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                String token = data.getString("token");
                JSONObject userInfo = data.getJSONObject("data");
                if (token != null) {
                    SharePrefsUtils.put(LoginActivity.this, "user", "token", token);
                    SharePrefsUtils.put(LoginActivity.this, "user", "userId", userInfo.getString("id"));
                }
                uiHandler.sendEmptyMessage(THIRD_LOGIN_SUCCESS);
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });

    }

    @Override
    public void loginError(Throwable t) {
        LogUtils.i(TAG, "loginError");
        uiHandler.sendEmptyMessage(THIRD_LOGIN_ERROR);
    }

    @Override
    public void loginCancel(Platform plat) {
        LogUtils.i(TAG, "loginCancel");
        uiHandler.sendEmptyMessage(THIRD_LOGIN_CANCEL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mThirdLogin != null) {
            ArrayList<LoginApi> loginApi = mThirdLogin.getLoginApi();
            if (loginApi != null) {
                for (int i = 0; i < loginApi.size(); i++) {
                    loginApi.get(i).cancelToast();
                }
            }
        }
    }



    @Subscribe
    public void onEvent(BroadCastEvent event){
        if(event.getContent().endsWith(LoginActivity.FINISH_EVENT)){
            finish();
        }

    }


    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == THIRD_LOGIN_SUCCESS) {
                //成功
                if(null != mProgressDialog)
                mProgressDialog.dismiss();
                toast("登录成功√");
                openActivity(MainActivity.class);
                LoginActivity.this.finish();
            } else if (msg.what == THIRD_LOGIN_ERROR) {
                //失败
                if(null != mProgressDialog)
                    mProgressDialog.dismiss();
                toast("登录失败!");
            } else if (msg.what == THIRD_LOGIN_CANCEL) {
                //取消
                if(null != mProgressDialog)
                    mProgressDialog.dismiss();
                toast("登录取消!");
            }
        }
    };
}
