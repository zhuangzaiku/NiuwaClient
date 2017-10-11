package tv.niuwa.live.living;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bru.toolkit.utils.screenrotate.OrientationSensorListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.rtc.kit.KSYRtcStreamer;
import com.ksyun.media.rtc.kit.RTCClient;
import com.ksyun.media.rtc.kit.RTCConstants;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.kit.OnAudioRawDataListener;
import com.ksyun.media.streamer.kit.OnPreviewFrameListener;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.logstats.StatsLogReport;
import com.bru.toolkit.views.HorizontalListView;
import com.smart.androidui.widget.input.SoftKeyboardStateHelper;
import com.smart.androidutils.activity.BaseActivity;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.LogUtils;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.SoftKeyboardUtils;
import com.smart.androidutils.utils.StringUtils;
import com.smart.loginsharesdk.share.OnShareStatusListener;
import com.smart.loginsharesdk.share.ThirdShare;
import com.smart.loginsharesdk.share.onekeyshare.Type;

import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import master.flame.danmaku.ui.widget.DanmakuView;
import tv.niuwa.live.MainActivity;
import tv.niuwa.live.MyApplication;
import tv.niuwa.live.R;
import tv.niuwa.live.danmaku.DanmaManager;
import tv.niuwa.live.home.model.VideoItem;
import tv.niuwa.live.intf.OnCustomClickListener;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.lean.Chat;
import tv.niuwa.live.lean.ConversationListActivity;
import tv.niuwa.live.living.adapter.DanmuAdapter;
import tv.niuwa.live.living.adapter.OnlineUserAdapter;
import tv.niuwa.live.living.model.DanmuModel;
import tv.niuwa.live.living.model.GiftModel;
import tv.niuwa.live.living.model.GiftSendModel;
import tv.niuwa.live.living.model.UserModel;
import tv.niuwa.live.own.UserMainActivity;
import tv.niuwa.live.own.userinfo.ContributionActivity;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.utils.DialogEnsureUtiles;
import tv.niuwa.live.utils.Util;
import tv.niuwa.live.view.BubbleView;
import tv.niuwa.live.view.SFProgrssDialog;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.event.LCIMIMTypeMessageEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarTextEvent;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMNotificationUtils;
import cn.sharesdk.framework.Platform;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

public class LivingActivity extends BaseActivity implements TextureView.SurfaceTextureListener, AdapterView.OnItemClickListener, View.OnClickListener, OnShareStatusListener {
    //rtc
    private final static int PERMISSION_REQUEST_CAMERA_AUDIOREC = 1;
    private static final String TAG = "LivingActivity";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private OrientationSensorListener listener;
    private final String RTC_AUTH_SERVER = "http://rtc.vcloud.ks-live.com:6002/rtcauth";
    private final String RTC_AUTH_URI = "https://rtc.vcloud.ks-live.com:6001";
    private final String RTC_UINIQUE_NAME = "apptest";
    public List<GiftSendModel> giftSendModelList = new ArrayList<GiftSendModel>();
    public GiftSendModel mGiftSendModel1;
    public GiftSendModel mGiftSendModel2;
    @Bind(R.id.live_top_layer)
    public FrameLayout mLiveTopLayer;
    public String balance;
    public VideoItem mVideoItem;
    public String token;
    public String userId;//用户id
    public String user_nicename;
    public String user_level;
    public String avatar;
    @Bind(R.id.audience_vote_rl)
    RelativeLayout audience_vote_rl;
    @Bind(R.id.gift_layout1)
    GiftFrameLayout giftFrameLayout1;
    @Bind(R.id.gift_layout2)
    GiftFrameLayout giftFrameLayout2;
    @Bind(R.id.live_viewflipper)
    ViewFlipper mLiveViewFlipper;
    @Bind(R.id.danmu_container)
    RelativeLayout mDanmuContainer;
    @Bind(R.id.danmu_check_box)
    CheckBox mDanmuCheckBox;
    //    DanmuAdapter mDanmuadapter;
    OnlineUserAdapter mOnlineUserAdapter;
    @Bind(R.id.live_share)
    ImageButton mLiveShare;
    @Bind(R.id.live_gift)
    ImageButton mLiveGift;
    @Bind(R.id.live_lianmai)
    ImageView mLiveLianMai;
    @Bind(R.id.live_user_avatar)
    CircleImageView mLiveUserAvatar;
    @Bind(R.id.live_user_nicename)
    TextView mLiveUserNicename;
    @Bind(R.id.live_user_online_num)
    TextView mLiveUserOnlineNum;
    @Bind(R.id.HorizontalListView)
    HorizontalListView mLiveOnlineUsers;
    @Bind(R.id.live_user_total)
    TextView mLiveUserTotal;
    @Bind(R.id.live_user_id)
    TextView mLiveUserId;
    @Bind(R.id.live_bottom_btn)
    RelativeLayout mLiveBottomBtn;
    @Bind(R.id.live_bottom_send)
    LinearLayout mLiveBottomSend;
    @Bind(R.id.live_message_container_rl)
    RelativeLayout live_message_container_rl;
    @Bind(R.id.live_btn_send)
    Button mLiveBtnSend;
    @Bind(R.id.living_gift_big)
    ImageView mLivingGiftBig;
    @Bind(R.id.live_camera)
    ImageButton mLiveCamera;
    @Bind(R.id.camera_reverse)
    ImageView mCameraReverse;
    @Bind(R.id.image_own_unread)
    ImageView mImageOwnUnread;
    @Bind(R.id.pause)
    ImageView mPauseBtn;
    @Bind(R.id.pause_layout)
    RelativeLayout mPauseLayout;
    @Bind(R.id.live_meiyan)
    ImageButton mLiveMeiyan;
    @Bind(R.id.live_gift_container)
    LinearLayout mLiveGiftContainer;
    @Bind(R.id.live_gift_scroll)
    ScrollView mLiveGiftScroll;
    @Bind(R.id.live_edit_input)
    EditText mLiveEditInput;
    @Bind(R.id.frame_living_root_container)
    FrameLayout mFrameLivingRootContainer;
    @Bind(R.id.living_danmu_container)
    LinearLayout mLivingDanmuContainer;
    @Bind(R.id.live_music)
    ImageView mLiveMusic;
    @Bind(R.id.linear_live_top_user_container)
    RelativeLayout mLinearLiveTopUserContainer;
    @Bind(R.id.pauseCover)
    ImageView mPauseCover;
    @Bind(R.id.gift_container)
    LinearLayout mGiftContainer;
    @Bind(R.id.live_end_container)
    LinearLayout mLiveEndContainer;
    @Bind(R.id.live_more)
    ImageView mLiveMore;
    @Bind(R.id.lianmai_stop)
    TextView mLianmaiStop;
    @Bind(R.id.btn_follow)
    Button mbtnFollow;
    @Bind(R.id.iv_zhibo_close_icon)
    CircleImageView iv_zhibo_close_icon;
    @Bind(R.id.tv_zhibo_close_username)
    TextView tv_zhibo_close_username;
    @Bind(R.id.tv_zhibo_close_num)
    TextView tv_zhibo_close_num;
    @Bind(R.id.live_send)
    ImageButton mLiveSend;

    @Bind(R.id.sv_danmaku)
    DanmakuView mDanmakuView;
    DanmaManager mDanmaManager;

    CircleImageView mDialogUserAvatar;
    TextView mDialogUserNicename;
    ImageView mDialogClose;
    CircleImageView mDialogUserAvatarSmall;
    CircleImageView mDialogUserAvatarSmall1;
    ImageView mDialogUserSex;
    TextView mDialogUserLevel;
    TextView mDialogUserId;
    TextView mDialogUserLocation;
    TextView mDialogUserSignature;
    TextView mDialogUserSpend;
    TextView mDialogUserAttentionNum;
    TextView mDialogUserFansNum;
    TextView mDialogUserTotal;
    ImageView mDialogUserReal;
    TextView mDialogUserAttention;
    TextView mDialogPrivateMessage;
    TextView mDialogUserprivateMessage;
    TextView mDialogUserMain;
    LinearLayout dialogUserChangkong;
    TextView mDialogCaozuo;
    LinearLayout mDialogAttentionArea;
    LinearLayout mDialogFansArea;
    LinearLayout mDialogTotalArea;
    JSONObject channelInfo;
    SoftKeyboardStateHelper helper;
    BubbleView bubbleView;
    //礼物
    GiftSendModel mTempGiftSendModel1;
    GiftSendModel mTempGiftSendModel2;
    ArrayList<GiftSendModel> bigAnim = new ArrayList();
    AnimatorSet animatorSet1;
    AnimatorSet animatorSet2;
    //实时数据
    Handler dataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private GLSurfaceView mCameraPreviewView = null;
    private KSYRtcStreamer mStreamer;
    private AuthHttpTask mRTCAuthTask;
    private AuthHttpTask.KSYOnHttpResponse mRTCAuthResponse;
    private boolean mIsRegisted;
    private boolean mIsConnected;
    private boolean mHWEncoderUnsupported;
    private boolean mSWEncoderUnsupported;
    private Handler mMainHandler;
    private boolean CLICKED_HEARD = false;//是否已经点亮
    //    private ArrayList<DanmuModel> mDanmuItems;
    private ArrayList<UserModel> mUserItems;
    private AlertDialog userInfoDialog;
    private String otherUserId;//点击item获取当前用户id
    private String channel_creater;//主播id
    private AVIMConversation mSquareConversation;
    private JSONArray sysMessage;
    private GiftFragment mGiftFragment;
    private VoteFragment mVoteFragment;
    private Handler myHandler;

    //漂浮心
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            myHandler.sendMessage(message);
        }
    };
    private boolean mPause = false;
    private KSYMediaPlayer ksyMediaPlayer;
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
    // SurfaceView需在Layout中定义，此处不在赘述
    private Surface mSurface = null;
    private SurfaceView mVideoSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;
    private TextureView mVideoTextureView = null;
    private SurfaceTexture mSurfaceTexture = null;
    private String mDataSource;
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;
    private Context mContext;
    private String guard_status = "0";
    private String channel_like = "0";

    @Bind(R.id.like_num)
    TextView mLikeNum;
    Runnable dataRunnable = new Runnable() {
        @Override
        public void run() {
            JSONObject params = new JSONObject();
            params.put("token", token);
            params.put("room_id", channelInfo.getString("room_id"));
            Api.getLiveRealTimeNum(LivingActivity.this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data1) {
                    if (isActive) {
                        JSONObject data = data1.getJSONObject("data");
                        if (null != mLiveUserOnlineNum && null != mLiveUserTotal) {
                            mLiveUserTotal.setText(getEarn(data.getString("total_earn")));
                            guard_status = data.getString("guard_status");
                            channel_like = data.getString("channel_like");
                            if(TextUtils.isEmpty(channel_like)) {
                                channel_like = "0";
                            }
                            mLikeNum.setText(channel_like);
                            mLiveUserOnlineNum.setText(data.getString("online_num"));
                            dataHandler.postDelayed(dataRunnable, 2000);
                        }
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    switch (code) {
                        case 502:
                            if (ksyMediaPlayer != null) {
                                ksyMediaPlayer.pause();
                            }
                            mLiveEndContainer.setVisibility(View.VISIBLE);
                            toast("直播结束");
                            break;
                        case 506:
                            if (ksyMediaPlayer != null) {
                                ksyMediaPlayer.pause();
                            }
                            mLiveEndContainer.setVisibility(View.VISIBLE);
                            toast("直播被封禁");
                            break;
                        case 500:
                            toast(msg);
                            break;
                        default:
                            dataHandler.postDelayed(dataRunnable, 2000);
                            break;
                    }

                }
            });
        }
    };

    Runnable addScoreRunnable = new Runnable() {
        @Override
        public void run() {
            JSONObject params = new JSONObject();
            params.put("userId", userId);
            Api.addScore(LivingActivity.this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data1) {
                }

                @Override
                public void requestFailure(int code, String msg) {
                }
            });
        }
    };

    private SFProgrssDialog loading_dialog;
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
                    //toast("Audio Rendering Start");
                    if (null != loading_dialog) {
                        loading_dialog.dismiss();
                    }
                    break;
                case KSYMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    //toast("Video Rendering Start");
                    if (null != loading_dialog) {
                        loading_dialog.dismiss();
                    }
                    break;
                case KSYMediaPlayer.MEDIA_INFO_SUGGEST_RELOAD:
                    // Player find a new stream(video or audio), and we could reload the video.
                    if (ksyMediaPlayer != null)
                        ksyMediaPlayer.reload(mDataSource, false);
                    break;
                case KSYMediaPlayer.MEDIA_INFO_RELOADED:
                    //toast("Succeed to reload video.");
                    Log.d(TAG, "Succeed to reload video.");
                    return false;
            }
            return false;
        }
    };
    private PopupWindow mPopupShareWindow;
    private PopupWindow mPopupCaozuoWindow;

    //礼物end
    private ThirdShare mThirdShare;
    private Boolean danmuChecked = false;
    private float mLastX;
    private float mLastY;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {

            mVideoWidth = ksyMediaPlayer.getVideoWidth();
            mVideoHeight = ksyMediaPlayer.getVideoHeight();

            // Set Video Scaling Mode
            ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

            //start player
            if (!isPaused)
                ksyMediaPlayer.start();


        }
    };
    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            long duration = ksyMediaPlayer.getDuration();
            long progress = duration * percent / 100;
//            mPlayerSeekbar.setSecondaryProgress((int)progress);
        }
    };
    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangeListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
            if (mVideoWidth > 0 && mVideoHeight > 0) {
                if (width != mVideoWidth || height != mVideoHeight) {
                    mVideoWidth = mp.getVideoWidth();
                    mVideoHeight = mp.getVideoHeight();

                    if (ksyMediaPlayer != null)
                        ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                }
            }
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
    private StatsLogReport.OnLogEventListener mOnLogEventListener =
            new StatsLogReport.OnLogEventListener() {
                @Override
                public void onLogEvent(StringBuilder singleLogContent) {
                    Log.i(TAG, "***onLogEvent : " + singleLogContent.toString());
                }
            };
    private OnAudioRawDataListener mOnAudioRawDataListener = new OnAudioRawDataListener() {
        @Override
        public short[] OnAudioRawData(short[] data, int count) {
            Log.d(TAG, "OnAudioRawData data.length=" + data.length + " count=" + count);
            //audio pcm data
            return data;
        }
    };
    private OnPreviewFrameListener mOnPreviewFrameListener = new OnPreviewFrameListener() {
        @Override
        public void onPreviewFrame(byte[] data, int width, int height, boolean isRecording) {
            Log.d(TAG, "onPreviewFrame data.length=" + data.length + " " +
                    width + "x" + height + " isRecording=" + isRecording);
        }
    };
    private RTCClient.RTCErrorListener mRTCErrorListener = new RTCClient.RTCErrorListener() {
        @Override
        public void onError(int errorType, int arg1) {
            switch (errorType) {
                case RTCClient.RTC_ERROR_AUTH_FAILED:
                    doAuthFailed();
                    break;
                case RTCClient.RTC_ERROR_REGISTED_FAILED:
                    doRegisteredFailed(arg1);
                    break;
                case RTCClient.RTC_ERROR_SERVER_ERROR:
                case RTCClient.RTC_ERROR_CONNECT_FAIL:
                    doRTCCallBreak();
                    break;
                case RTCClient.RTC_ERROR_STARTED_FAILED:
                    doStartCallFailed(arg1);
                    break;
                default:
                    break;
            }
        }
    };
    private RTCClient.RTCEventChangedListener mRTCEventListener = new RTCClient.RTCEventChangedListener() {
        @Override
        public void onEventChanged(int event, Object arg1) {
            switch (event) {
                case RTCClient.RTC_EVENT_REGISTED:
                    doRegisteredSuccess();
                    break;
                case RTCClient.RTC_EVENT_STARTED:
                    LogUtils.d("zzk RTC_EVENT_STARTED");
                    doStartCallSuccess();
                    break;
                case RTCClient.RTC_EVENT_CALL_COMMING:
                    doReceiveRemoteCall(String.valueOf(arg1));
                    break;
                case RTCClient.RTC_EVENT_STOPPED:
                    Log.d(TAG, "stop result:" + arg1);
                    doStopCallResult();
                    break;
                case RTCClient.RTC_EVENT_UNREGISTED:
                    Log.d(TAG, "unregister result:" + arg1);
                    doUnRegisteredResult();
                    break;
                default:
                    break;
            }

        }
    };

    public void starGiftAnimation(GiftSendModel model) {
        LogUtils.i("delong", model.getGiftCount() + "");
        showBigAnim(model.getGift_id(), model);
        // layout1正在显示你的礼物动画//并且gift num  大于当前的num
        if (giftFrameLayout1.isShowing()) {
            GiftSendModel temp1 = giftFrameLayout1.getSendModel();
            if (null != temp1 && temp1.getGift_id().equals(model.getGift_id()) && temp1.getUserId().equals(model.getUserId()) && model.getGiftCount() > giftFrameLayout1.getRepeatCount()) {
                LogUtils.i("delong", model.getGiftCount() + "------" + mTempGiftSendModel1.getGiftCount());
                giftFrameLayout1.setRepeatCount(model.getGiftCount() - mTempGiftSendModel1.getGiftCount());
                mGiftSendModel1.setGiftCount(model.getGiftCount());
                return;
            }
        }
        //layout2正在显示你的礼物动画
        if (giftFrameLayout2.isShowing()) {
            GiftSendModel temp2 = giftFrameLayout2.getSendModel();
            if (null != temp2 && temp2.getGift_id().equals(model.getGift_id()) && temp2.getUserId().equals(model.getUserId()) && model.getGiftCount() > giftFrameLayout2.getRepeatCount()) {
                giftFrameLayout2.setRepeatCount(model.getGiftCount() - mTempGiftSendModel1.getGiftCount());
                mGiftSendModel2.setGiftCount(model.getGiftCount());
                return;
            }
        }
        if (!giftFrameLayout1.isShowing()) {

            sendGiftAnimation1(giftFrameLayout1, model);
            mGiftSendModel1 = model;
            mTempGiftSendModel1 = new GiftSendModel(model.getGiftCount());
            mTempGiftSendModel1.setGiftCount(model.getGiftCount());
        } else if (!giftFrameLayout2.isShowing()) {
            mTempGiftSendModel2 = new GiftSendModel(model.getGiftCount());
            mTempGiftSendModel2.setGiftCount(model.getGiftCount());
            sendGiftAnimation2(giftFrameLayout2, model);
            mGiftSendModel2 = model;
        } else {
            //如果两个都在显示 将礼物加入缓存队列  如果缓存队列有 就更新次数
            for (int i = 0; i < giftSendModelList.size(); i++) {
                if (giftSendModelList.get(i).getGift_id() == model.getGift_id() && giftSendModelList.get(i).getUserId() == model.getUserId()) {
                    giftSendModelList.get(i).setGiftCount(model.getGiftCount());
                    return;
                }
            }
            giftSendModelList.add(model);
        }
    }

    private void showBigAnim(String giftId, final GiftSendModel model) {
        if (mLivingGiftBig.getVisibility() == View.VISIBLE) {
            bigAnim.add(model);
        } else {
            mLivingGiftBig.setVisibility(View.VISIBLE);
            mLivingGiftBig.setBackground(null);
            int giftBgId = -1;
            switch (giftId) {
//                case "4":
//                    giftBgId = R.drawable.gift_anim_5;
//                    break;
                case "13":
                    giftBgId = R.drawable.gift_anim_1;
                    break;
//                case "48":
//                    giftBgId = R.drawable.gift_anim_3;
//                    break;
//                case "46":
//                    giftBgId = R.drawable.gift_anim_4;
//                    break;
                case "34":
                    giftBgId = R.drawable.gift_anim_cheers;
                    break;
                case "36":
                    giftBgId = R.drawable.gift_anim_feiwen;
                    break;
                case "32":
                    giftBgId = R.drawable.gift_anim_huojian;
                    break;
                case "33":
                    giftBgId = R.drawable.gift_anim_qiuanwei;
                    break;
                case "31":
                    giftBgId = R.drawable.gift_anim_tang;
                    break;
                case "35":
                    giftBgId = R.drawable.gift_anim_youting;
            }
            mLivingGiftBig.setBackgroundResource(giftBgId);
            
            AnimationDrawable animTemp = (AnimationDrawable) mLivingGiftBig.getBackground();
            animTemp.start();
            int duration = 0;
            for (int i = 0; i < animTemp.getNumberOfFrames(); i++) {
                duration += animTemp.getDuration(i);
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (isActive) {
                        mLivingGiftBig.setVisibility(View.GONE);
                        if (null != bigAnim && bigAnim.size() > 0) {
                            GiftSendModel model1 = bigAnim.get(bigAnim.size() - 1);
                            bigAnim.remove(bigAnim.size() - 1);
                            showBigAnim(model1.getGift_id(), model1);
                        } else {
                            mLivingGiftBig.setVisibility(View.GONE);
                        }
                    }

                }
            }, duration);
        }
    }

    private void sendGiftAnimation1(final GiftFrameLayout view, final GiftSendModel model) {
        view.setModel(model);
        //判断前面一个是否是你送的  并且是同一个礼物
        if (null != mGiftSendModel1 && mGiftSendModel1.getGift_id().equals(model.getGift_id()) && mGiftSendModel1.getUserId().equals(model.getUserId())) {
            //设置开始
            if (model.getGiftCount() > 1)
                view.setStarNum(mGiftSendModel1.getGiftCount() + 1);
            //view.setRepeatCount(model.getGiftCount() - mGiftSendModel1.getGiftCount() -1);
            int tem = model.getGiftCount() - mGiftSendModel1.getGiftCount() - 1;
            view.setRepeatCount(tem > 0 ? tem : 0);
            animatorSet1 = view.startAnimation();
        } else {
            view.setStarNum(1);
            animatorSet1 = view.startAnimation();
        }
        animatorSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                synchronized (giftSendModelList) {
                    if (giftSendModelList.size() > 0) {
                        mGiftSendModel1 = giftSendModelList.get(giftSendModelList.size() - 1);
                        sendGiftAnimation1(view, giftSendModelList.get(giftSendModelList.size() - 1));
                        //view.setModel(giftSendModelList.get(giftSendModelList.size() - 1));
                        //view.startAnimation(giftSendModelList.get(giftSendModelList.size() - 1).getGiftCount()-1);
                        giftSendModelList.remove(giftSendModelList.size() - 1);
                    }
                }
            }
        });
    }

    private void sendGiftAnimation2(final GiftFrameLayout view, GiftSendModel model) {
        view.setModel(model);
        //判断前面一个是否是你送的  并且是同一个礼物
        if (null != mGiftSendModel2 && mGiftSendModel2.getGift_id() == model.getGift_id() && mGiftSendModel2.getUserId() == model.getUserId()) {
            //设置开始
            if (model.getGiftCount() > 1)
                view.setStarNum(mGiftSendModel2.getGiftCount() + 1);
            //view.setRepeatCount(model.getGiftCount() - mGiftSendModel1.getGiftCount() -1);
            //view.setRepeatCount(model.getGiftCount() - mGiftSendModel2.getGiftCount() -1);
            int tem = model.getGiftCount() - mGiftSendModel2.getGiftCount() - 1;
            view.setRepeatCount(tem > 0 ? tem : 0);
            animatorSet2 = view.startAnimation();
        } else {
            view.setStarNum(1);
            animatorSet2 = view.startAnimation();
        }
        animatorSet2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                synchronized (giftSendModelList) {
                    if (giftSendModelList.size() > 0) {
                        mGiftSendModel2 = giftSendModelList.get(giftSendModelList.size() - 1);
                        sendGiftAnimation2(view, giftSendModelList.get(giftSendModelList.size() - 1));
                        //view.setModel(giftSendModelList.get(giftSendModelList.size() - 1));
                        //view.startAnimation(giftSendModelList.get(giftSendModelList.size() - 1).getGiftCount()-1);
                        giftSendModelList.remove(giftSendModelList.size() - 1);
                    }
                }
            }
        });
    }

    //@OnClick(R.id.live_share)
    @OnClick(R.id.live_audience_share_iv)
    public void showShare(View view) {
        // int locY = DensityUtils.dip2px(LivingActivity.this, 60);
        // int locX = DensityUtils.dip2px(LivingActivity.this, 45) / 2;
        int locX = 0;
        int locY = 0;
        if (mPopupShareWindow == null || !mPopupShareWindow.isShowing()) {
            View inflate = LayoutInflater.from(LivingActivity.this).inflate(R.layout.layout_shape_dialog, null);
            mPopupShareWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupShareWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopupShareWindow.setFocusable(true);
            mPopupShareWindow.showAtLocation(mLiveShare, Gravity.BOTTOM, locX, locY);
            ImageView shareWechat = (ImageView) inflate.findViewById(R.id.image_live_share_wechat);
            ImageView shareWechatMoment = (ImageView) inflate.findViewById(R.id.image_live_share_wechat_moment);
            shareWechat.setOnClickListener(this);
            shareWechatMoment.setOnClickListener(this);
        } else {
            mPopupShareWindow.showAtLocation(mLiveShare, Gravity.BOTTOM, locX, locY);
        }
    }

    @OnCheckedChanged(R.id.danmu_check_box)
    public void danmuCheckChangerd(CompoundButton buttonView, boolean isChecked) {
        CheckBox temp = (CheckBox) buttonView;
        danmuChecked = temp.isChecked();
//        if (danmuChecked) {
//            mLiveEditInput.setHint("开启大喇叭，1牛币/条");
//        } else {
//            mLiveEditInput.setHint("我也要给主播小主发言...");
//        }
    }

    @OnClick(R.id.image_own_message)
    public void imageOwnMessage(View v) {
        openActivity(ConversationListActivity.class);
    }

    @OnClick(R.id.publish_shop_icon)
    public void publishShopIcon(View v) {
//        JSONObject params = new JSONObject();
//        params.put("token", token);
//        params.put("room_id", mVideoItem.getRoom_id());
//        Api.getShopLink(this, params, new OnRequestDataListener() {
//            @Override
//            public void requestSuccess(int code, JSONObject data) {
//                Bundle data1 = new Bundle();
//                data1.putString("jump", data.getJSONObject("info").getString("shop_url"));
//                data1.putString("title", mVideoItem.getUser_nicename());
//                openActivity(WebviewActivity.class, data1);
//            }
//
//            @Override
//            public void requestFailure(int code, String msg) {
//                toast(msg);
//            }
//        });

    }

    private boolean isPaused = true;

    @OnClick(R.id.pause_layout)
    public void pauseAndResume(View v) {
        if (ksyMediaPlayer != null) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mPauseBtn.getLayoutParams();
            if (isPaused) {
                mPauseCover.setVisibility(View.GONE);
                mPauseLayout.setBackgroundResource(R.drawable.img_kaiguan01);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                ksyMediaPlayer.start();
            } else {
                mPauseCover.setVisibility(View.VISIBLE);
                mPauseLayout.setBackgroundResource(R.drawable.img_kaiguan02);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                ksyMediaPlayer.pause();
            }
            mPauseBtn.setLayoutParams(lp);
            isPaused = !isPaused;
        }
    }

    @OnClick(R.id.lianmai_stop)
    public void lianmaiClose(View v) {
        if (null != mStreamer) {
            mStreamer.getRtcClient().stopCall();
            v.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(final View v) {
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("room_id", mVideoItem.getRoom_id());
        Api.getShareInfo(LivingActivity.this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONObject info = data.getJSONObject("data");
                if (mPopupShareWindow != null && mPopupShareWindow.isShowing()) {
                    mPopupShareWindow.dismiss();
                }
                switch (v.getId()) {
                    case R.id.image_live_share_wechat:
                        //toast("wechat");
                        mThirdShare.setTitle(info.getString("title"));
                        mThirdShare.setText(info.getString("content"));
                        mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                        mThirdShare.setImageType(Type.IMAGE_NETWORK);
                        mThirdShare.setImageUrl(info.getString("image"));
                        mThirdShare.setUrl(info.getString("url"));
                        mThirdShare.share2Wechat();
                        break;
                    case R.id.image_live_share_wechat_moment:
                        //toast("wechat moment");
                        mThirdShare.setTitle(info.getString("title"));
                        mThirdShare.setText(info.getString("content"));
                        mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                        mThirdShare.setImageType(Type.IMAGE_NETWORK);
                        mThirdShare.setImageUrl(info.getString("image"));
                        mThirdShare.setUrl(info.getString("url"));
                        mThirdShare.share2WechatMoments();
                        break;
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });

        if (mPopupShareWindow != null && mPopupShareWindow.isShowing()) {
            mPopupShareWindow.dismiss();
        }
    }

    @OnClick(R.id.btn_like)
    public void clickHeart() {
        if (!CLICKED_HEARD) {
            DanmuModel model = new DanmuModel();
            model.setType("4");
            model.setUserName(user_nicename);
            model.setUserLevel(user_level);
            model.setAvatar(avatar);
            model.setUserId(userId);
            model.setContent("点亮了❤");
            sendMessage(model);
            CLICKED_HEARD = true;
            JSONObject params = new JSONObject();
            params.put("token", token);
            params.put("room_id", mVideoItem.getRoom_id());
            Api.addLike(this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {

                }

                @Override
                public void requestFailure(int code, String msg) {

                }
            });
        }

    }

    @OnClick(R.id.btn_close)
    public void clickClose(View view) {
        if (ksyMediaPlayer != null)
            ksyMediaPlayer.release();
        ksyMediaPlayer = null;
        finish();
    }

    //@OnClick(R.id.live_gift)
    @OnClick(R.id.live_audience_gift_iv)
    public void liveGift(View view) {
        mLivingDanmuContainer.setVisibility(View.GONE);
        mLiveBottomBtn.setVisibility(View.GONE);
        //mGiftContainer.setVisibility(View.VISIBLE);
        showGift();
    }

//    @OnClick(R.id.live_audience_rec_iv)
//    public void liveRecord(View view) {
//    }

    @OnClick(R.id.live_audience_vote_iv)
    public void liveVote(View view) {
        showVote();
        audience_vote_rl.setVisibility(View.VISIBLE);
        mLiveBottomBtn.setVisibility(View.GONE);
    }

    @OnClick(R.id.audience_vote_rl)
    public void audience_vote_rl(View view) {
        audience_vote_rl.setVisibility(View.GONE);
        mLiveBottomBtn.setVisibility(View.VISIBLE);
    }

    private void showGift() {
        if (mGiftFragment == null) {
            mGiftFragment = new GiftFragment();
            FragmentManager mManager = getSupportFragmentManager();
            FragmentTransaction mTransaction = mManager.beginTransaction();
            mTransaction.addToBackStack("");
            mTransaction.add(R.id.gift_container, mGiftFragment);
            mTransaction.commit();
        }
        mGiftContainer.setVisibility(View.VISIBLE);
    }

    private void showVote() {
        if (mVoteFragment == null) {
            mVoteFragment = new VoteFragment();
            FragmentManager mManager = getSupportFragmentManager();
            FragmentTransaction mTransaction = mManager.beginTransaction();
            mTransaction.addToBackStack("");
            mTransaction.add(R.id.audience_vote_rl, mVoteFragment);
            mTransaction.commit();
        }
        audience_vote_rl.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGiftContainer.getVisibility() == View.VISIBLE) {
            mGiftContainer.setVisibility(View.GONE);
            mLivingDanmuContainer.setVisibility(View.VISIBLE);
            mLiveBottomBtn.setVisibility(View.VISIBLE);
        }

        return super.onTouchEvent(event);
    }

    @OnClick(R.id.btn_living_back_home)
    public void livingBackHome(View view) {
        Intent intent = new Intent(LivingActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.live_close)
    public void close(View view) {
        if (ksyMediaPlayer != null)
            ksyMediaPlayer.release();
        ksyMediaPlayer = null;
        finish();
    }

    @OnClick(R.id.btn_living_share)
    public void livingShareDialog(View view) {
    }

    @OnClick(R.id.live_user_avatar)
    public void getUserInfo(View view) {
        ImageView temp = (ImageView) view;
        String uid = (String) temp.getTag(R.id.image_live_avatar);
        otherUserId = uid;
        showUserInfoDialogById(uid);
    }

    @OnClick(R.id.btn_follow)
    public void follow(final View view) {
        String uid = (String) mLiveUserAvatar.getTag(R.id.image_live_avatar);
        otherUserId = uid;
        JSONObject requestParams = new JSONObject();
        requestParams.put("token", token);
        requestParams.put("userid", otherUserId);
        final Button v = (Button) view;
        if ("关注".equals(v.getText())) {
            Api.addAttention(LivingActivity.this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    //toast(data.getString("descrp"));
                    v.setText("取消");
                    if (otherUserId.equals(channel_creater)) {
                        DanmuModel model = new DanmuModel();
                        model.setType("3");
                        model.setUserName("系统消息");
                        model.setAvatar(avatar);
                        model.setUserId(userId);
                        model.setContent(user_nicename + "关注了主播");
                        mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
//                        mDanmuItems.add(model);
//                        mDanmuadapter.notifyDataSetChanged();
//                        mLiveingDanmu.setSelection(mDanmuadapter.getCount() - 1);
                        String message = JSONObject.toJSONString(model);
                        EventBus.getDefault().post(
                                new LCIMInputBottomBarTextEvent(LCIMInputBottomBarEvent.INPUTBOTTOMBAR_SEND_TEXT_ACTION, message, channelInfo.getString("leancloud_room")));
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });

        } else {
            Api.cancelAttention(LivingActivity.this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    v.setText("关注");
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        }

    }

    @OnClick(R.id.live_sidou)
    public void showUserContribution() {
        Bundle data = new Bundle();
        data.putString("id", channel_creater);
        openActivity(ContributionActivity.class, data);
    }

    public void showUserInfoDialogById(String uid) {
        if (StringUtils.isEmpty(uid)) {
            toast("uid数据有误");
            return;
        }
        if (uid == userId) return;
        if (userInfoDialog == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(LivingActivity.this);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_userinfo_tips, null);
            dialogUserChangkong = (LinearLayout) dialogView.findViewById(R.id.dialog_changkong_container);
            dialogUserChangkong.setVisibility(View.GONE);
            mDialogCaozuo = (TextView) dialogView.findViewById(R.id.dialog_caozuo);
            mDialogCaozuo.setVisibility(View.VISIBLE);
            mDialogUserAvatar = (CircleImageView) dialogView.findViewById(R.id.dialog_user_avatar);
            mDialogUserAvatarSmall = (CircleImageView) dialogView.findViewById(R.id.dialog_user_avatar_small);
            mDialogUserAvatarSmall1 = (CircleImageView) dialogView.findViewById(R.id.dialog_user_avatar_small1);
            mDialogUserAvatarSmall.setVisibility(View.GONE);
            mDialogUserAvatarSmall1.setVisibility(View.GONE);
            mDialogUserNicename = (TextView) dialogView.findViewById(R.id.dialog_user_nicename);
            mDialogUserSex = (ImageView) dialogView.findViewById(R.id.dialog_user_sex);
            mDialogUserLevel = (TextView) dialogView.findViewById(R.id.dialog_user_level);
            mDialogUserId = (TextView) dialogView.findViewById(R.id.dialog_user_id);
            mDialogUserLocation = (TextView) dialogView.findViewById(R.id.dialog_user_location);
            mDialogUserSignature = (TextView) dialogView.findViewById(R.id.dialog_user_signature);
            mDialogUserSpend = (TextView) dialogView.findViewById(R.id.dialog_user_spend);
            mDialogUserAttentionNum = (TextView) dialogView.findViewById(R.id.dialog_user_attention_num);
            mDialogUserFansNum = (TextView) dialogView.findViewById(R.id.dialog_user_fans_num);
            mDialogUserTotal = (TextView) dialogView.findViewById(R.id.dialog_user_total);
            mDialogUserReal = (ImageView) dialogView.findViewById(R.id.dialog_user_real);
            mDialogUserAttention = (TextView) dialogView.findViewById(R.id.dialog_user_attention);
            mDialogPrivateMessage = (TextView) dialogView.findViewById(R.id.dialog_user_private_message);
            mDialogUserMain = (TextView) dialogView.findViewById(R.id.dialog_user_main);
            mDialogClose = (ImageView) dialogView.findViewById(R.id.dialog_close);
            mDialogAttentionArea = (LinearLayout) dialogView.findViewById(R.id.dialog_user_attention_area);
            mDialogFansArea = (LinearLayout) dialogView.findViewById(R.id.dialog_user_fans_area);
            mDialogTotalArea = (LinearLayout) dialogView.findViewById(R.id.dialog_user_total_area);
            ClickListener clickListener = new ClickListener();
            mDialogClose.setOnClickListener(clickListener);
            mDialogUserAttention.setOnClickListener(clickListener);
            mDialogPrivateMessage.setOnClickListener(clickListener);
            mDialogUserMain.setOnClickListener(clickListener);
            mDialogCaozuo.setOnClickListener(clickListener);
            mDialogAttentionArea.setOnClickListener(clickListener);
            mDialogFansArea.setOnClickListener(clickListener);
            mDialogTotalArea.setOnClickListener(clickListener);
            this.userInfoDialog = builder.setView(dialogView)
                    .create();
            Window window = userInfoDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//            int dip2px = DensityUtils.dip2px(this, 40);
//            window.getDecorView().setPadding(dip2px, 0, dip2px, 0);
        }
        mDialogUserAttention.setText("关注");

        mDialogUserAttention.setEnabled(true);
        mDialogCaozuo.setVisibility(View.VISIBLE);
//        if (!uid.equals(channel_creater)) {
//            mDialogCaozuo.setVisibility(View.GONE);
//        }
        userInfoDialog.show();
        getInfo(token, uid);
//        getFirstContribution(token, uid);
    }

    public void getInfo(String token, String uid) {
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("id", uid);
        Api.getUserInfo(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONObject userInfo = data.getJSONObject("data");
                Glide.with(LivingActivity.this).load(userInfo.getString("avatar"))
                        .error(R.drawable.icon_avatar_default)
                        .transform(new GlideCircleTransform(LivingActivity.this))
                        .into(mDialogUserAvatar);
//                Glide.with(LivingActivity.this).load(userInfo.getString("avatar"))
//                        .error(R.drawable.icon_avatar_default)
//                        .transform(new GlideCircleTransform(LivingActivity.this))
//                        .into(mDialogUserAvatarSmall);
                mDialogUserNicename.setText(userInfo.getString("user_nicename"));
                if ("1".equals(userInfo.getString("sex"))) {
                    mDialogUserSex.setImageResource(R.drawable.userinfo_male);
                }
                mDialogUserLevel.setText(userInfo.getString("user_level"));
                int level = Integer.parseInt(userInfo.getString("user_level"));

                if (level < 4) {
                    mDialogUserLevel.setBackgroundResource(R.drawable.new_userlevel_1);
                } else if (level >= 4 && level < 16) {
                    mDialogUserLevel.setBackgroundResource(R.drawable.new_userlevel_2);
                } else if (level >= 16) {
                    mDialogUserLevel.setBackgroundResource(R.drawable.new_userlevel_3);
                }

                mDialogUserId.setText(userInfo.getString("id"));
                mDialogUserLocation.setText(userInfo.getString("location"));
                if (StringUtils.isNotEmpty(userInfo.getString("signature")))
                    mDialogUserSignature.setText(userInfo.getString("signature"));
                mDialogUserSpend.setText(userInfo.getString("total_spend"));
                mDialogUserAttentionNum.setText(userInfo.getString("attention_num"));
                mDialogUserFansNum.setText(userInfo.getString("fans_num"));
                mDialogUserTotal.setText(userInfo.getString("sidou"));
                if ("1".equals(userInfo.getString("is_truename"))) {
                    mDialogUserReal.setVisibility(View.VISIBLE);
                }
                if ("1".equals(userInfo.getString("attention_status"))) {
                    mDialogUserAttention.setText("已关注");
                    mDialogUserAttention.setEnabled(false);
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    public void getFirstContribution(String token, String uid) {
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("id", uid);
        params.put("limit_num", 2);
        Api.getUserContributionList(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray list = data.getJSONArray("data");
                JSONObject item = list.getJSONObject(0);
                Glide.with(LivingActivity.this).load(item.getString("avatar"))
                        .error(R.drawable.icon_avatar_default)
                        .transform(new GlideCircleTransform(LivingActivity.this))
                        .into(mDialogUserAvatarSmall1);
                item = list.getJSONObject(1);
                Glide.with(LivingActivity.this).load(item.getString("avatar"))
                        .error(R.drawable.icon_avatar_default)
                        .transform(new GlideCircleTransform(LivingActivity.this))
                        .into(mDialogUserAvatarSmall);
            }

            @Override
            public void requestFailure(int code, String msg) {
                //toast(msg);
                Glide.with(LivingActivity.this).load(R.drawable.icon_avatar_default).into(mDialogUserAvatarSmall);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);
//        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, 2);
        Bundle data = getIntent().getExtras();
        if (null != data) {
            mVideoItem = (VideoItem) data.getSerializable("videoItem");
        }
        String roomPassword = data.getString("password");
        token = (String) SharePrefsUtils.get(this, "user", "token", "");
        userId = (String) SharePrefsUtils.get(this, "user", "userId", "");
        user_nicename = (String) SharePrefsUtils.get(this, "user", "user_nicename", "");
        user_level = (String) SharePrefsUtils.get(this, "user", "user_level", "");
        avatar = (String) SharePrefsUtils.get(this, "user", "avatar", "");
        JSONObject params = new JSONObject();
        params.put("room_id", mVideoItem.getRoom_id());
        params.put("token", token);
        params.put("room_password", roomPassword);
//        mVideoTextureView = (TextureView) findViewById(R.id.player_texture);
//        mVideoTextureView.setSurfaceTextureListener(LivingActivity.this);
//        mVideoTextureView.setKeepScreenOn(true);
        mVideoSurfaceView = (SurfaceView) findViewById(R.id.player_surface);
        mSurfaceHolder = mVideoSurfaceView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceCallback);
        mVideoSurfaceView.setKeepScreenOn(true);
        mContext = this;
        ksyMediaPlayer = new KSYMediaPlayer.Builder(this).build();
        ksyMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        ksyMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        ksyMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        ksyMediaPlayer.setOnInfoListener(mOnInfoListener);
        ksyMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangeListener);
        ksyMediaPlayer.setOnErrorListener(mOnErrorListener);
        ksyMediaPlayer.setOnSeekCompleteListener(mOnSeekCompletedListener);
        ksyMediaPlayer.setScreenOnWhilePlaying(true);
        ksyMediaPlayer.setBufferTimeMax(3);
        ksyMediaPlayer.setTimeout(5, 30);

        Api.getChannleInfo(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                if (isActive) {
                    channelInfo = data.getJSONObject("data");
                    sysMessage = data.getJSONArray("msg");
                    initData();
                    joinChat(userId, channelInfo.getString("leancloud_room"));
                    Glide.with(LivingActivity.this).load(channelInfo.getString("avatar"))
                            .error(R.drawable.icon_avatar_default)
                            .transform(new GlideCircleTransform(LivingActivity.this))
                            .into(mLiveUserAvatar);
                    Glide.with(LivingActivity.this).load(channelInfo.getString("avatar"))
                            .error(R.drawable.icon_avatar_default)
                            .transform(new GlideCircleTransform(LivingActivity.this))
                            .into(iv_zhibo_close_icon);
                    mLiveUserAvatar.setTag(R.id.image_live_avatar, channelInfo.getString("channel_creater"));


                    mLiveUserTotal.setText(getEarn(channelInfo.getString("total_earn")));

                    mLiveUserId.setText(channelInfo.getString("channel_creater"));
                    channel_creater = channelInfo.getString("channel_creater");
                    mLiveUserNicename.setText(channelInfo.getString("user_nicename"));
                    tv_zhibo_close_username.setText(channelInfo.getString("user_nicename"));
                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    if ("1".equals(channelInfo.getString("attention_status"))) {
                        mbtnFollow.setText("取消");
                    }
                    mDataSource = channelInfo.getString("channel_source");
                    //mDataSource = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
                    if (null != mDataSource) {
                        //loading_dialog = SFProgrssDialog.show(LivingActivity.this, "");
                        try {
                            ksyMediaPlayer.setDataSource(mDataSource);
                            ksyMediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    dataHandler.postDelayed(dataRunnable, 2000);
                    dataHandler.postDelayed(addScoreRunnable, 1000 * 60 * 5);
                    mLiveEndContainer.setVisibility(View.GONE);
                }

            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
                switch (code) {
                    case 508:
                        DialogEnsureUtiles.showInfo(LivingActivity.this, new OnCustomClickListener() {
                            @Override
                            public void onClick(final String value) {
                                JSONObject params = new JSONObject();
                                params.put("room_id", mVideoItem.getRoom_id());
                                params.put("token", token);
                                params.put("room_password", value);
                                Api.checkPass(LivingActivity.this, params, new OnRequestDataListener() {
                                    @Override
                                    public void requestSuccess(int code, JSONObject data) {
                                        Bundle data1 = new Bundle();
                                        data1.putSerializable("videoItem", mVideoItem);
                                        data1.putString("password", value);
                                        finish();
                                        openActivity(LivingActivity.class, data1);
                                    }

                                    @Override
                                    public void requestFailure(int code, String msg) {
                                        toast(msg);
                                    }
                                });
                            }
                        }, "", "该房间需要密码");
                        break;
                    default:
                        finish();
                }

                //mLiveEndContainer.setVisibility(View.VISIBLE);
            }
        });

//        mLiveSend.setVisibility(View.GONE);
        mLiveShare.setVisibility(View.GONE);
        mLiveGift.setVisibility(View.GONE);
        mLiveCamera.setVisibility(View.GONE);
        mLiveMeiyan.setVisibility(View.GONE);
        mLiveMusic.setVisibility(View.GONE);
        mCameraReverse.setVisibility(View.GONE);
        mLiveMore.setVisibility(View.GONE);
        mLiveTopLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (live_message_container_rl.getVisibility() == View.VISIBLE) {
                    live_message_container_rl.setVisibility(View.GONE);
                    mLiveBottomBtn.setVisibility(View.VISIBLE);
                }
                if (mGiftContainer.getVisibility() == View.VISIBLE) {
                    mGiftContainer.setVisibility(View.GONE);
                    mLivingDanmuContainer.setVisibility(View.VISIBLE);
                    mLiveBottomBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        mThirdShare = new ThirdShare(this);
        mThirdShare.setOnShareStatusListener(this);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        bubbleView = (BubbleView) findViewById(R.id.praise_anim);
        bubbleView.setDefaultDrawableList();
//        setDrawbleList();
        myHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());
                }
                super.handleMessage(msg);
            }
        };
        Timer heart_timer = new Timer(true);
        heart_timer.schedule(task, 500, 500); //延时1000ms后执行，1000ms执行一次

        //rtc
        mCameraPreviewView = (GLSurfaceView) findViewById(R.id.camera_preview);
        mMainHandler = new Handler();
        mStreamer = new KSYRtcStreamer(this);
        //mStreamer.setUrl("rtmp://test.uplive.ks-cdn.com/live/androidtest");
        mStreamer.setPreviewFps(15);
        mStreamer.setTargetFps(15);
        int videoBitrate = 800;
        videoBitrate *= 1000;
        mStreamer.setVideoBitrate(videoBitrate * 3 / 4, videoBitrate, videoBitrate / 4);
        mStreamer.setAudioBitrate(48 * 1000);
        mStreamer.setPreviewResolution(StreamerConstants.VIDEO_RESOLUTION_480P);
        mStreamer.setTargetResolution(StreamerConstants.VIDEO_RESOLUTION_480P);
        mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mStreamer.setDisplayPreview(mCameraPreviewView);
        mStreamer.setEnableStreamStatModule(true);
        mStreamer.enableDebugLog(true);
        mStreamer.setFrontCameraMirror(false);
        mStreamer.setEnableAudioPreview(false);
        mStreamer.setMuteAudio(true);
        // mStreamer.startCameraPreview();
        //mStreamer.setOnInfoListener(mOnInfoListener);
        // mStreamer.setOnErrorListener(mOnErrorListener);
        mStreamer.setOnLogEventListener(mOnLogEventListener);
        mStreamer.setVoiceVolume(1.0f);
        mStreamer.setRTCRemoteVoiceVolume(1.0f);
        //mStreamer.setOnAudioRawDataListener(mOnAudioRawDataListener);
        //mStreamer.setOnPreviewFrameListener(mOnPreviewFrameListener);
        mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                ImgTexFilterMgt.KSY_FILTER_BEAUTY_DENOISE);
        mStreamer.setEnableImgBufBeauty(true);
        mStreamer.getImgTexFilterMgt().setOnErrorListener(new ImgTexFilterBase.OnErrorListener() {
            @Override
            public void onError(ImgTexFilterBase filter, int errno) {
                toast("当前机型不支持该滤镜");
                mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                        ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
            }
        });
        mStreamer.getRtcClient().setRTCErrorListener(mRTCErrorListener);
        //toast(mStreamer.getVersion());
        mStreamer.getRtcClient().setRTCEventListener(mRTCEventListener);
        dataHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onRTCRegisterClick();
            }
        }, 3000);
        listener = new OrientationSensorListener(this, null);

        Api.getLivingBg(this, new JSONObject(), new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                String url = data.getString("data");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!isDestroyed()) {
                        Glide.with(LivingActivity.this).load(url).into(mPauseCover);
                    }
                } else {
                    if (Util.isOnMainThread() && !isFinishing()) {
                        Glide.with(LivingActivity.this).load(url).into(mPauseCover);
                    }
                }
            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });

        mDanmaManager = new DanmaManager(this,mDanmakuView);
    }


    //获取点赞图标
    private List<Drawable> mDrawbleList = new ArrayList<>();

//    private void setDrawbleList() {
//        Api.getLikePic(this, new JSONObject(), new OnRequestDataListener() {
//            @Override
//            public void requestSuccess(int code, final JSONObject data) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mDrawbleList.add(loadImageFromNetwork(data.getJSONObject("data").getString("First")));
//                        mDrawbleList.add(loadImageFromNetwork(data.getJSONObject("data").getString("Second")));
//                        mDrawbleList.add(loadImageFromNetwork(data.getJSONObject("data").getString("Third")));
//                        mDrawbleList.add(loadImageFromNetwork(data.getJSONObject("data").getString("Fourth")));
//                        mDrawbleList.add(loadImageFromNetwork(data.getJSONObject("data").getString("Fifth")));
//                        if (null != mDrawbleList && mDrawbleList.size() > 0) {
//                            bubbleView.setDrawableList(mDrawbleList);
//                        }
//                    }
//                }).start();
//                //Timer heart_timer = new Timer(true);
//                //heart_timer.schedule(task, 500, 500); //延时1000ms后执行，1000ms执行一次
//            }
//
//            @Override
//            public void requestFailure(int code, String msg) {
//
//            }
//        });
//    }

    private Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromResourceStream(getResources(), null,
                    new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable;
    }

    private String getEarn(String total_earn) {
        Long n = Long.valueOf(total_earn);
        DecimalFormat df = new DecimalFormat("#.0");

        if (n > 100000000) {
            double n2 = n / 100000000.0;
            return df.format(n2) + "亿";
        } else if (n > 10000) {
            double n2 = n / 10000.0;
            return df.format(n2) + "万";
        }
        return total_earn;
    }

    @OnClick(R.id.live_message_container_rl)
    public void live_message_container_rl(View view) {
        if (live_message_container_rl.getVisibility() == View.VISIBLE) {
            live_message_container_rl.setVisibility(View.GONE);
            mLiveBottomBtn.setVisibility(View.VISIBLE);
        }
        if (mGiftContainer.getVisibility() == View.VISIBLE) {
            mGiftContainer.setVisibility(View.GONE);
            mLivingDanmuContainer.setVisibility(View.VISIBLE);
            mLiveBottomBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = ev.getRawX();
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE: {
                float mX = ev.getRawX();
                float mY = ev.getRawY();
                float disX = Math.abs(mX - mLastX);
                float disY = Math.abs(mY - mLastY);
                if (disY > disX) {
                    return super.dispatchTouchEvent(ev);
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                float mX = ev.getRawX();
                float mY = ev.getRawY();
                float disX = Math.abs(mX - mLastX);
                float disY = Math.abs(mY - mLastY);
                if (!mLiveOnlineUsers.isHasScroll() && (mGiftFragment == null ? true : !mGiftFragment.isHasScroll())) {
                    if (disX > 100 && disX > disY) {
                        if (mX - mLastX > 0) {
                            //向右滑动
                            mLiveViewFlipper.setInAnimation(this, R.anim.in_leftright);
                            mLiveViewFlipper.setOutAnimation(this, R.anim.out_leftright);
                            mLiveViewFlipper.showNext();
                        } else {
                            //向左滑动
                            mLiveViewFlipper.setInAnimation(this, R.anim.in_rightleft);
                            mLiveViewFlipper.setOutAnimation(this, R.anim.out_rightleft);
                            mLiveViewFlipper.showPrevious();
                        }
                        return super.dispatchTouchEvent(ev);
                    }
                }
                mLiveOnlineUsers.setHasScroll(false);
                if (mGiftFragment != null) {
                    mGiftFragment.setHasScroll(false);
                }
            }
            break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int i, int i1) {
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surface;
        }
        if (mSurface == null) {
            mSurface = new Surface(mSurfaceTexture);
        }

        if (ksyMediaPlayer != null) {
            Log.e(TAG, "setSurface ..........");
            ksyMediaPlayer.setSurface(mSurface);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        Log.e(TAG, "onSurfaceTextureDestroyed ...............");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    //@OnClick(R.id.live_send)
    @OnClick(R.id.live_audience_chat_iv)
    public void liveSend(View view) {
        mLiveBottomBtn.setVisibility(View.GONE);
        live_message_container_rl.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) mLiveEditInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        mLiveEditInput.setFocusable(true);
        mLiveEditInput.setFocusableInTouchMode(true);
        mLiveEditInput.findFocus();
        mLiveEditInput.requestFocus();
    }

    @OnClick(R.id.live_btn_send)
    public void liveBtnSend(final View view) {

        final String content = mLiveEditInput.getText().toString();
        if (StringUtils.isEmpty(content)) {
            toast("请先输入内容");
            return;
        }
        final DanmuModel model = new DanmuModel();

        if (danmuChecked) {
            view.setEnabled(false);
            JSONObject params = new JSONObject();
            params.put("token", token);
            params.put("room_id", channelInfo.getString("room_id"));
            params.put("giftid", "19");
            params.put("number", "1");
            Api.sendDanmu(this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    view.setEnabled(true);
                    JSONObject result = data.getJSONObject("data");
                    MyApplication app = (MyApplication) getApplication();
                    app.setBalance((result.getString("balance")));
                    model.setUserName(user_nicename);
                    model.setUserLevel(user_level);
                    model.setUserId(userId);
                    model.setType("7");//弹幕
                    model.setAvatar(avatar);
                    model.setContent(content);
                    sendMessage(model);
                    mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
                }

                @Override
                public void requestFailure(int code, String msg) {
                    view.setEnabled(true);
                    toast(msg);
                }
            });


        } else {
            model.setUserName(user_nicename);
            model.setUserLevel(user_level);
            model.setUserId(userId);
            model.setType("1");
            model.setAvatar(avatar);
            model.setContent(content);
//            sendMessage(model);
            JSONObject jo = new JSONObject();
            jo.put("text", content);
            jo.put("message", new Gson().toJson(model));
            mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
            Api.sendDanmuNew(this, jo, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    LogUtils.d("zzk" + data);
                }

                @Override
                public void requestFailure(int code, String msg) {
                    LogUtils.d("error" + msg);
                }
            });
            if (mLivingDanmuContainer.getVisibility() == View.VISIBLE) {
                mLiveBottomBtn.setVisibility(View.VISIBLE);
            }
            mLiveEditInput.setText("");
            live_message_container_rl.setVisibility(View.GONE);
            SoftKeyboardUtils.closeSoftInputMethod(mLiveEditInput);
        }
    }

//    public void showDanmuAnim(final LivingActivity temp, DanmuModel model) {
//        final View giftPop = View.inflate(temp, R.layout.item_danmu_pop, null);
//        ImageView giftAvatar = (ImageView) giftPop.findViewById(R.id.gift_pop_avatar);
//        TextView giftUserName = (TextView) giftPop.findViewById(R.id.gift_pop_username);
//        TextView giftContent = (TextView) giftPop.findViewById(R.id.gift_pop_content);
//        Glide.with(temp).load(temp.avatar)
//                .error(R.drawable.icon_avatar_default)
//                .into(giftAvatar);
//        giftUserName.setText(temp.user_nicename);
//        giftContent.setText(model.getContent());
//        temp.mDanmuContainer.addView(giftPop);
//        Animation anim = AnimationUtils.loadAnimation(temp, R.anim.danmu_enter);
//        giftPop.startAnimation(anim);
//        anim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (isActive) {
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            temp.mDanmuContainer.removeView(giftPop);
//                        }
//                    });
//
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//    }

    public void sendMessage(DanmuModel model) {
        if (isActive) {
//            mDanmuItems.add(model);
//            mDanmuadapter.notifyDataSetChanged();
//            mLiveingDanmu.setSelection(mDanmuadapter.getCount() - 1);
            mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
            mLiveEditInput.setText("");
            if (mLivingDanmuContainer.getVisibility() == View.VISIBLE) {
                mLiveBottomBtn.setVisibility(View.VISIBLE);
            }
            live_message_container_rl.setVisibility(View.GONE);
            SoftKeyboardUtils.closeSoftInputMethod(mLiveEditInput);
        }
        String message = JSONObject.toJSONString(model);
        EventBus.getDefault().post(
                new LCIMInputBottomBarTextEvent(LCIMInputBottomBarEvent.INPUTBOTTOMBAR_SEND_TEXT_ACTION, message, channelInfo.getString("leancloud_room")));

    }

    private void initData() {
//        mDanmuItems = new ArrayList<DanmuModel>();
        mUserItems = new ArrayList<UserModel>();
        if (null != sysMessage) {
            for (int i = 0; i < sysMessage.size(); i++) {
                DanmuModel model = new DanmuModel();
                model.setType("3");
                model.setUserName(sysMessage.getJSONObject(i).getString("title"));
                model.setContent(sysMessage.getJSONObject(i).getString("msg"));
                mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
//                mDanmuItems.add(model);
            }

        }
//        mDanmuadapter = new DanmuAdapter(this, mDanmuItems);
//        mLiveingDanmu.setAdapter(mDanmuadapter);
        mOnlineUserAdapter = new OnlineUserAdapter(this, mUserItems);
//        mLiveOnlineUsers.requestParentListViewToNotInterceptTouchEvents(false);
        mLiveOnlineUsers.setOnItemClickListener(this);
        mLiveOnlineUsers.setAdapter(mOnlineUserAdapter);
//        mLiveingDanmu.setOnItemClickListener(this);
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("room_id", channelInfo.getString("room_id"));
        Api.getOnlineUsers(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray users = JSON.parseArray(data.getString("data"));
                for (int i = 0; i < users.size(); i++) {
                    UserModel user = new UserModel();
                    user.setAvatar(users.getJSONObject(i).getString("avatar"));
                    user.setId(users.getJSONObject(i).getString("id"));
                    user.setUser_nicename(users.getJSONObject(i).getString("user_nicename"));
                    user.setLevel(users.getJSONObject(i).getString("user_level"));
                    mUserItems.add(user);
                    mOnlineUserAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });
    }

    public void joinChat(String ClientId, final String conversationId) {
        if (conversationId.length() == 0) {
            toast("无法获取LC_Room信息，请退出重试");
            return;
        }
        LCChatKit.getInstance().open(ClientId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                AVIMClient client = LCChatKit.getInstance().getClient();
                if (null != client) {
                    mSquareConversation = client.getConversation(conversationId);
                    LCIMNotificationUtils.addTag(conversationId);
                    joinSquare();
                } else {
                    toast("网络貌似有点问题，请退出重试");
                }
            }
        });
    }

    /**
     * 加入 conversation
     */
    private void joinSquare() {
        mSquareConversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (filterException(e)) {
                    DanmuModel model = new DanmuModel();
                    model.setType("6");
                    model.setUserName("系统消息");
                    model.setAvatar(avatar);
                    model.setUserId(userId);
                    model.setUserLevel(user_level);
                    model.setContent(user_nicename + "进入房间");
                    mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
//                    mDanmuItems.add(model);
//                    mDanmuadapter.notifyDataSetChanged();
//                    mLiveingDanmu.setSelection(mDanmuadapter.getCount() - 1);
                    String message = JSONObject.toJSONString(model);
                    EventBus.getDefault().post(
                            new LCIMInputBottomBarTextEvent(LCIMInputBottomBarEvent.INPUTBOTTOMBAR_SEND_TEXT_ACTION, message, channelInfo.getString("leancloud_room")));
                }
            }
        });
    }

    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            toast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    /**
     * 输入事件处理，接收后构造成 AVIMTextMessage 然后发送
     * 因为不排除某些特殊情况会受到其他页面过来的无效消息，所以此处加了 tag 判断
     */
    public void onEvent(LCIMInputBottomBarTextEvent textEvent) {
        if (null != mSquareConversation && null != textEvent) {
            if (!TextUtils.isEmpty(textEvent.sendContent) && mSquareConversation.getConversationId().equals(textEvent.tag)) {
                AVIMTextMessage message = new AVIMTextMessage();
                message.setText(textEvent.sendContent);
                mSquareConversation.sendMessage(message, new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        if (null != e) {
                            toast("消息发送失败");
                        }
                    }
                });
            }
        }
    }

    //  03/09/2017  消息响应
    public void onEvent(LCIMIMTypeMessageEvent event) {
        getUnread();
        if (null != mSquareConversation && null != event &&
                mSquareConversation.getConversationId().equals(event.conversation.getConversationId())) {
            JSONObject temp = JSON.parseObject(((AVIMTextMessage) event.message).getText());
            DanmuModel model = new DanmuModel();
            model.setType(temp.getString("type"));
            model.setUserName(temp.getString("userName"));
            model.setUserLevel(temp.getString("userLevel"));
            model.setContent(temp.getString("content"));
            model.setUserId(temp.getString("userId"));
            model.setAvatar(temp.getString("avatar"));
            if (!model.getType().equals("9") && !model.getType().equals("10")) {
                mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
//                mDanmuItems.add(model);
//                mDanmuadapter.notifyDataSetChanged();
//                mLiveingDanmu.setSelection(mDanmuadapter.getCount() - 1);
            }
            switch (model.getType()) {
                case "4":
                    clickHeart();
                    break;
                case "7":
                    mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
                    break;
                case "6":
                    //系统消息  -  进入房间
                    if (channel_creater.equals(model.getUserId())) {
                        return;
                    }
                    UserModel user = new UserModel();
                    user.setAvatar(model.getAvatar());
                    user.setId(model.getUserId());
                    user.setUser_nicename(model.getUserName());
                    mUserItems.add(user);
                    mOnlineUserAdapter.notifyDataSetChanged();
                    break;
                case "5":
                    //系统消息  -  离开房间
                    UserModel utemp = new UserModel();
                    utemp.setId(model.getUserId());
                    mUserItems.remove(utemp);
                    mOnlineUserAdapter.notifyDataSetChanged();
                    break;
                case "2":
                    //toast("显示礼物画面");
                    JSONObject gift1 = temp.getJSONObject("other");
                    JSONObject giftO = gift1.getJSONObject("giftInfo");
                    GiftModel gift = new GiftModel();
                    gift.setGifticon(giftO.getString("gifticon"));
                    gift.setGiftname(giftO.getString("giftname"));
                    gift.setGiftid(giftO.getString("giftid"));
                    gift.setContinuous(giftO.getString("continuous"));
                    String num = (null == giftO.getString("continuousNum")) ? "1" : giftO.getString("continuousNum");
                    showGiftAnim1(LivingActivity.this, model, gift, Integer.parseInt(num));
                    break;
                case "9":
                    showVote();
                    break;
                case "10":
                    audience_vote_rl.setVisibility(View.GONE);
                    break;
                case "11":
                    // TODO: 19/09/2017
                    showLotteryDialog();
//                    用户端  显示抽奖结果   消息 type:11
//                    新增reward字段
//                            $reward = array(
//                            array('userId'=>'156277','userName'=>'测试1','avatar'=>'http:zhibo.519wan.com/data/upload/avatar/default.png'),
//                    array('userId'=>'156278','userName'=>'测试2','avatar'=>'http:zhibo.519wan.com/data/upload/avatar/default.png'),
                    break;
//	);
            }
        }
    }

    private Dialog mLotteryDialog;

    private void showLotteryDialog() {
        if(mLotteryDialog == null) {
        }
    }

    public void showGiftAnim1(final LivingActivity temp, DanmuModel danmuModel, GiftModel giftModel, int num) {
        GiftSendModel modelTemp = new GiftSendModel(num);
        modelTemp.setGiftCount(num);
        modelTemp.setGift_id(giftModel.getGiftid());
        modelTemp.setUserId(danmuModel.getUserId());
        modelTemp.setNickname(danmuModel.getUserName());
        modelTemp.setSig(danmuModel.getContent());
        modelTemp.setUserAvatarRes(danmuModel.getAvatar());
        modelTemp.setGiftRes(giftModel.getGifticon());
        temp.starGiftAnimation(modelTemp);
    }

    public void showGiftAnim(final LivingActivity temp, DanmuModel danmuModel, GiftModel model, int num) {
        final View giftPop = View.inflate(temp, R.layout.item_gift_pop, null);
        ImageView giftAvatar = (ImageView) giftPop.findViewById(R.id.gift_pop_avatar);
        ImageView giftImage = (ImageView) giftPop.findViewById(R.id.gift_pop_gift);
        TextView giftUserName = (TextView) giftPop.findViewById(R.id.gift_pop_username);
        TextView giftContent = (TextView) giftPop.findViewById(R.id.gift_pop_content);
        TextView giftPopX = (TextView) giftPop.findViewById(R.id.gift_pop_x);
        TextView giftPopNum = (TextView) giftPop.findViewById(R.id.gift_pop_num);
        if (num == 1) {
            giftPopX.setVisibility(View.INVISIBLE);
            giftPopNum.setVisibility(View.INVISIBLE);
        } else {
            giftPopNum.setText(num + "");
        }
        Glide.with(temp).load(danmuModel.getAvatar())
                .error(R.drawable.icon_avatar_default)
                .into(giftAvatar);
        Glide.with(temp).load(model.getGifticon())
                .error(R.drawable.icon_avatar_default)
                .into(giftImage);
        giftUserName.setText(danmuModel.getUserName());
        giftContent.setText("赠送一个" + model.getGiftname());
        Animation anim = AnimationUtils.loadAnimation(temp, R.anim.gift_enter);
        giftPop.startAnimation(anim);
        temp.mLiveGiftContainer.addView(giftPop);
        Runnable giftTimer = new Runnable() {
            @Override
            public void run() {
                temp.mLiveGiftContainer.removeView(giftPop);
            }
        };
        giftPop.postDelayed(giftTimer, 2000);
        temp.mLiveGiftScroll.fullScroll(temp.mLiveGiftScroll.FOCUS_DOWN);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_living;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDanmaManager.destroy();
        DanmuModel model = new DanmuModel();
        model.setType("5");
        model.setUserName("系统消息");
        model.setAvatar(avatar);
        model.setUserId(userId);
        model.setContent(user_nicename + "离开房间");
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(JSONObject.toJSONString(model));
        if (mSquareConversation != null) {
            mSquareConversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {

                }
            });
            // sendMessage(model);
            mSquareConversation.quit(new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {

                }
            });
        }
        if (StringUtils.isNotEmpty(token) && channelInfo != null) {
            JSONObject params = new JSONObject();
            params.put("token", token);
            params.put("room_id", channelInfo.getString("room_id"));
            Api.exitLiveRoom(this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {

                }

                @Override
                public void requestFailure(int code, String msg) {

                }
            });
        }
        if (dataHandler != null) {
            dataHandler.removeCallbacks(dataRunnable);
        }
        if (helper != null) {
            // helper.removeSoftKeyboardStateListener(mSoftKeyboardStateListener);
        }
        if (ksyMediaPlayer != null) {
            ksyMediaPlayer.release();
            ksyMediaPlayer = null;
        }
        mVideoTextureView = null;
        mSurfaceTexture = null;
        if (mIsConnected) {
            mStreamer.getRtcClient().stopCall();
        }

        if (mIsRegisted) {
            mStreamer.getRtcClient().unRegisterRTC();
        }
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        mStreamer.stopCameraPreview();
        mStreamer.release();
    }

    @Override
    public void onBackPressed() {
        if (mGiftContainer.getVisibility() == View.VISIBLE) {
            mGiftContainer.setVisibility(View.GONE);
            mLivingDanmuContainer.setVisibility(View.VISIBLE);
            mLiveBottomBtn.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() instanceof DanmuAdapter) {
            DanmuModel model = (DanmuModel) adapterView.getAdapter().getItem(i);
            if (null != model.getUserId()) {
                //toast(model.getUserId());
                otherUserId = model.getUserId();
                showUserInfoDialogById(otherUserId);
            }
        }
        if (adapterView.getAdapter() instanceof OnlineUserAdapter) {
            UserModel model = (UserModel) adapterView.getAdapter().getItem(i);
            if (null != model.getId()) {
                //toast(model.getId());
                otherUserId = model.getId();
                showUserInfoDialogById(otherUserId);
            }
        }

    }

    @Override
    public void shareSuccess(Platform platform) {
        toast("分享成功");
    }

    @Override
    public void shareError(Platform platform) {
        toast("分享失败");
    }

    @Override
    public void shareCancel(Platform platform) {
        toast("分享取消");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUnread();
        startCameraPreviewWithPermCheck();
        mDanmaManager.resume();
        EventBus.getDefault().register(this);
        if (ksyMediaPlayer != null && !isPaused) {
            ksyMediaPlayer.start();
            mPause = false;
        }
        mStreamer.setAudioOnly(false);
        mStreamer.onResume();
        mStreamer.getRtcClient().setRecMute(true);
        listener.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDanmaManager.pause();
        EventBus.getDefault().unregister(this);
        if (ksyMediaPlayer != null) {
//            ksyMediaPlayer.pause();
            mPause = true;
        }
        mStreamer.onPause();
        mStreamer.stopCameraPreview();
        listener.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //reinitTextureView((TextureView) findViewById(R.id.player_texture));

        if (ksyMediaPlayer != null && !isPaused) {
            ksyMediaPlayer.start();
            mPause = false;
        }
    }

    private void onRTCRegisterClick() {
        if (mRTCAuthResponse == null) {
            if (isActive) {
                mRTCAuthResponse = new AuthHttpTask.KSYOnHttpResponse() {
                    @Override
                    public void onHttpResponse(int responseCode, final String response) {
                        if (responseCode == 200) {
                            mMainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    doRegister(response);
                                }
                            });
                        } else {

                        }
                    }
                };

            }
        }
        if (!mIsRegisted) {
            if (null != mRTCAuthResponse) {
                mRTCAuthTask = new AuthHttpTask(mRTCAuthResponse);
                mRTCAuthTask.execute(RTC_AUTH_SERVER + "?uid=" + userId);
                //do not registed when registing
            }

        } else {
            doUnRegister();
        }

    }

    private void onRTCRemoteCallClick() {
        if (!mIsConnected && mIsRegisted) {
            mStreamer.getRtcClient().startCall(otherUserId);

        } else {
            mStreamer.getRtcClient().stopCall();
        }
    }

    private void doRegisteredSuccess() {
        mIsRegisted = true;
    }

    private void doStartCallSuccess() {
        mIsConnected = true;
        //can stop call
        mStreamer.startRTC();
        mStreamer.setRTCMainScreen(RTCConstants.RTC_MAIN_SCREEN_CAMERA);
        mLianmaiStop.setVisibility(View.VISIBLE);
    }

    private void doStopCallResult() {
        mIsConnected = false;
        //can start call again
        //to stop RTC video/audio
        mStreamer.stopRTC();
        mStreamer.setRTCMainScreen(RTCConstants.RTC_MAIN_SCREEN_CAMERA);
        mLianmaiStop.setVisibility(View.GONE);
        if (ksyMediaPlayer != null && !isPaused) {
            FrameLayout.LayoutParams tvLayoutParams = (FrameLayout.LayoutParams) mCameraPreviewView.getLayoutParams();
            tvLayoutParams.height = 1;
            tvLayoutParams.width = 1;
            mCameraPreviewView.setLayoutParams(tvLayoutParams);
            mVideoSurfaceView.setVisibility(View.VISIBLE);
            ksyMediaPlayer.start();
            mPause = false;

        }

    }

    private void doStartCallFailed(int status) {
        mIsConnected = false;
        //if remote receive visible need hide

        Toast.makeText(this, "call failed: " + status, Toast
                .LENGTH_SHORT).show();

    }

    private void doRTCCallBreak() {
        mIsConnected = false;
        Toast.makeText(this, "call break", Toast
                .LENGTH_SHORT).show();
        //to stop RTC video/audio
        mStreamer.stopRTC();
        mStreamer.setRTCMainScreen(RTCConstants.RTC_MAIN_SCREEN_CAMERA);
    }

    private void doReceiveRemoteCall(final String remoteUri) {
        //当前版本支持1对1的call
        if (mIsConnected) {
            mStreamer.getRtcClient().rejectCall();
        } else {
            //startCameraPreviewWithPermCheck();
            //mLianmaiContainer.setVisibility(View.VISIBLE);
            //接收拒绝
            new AlertDialog.Builder(this)
                    .setMessage("主播想与你连麦？")
                    .setPositiveButton("接收", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ksyMediaPlayer != null) {
                                ksyMediaPlayer.pause();
                                mVideoSurfaceView.setVisibility(View.INVISIBLE);
                            }
                            //startCameraPreviewWithPermCheck();
                            FrameLayout.LayoutParams tvLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                    FrameLayout.LayoutParams.MATCH_PARENT);
                            mCameraPreviewView.setLayoutParams(tvLayoutParams);
                            mStreamer.getRtcClient().answerCall();
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mStreamer.getRtcClient().rejectCall();
                        }
                    })
                    .show();

        }
    }

    private void doAuthFailed() {
        //register failed

        //can register again
        mIsRegisted = false;
    }

    private void doRegisteredFailed(int failed) {
        //register failed

        //can register again
        mIsRegisted = false;
        if (mIsConnected) {
            mStreamer.stopRTC();
            mIsConnected = false;
        }
    }


    private void doUnRegisteredResult() {
        mIsRegisted = false;
        if (mIsConnected) {
            mStreamer.stopRTC();
            mIsConnected = false;
        }
        //can register again

        mStreamer.setRTCMainScreen(RTCConstants.RTC_MAIN_SCREEN_CAMERA);
    }

    private void doRegister(String authString) {
        //must set before register
        mStreamer.setRTCSubScreenRect(0.65f, 0.7f, 0.35f, 0.3f, RTCConstants.SCALING_MODE_CENTER_CROP);
        mStreamer.getRtcClient().setRTCAuthInfo(RTC_AUTH_URI, authString, userId);
        mStreamer.getRtcClient().setRTCUniqueName(RTC_UINIQUE_NAME);
        //has default value
        mStreamer.getRtcClient().openChattingRoom(false);
        mStreamer.getRtcClient().setRTCResolutionScale(0.5f);
        mStreamer.getRtcClient().setRTCFps(15);
        mStreamer.getRtcClient().setRTCVideoBitrate(256 * 1024);
        mStreamer.getRtcClient().registerRTC();
    }

    private void doUnRegister() {
        mStreamer.getRtcClient().unRegisterRTC();
    }

    private void startCameraPreviewWithPermCheck() {
        int cameraPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int audioPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (cameraPerm != PackageManager.PERMISSION_GRANTED ||
                audioPerm != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Log.e(TAG, "No CAMERA or AudioRecord permission, please check");
                Toast.makeText(this, "No CAMERA or AudioRecord permission, please check",
                        Toast.LENGTH_LONG).show();
            } else {
                String[] permissions = {Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions,
                        PERMISSION_REQUEST_CAMERA_AUDIOREC);
            }
        } else {
            mStreamer.startCameraPreview();
        }
    }

    private void getUnread() {
        int num = 0;
        List<String> convIdList = LCIMConversationItemCache.getInstance().getSortedConversationList();
        for (String id : convIdList) {
            AVIMConversation conversation = LCChatKit.getInstance().getClient().getConversation(id);
            if (conversation.getMembers().size() != 2)
                continue;
            num += LCIMConversationItemCache.getInstance().getUnreadCount(conversation.getConversationId());
        }
        mImageOwnUnread.setVisibility(View.GONE);
        if (num > 0) {
            mImageOwnUnread.setVisibility(View.VISIBLE);
        }
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(final View view) {
            switch (view.getId()) {
                case R.id.dialog_close:
                    if (userInfoDialog != null) {
                        userInfoDialog.dismiss();
                    }
                    break;
                case R.id.caozuo_cancel:
                    mPopupCaozuoWindow.dismiss();
                    break;
                case R.id.tv_cancel:
                    mPopupShareWindow.dismiss();
                    break;
                case R.id.jinyan_container:
                    JSONObject temp = new JSONObject();
                    temp.put("token", token);
                    temp.put("room_id", mVideoItem.getRoom_id());
                    temp.put("id", otherUserId);
                    Api.addJinyan(LivingActivity.this, temp, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            toast(data.getString("descrp"));
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            toast(msg);
                        }
                    });
                    break;
                case R.id.jubao_container:
                    JSONObject params = new JSONObject();
                    params.put("token", token);
                    params.put("room_id", mVideoItem.getRoom_id());
                    Api.sendReport(LivingActivity.this, params, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            toast(data.getString("descrp"));
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            toast(msg);
                        }
                    });
                    break;
                case R.id.dialog_user_main:
                    Bundle data = new Bundle();
                    data.putString("id", otherUserId);
                    openActivity(UserMainActivity.class, data);
                    break;
                case R.id.dialog_user_attention:
                    JSONObject requestParams = new JSONObject();
                    requestParams.put("token", token);
                    requestParams.put("userid", otherUserId);
                    Api.addAttention(LivingActivity.this, requestParams, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            //toast(data.getString("descrp"));
                            TextView v = (TextView) view;
                            v.setText("已关注");
                            v.setEnabled(false);
                            if (otherUserId.equals(channel_creater)) {
                                DanmuModel model = new DanmuModel();
                                model.setType("3");
                                model.setUserName("系统消息");
                                model.setAvatar(avatar);
                                model.setUserId(userId);
                                model.setContent(user_nicename + "关注了主播");
                                mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
//                                mDanmuItems.add(model);
//                                mDanmuadapter.notifyDataSetChanged();
//                                mLiveingDanmu.setSelection(mDanmuadapter.getCount() - 1);
                                String message = JSONObject.toJSONString(model);
                                EventBus.getDefault().post(
                                        new LCIMInputBottomBarTextEvent(LCIMInputBottomBarEvent.INPUTBOTTOMBAR_SEND_TEXT_ACTION, message, channelInfo.getString("leancloud_room")));
                            }
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            toast(msg);
                        }
                    });
                    break;
                case R.id.dialog_caozuo:
                    userInfoDialog.dismiss();
                    int locX = 0;
                    int locY = 0;
                    if (mPopupCaozuoWindow == null || !mPopupCaozuoWindow.isShowing()) {
                        View inflate = LayoutInflater.from(LivingActivity.this).inflate(R.layout.layout_shape_dialog_caozuo, null);
                        mPopupCaozuoWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        mPopupCaozuoWindow.setBackgroundDrawable(new ColorDrawable(0xe5000000));
                        mPopupCaozuoWindow.setFocusable(true);
                        mPopupCaozuoWindow.setAnimationStyle(R.anim.bottom_in);
                        mPopupCaozuoWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, locX, locY);
                        LinearLayout imageLianmai = (LinearLayout) inflate.findViewById(R.id.lianmai_container);
                        LinearLayout imageChangkong = (LinearLayout) inflate.findViewById(R.id.changkong_container);
                        LinearLayout imageJinyan = (LinearLayout) inflate.findViewById(R.id.jinyan_container);
                        LinearLayout imageLahei = (LinearLayout) inflate.findViewById(R.id.lahei_container);
                        LinearLayout imageJubao = (LinearLayout) inflate.findViewById(R.id.jubao_container);
                        TextView textView = (TextView) inflate.findViewById(R.id.caozuo_cancel);
                        ClickListener clickListener = new ClickListener();
                        textView.setOnClickListener(clickListener);
                        imageLianmai.setVisibility(View.GONE);
                        imageChangkong.setVisibility(View.GONE);
                        imageJinyan.setVisibility(View.GONE);
                        imageJubao.setVisibility(View.VISIBLE);
                        imageJubao.setOnClickListener(clickListener);
                        if ("1".equals(guard_status) && !otherUserId.equals(channel_creater)) {
                            imageJinyan.setVisibility(View.VISIBLE);
                            imageJinyan.setOnClickListener(clickListener);
                        }

                    } else {
                        mPopupCaozuoWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, locX, locY);
                    }
                    break;
                case R.id.dialog_user_private_message:
                    LCChatKit.getInstance().open(userId, new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVIMException e) {
                            if (null == e) {
                                Intent intent = new Intent(LivingActivity.this, Chat.class);
                                intent.putExtra(LCIMConstants.PEER_ID, otherUserId);
                                startActivity(intent);
                                // LivingActivity.this.finish();
                            } else {
                                toast(e.toString());
                            }
                        }
                    });
                    break;
                case R.id.dialog_user_attention_area: //关注数
                    Bundle attention_data = new Bundle();
                    attention_data.putString("id", otherUserId);
                    openActivity(UserMainActivity.class, attention_data);
                    break;
                case R.id.dialog_user_fans_area: //粉丝数
                    Bundle fans_data = new Bundle();
                    fans_data.putString("id", otherUserId);
                    fans_data.putInt("type", 2);
                    openActivity(UserMainActivity.class, fans_data);
                    break;
                case R.id.dialog_user_total_area: //贡献榜
                    Bundle total_data = new Bundle();
                    total_data.putString("id", otherUserId);
                    openActivity(ContributionActivity.class, total_data);
                    break;
            }
        }
    }
}
