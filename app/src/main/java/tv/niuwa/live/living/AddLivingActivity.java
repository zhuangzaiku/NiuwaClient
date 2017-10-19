package tv.niuwa.live.living;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.smart.androidutils.utils.LogUtils;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;
import com.smart.loginsharesdk.share.OnShareStatusListener;
import com.smart.loginsharesdk.share.ThirdShare;
import com.smart.loginsharesdk.share.onekeyshare.Type;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import tv.niuwa.live.LocationService;
import tv.niuwa.live.MyApplication;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiEditActivity;
import tv.niuwa.live.intf.OnCustomClickListener;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.own.authorize.VerificationActivity;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.utils.DialogEnsureUtiles;
import tv.niuwa.live.utils.Utile;

public class AddLivingActivity extends BaseSiSiEditActivity implements OnShareStatusListener, View.OnClickListener {

    private static final String TAG = "AddLivingActivity";

    @Bind(R.id.input_live_title)
    EditText mInputLiveTitle;
    @Bind(R.id.add_living_location)
    TextView addLivingLocation;
    private ThirdShare mThirdShare;
    private LocationService locationService;
    private double mLatitude;
    private double mLongitude;
    private String mLocation;
    private Boolean ifLocation = true;
    private PopupWindow mPopupShareWindow;
    private String payMoney;
    private String pass;

    //    @OnClick(R.id.add_living_close)
//    public void close(){
//        DialogEnsureUtiles.showDialog(this);
//    }
    @OnClick(R.id.add_living_open_location)
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

    @OnClick({R.id.image_add_live_share_wechat_moment, R.id.image_add_live_share_wechat,
            R.id.image_add_live_share_weibo, R.id.image_add_live_share_qq, R.id.image_add_live_share_qzone})
    public void onClickShare(final View v) {
        String liveTitle = mInputLiveTitle.getText().toString().trim();
        if (StringUtils.isEmpty(liveTitle)) {
            toast("快来给直播起个名字吧~~");
            return;
        }
        if (mThirdShare != null) {
            String token = (String) SharePrefsUtils.get(this, "user", "token", "");
            JSONObject params = new JSONObject();
            params.put("token", token);
            params.put("room_id", token);
            Api.getShareInfo(this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    JSONObject info = data.getJSONObject("data");
                    switch (v.getId()) {
                        case R.id.image_add_live_share_qzone:
                            //toast("qzone");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setTitleUrl(info.getString("shareUrl"));
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.share2QZone();
                            break;
                        case R.id.image_add_live_share_qq:
                            //toast("qq");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setTitleUrl(info.getString("shareUrl"));
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.share2QQ();
                            break;
                        case R.id.image_add_live_share_weibo:
                            //toast("weibo");
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.share2SinaWeibo(false);
                            break;
                        case R.id.image_add_live_share_wechat:
                            //toast("wechat");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.setUrl(info.getString("shareUrl"));
                            mThirdShare.share2Wechat();
                            break;
                        case R.id.image_add_live_share_wechat_moment:
                            //toast("wechat moment");
                            mThirdShare.setTitle(info.getString("content"));
                            mThirdShare.setText(info.getString("content"));
                            mThirdShare.setShareType(Type.SHARE_WEBPAGE);
                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
                            mThirdShare.setImageUrl(info.getString("pic"));
                            mThirdShare.setUrl(info.getString("shareUrl"));
                            mThirdShare.share2WechatMoments();
                            break;
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        }
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
            TextView textView = (TextView) inflate.findViewById(R.id.tv_cancel);
            textView.setOnClickListener(this);
            imagePublic.setOnClickListener(this);
            imagePass.setOnClickListener(this);
            imagePay.setOnClickListener(this);
        } else {
            mPopupShareWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, locX, locY);
        }
    }

    @OnClick(R.id.btn_start_living)
    public void startLiving(View view) {
        String title = mInputLiveTitle.getText().toString();
        if (StringUtils.isEmpty(title)) {
            toast("先输入一个标题吧");
            return;
        }

        String token = (String) SharePrefsUtils.get(this, "user", "token", "");
        if (!StringUtils.isEmpty(token)) {
//            Location location = null;
//            Location location = LocationUtile.getLocation(this);
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
            params.remove("longitude");
            params.remove("latitude");
            params.put("title", title);
            Api.startLive(this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    JSONObject temp = data.getJSONObject("data");
                    JSONArray msgs = data.getJSONArray("msg");
                    if (temp != null) {
                        Bundle liveInfo = new Bundle();
                        liveInfo.putString("liveInfo", temp.toJSONString());
                        liveInfo.putString("msgs", msgs.toJSONString());
                        openActivity(PublishActivity.class, liveInfo);
                        finish();
                    } else {
                        toast("数据异常");
                    }

                }

                @Override
                public void requestFailure(int code, String msg) {
                    if (508 == code) {
                        openActivity(VerificationActivity.class);
                    } else {
                        toast(msg);
                    }
                }
            });

        } else {
            openActivity(LoginActivity.class);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThirdShare = new ThirdShare(this);
        mThirdShare.setOnShareStatusListener(this);
        openLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (null != locationService) {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }

        super.onStop();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_add_living;
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

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                mLocation = location.getCountry() + "-" + location.getProvince() + "-" + location.getCity();
                addLivingLocation.setText(location.getCity());
                LogUtils.i(TAG, "onReceiveLocation==latitude=" + mLatitude);
                LogUtils.i(TAG, "onReceiveLocation==longitude=" + mLongitude);
                LogUtils.i(TAG, "onReceiveLocation==location=" + mLocation);
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
//                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
//                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
//                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
            }
        }

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                DialogEnsureUtiles.showConfirm(this, "确定退出直播间吗", new OnCustomClickListener() {
                    @Override
                    public void onClick(String value) {
                        finish();
                    }
                });
            }
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (mPopupShareWindow != null && mPopupShareWindow.isShowing()) {
                    mPopupShareWindow.dismiss();
                }
                break;
            case R.id.image_live_public:
                pass = "";
                payMoney = "";
                break;
            case R.id.image_live_pass:
                DialogEnsureUtiles.showInfo(AddLivingActivity.this, new OnCustomClickListener() {
                    @Override
                    public void onClick(String value) {
                        pass = value;
                    }
                }, pass, "请输入密码");
                break;
            case R.id.image_live_pay:
                DialogEnsureUtiles.showInfo(AddLivingActivity.this, new OnCustomClickListener() {
                    @Override
                    public void onClick(String value) {
                        if (Utile.isNumeric(value)) {
                            payMoney = value;
                        } else {
                            toast("请输入整数");
                        }

                    }
                }, payMoney, "请输入整数(钻石)");
                break;
        }
    }
}
