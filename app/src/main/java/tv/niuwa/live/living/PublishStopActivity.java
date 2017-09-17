package tv.niuwa.live.living;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import com.smart.androidutils.activity.BaseActivity;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;
import com.smart.loginsharesdk.share.OnShareStatusListener;
import com.smart.loginsharesdk.share.ThirdShare;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

import tv.niuwa.live.MainActivity;
import tv.niuwa.live.R;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.utils.Api;

public class PublishStopActivity extends BaseActivity implements OnShareStatusListener {
    private ThirdShare mThirdShare;
//    @Bind(R.id.text_publish_stop_id)
//    TextView mTextPublishStopId;

    @Bind(R.id.text_publish_stop_see_num)
    TextView mTextPublishStopSeeNum;

    @Bind(R.id.text_publish_stop_zan_num)
    TextView mTextPublishStopZanNum;

    @Bind(R.id.text_publish_stop_sidou_num)
    TextView mTextPublishStopSidouNum;

    @Bind(R.id.publish_stop_name)
    TextView mPublishStopName;

    @Bind(R.id.publish_shop_avatar)
    ImageView mPublishShopAvatar;

    @Bind(R.id.publish_stop_time)
    TextView mPublishStopTime;
    private String token;
    private String otherUserId;

    @OnClick(R.id.btn_publish_stop_back_home)
    public void mBackHome(View view) {
        Intent intent = new Intent(PublishStopActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


//    @OnClick({R.id.image_add_live_share_wechat_moment, R.id.image_add_live_share_wechat,
//            R.id.image_add_live_share_weibo, R.id.image_add_live_share_qq, R.id.image_add_live_share_qzone})
//    public void onClickShare(final View v) {
//        if (mThirdShare != null) {
//            String token = (String)SharePrefsUtils.get(this,"user","token","");
//            JSONObject params = new JSONObject();
//            params.put("token",token);
//            params.put("room_id",token);
//            Api.getShareInfo(this, params, new OnRequestDataListener() {
//                @Override
//                public void requestSuccess(int code, JSONObject data) {
//                    JSONObject info = data.getJSONObject("data");
//                    switch (v.getId()) {
//                        case R.id.image_add_live_share_qzone:
//                            //toast("qzone");
//                            mThirdShare.setTitle(info.getString("content"));
//                            mThirdShare.setText(info.getString("content"));
//                            mThirdShare.setTitleUrl(info.getString("shareUrl"));
//                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
//                            mThirdShare.setImageUrl(info.getString("pic"));
//                            mThirdShare.share2QZone();
//                            break;
//                        case R.id.image_add_live_share_qq:
//                            //toast("qq");
//                            mThirdShare.setTitle(info.getString("content"));
//                            mThirdShare.setText(info.getString("content"));
//                            mThirdShare.setTitleUrl(info.getString("shareUrl"));
//                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
//                            mThirdShare.setImageUrl(info.getString("pic"));
//                            mThirdShare.share2QQ();
//                            break;
//                        case R.id.image_add_live_share_weibo:
//                            //toast("weibo");
//                            mThirdShare.setText(info.getString("content"));
//                            mThirdShare.setImageUrl(info.getString("pic"));
//                            mThirdShare.share2SinaWeibo(false);
//                            break;
//                        case R.id.image_add_live_share_wechat:
//                            //toast("wechat");
//                            mThirdShare.setTitle(info.getString("content"));
//                            mThirdShare.setText(info.getString("content"));
//                            mThirdShare.setShareType(Type.SHARE_WEBPAGE);
//                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
//                            mThirdShare.setImageUrl(info.getString("pic"));
//                            mThirdShare.setUrl(info.getString("shareUrl"));
//                            mThirdShare.share2Wechat();
//                            break;
//                        case R.id.image_add_live_share_wechat_moment:
//                            //toast("wechat moment");
//                            mThirdShare.setTitle(info.getString("content"));
//                            mThirdShare.setText(info.getString("content"));
//                            mThirdShare.setShareType(Type.SHARE_WEBPAGE);
//                            mThirdShare.setImageType(Type.IMAGE_NETWORK);
//                            mThirdShare.setImageUrl(info.getString("pic"));
//                            mThirdShare.setUrl(info.getString("shareUrl"));
//                            mThirdShare.share2WechatMoments();
//                            break;
//                    }
//                }
//
//                @Override
//                public void requestFailure(int code, String msg) {
//                    toast(msg);
//                }
//            });
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThirdShare = new ThirdShare(this);
        mThirdShare.setOnShareStatusListener(this);
        token = (String) SharePrefsUtils.get(this, "user", "token", "");
        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PublishStopActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void getData() {
        String token = (String) SharePrefsUtils.get(this, "user", "token", "");
        if (StringUtils.isNotEmpty(token)) {
            JSONObject params = new JSONObject();
            params.put("token", token);
            Api.stopPublish(this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if (isActive){
                        JSONObject temp = data.getJSONObject("data");
//                    mTextPublishStopId.setText(temp.getString("id"));
                        otherUserId = temp.getString("id");
                        mTextPublishStopSeeNum.setText(temp.getString("online_num"));
                        mTextPublishStopZanNum.setText(temp.getString("channel_like"));
                        mTextPublishStopSidouNum.setText(temp.getString("earn"));
                        mPublishStopName.setText(temp.getString("user_nicename"));
                        mPublishStopTime.setText(temp.getString("all_time"));
                        Glide.with(PublishStopActivity.this).load(temp.getString("avatar")).error(R.drawable.icon_avatar_default).transform(new GlideCircleTransform(PublishStopActivity.this)).into(mPublishShopAvatar);
                    }

                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        } else {
            openActivity(LoginActivity.class);
            finish();
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
    public int getLayoutResource() {
        return R.layout.activity_publish_stop;
    }
}
