package tv.niuwa.live.living;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import android.widget.ListView;
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
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bru.toolkit.utils.Logs;
import com.bru.toolkit.utils.screenrotate.OrientationSensorListener;
import com.bumptech.glide.Glide;
import com.ksyun.media.rtc.kit.KSYRtcStreamer;
import com.ksyun.media.rtc.kit.RTCClient;
import com.ksyun.media.rtc.kit.RTCConstants;
import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.capture.camera.CameraTouchHelper;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.OnAudioRawDataListener;
import com.ksyun.media.streamer.kit.OnPreviewFrameListener;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.logstats.StatsLogReport;
import com.ksyun.media.streamer.util.audio.KSYBgmPlayer;
import com.bru.toolkit.views.HorizontalListView;
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
import tv.niuwa.live.LocationService;
import tv.niuwa.live.MyApplication;
import tv.niuwa.live.danmaku.DanmaManager;
import tv.niuwa.live.intf.OnCustomClickListener;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.lean.Chat;
import tv.niuwa.live.lean.ConversationListActivity;
import tv.niuwa.live.living.adapter.DanmuAdapter;
import tv.niuwa.live.living.adapter.ManagerAdapter;
import tv.niuwa.live.living.adapter.OnlineUserAdapter;
import tv.niuwa.live.living.model.DanmuModel;
import tv.niuwa.live.living.model.GiftModel;
import tv.niuwa.live.living.model.GiftSendModel;
import tv.niuwa.live.living.model.ManagerModel;
import tv.niuwa.live.living.model.TermModel;
import tv.niuwa.live.living.model.UserModel;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.music.MusicActivity;
import tv.niuwa.live.music.MusicItem;
import tv.niuwa.live.music.MusicListAdapter;
import tv.niuwa.live.own.UserMainActivity;
import tv.niuwa.live.own.authorize.AuthorizeActivity;
import tv.niuwa.live.own.userinfo.ContributionActivity;
import tv.niuwa.live.utils.AVImClientManager;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.utils.DialogEnsureUtiles;
import tv.niuwa.live.utils.Utile;
import tv.niuwa.live.view.LotteryDialog;
import tv.niuwa.live.view.LoveAnimView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

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
import tv.niuwa.live.R;

public class PublishActivity extends BaseActivity implements AdapterView.OnItemClickListener, ActivityCompat.OnRequestPermissionsResultCallback, OnShareStatusListener {

    private OrientationSensorListener listener;
    private final static int PERMISSION_REQUEST_CAMERA_AUDIOREC = 1;
    public List<GiftSendModel> giftSendModelList = new ArrayList<GiftSendModel>();
    public GiftSendModel mGiftSendModel1;
    public GiftSendModel mGiftSendModel2;
    @Bind(R.id.add_living_location)
    TextView addLivingLocation;
    @Bind(R.id.gift_layout1)
    GiftFrameLayout giftFrameLayout1;
    @Bind(R.id.gift_layout2)
    GiftFrameLayout giftFrameLayout2;
    @Bind(R.id.live_viewflipper)
    ViewFlipper mLiveViewFlipper;
    OnlineUserAdapter mOnlineUserAdapter;
    @Bind(R.id.live_share)
    ImageButton mLiveShare;
    @Bind(R.id.live_lianmai_num)
    TextView mLiveLianmaiNum;
    @Bind(R.id.HorizontalListView)
    HorizontalListView mLiveOnlineUsers;
    @Bind(R.id.live_gift)
    ImageButton mLiveGift;
    @Bind(R.id.live_meiyan)
    ImageButton mLiveMeiyan;
    @Bind(R.id.danmu_container)
    RelativeLayout mDanmuContainer;
//    @Bind(R.id.live_user_avatar)
//    CircleImageView mLiveUserAvatar;
//    @Bind(R.id.live_user_nicename)
//    TextView mLiveUserNicename;
    @Bind(R.id.input_live_title)
    EditText mInputLiveTitle;
    @Bind(R.id.live_user_online_num)
    TextView mLiveUserOnlineNum;
//    @Bind(R.id.live_user_total)
//    TextView mLiveUserTotal;
//    @Bind(R.id.live_user_id)
//    TextView mLiveUserId;
    @Bind(R.id.radio_share_wechat_moment)
    CheckBox mRadioShareWechatMoment;
    @Bind(R.id.radio_share_wechat)
    CheckBox mRadioShareWechat;
    @Bind(R.id.radio_share_sina)
    CheckBox mRadioShareSina;
    @Bind(R.id.radio_share_qq)
    CheckBox mRadioShareqq;
    @Bind(R.id.radio_share_zone)
    CheckBox mRadioShareZone;
    @Bind(R.id.live_bottom_btn)
    LinearLayout mLiveBottomBtn;
    @Bind(R.id.live_bottom_send)
    LinearLayout mLiveBottomSend;
    @Bind(R.id.live_message_container_rl)
    RelativeLayout live_message_container_rl;
    @Bind(R.id.live_edit_input)
    EditText mLiveEditInput;
    @Bind(R.id.live_btn_send)
    Button mLiveBtnSend;
    @Bind(R.id.live_top_layer)
    FrameLayout mLiveTopLayer;
    @Bind(R.id.music_stop)
    TextView mMusicStop;
    @Bind(R.id.live_gift_container)
    LinearLayout mLiveGiftContainer;
    @Bind(R.id.living_gift_big)
    ImageView mLivingGiftBig;
    @Bind(R.id.live_gift_scroll)
    ScrollView mLiveGiftScroll;
    LinearLayout mUserDialogControlContainer;
    @Bind(R.id.image_own_message)
    ImageView mImageOwnMessage;
//    @Bind(R.id.btn_follow)
//    Button mBtnFollow;
    @Bind(R.id.image_own_unread)
    ImageView mImageOwnUnread;
    @Bind(R.id.camera_reverse)
    ImageView mCameraReverse;
    @Bind(R.id.live_music)
    ImageView mLiveMusic;
    @Bind(R.id.add_living)
    LinearLayout mAddLiving;
    @Bind(R.id.publish_more)
    LinearLayout mPublishMore;
    @Bind(R.id.text_topic)
    TextView mTextTopic;
    @Bind(R.id.live_icon_large_ll)
    LinearLayout live_icon_large_ll;
    @Bind(R.id.publish_shop_icon)
    ImageView publish_shop_icon;
//    @Bind(R.id.live_sidou)
//    LinearLayout live_sidou;

    @Bind(R.id.audience_vote_rl)
    RelativeLayout audience_vote_rl;

    @Bind(R.id.sv_danmaku)
    DanmakuView mDanmakuView;
    DanmaManager mDanmaManager;

    CircleImageView mDialogUserAvatar;
    TextView mDialogUserNicename;
    ImageView mDialogClose;
    CircleImageView mDialogUserAvatarSmall;
    ImageView mDialogUserSex;
    TextView mDialogUserLevel;
    TextView mDialogUserId;
    TextView mDialogUserLocation;
    TextView mDialogUserSignature;
    TextView mDialogUserSpend;
    TextView mDialogUserAttentionNum;
    TextView mDialogUserFansNum;
    TextView mDialogPrivateMessage;
    TextView mDialogUserTotal;
    ImageView mDialogUserReal;
    TextView mDialogUserAttention;
    TextView mDialogUserMain;
    TextView mDialogCaozuo;
    TextView dialogUserChangkong;
    ImageView dialogAttentionImage;
    ImageView dialogChangkongImageOff;
    private final int MUSIC_CODE = 1;
    private final int TOPIC_CODE = 2;
    //live music

    @Bind(R.id.add_living_type)
    TextView mAddLivingType;
    //live end
    //实时数据
    Handler dataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    Boolean bgm = false;
    ArrayList<MusicItem> localMusic;
    MusicListAdapter localMusicList1;
    ArrayList<MusicItem> netMusic;
    MusicListAdapter netMusicList1;
    //礼物
    GiftSendModel mTempGiftSendModel1;
    GiftSendModel mTempGiftSendModel2;
    ArrayList<GiftSendModel> bigAnim = new ArrayList();
    AnimatorSet animatorSet1;
    AnimatorSet animatorSet2;
    private PopupWindow mPopupShareWindow;//类型选择
    private PopupWindow mPopupCaozuoWindow;//操作类型
    private String payMoney = "";
    private String privacy = "0";
    private String pass = "";
    private String shopUrl = "";
    private ThirdShare mThirdShare;
    private LocationService locationService;
    private double mLatitude;
    private double mLongitude;
    private String mLocation;
    private Boolean ifLocation = true;
    //live start
    private boolean meiyan = false;
    private boolean slight = false;
    private GLSurfaceView mCameraPreviewView;
    //private TextureView mCameraPreviewView;
    private CameraHintView mCameraHintView;
    private KSYRtcStreamer mStreamer;
    private Handler mMainHandler;
    private Timer mTimer;
    private boolean mAutoStart = false;
    private boolean mIsLandscape;
    private boolean mPrintDebugInfo = false;
    private boolean mRecording = false;
    private boolean isFlashOpened = false;
    private String mUrl;
    private String mDebugInfo = "";
    private String mBgmPath = "/sdcard/test.mp3";
    private String mLogoPath = "file:///sdcard/test.png";
    private ArrayList<UserModel> mUserItems;
    private AVIMConversation mSquareConversation;
    private String TAG = LivingActivity.class.getName();
    private JSONObject liveInfo;
    private String user_nicename;
    private String user_level;
    private String avatar;
    private String otherUserId;
    private String otherUserName;
    private int onlineNum = 0;
    private boolean mIsTorchOn = false;
    private AlertDialog userInfoDialog;
    private String token;

    //rtc
    private AuthHttpTask mRTCAuthTask;
    private AuthHttpTask.KSYOnHttpResponse mRTCAuthResponse;
    private boolean mIsRegisted;
    private boolean mIsConnected;
    private boolean mHWEncoderUnsupported;
    private boolean mSWEncoderUnsupported;
    private final String RTC_AUTH_SERVER = "http://rtc.vcloud.ks-live.com:6002/rtcauth";
    private final String RTC_AUTH_URI = "https://rtc.vcloud.ks-live.com:6001";
    private final String RTC_UINIQUE_NAME = "apptest";
    @Bind(R.id.lianmai_stop)
    TextView mLianmaiStop;
    Runnable dataRunnable = new Runnable() {
        @Override
        public void run() {
            JSONObject params = new JSONObject();
            params.put("token", token);
            params.put("room_id", liveInfo.getString("room_id"));
            Api.getLiveRealTimeNum(PublishActivity.this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data1) {
                    if (isActive) {
                        JSONObject data = data1.getJSONObject("data");
                        if (null != mLiveUserOnlineNum /*&& null != mLiveUserTotal*/) {
//                            mLiveUserTotal.setText(data.getString("total_earn"));
                            // mLiveUserOnlineNum.setText(onlineNum + "");
                            mLiveUserOnlineNum.setText(data.getString("online_num"));
                            dataHandler.postDelayed(dataRunnable, 2000);
                        }
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    switch (code) {
                        case 506:
                            toast("直播被封禁");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (isActive) {
                                        openActivity(PublishStopActivity.class);
                                        PublishActivity.this.finish();
                                    }
                                }
                            }, 2000);
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
    private String userId;//用户id
    private String channel_creater;//主播id
    private JSONArray sysMessage;
    private float mLastX;
    private float mLastY;
    private Boolean danmuChecked = false;
    private ArrayList<DanmuModel> lianmaiList;
    private KSYStreamer.OnInfoListener mOnInfoListener = new KSYStreamer.OnInfoListener() {
        @Override
        public void onInfo(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_INIT_DONE:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_INIT_DONE");
                    break;
                case StreamerConstants.KSY_STREAMER_OPEN_STREAM_SUCCESS:
                    Log.d(TAG, "KSY_STREAMER_OPEN_STREAM_SUCCESS");
                    pushCallBack();
                    break;
                case StreamerConstants.KSY_STREAMER_FRAME_SEND_SLOW:
                    Log.d(TAG, "KSY_STREAMER_FRAME_SEND_SLOW " + msg1 + "ms");
                    toast("Network not good!");
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_RAISE:
                    Log.d(TAG, "BW raise to " + msg1 / 1000 + "kbps");
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_DROP:
                    Log.d(TAG, "BW drop to " + msg1 / 1000 + "kpbs");
                    break;
                default:
                    Log.d(TAG, "OnInfo: " + what + " msg1: " + msg1 + " msg2: " + msg2);
                    break;
            }
        }
    };
    private KSYStreamer.OnErrorListener mOnErrorListener = new KSYStreamer.OnErrorListener() {
        @Override
        public void onError(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_ERROR_DNS_PARSE_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_DNS_PARSE_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_PUBLISH_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_PUBLISH_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_BREAKED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_BREAKED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_AV_ASYNC:
                    Log.d(TAG, "KSY_STREAMER_ERROR_AV_ASYNC " + msg1 + "ms");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_SERVER_DIED");
                    break;
                default:
                    Log.d(TAG, "what=" + what + " msg1=" + msg1 + " msg2=" + msg2);
                    break;
            }
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    mStreamer.stopCameraPreview();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startCameraPreviewWithPermCheck();
                        }
                    }, 5000);
                    break;
                default:
                    stopStream();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startStream();
                        }
                    }, 3000);
                    break;
            }
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
    private long lastClickTime = 0;
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                mLocation = location.getCountry() + "-" + location.getProvince() + "-" + location.getCity();
                addLivingLocation.setText(location.getCity());
                if (null != locationService) {
                    locationService.stop(); //停止定位服务
                }

            }
        }

    };

    private void pushCallBack() {
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("action", "push");
        Api.pushCallback(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {

            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });
    }

    int radioId = 0;

    @OnClick({R.id.radio_share_wechat_moment, R.id.radio_share_wechat, R.id.radio_share_sina, R.id.radio_share_qq, R.id.radio_share_zone})
    public void shareRadio(CheckBox v) {
        radioId = 0;
        switch (v.getId()) {
            case R.id.radio_share_wechat:
                if (v.isChecked()) {
                    radioId = v.getId();
                    setCheckByid(v);
                }
                break;
            case R.id.radio_share_sina:
                if (v.isChecked()) {
                    radioId = v.getId();
                    setCheckByid(v);
                }
                break;
            case R.id.radio_share_qq:
                if (v.isChecked()) {
                    radioId = v.getId();
                    setCheckByid(v);
                }
                break;
            case R.id.radio_share_wechat_moment:
                if (v.isChecked()) {
                    radioId = v.getId();
                    setCheckByid(v);
                }
                break;
            case R.id.radio_share_zone:
                if (v.isChecked()) {
                    radioId = v.getId();
                    setCheckByid(v);
                }
                break;
        }
    }

    private void setCheckByid(CheckBox c) {
        mRadioShareWechatMoment.setChecked(false);
        mRadioShareWechat.setChecked(false);
        mRadioShareqq.setChecked(false);
        mRadioShareSina.setChecked(false);
        mRadioShareZone.setChecked(false);
        c.setChecked(true);
    }

    JSONObject info;

    @OnClick(R.id.btn_start_living)
    public void btnStartLiveing() {
        String title = mInputLiveTitle.getText().toString();
        if (StringUtils.isEmpty(title)) {
            toast("先输入一个标题吧");
            return;
        }

        if (mThirdShare != null && radioId != 0) {
            JSONObject params = new JSONObject();
            params.put("token", token);
            params.put("room_id", token);
            Api.getShareInfo(this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    info = data.getJSONObject("data");
                    switch (radioId) {
                        case R.id.radio_share_zone:
                            //toast("qzone");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setTitleUrl(info.getString("shareUrl"));
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.share2QZone();
                            break;
                        case R.id.radio_share_qq:
                            //toast("qq");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setTitleUrl(info.getString("shareUrl"));
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.share2QQ();
                            break;
                        case R.id.radio_share_sina:
                            //toast("weibo");
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.share2SinaWeibo(false);
                            break;
                        case R.id.radio_share_wechat:
                            //toast("wechat");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.setUrl(info.getString("shareUrl"));
                            mThirdShare.share2Wechat();
                            break;
                        case R.id.radio_share_wechat_moment:
                            //toast("wechat moment");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.setUrl(info.getString("shareUrl"));
                            mThirdShare.share2WechatMoments();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        } else {
            startLive(title);
        }
    }

    @OnClick(R.id.lianmai_stop)
    public void lianmaiStop(View v) {
        if (null != mStreamer) {
            mStreamer.getRtcClient().stopCall();
            v.setVisibility(View.GONE);
        }
    }

    private void uploadLocation() {
        if (!StringUtils.isEmpty(token)) {
            JSONObject params = new JSONObject();
            params.put("token", token);
            if (mLongitude != 0 && mLatitude != 0) {
                params.put("longitude", mLongitude);
                params.put("latitude", mLatitude);
                params.put("location", mLocation);
                Api.setLocation(this, params, new OnRequestDataListener() {
                    @Override
                    public void requestSuccess(int code, JSONObject data) {

                    }

                    @Override
                    public void requestFailure(int code, String msg) {

                    }
                });
            }
        }
    }

    @OnClick(R.id.publish_shop_icon)
    public void shop(View v) {
        DialogEnsureUtiles.showInfo(PublishActivity.this, new OnCustomClickListener() {
            @Override
            public void onClick(String value) {
                if (StringUtils.isEmpty(value)) {
                    toast("请输入链接");
                    return;
                }
                shopUrl = value;
                JSONObject params = new JSONObject();
                params.put("token", token);
                params.put("room_id", liveInfo.getString("room_id"));
                params.put("shop_url", value);
                Api.setShopLink(PublishActivity.this, params, new OnRequestDataListener() {
                    @Override
                    public void requestSuccess(int code, JSONObject data) {
                        toast(data.getString("descrp"));
                    }

                    @Override
                    public void requestFailure(int code, String msg) {
                        toast(msg);
                    }
                });
            }
        }, shopUrl, "请输入推广地址");
    }

//    @OnClick(R.id.live_user_avatar)
//    public void getUserInfo(View view) {
//        ImageView temp = (ImageView) view;
//        String uid = (String) temp.getTag(R.id.image_live_avatar);
//        otherUserId = uid;
//        showUserInfoDialogById(uid);
//    }

    @OnClick(R.id.text_topic)
    public void textTopic(View v) {
        Intent it = new Intent(this, TopicActivity.class);
        startActivityForResult(it, TOPIC_CODE);
        //openActivity(TopicActivity.class);
    }

    @OnClick(R.id.add_living_location)
    public void openLocation() {
        if (ifLocation) {
            addLivingLocation.setText("请稍后...");
            if (null == locationService) {
                locationService = ((MyApplication) getApplication()).locationService;
                //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
                locationService.registerListener(mListener);
                //注册监听
                locationService.setLocationOption(locationService.getDefaultLocationClientOption());
            }
            locationService.start();
        } else {
            addLivingLocation.setText("开启定位");
            mLongitude = 0;
            locationService.stop();
        }

        ifLocation = !ifLocation;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case MUSIC_CODE:
                    mBgmPath = data.getStringExtra("path");
                    if (StringUtils.isNotEmpty(mBgmPath)) {
                        mStreamer.getAudioPlayerCapture().getBgmPlayer()
                                .setOnCompletionListener(new KSYBgmPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(KSYBgmPlayer bgmPlayer) {
                                        Log.d(TAG, "End of the currently playing music");
                                    }
                                });
                        mStreamer.getAudioPlayerCapture().getBgmPlayer()
                                .setOnErrorListener(new KSYBgmPlayer.OnErrorListener() {
                                    @Override
                                    public void onError(KSYBgmPlayer bgmPlayer, int what, int extra) {
                                        Log.e(TAG, "onBgmError: " + what);
                                    }
                                });
                        mStreamer.getAudioPlayerCapture().getBgmPlayer().setVolume(0.3f);
                        mStreamer.getAudioPlayerCapture().getBgmPlayer().setMute(false);
                        mStreamer.stopBgm();
                        mStreamer.startBgm(mBgmPath, true);
                        mLiveMusic.setImageResource(R.drawable.bgm_on);
                        mMusicStop.setVisibility(View.VISIBLE);
                    }
                    break;
                case TOPIC_CODE:
                    TermModel term = (TermModel) data.getSerializableExtra("term");
                    mTextTopic.setText(term.getName());
                    mTextTopic.setTag(term.getTerm_id());
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick(R.id.music_stop)
    public void musicStop() {
        if (null != mStreamer) {
            mStreamer.stopBgm();
            mMusicStop.setVisibility(View.GONE);
            mLiveMusic.setImageResource(R.drawable.bgm_off);

        }
    }

    public void setBGM(String path) {
        bgm = !bgm;
        mStreamer.getAudioPlayerCapture().getBgmPlayer()
                .setOnCompletionListener(new KSYBgmPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(KSYBgmPlayer bgmPlayer) {
                        Log.d(TAG, "End of the currently playing music");
                    }
                });
        mStreamer.getAudioPlayerCapture().getBgmPlayer()
                .setOnErrorListener(new KSYBgmPlayer.OnErrorListener() {
                    @Override
                    public void onError(KSYBgmPlayer bgmPlayer, int what, int extra) {
                        Log.e(TAG, "onBgmError: " + what);
                    }
                });
        mStreamer.getAudioPlayerCapture().getBgmPlayer().setVolume(1.0f);
        mStreamer.getAudioPlayerCapture().getBgmPlayer().setMute(false);
        mStreamer.startBgm(path, true);
        mLiveMusic.setImageResource(R.drawable.bgm_on);
    }

    @OnClick(R.id.add_living_type)
    public void addLivingType() {
        int locX = 0;
        int locY = 0;
        if (mPopupShareWindow == null || !mPopupShareWindow.isShowing()) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.layout_shape_dialog_livetype, null);
            mPopupShareWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupShareWindow.setBackgroundDrawable(new ColorDrawable(0x50000000));
            mPopupShareWindow.setFocusable(true);
            mPopupShareWindow.setAnimationStyle(R.anim.bottom_in);
            mPopupShareWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, locX, locY);
            ImageView imagePublic = (ImageView) inflate.findViewById(R.id.image_live_public);
            ImageView imagePass = (ImageView) inflate.findViewById(R.id.image_live_pass);
            ImageView imagePay = (ImageView) inflate.findViewById(R.id.image_live_pay);
            ImageView imagePrivate = (ImageView) inflate.findViewById(R.id.image_live_private);
            TextView textView = (TextView) inflate.findViewById(R.id.tv_cancel);
            ClickListener clickListener = new ClickListener();
            textView.setOnClickListener(clickListener);
            imagePublic.setOnClickListener(clickListener);
            imagePass.setOnClickListener(clickListener);
            imagePay.setOnClickListener(clickListener);
            imagePrivate.setOnClickListener(clickListener);
        } else {
            mPopupShareWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, locX, locY);
        }
    }

    @OnClick(R.id.live_music)
    public void livMusic(View view) {
        //openActivity(MusicActivity.class);
        Intent i = new Intent(this, MusicActivity.class);
        startActivityForResult(i, MUSIC_CODE);
    }

    @OnClick(R.id.live_more)
    public void liveMore() {
        if (mPublishMore.getVisibility() == View.VISIBLE) {
            mPublishMore.setVisibility(View.GONE);
        } else {
            mPublishMore.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.my_changkong)
    public void myChangkong() {
        JSONObject params1 = new JSONObject();
        params1.put("token", token);
        Api.getManagerList(PublishActivity.this, params1, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                ArrayList<ManagerModel> managerList = new ArrayList<ManagerModel>();
                JSONArray list1 = data.getJSONArray("data");
                for (int i = 0; i < list1.size(); i++) {
                    ManagerModel aa = new ManagerModel();
                    JSONObject tempUser = list1.getJSONObject(i);
                    aa.setUserId(tempUser.getString("id"));
                    aa.setUserName(tempUser.getString("user_nicename"));
                    aa.setAvatar(tempUser.getString("avatar"));
                    managerList.add(aa);
                }
                LinearLayout linearLayoutMain = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item_dialog_manager, null);
                ListView list = (ListView) linearLayoutMain.findViewById(R.id.manager_list);
                ManagerAdapter managerAdapter = new ManagerAdapter(PublishActivity.this, managerList);
                list.setAdapter(managerAdapter);
                AlertDialog dialog1 = new AlertDialog.Builder(PublishActivity.this)
                        .setTitle("我的场控").setView(linearLayoutMain)//在这里把写好的这个listview的布局加载dialog中
                        .create();
                dialog1.setCanceledOnTouchOutside(true);//使除了dialog以外的地方不能被点击
                dialog1.show();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    PopupWindow mPopupShareWindow1;

    @OnClick(R.id.my_share)
    public void mgShare() {
        int locX = 0;
        int locY = 0;
        if (mPopupShareWindow1 == null || !mPopupShareWindow1.isShowing()) {
            View inflate = LayoutInflater.from(PublishActivity.this).inflate(R.layout.layout_shape_dialog, null);
            mPopupShareWindow1 = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupShareWindow1.setBackgroundDrawable(new ColorDrawable(0x50000000));
            mPopupShareWindow1.setFocusable(true);
            mPopupShareWindow1.showAtLocation(mLiveShare, Gravity.BOTTOM, locX, locY);
            ImageView shareQZone = (ImageView) inflate.findViewById(R.id.image_live_share_qzone);
            ImageView shareQQ = (ImageView) inflate.findViewById(R.id.image_live_share_qq);
            ImageView shareSina = (ImageView) inflate.findViewById(R.id.image_live_share_sina);
            ImageView shareWechat = (ImageView) inflate.findViewById(R.id.image_live_share_wechat);
            ImageView shareWechatMoment = (ImageView) inflate.findViewById(R.id.image_live_share_wechat_moment);
            TextView textView = (TextView) inflate.findViewById(R.id.tv_cancel);
            PublishActivity.ClickListener clickListener = new PublishActivity.ClickListener();
            textView.setOnClickListener(clickListener);
            shareQZone.setOnClickListener(clickListener);
            shareQQ.setOnClickListener(clickListener);
            shareSina.setOnClickListener(clickListener);
            shareWechat.setOnClickListener(clickListener);
            shareWechatMoment.setOnClickListener(clickListener);
        } else {
            mPopupShareWindow1.showAtLocation(mLiveShare, Gravity.BOTTOM, locX, locY);
        }
    }

//    @OnClick(R.id.live_sidou)
//    public void showUserContribution() {
//        Bundle data = new Bundle();
//        data.putString("id", channel_creater);
//        openActivity(ContributionActivity.class, data);
//    }

    public void showUserInfoDialogById(String uid) {
        if (StringUtils.isEmpty(uid)) {
            toast("uid数据有误");
            return;
        }
        if (userInfoDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PublishActivity.this);
            View inflate = LayoutInflater.from(this).inflate(R.layout.layout_userinfo_tips, null);
            mUserDialogControlContainer = (LinearLayout) inflate.findViewById(R.id.user_dialog_control_container);
            dialogUserChangkong = (TextView) inflate.findViewById(R.id.dialog_user_changkong);
//            dialogChangkongImageOff = (ImageView) inflate.findViewById(R.id.changkong_image_off);
            mDialogCaozuo = (TextView) inflate.findViewById(R.id.dialog_caozuo);
            mDialogUserAvatar = (CircleImageView) inflate.findViewById(R.id.dialog_user_avatar);
            mDialogUserAvatarSmall = (CircleImageView) inflate.findViewById(R.id.dialog_user_avatar_small);
            mDialogUserNicename = (TextView) inflate.findViewById(R.id.dialog_user_nicename);
            mDialogUserSex = (ImageView) inflate.findViewById(R.id.dialog_user_sex);
            mDialogUserLevel = (TextView) inflate.findViewById(R.id.dialog_user_level);
            mDialogUserId = (TextView) inflate.findViewById(R.id.dialog_user_id);
            mDialogUserLocation = (TextView) inflate.findViewById(R.id.dialog_user_location);
            mDialogUserSignature = (TextView) inflate.findViewById(R.id.dialog_user_signature);
            mDialogUserSpend = (TextView) inflate.findViewById(R.id.dialog_user_spend);
            mDialogUserAttentionNum = (TextView) inflate.findViewById(R.id.dialog_user_attention_num);
            mDialogUserFansNum = (TextView) inflate.findViewById(R.id.dialog_user_fans_num);
            mDialogUserTotal = (TextView) inflate.findViewById(R.id.dialog_user_total);
            mDialogUserReal = (ImageView) inflate.findViewById(R.id.dialog_user_real);
            mDialogUserAttention = (TextView) inflate.findViewById(R.id.dialog_user_attention);
//            dialogAttentionImage = (ImageView) inflate.findViewById(R.id.dialog_attention_image);
            mDialogPrivateMessage = (TextView) inflate.findViewById(R.id.dialog_user_private_message);
            mDialogUserMain = (TextView) inflate.findViewById(R.id.dialog_user_main);
            mDialogClose = (ImageView) inflate.findViewById(R.id.dialog_close);
            ClickListener clickListener = new ClickListener();
            mDialogClose.setOnClickListener(clickListener);
            mDialogUserAttention.setOnClickListener(clickListener);
            mDialogUserMain.setOnClickListener(clickListener);
            mDialogCaozuo.setOnClickListener(clickListener);
            mDialogPrivateMessage.setOnClickListener(clickListener);
            dialogUserChangkong.setOnClickListener(clickListener);
            userInfoDialog = builder.setView(inflate)
                    .create();
//            Window window = userInfoDialog.getWindow();
//            int dip2px = DensityUtils.dip2px(this, 40);
//            window.getDecorView().setPadding(dip2px, 0, dip2px, 0);
            Window window = userInfoDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        //dialogAttentionImage.setVisibility(View.VISIBLE);
        mUserDialogControlContainer.setVisibility(View.VISIBLE);
//        mDialogUserAttention.setVisibility(View.VISIBLE);
//        mDialogUserMain.setVisibility(View.VISIBLE);
//        mDialogPrivateMessage.setVisibility(View.VISIBLE);
        mDialogCaozuo.setVisibility(View.VISIBLE);
        mDialogUserAttention.setVisibility(View.VISIBLE);
        mDialogPrivateMessage.setVisibility(View.VISIBLE);
        //dialogChangkongImageOff.setImageResource(R.drawable.icon_changkong);
        // dialogUserChangkong.setTextColor(Color.BLACK);
        if (uid.equals(channel_creater)) {
            //mUserDialogControlContainer.setVisibility(View.INVISIBLE);
            mDialogCaozuo.setVisibility(View.GONE);
            mDialogUserAttention.setVisibility(View.GONE);
            //mDialogUserMain.setVisibility(View.INVISIBLE);
            mDialogPrivateMessage.setVisibility(View.GONE);
            // dialogUserChangkong.setVisibility(View.INVISIBLE);

        }
//        mDialogUserAttention.setText("关注");
//        mDialogJubao.setText("禁言");
//        dialogUserChangkong.setText("设为场控");
        userInfoDialog.show();
        getInfo(token, uid);
        //getFirstContribution(token, uid);
    }

    public void getFirstContribution(String token, String uid) {
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("id", uid);
        params.put("limit_num", 1);
        Api.getUserContributionList(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray list = data.getJSONArray("data");
                JSONObject item = list.getJSONObject(0);
                Glide.with(PublishActivity.this).load(item.getString("avatar"))
                        .error(R.drawable.icon_avatar_default)
                        .transform(new GlideCircleTransform(PublishActivity.this))
                        .into(mDialogUserAvatarSmall);
            }

            @Override
            public void requestFailure(int code, String msg) {
                //toast(msg);
                Glide.with(PublishActivity.this).load(R.drawable.icon_avatar_default).into(mDialogUserAvatarSmall);
            }
        });
    }

    public void getInfo(String token, String uid) {
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("id", uid);
        Api.getUserInfo(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONObject userInfo = data.getJSONObject("data");
                Glide.with(PublishActivity.this).load(userInfo.getString("avatar"))
                        .error(R.drawable.icon_avatar_default)
                        .transform(new GlideCircleTransform(PublishActivity.this))
                        .into(mDialogUserAvatar);
                Glide.with(PublishActivity.this).load(userInfo.getString("avatar"))
                        .error(R.drawable.icon_avatar_default)
                        .transform(new GlideCircleTransform(PublishActivity.this))
                        .into(mDialogUserAvatarSmall);
                mDialogUserNicename.setText(userInfo.getString("user_nicename"));
                if ("1".equals(userInfo.getString("sex"))) {
                    mDialogUserSex.setImageResource(R.drawable.userinfo_male);
                }
                mDialogUserLevel.setText(userInfo.getString("user_level"));
                int level = Integer.parseInt(userInfo.getString("user_level"));
                if (level > 4 && level < 9) {
                    Drawable levelMoon = PublishActivity.this.getResources().getDrawable(R.drawable.moon);
                    levelMoon.setBounds(0, 0, levelMoon.getMinimumWidth(), levelMoon.getMinimumHeight());
                    mDialogUserLevel.setCompoundDrawables(levelMoon, null, null, null);
                }
                if (level > 8 && level < 13) {
                    Drawable levelMoon = PublishActivity.this.getResources().getDrawable(R.drawable.sun);
                    levelMoon.setBounds(0, 0, levelMoon.getMinimumWidth(), levelMoon.getMinimumHeight());
                    mDialogUserLevel.setCompoundDrawables(levelMoon, null, null, null);
                }
                if (level > 12) {
                    Drawable levelMoon = PublishActivity.this.getResources().getDrawable(R.drawable.top);
                    levelMoon.setBounds(0, 0, levelMoon.getMinimumWidth(), levelMoon.getMinimumHeight());
                    mDialogUserLevel.setCompoundDrawables(levelMoon, null, null, null);
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

    @OnClick({R.id.live_meiyan, R.id.dl_image_meiyan})
    public void meiyan(View view) {
        meiyan = !meiyan;
        if (meiyan) {
            Drawable drawable_meiyan_off = PublishActivity.this.getResources().getDrawable(R.drawable.meiyan_on);
            mLiveMeiyan.setImageDrawable(drawable_meiyan_off);
            mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                    ImgTexFilterMgt.KSY_FILTER_BEAUTY_DENOISE);
            mStreamer.setEnableImgBufBeauty(true);
        } else {
            Drawable drawable_meiyan_off = PublishActivity.this.getResources().getDrawable(R.drawable.meiyan_off);
            mLiveMeiyan.setImageDrawable(drawable_meiyan_off);
            mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                    ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
            mStreamer.setEnableImgBufBeauty(false);
        }
    }

    @OnClick(R.id.living_danmu_container)
    public void start(View view) {


    }

    @OnClick({R.id.live_close, R.id.dl_image_close})
    public void close(View view) {
        DialogEnsureUtiles.showConfirm(this, "确定退出直播间吗", new OnCustomClickListener() {
            @Override
            public void onClick(String value) {
                //老版退出到结束页面
//                if (mRecording) {
//                    openActivity(PublishStopActivity.class);
//                }
//                PublishActivity.this.finish();

                //新版电视端tu退出
                PublishActivity.this.finish();
                Process.killProcess(Process.myPid());
            }
        });
    }

    @OnClick(R.id.live_send)
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

    @OnClick(R.id.image_own_message)
    public void imageOwnMessage(View v) {
        openActivity(ConversationListActivity.class);
    }

    @OnClick({R.id.camera_reverse, R.id.dl_image_camera})
    public void cameraReverse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                onSwitchCamClick();
            }
        }).start();
    }

    @OnClick(R.id.camera_onoff)
    public void cameraOnOff() {
        if (mStreamer.isFrontCamera()) return;
        slight = !slight;
        if (slight) {

        } else {
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                onFlashClick();
            }
        }).start();
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
            params.put("room_id", liveInfo.getString("room_id"));
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
                    showDanmuAnim(PublishActivity.this, model);
                }

                @Override
                public void requestFailure(int code, String msg) {
                    view.setEnabled(true);
                    toast(msg);
                }
            });
        } else {
            model.setType("1");
            model.setUserName(user_nicename);
            model.setUserLevel(user_level);
            model.setContent(content);
            model.setUserId(userId);
            model.setAvatar(avatar);
            sendMessage(model);
        }
    }

    public void sendMessage(DanmuModel model) {
        if (isActive) {

            mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
            mLiveEditInput.setText("");
            mLiveBottomBtn.setVisibility(View.VISIBLE);
            live_message_container_rl.setVisibility(View.GONE);
            SoftKeyboardUtils.closeSoftInputMethod(mLiveEditInput);
        }
        String message = JSONObject.toJSONString(model);
        EventBus.getDefault().post(
                new LCIMInputBottomBarTextEvent(LCIMInputBottomBarEvent.INPUTBOTTOMBAR_SEND_TEXT_ACTION, message, liveInfo.getString("leancloud_room")));

    }

    public void showDanmuAnim(final PublishActivity temp, DanmuModel model) {
        final View giftPop = View.inflate(temp, R.layout.item_danmu_pop, null);
        ImageView giftAvatar = (ImageView) giftPop.findViewById(R.id.gift_pop_avatar);
        TextView giftUserName = (TextView) giftPop.findViewById(R.id.gift_pop_username);
        TextView giftContent = (TextView) giftPop.findViewById(R.id.gift_pop_content);
        Glide.with(temp).load(model.getAvatar())
                .error(R.drawable.icon_avatar_default)
                .into(giftAvatar);
        giftUserName.setText(model.getUserName());
        giftContent.setText(model.getContent());
        temp.mDanmuContainer.addView(giftPop);
        Animation anim = AnimationUtils.loadAnimation(temp, R.anim.danmu_enter);
        giftPop.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isActive) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            temp.mDanmuContainer.removeView(giftPop);
                        }
                    });

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logs.e(getClass().getSimpleName(), "onCreate");
        token = (String) SharePrefsUtils.get(this, "user", "token", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //live start
        mCameraPreviewView = (GLSurfaceView) findViewById(R.id.camera_preview);
        mCameraHintView = (CameraHintView) findViewById(R.id.camera_hint);
        mMainHandler = new Handler();
        mStreamer = new KSYRtcStreamer(this);
        mStreamer.setPreviewFps(15);
        mStreamer.setTargetFps(15);
        int videoBitrate = 800;
        videoBitrate *= 1000;
        mStreamer.setVideoBitrate(videoBitrate * 3 / 4, videoBitrate, videoBitrate / 4);
        mStreamer.setAudioBitrate(48 * 1000);
        mStreamer.setPreviewResolution(StreamerConstants.VIDEO_RESOLUTION_480P);
        mStreamer.setTargetResolution(StreamerConstants.VIDEO_RESOLUTION_480P);
        mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //电视端 关闭摄像头功能
        mStreamer.getCameraCapture().setOnCameraCaptureListener(new CameraCapture.OnCameraCaptureListener() {
            @Override
            public void onStarted() {
                mStreamer.getCameraCapture().setOrientation(90);
//                        listener = new OrientationSensorListener(PublishActivity.this, new OnOrientationChanged() {
//                            @Override
//                            public void orientationChanged(int orientation) {
//                                Logs.e(TAG, "orientation --> " + orientation);
//                                switch (orientation) {
//                                    case 0:
//                                        mStreamer.getCameraCapture().setOrientation(0);
//                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                                        break;
//                                    case 90:
//                                        mStreamer.getCameraCapture().setOrientation(270);
//                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//                                        break;
//                                    case 180:
//                                        mStreamer.getCameraCapture().setOrientation(0);
//                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
//                                        break;
//                                    case 270:
//                                        mStreamer.getCameraCapture().setOrientation(90);
//                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                                        break;
//                                    default:
//                                        break;
//                                }
//
//
//                            }
//                        });

            }

            @Override
            public void onFacingChanged(int i) {

            }

            @Override
            public void onError(int i) {

            }
        });

        mStreamer.setDisplayPreview(mCameraPreviewView);
        mStreamer.setEnableStreamStatModule(true);
        mStreamer.enableDebugLog(true);
        mStreamer.setFrontCameraMirror(true);
        mStreamer.setMuteAudio(false);
        mStreamer.setEnableAudioPreview(false);
        mStreamer.setOnInfoListener(mOnInfoListener);
        mStreamer.setOnErrorListener(mOnErrorListener);
        mStreamer.setOnLogEventListener(mOnLogEventListener);
        //mStreamer.setOnAudioRawDataListener(mOnAudioRawDataListener);
        //mStreamer.setOnPreviewFrameListener(mOnPreviewFrameListener);
//        mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
//                ImgTexFilterMgt.KSY_FILTER_BEAUTY_DENOISE);
//        mStreamer.setEnableImgBufBeauty(true);
        mStreamer.getImgTexFilterMgt().setOnErrorListener(new ImgTexFilterBase.OnErrorListener() {
            @Override
            public void onError(ImgTexFilterBase filter, int errno) {
                toast("当前机型不支持该滤镜");
                mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                        ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
            }
        });
        // touch focus and zoom support
        CameraTouchHelper cameraTouchHelper = new CameraTouchHelper();
        cameraTouchHelper.setCameraCapture(mStreamer.getCameraCapture());
        mCameraPreviewView.setOnTouchListener(cameraTouchHelper);
        // set CameraHintView to show focus rect and zoom ratio
        cameraTouchHelper.setCameraHintView(mCameraHintView);

        //live end

        //set rtc info

        //edit by sbdx
        //mStreamer.getRtcClient().setRTCErrorListener(mRTCErrorListener);
        //mStreamer.getRtcClient().setRTCEventListener(mRTCEventListener);

//        mNetworkStateReceiver = new NetworkStateReceiver();
//        mNetworkStateReceiver.addListener(new WeakReference<NetworkStateReceiver.NetworkStateReceiverListener>(this));
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        intentFilter.addAction(NetworkStateReceiver.CONNECTIVITY_ACTION_LOLLIPOP);
//        this.registerReceiver(mNetworkStateReceiver, intentFilter);
//        registerConnectivityActionLollipop();
        //rtc

        mLiveGift.setVisibility(View.GONE);
        mLiveShare.setVisibility(View.GONE);
//        mBtnFollow.setVisibility(View.GONE);
        live_icon_large_ll.setVisibility(View.GONE);
        publish_shop_icon.setVisibility(View.GONE);
//        live_sidou.setVisibility(View.GONE);
        mLiveTopLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (live_message_container_rl.getVisibility() == View.VISIBLE) {
                    live_message_container_rl.setVisibility(View.GONE);
                    mLiveBottomBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        openLocation();

        mThirdShare = new ThirdShare(this);
        mThirdShare.setOnShareStatusListener(this);
        startLive("niuwa");

        mDanmaManager = new DanmaManager(this,mDanmakuView);
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
                if (!mLiveOnlineUsers.isHasScroll()) {
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
            }
            break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initData() {
        mUserItems = new ArrayList<UserModel>();
        if (null != sysMessage) {
            for (int i = 0; i < sysMessage.size(); i++) {
                DanmuModel model = new DanmuModel();
                model.setType("3");
                model.setUserName(sysMessage.getJSONObject(i).getString("title"));
                model.setContent(sysMessage.getJSONObject(i).getString("msg"));
                mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
            }

        }
        mOnlineUserAdapter = new OnlineUserAdapter(this, mUserItems);
        mLiveOnlineUsers.setAdapter(mOnlineUserAdapter);
        mLiveOnlineUsers.setOnItemClickListener(this);

        userId = (String) SharePrefsUtils.get(this, "user", "userId", "");
        user_nicename = (String) SharePrefsUtils.get(this, "user", "user_nicename", "");
        user_level = (String) SharePrefsUtils.get(this, "user", "user_level", "");
        avatar = (String) SharePrefsUtils.get(this, "user", "avatar", "");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            joinChat(userId, liveInfo.getString("leancloud_room"));
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            Api.getUserInfo(this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    JSONObject userInfo = data.getJSONObject("data");
//                    mLiveUserNicename.setText(userInfo.getString("user_nicename"));
//                    mLiveUserId.setText("ID:" + userInfo.getString("id"));
                    channel_creater = userInfo.getString("id");
//                    mLiveUserTotal.setText(userInfo.getString("sidou"));
//                    Glide.with(PublishActivity.this).load(userInfo.getString("avatar"))
//                            .error(R.drawable.icon_avatar_default)
//                            .transform(new GlideCircleTransform(PublishActivity.this))
//                            .into(mLiveUserAvatar);

//                    mLiveUserAvatar.setTag(R.id.image_live_avatar, userInfo.getString("id"));
                    dataHandler.postDelayed(dataRunnable, 2000);

                    dataHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onRTCRegisterClick();
                        }
                    }, 3000);

                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        } else {
            toast("请重新登录");
            openActivity(LoginActivity.class);
            finish();
        }

        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("room_id", token);
        Api.getShareInfo(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                info = data.getJSONObject("data");
            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });
    }

    private void startLive(String title) {
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("title", title);
        params.put("term_id", "1");
        if (null == mTextTopic.getTag()) {
            params.put("term_id", "1");
        } else {
            params.put("term_id", mTextTopic.getTag());
        }

        params.put("price", payMoney);
        params.put("privacy", privacy);
        params.put("room_password", pass);
        Api.startLive(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                mAddLiving.setVisibility(View.GONE);
                mLiveViewFlipper.setVisibility(View.VISIBLE);
                liveInfo = data.getJSONObject("data");
                sysMessage = data.getJSONArray("msg");
                if (liveInfo != null) {
                    initData();
                    mStreamer.setUrl(liveInfo.getString("push_rtmp"));
                    //电视端  不采集视频
//                    startStream();
                    pushCallBack();
                    uploadLocation();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getOnlineUsers();
                        }
                    }, 2000);
                } else {
                    toast("数据异常");
                }

            }

            @Override
            public void requestFailure(int code, String msg) {
                if (508 == code) {
                    openActivity(AuthorizeActivity.class);
                } else {
                    toast(msg);
                }
            }
        });
    }

    private void getOnlineUsers() {
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("room_id", liveInfo.getString("room_id"));
        Api.getOnlineUsers(PublishActivity.this, params, new OnRequestDataListener() {
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

    @Override
    public int getLayoutResource() {
        return R.layout.activity_publish;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUnread();
        EventBus.getDefault().register(this);
        startCameraPreviewWithPermCheck();
        mStreamer.onResume();
        if (mStreamer.isRecording() && !false) {
            mStreamer.setAudioOnly(false);
        }
        if (false) {
            showWaterMark();
        }
        if (null != mSquareConversation) {
            DanmuModel model = new DanmuModel();
            model.setType("6");
            model.setUserName("系统消息");
            model.setAvatar(avatar);
            model.setUserId(userId);
            model.setContent("主播回来啦，视频即将恢复");
            AVIMTextMessage message = new AVIMTextMessage();
            message.setText(JSONObject.toJSONString(model));
            mSquareConversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {

                }
            });
        }
        if (null != listener)
            listener.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        mStreamer.onPause();
        DanmuModel model = new DanmuModel();
        model.setType("6");
        model.setUserName("系统消息");
        model.setAvatar(avatar);
        model.setUserId(userId);
        model.setContent("主播离开一下，精彩不中断，不要走开哦");
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(JSONObject.toJSONString(model));
        if (mSquareConversation != null) {
            mSquareConversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {

                }
            });

        }
        mStreamer.stopCameraPreview();
        if (mStreamer.isRecording() && !false) {
            mStreamer.setAudioOnly(false);
        }
        hideWaterMark();
        if (null != listener)
            listener.onPause();
    }

    @Override
    protected void onStop() {
        if (null != locationService) {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        super.onStop();
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

                    }
                });
            }
        }
    }

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
            if(!model.getType().equals("5") && !model.getType().equals("6") && !model.getType().equals("10") && !model.getType().equals("10")) {
                mDanmaManager.addChatDanma(model.getUserId(),model.getUserName(),model.getContent());
            }
            switch (model.getType()) {
                case "4":
                    //clickHeart();
                    break;
                case "7":
                    showDanmuAnim(this, model);
                    break;
                case "8":
                    if (null == lianmaiList) {
                        lianmaiList = new ArrayList<>();
                    }
                    //最多连麦人数限制10
                    if (lianmaiList.size() >= 10) {
                        lianmaiList.remove(0);
                    }
                    for (int i = 0; i < lianmaiList.size(); i++) {
                        if (lianmaiList.get(i).getUserId().equals(model.getUserId())) {
                            lianmaiList.remove(i);
                            mLiveLianmaiNum.setText("" + lianmaiList.size());
                            return;
                        }

                    }
                    lianmaiList.add(model);
                    mLiveLianmaiNum.setText("" + lianmaiList.size());
                    break;
                case "6":
                    //系统消息  -  进入房间
                    UserModel user = new UserModel();
                    user.setAvatar(model.getAvatar());
                    user.setId(model.getUserId());
                    user.setUser_nicename(model.getUserName());
                    mUserItems.add(user);
                    mOnlineUserAdapter.notifyDataSetChanged();
                    onlineNum++;
                    break;
                case "2":
                    //toast("显示礼物画面");
                    JSONObject giftO = temp.getJSONObject("other");
                    giftO = giftO.getJSONObject("giftInfo");
                    GiftModel gift = new GiftModel();
                    gift.setGifticon(giftO.getString("gifticon"));
                    gift.setGiftname(giftO.getString("giftname"));
                    gift.setGiftid(giftO.getString("giftid"));
                    gift.setContinuous(giftO.getString("continuous"));
                    String num = (null == giftO.getString("continuousNum")) ? "1" : giftO.getString("continuousNum");
                    showGiftAnim1(PublishActivity.this, model, gift, Integer.parseInt(num));
                    break;
                case "5":
                    //系统消息  -  离开房间
                    UserModel utemp = new UserModel();
                    utemp.setId(model.getUserId());
                    mUserItems.remove(utemp);
                    mOnlineUserAdapter.notifyDataSetChanged();
                    onlineNum--;
                    break;
                case "9":
                    showVote();
                    break;
                case "10":
                    audience_vote_rl.setVisibility(View.GONE);
                    break;
                case "13":
                    JSONArray array = temp.getJSONArray("reward");
                    StringBuffer sb = new StringBuffer();
                    for(int i = 0; i < array.size(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        sb.append(jo.getString("userId"));
                        if(i < array.size() - 1) {
                            sb.append(" ");
                        }
                    }
                    showLotteryDialog(sb.toString());
                    break;
                case "14":
                    hideLotteryDialog();
                    break;
                case "15":
                    // TODO: 15/10/2017  换背景图 
                    break;
            }
        }
    }

    private LotteryDialog mLotteryDialog;

    private void showLotteryDialog(String users) {
        LogUtils.d(TAG, "lottery users->" + users);
        if(mLotteryDialog == null) {
            mLotteryDialog = new LotteryDialog.Builder(this).create(users);
            mLotteryDialog.show();
        }
    }

    private void hideLotteryDialog() {
        if(mLotteryDialog != null && mLotteryDialog.isShowing()) {
            mLotteryDialog.dismiss();
            mLotteryDialog = null;
        }
    }

    private VoteFragment mVoteFragment;

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

    public void showGiftAnim1(final PublishActivity temp, DanmuModel danmuModel, GiftModel giftModel, int num) {
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

    public void showGiftAnim(final PublishActivity temp, DanmuModel danmuModel, GiftModel model, int num) {
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

    public void clickHeart() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LoveAnimView loveAnimView = new LoveAnimView(this);
        loveAnimView.setLayoutParams(params);
        loveAnimView.setStartPosition(new Point(530, 712));
        loveAnimView.setEndPosition(new Point(530 - (int) (Math.random() * 200), 712 - ((int) (Math.random() * 500) + 200)));
        loveAnimView.startLoveAnimation();
        mLiveTopLayer.addView(loveAnimView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataHandler.removeCallbacks(dataRunnable);
        if (null != mSquareConversation) {
            mSquareConversation.quit(new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {

                }
            });
        }
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
        if (mTimer != null) {
            mTimer.cancel();
        }
        mStreamer.release();
    }

    private void startStream() {
        mStreamer.startStream();
        mRecording = true;
    }

    private void stopStream() {
        mStreamer.stopStream();
        mRecording = false;
    }

    private void showWaterMark() {
        if (!mIsLandscape) {
            mStreamer.showWaterMarkLogo(mLogoPath, 0.08f, 0.04f, 0.20f, 0, 0.8f);
            mStreamer.showWaterMarkTime(0.03f, 0.01f, 0.35f, Color.RED, 1.0f);
        } else {
            mStreamer.showWaterMarkLogo(mLogoPath, 0.05f, 0.09f, 0, 0.20f, 0.8f);
            mStreamer.showWaterMarkTime(0.01f, 0.03f, 0.22f, Color.RED, 1.0f);
        }
    }

    @OnClick(R.id.live_message_container_rl)
    public void live_message_container_rl(View view) {
        if (live_message_container_rl.getVisibility() == View.VISIBLE) {
            live_message_container_rl.setVisibility(View.GONE);
            mLiveBottomBtn.setVisibility(View.VISIBLE);
        }
    }

    private void hideWaterMark() {
        mStreamer.hideWaterMarkLogo();
        mStreamer.hideWaterMarkTime();
    }

    private void onFlashClick() {
        if (isFlashOpened) {
            mStreamer.toggleTorch(false);
            isFlashOpened = false;
        } else {
            mStreamer.toggleTorch(true);
            isFlashOpened = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA_AUDIOREC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mStreamer.startCameraPreview();
                } else {
                    Log.e(TAG, "No CAMERA or AudioRecord permission");
                    Toast.makeText(this, "No CAMERA or AudioRecord permission",
                            Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                DialogEnsureUtiles.showConfirm(this, "确定退出直播间吗", new OnCustomClickListener() {
                    @Override
                    public void onClick(String value) {
                        //finish();
                        //客户端 需要返回至结束页面
//                        if (mRecording) {
//                            openActivity(PublishStopActivity.class);
//                        }
                        //电视端 直接退出
                        PublishActivity.this.finish();
                        Process.killProcess(Process.myPid());
                    }
                });
            }
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void joinChat(String ClientId, final String conversationId) {
        LCChatKit.getInstance().open(ClientId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                AVIMClient client = LCChatKit.getInstance().getClient();
                if (null != client) {
                    mSquareConversation = client.getConversation(conversationId);
                    LCIMNotificationUtils.addTag(conversationId);
                    joinSquare();
                } else {
                    toast("Please call open first!");
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

                }
            }
        });
    }

    /**
     * 先查询自己是否已经在该 conversation，如果存在则直接给 chatFragment 赋值，否则先加入，再赋值
     */
    private void queryInSquare(String conversationId) {
        final AVIMClient client = AVImClientManager.getInstance().getClient();
        AVIMConversationQuery conversationQuery = client.getQuery();
        conversationQuery.whereEqualTo("objectId", conversationId);
        conversationQuery.containsMembers(Arrays.asList(AVImClientManager.getInstance().getClientId()));
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (list.size() == 0) {

                } else {
                    joinSquare();
                }
            }
        });
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

    private void onSwitchCamClick() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastClickTime < 1000) {
            return;
        }
        lastClickTime = curTime;
        mStreamer.switchCamera();

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
                case R.id.dialog_user_main:
                    Bundle data = new Bundle();
                    data.putString("id", otherUserId);
                    openActivity(UserMainActivity.class, data);
                    break;
                case R.id.dialog_user_attention:
                    JSONObject requestParams = new JSONObject();
                    requestParams.put("token", token);
                    requestParams.put("userid", otherUserId);
                    Api.addAttention(PublishActivity.this, requestParams, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            TextView v = (TextView) view;
                            v.setText("已关注");
                            v.setEnabled(false);
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
                        View inflate = LayoutInflater.from(PublishActivity.this).inflate(R.layout.layout_shape_dialog_caozuo, null);
                        mPopupCaozuoWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        mPopupCaozuoWindow.setBackgroundDrawable(new ColorDrawable(0xe5000000));
                        mPopupCaozuoWindow.setFocusable(true);
                        mPopupCaozuoWindow.setAnimationStyle(R.anim.bottom_in);
                        mPopupCaozuoWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, locX, locY);
                        ImageView imageLianmai = (ImageView) inflate.findViewById(R.id.image_live_lianmai);
                        ImageView imageChangkong = (ImageView) inflate.findViewById(R.id.image_live_changkong);
                        ImageView imageJinyan = (ImageView) inflate.findViewById(R.id.image_live_jinyan);
                        TextView textView = (TextView) inflate.findViewById(R.id.caozuo_cancel);
                        ClickListener clickListener = new ClickListener();
                        textView.setOnClickListener(clickListener);
                        imageLianmai.setOnClickListener(clickListener);
                        imageChangkong.setOnClickListener(clickListener);
                        imageJinyan.setOnClickListener(clickListener);
                    } else {
                        mPopupCaozuoWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, locX, locY);
                    }
                    break;
                case R.id.dialog_user_private_message:
                    LCChatKit.getInstance().open(userId, new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVIMException e) {
                            if (null == e) {
                                Intent intent = new Intent(PublishActivity.this, Chat.class);
                                intent.putExtra(LCIMConstants.PEER_ID, otherUserId);
                                startActivity(intent);
                            } else {
                                toast(e.toString());
                            }
                        }
                    });
                    break;
                case R.id.camera_reverse:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            onSwitchCamClick();
                        }
                    }).start();
                    break;
                case R.id.camera_onoff:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            onFlashClick();
                        }
                    }).start();
                    break;
                case R.id.manager_jinyan:

                    break;
                case R.id.manager_changkong:
                    final Button a = (Button) view;
                    a.setEnabled(false);
                    JSONObject temp1 = new JSONObject();
                    temp1.put("token", token);
                    temp1.put("id", otherUserId);
                    if ("设为场控".equals(a.getText())) {
                        Api.setManager(PublishActivity.this, temp1, new OnRequestDataListener() {
                            @Override
                            public void requestSuccess(int code, JSONObject data) {
                                a.setEnabled(true);
                                a.setText("取消场控");
                                a.setTextColor(getResources().getColor(R.color.colorPrimary));
                                //dialogChangkongImageOff.setImageResource(R.drawable.icon_changkong_on);
                            }

                            @Override
                            public void requestFailure(int code, String msg) {
                                a.setEnabled(true);
                                toast(msg);
                            }
                        });
                    } else {
                        Api.cancelManager(PublishActivity.this, temp1, new OnRequestDataListener() {
                            @Override
                            public void requestSuccess(int code, JSONObject data) {
                                a.setEnabled(true);
                                a.setText("设为场控");
                                a.setTextColor(Color.BLACK);
                                //dialogChangkongImageOff.setImageResource(R.drawable.icon_changkong);
                            }

                            @Override
                            public void requestFailure(int code, String msg) {
                                a.setEnabled(true);
                                toast(msg);
                            }
                        });
                    }
                    break;
                case R.id.dialog_user_changkong:

                    break;
                case R.id.manager_changkong_list:
                    JSONObject params1 = new JSONObject();
                    params1.put("token", token);
                    Api.getManagerList(PublishActivity.this, params1, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            ArrayList<ManagerModel> managerList = new ArrayList<ManagerModel>();
                            JSONArray list1 = data.getJSONArray("data");
                            for (int i = 0; i < list1.size(); i++) {
                                ManagerModel aa = new ManagerModel();
                                JSONObject tempUser = list1.getJSONObject(i);
                                aa.setUserId(tempUser.getString("id"));
                                aa.setUserName(tempUser.getString("user_nicename"));
                                aa.setAvatar(tempUser.getString("avatar"));
                                managerList.add(aa);
                            }
                            LinearLayout linearLayoutMain = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item_dialog_manager, null);
                            ListView list = (ListView) linearLayoutMain.findViewById(R.id.manager_list);
                            ManagerAdapter managerAdapter = new ManagerAdapter(PublishActivity.this, managerList);
                            list.setAdapter(managerAdapter);
                            AlertDialog dialog1 = new AlertDialog.Builder(PublishActivity.this).setView(linearLayoutMain)//在这里把写好的这个listview的布局加载dialog中
                                    .create();
                            dialog1.setCanceledOnTouchOutside(true);//使除了dialog以外的地方不能被点击
                            dialog1.show();
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            toast(msg);
                        }
                    });

                    break;
                case R.id.tv_cancel:
                    if (mPopupShareWindow != null && mPopupShareWindow.isShowing()) {
                        mPopupShareWindow.dismiss();
                    }
                    if (mPopupShareWindow1 != null && mPopupShareWindow1.isShowing()) {
                        mPopupShareWindow1.dismiss();
                    }
                    break;
                case R.id.image_live_share_qzone:
                    mThirdShare.setTitle(info.getString("content"));
                    mThirdShare.setText(info.getString("content"));
                    mThirdShare.setTitleUrl(info.getString("shareUrl"));
                    mThirdShare.setImageType(Type.IMAGE_NETWORK);
                    mThirdShare.setImageUrl(info.getString("pic"));
                    mThirdShare.share2QZone();
                    break;
                case R.id.image_live_share_qq:
                    //toast("qq");
                    mThirdShare.setTitle(info.getString("content"));
                    mThirdShare.setText(info.getString("content"));
                    mThirdShare.setTitleUrl(info.getString("shareUrl"));
                    mThirdShare.setImageType(Type.IMAGE_NETWORK);
                    mThirdShare.setImageUrl(info.getString("pic"));
                    mThirdShare.share2QQ();
                    break;
                case R.id.image_live_share_sina:
                    //toast("weibo");
                    mThirdShare.setText(info.getString("content"));
                    mThirdShare.setImageUrl(info.getString("pic"));
                    mThirdShare.share2SinaWeibo(false);
                    break;
                case R.id.image_live_share_wechat:
                    //toast("wechat");
                    mThirdShare.setTitle(info.getString("content"));
                    mThirdShare.setText(info.getString("content"));
                    mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                    mThirdShare.setImageType(Type.IMAGE_NETWORK);
                    mThirdShare.setImageUrl(info.getString("pic"));
                    mThirdShare.setUrl(info.getString("shareUrl"));
                    mThirdShare.share2Wechat();
                    break;
                case R.id.image_live_share_wechat_moment:
                    //toast("wechat moment");
                    mThirdShare.setTitle(info.getString("content"));
                    mThirdShare.setText(info.getString("content"));
                    mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                    mThirdShare.setImageType(Type.IMAGE_NETWORK);
                    mThirdShare.setImageUrl(info.getString("pic"));
                    mThirdShare.setUrl(info.getString("shareUrl"));
                    mThirdShare.share2WechatMoments();
                    break;
                case R.id.image_live_public:
                    pass = "";
                    payMoney = "";
                    privacy = "0";
                    mAddLivingType.setText(getText(R.string.live_type_pub));
                    mPopupShareWindow.dismiss();
                    break;
                case R.id.image_live_pass:
                    DialogEnsureUtiles.showInfo(PublishActivity.this, new OnCustomClickListener() {
                        @Override
                        public void onClick(String value) {
                            pass = value;
                            payMoney = "";
                            privacy = "0";
                            mAddLivingType.setText(getText(R.string.live_type_pass));
                            mPopupShareWindow.dismiss();
                        }
                    }, pass, "请输入密码");
                    break;
                case R.id.image_live_pay:
                    DialogEnsureUtiles.showInfo(PublishActivity.this, new OnCustomClickListener() {
                        @Override
                        public void onClick(String value) {
                            if (Utile.isNumeric(value)) {
                                payMoney = value;
                                pass = "";
                                privacy = "0";
                                mAddLivingType.setText(getText(R.string.live_type_pay));
                                mPopupShareWindow.dismiss();
                            } else {
                                toast("请输入整数");
                            }

                        }
                    }, payMoney, "请输入整数(钻石)");
                    break;
                case R.id.image_live_private:
                    privacy = "1";
                    pass = "";
                    payMoney = "";
                    mAddLivingType.setText(getText(R.string.live_type_private));
                    mPopupShareWindow.dismiss();
                    break;
                case R.id.image_live_lianmai:
                    if (mIsRegisted) {
                        mStreamer.getRtcClient().startCall(otherUserId);
                        mLianmaiStop.setText("连麦中...");
                        mLianmaiStop.setVisibility(View.VISIBLE);
                    } else {
                        toast("连麦注册失败");
                    }
                    break;
                case R.id.image_live_jinyan:
                    JSONObject temp = new JSONObject();
                    temp.put("token", token);
                    temp.put("room_id", liveInfo.getString("room_id"));
                    temp.put("id", otherUserId);
                    Api.addJinyan(PublishActivity.this, temp, new OnRequestDataListener() {
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
                case R.id.image_live_changkong:
                    JSONObject temp3 = new JSONObject();
                    temp3.put("token", token);
                    temp3.put("id", otherUserId);
                    Api.setManager(PublishActivity.this, temp3, new OnRequestDataListener() {
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
                case R.id.caozuo_cancel:
                    mPopupCaozuoWindow.dismiss();
                    break;
            }

        }
    }

    private RTCClient.RTCEventChangedListener mRTCEventListener = new RTCClient.RTCEventChangedListener() {
        @Override
        public void onEventChanged(int event, Object arg1) {
            switch (event) {
                case RTCClient.RTC_EVENT_REGISTED:
                    doRegisteredSuccess();
                    break;
                case RTCClient.RTC_EVENT_STARTED:
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

    private void onRTCRegisterClick() {
        if (mRTCAuthResponse == null) {
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
        if (!mIsRegisted) {
            mRTCAuthTask = new AuthHttpTask(mRTCAuthResponse);
            mRTCAuthTask.execute(RTC_AUTH_SERVER + "?uid=" + channel_creater);
        } else {
            doUnRegister();
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
        mLianmaiStop.setText("停止连麦");
    }

    private void doStopCallResult() {
        mIsConnected = false;
        //can start call again
        //to stop RTC video/audio
        mStreamer.stopRTC();
        mStreamer.setRTCMainScreen(RTCConstants.RTC_MAIN_SCREEN_CAMERA);
        mLianmaiStop.setVisibility(View.GONE);
    }

    private void doStartCallFailed(int status) {
        mIsConnected = false;
        //if remote receive visible need hide

//        Toast.makeText(this, "call failed: " + status, Toast
//                .LENGTH_SHORT).show();
        toast("连麦失败");
        mLianmaiStop.setVisibility(View.GONE);

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
        mStreamer.getRtcClient().setRTCAuthInfo(RTC_AUTH_URI, authString, channel_creater);
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


    @Override
    public void shareError(Platform platform) {
        toast("分享失败");
    }

    @Override
    public void shareCancel(Platform platform) {
        toast("分享取消");
    }

    @Override
    public void shareSuccess(Platform platform) {
        toast("分享成功");
        startLive(mInputLiveTitle.getText().toString());
    }

}
