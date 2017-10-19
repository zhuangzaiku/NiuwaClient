package tv.niuwa.live.own;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.bumptech.glide.Glide;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.DensityUtils;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;
import com.smart.loginsharesdk.share.OnShareStatusListener;
import com.smart.loginsharesdk.share.ThirdShare;
import com.smart.loginsharesdk.share.onekeyshare.Type;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.event.LCIMIMTypeMessageEvent;
import cn.sharesdk.framework.Platform;
import de.greenrobot.event.EventBus;
import tv.niuwa.live.MyApplication;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiFragment;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.lean.ConversationListActivity;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.own.authorize.VerificationActivity;
import tv.niuwa.live.own.family.FamilyActivity;
import tv.niuwa.live.own.fans.FansActivity;
import tv.niuwa.live.own.follow.FollowActivity;
import tv.niuwa.live.own.goexchange.GoExchangeActivity;
import tv.niuwa.live.own.level.MyLevelActivity;
import tv.niuwa.live.own.money.ChargeMoneyActivity;
import tv.niuwa.live.own.publishrecord.PublishRecordActivity;
import tv.niuwa.live.own.setting.SettingActivity;
import tv.niuwa.live.own.userinfo.ContributionActivity;
import tv.niuwa.live.own.userinfo.MyDataActivity;
import tv.niuwa.live.utils.Api;

/**
 * Created by fengjh on 16/7/19.
 */
public class OwnFragment extends BaseSiSiFragment implements OnShareStatusListener ,View.OnClickListener {

    private static final String TAG = OwnFragment.class.getName();

    @Bind(R.id.frame_own_avatar_container)
    FrameLayout mFrameOwnAvatarContainer;
    @Bind(R.id.text_user_id)
    TextView mTextUserId;
    @Bind(R.id.text_user_nick)
    TextView mTextUserNick;
    @Bind(R.id.text_user_signature)
    TextView mTextUserSignature;
    @Bind(R.id.image_own_user_avatar)
    ImageView mImageOwnUserAvatar;
    @Bind(R.id.text_user_level)
    TextView mTextUserLevel;
//    @Bind(R.id.text_user_balance)
//    TextView mTextUserBalance;
//    @Bind(R.id.text_user_spendcoin)
//    TextView mTextUserSpendcoin;
    @Bind(R.id.image_sex)
    ImageView mImageSex;
//    @Bind(R.id.image_first)
//    ImageView mImageFirst;
//    @Bind(R.id.image_second)
//    ImageView mImageSecond;
//    @Bind(R.id.image_third)
//    ImageView mImageThird;
//    @Bind(R.id.image_fourth)
//    ImageView mImageFourth;
    @Bind(R.id.text_user_fans)
    TextView mTextUserFans;
    @Bind(R.id.text_user_follows)
    TextView mTextUserFollows;
//    @Bind(R.id.text_user_sidou)
//    TextView mTextUserSidou;
//    @Bind(R.id.text_user_level_LV)
//    TextView mTextUserLevelLv;
    @Bind(R.id.image_real)
    ImageView mImageReal;
    @Bind(R.id.image_own_unread)
    ImageView mImageOwnUnread;
    private int mAvatarContainerWidth;
    private int mAvatarContainerHeight;
    private String balance;

    private String[] SeatUserIDs = {"0", "0", "0", "0"};

    @OnClick(R.id.image_own_user_avatar)
    public void clickAvatarName(View view) {
        Intent i = new Intent(this.getContext(), MyDataActivity.class);
        startActivityForResult(i, 1);
    }

    @OnClick(R.id.image_own_edit)
    public void clickAvatarName1(View view) {
        Intent i = new Intent(this.getContext(), MyDataActivity.class);
        startActivityForResult(i, 1);
    }
    @OnClick(R.id.text_contribution)
    public void contribution(View view) {
        openActivity(ContributionActivity.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                initData();
        }
    }
    @OnClick(R.id.linear_own_setting_family)
    public void linearOwnSetingFamily(){
        openActivity(FamilyActivity.class);
    }

    @OnClick(R.id.linear_own_setting_publish)
    public void publishRecord(View v){
        openActivity(PublishRecordActivity.class);
    }

    @OnClick(R.id.image_own_message)
    public void clickMessage(View view) {
        openActivity(ConversationListActivity.class);
    }

    private PopupWindow mPopupShareWindow;
    private ThirdShare mThirdShare;

    //@OnClick(R.id.linear_own_setting_friend)
    public void linearOwnSettingFriend(View v){
//        int locX = 0;
//        int locY = 0;
//        if (mPopupShareWindow == null || !mPopupShareWindow.isShowing()) {
//            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_shape_dialog, null);
//            mPopupShareWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            mPopupShareWindow.setBackgroundDrawable(new ColorDrawable(0x50000000));
//            mPopupShareWindow.setFocusable(true);
//            mPopupShareWindow.showAtLocation(v, Gravity.BOTTOM, locX, locY);
//            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//            lp.alpha = 0.5f;
//            getActivity().getWindow().setAttributes(lp);
//            //ButterKnife.bind(this, inflate);
////            ImageView shareQZone = ButterKnife.findById(inflate, R.id.image_live_share_qzone);
////            ImageView shareQQ = ButterKnife.findById(inflate, R.id.image_live_share_qq);
////            ImageView shareSina = ButterKnife.findById(inflate, R.id.image_live_share_sina);
////            ImageView shareWechat = ButterKnife.findById(inflate, R.id.image_live_share_wechat);
////            ImageView shareWechatMoment = ButterKnife.findById(inflate, R.id.image_live_share_wechat_moment);
//            ImageView shareQZone = (ImageView) inflate.findViewById(R.id.image_live_share_qzone);
//            ImageView shareQQ = (ImageView) inflate.findViewById(R.id.image_live_share_qq);
//            ImageView shareSina = (ImageView) inflate.findViewById(R.id.image_live_share_sina);
//            ImageView shareWechat = (ImageView) inflate.findViewById(R.id.image_live_share_wechat);
//            ImageView shareWechatMoment = (ImageView) inflate.findViewById(R.id.image_live_share_wechat_moment);
//            shareQZone.setOnClickListener(this);
//            shareQQ.setOnClickListener(this);
//            shareSina.setOnClickListener(this);
//            shareWechat.setOnClickListener(this);
//            shareWechatMoment.setOnClickListener(this);
//        } else {
//            mPopupShareWindow.showAtLocation(v, Gravity.BOTTOM, locX, locY);
//        }
//        mPopupShareWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                lp.alpha = 1f;
//                getActivity().getWindow().setAttributes(lp);
//            }
//        });
    }



    @Override
    public void onClick(final View v) {
        JSONObject params = new JSONObject();
        String token = (String)SharePrefsUtils.get(getContext(),"user","token","");
        params.put("token", token);
        params.put("room_id", token);
        params.put("type","2");
        Api.getShareInfo(getContext(), params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONObject info = data.getJSONObject("data");
                switch (v.getId()) {
                    case R.id.image_live_share_qzone:
                        //toast("qzone");
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

    @OnClick(R.id.linear_own_money_container)
    public void chargeMoney(View view) {
        Bundle data = new Bundle();
        data.putString("balance", balance);
        openActivity(ChargeMoneyActivity.class, data);
    }

    @OnClick(R.id.linear_own_info_container)
    public void message(View view) {
        Intent i = new Intent(this.getContext(), MyDataActivity.class);
        startActivityForResult(i, 1);
    }

    @OnClick(R.id.linear_own_setting_container)
    public void setting(View view) {
        openActivity(SettingActivity.class);
    }

    @OnClick(R.id.linear_own_follow_container)
    public void follow(View view) {
        openActivity(FollowActivity.class);
    }

    @OnClick(R.id.linear_own_fans_container)
    public void fans(View view) {
        openActivity(FansActivity.class);
    }

    @OnClick(R.id.text_own_goExchange)
    public void GoExchange(View view) {
        openActivity(GoExchangeActivity.class);
    }

    @OnClick(R.id.linear_own_level_container)
    public void myLevel(View view) {
        openActivity(MyLevelActivity.class);
    }

    @OnClick(R.id.linear_own_authorize_container)
    public void authorize(View view) {
        openActivity(VerificationActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThirdShare = new ThirdShare(getContext());
        mThirdShare.setOnShareStatusListener(this);
        mAvatarContainerWidth = DensityUtils.screenWidth(getActivity());
        mAvatarContainerHeight = (mAvatarContainerWidth * 440) / 750;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mAvatarContainerWidth, mAvatarContainerHeight);
//        mFrameOwnAvatarContainer.setLayoutParams(params);
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        initData();
    }
    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        String token = (String) SharePrefsUtils.get(this.getContext(), "user", "token", "");
        String userId = (String) SharePrefsUtils.get(this.getContext(), "user", "userId", "");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            Api.getUserInfo(this.getContext(), requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if(isActive){
                        JSONObject userInfo = data.getJSONObject("data");
                        SharePrefsUtils.put(OwnFragment.this.getContext(), "user", "user_nicename", userInfo.getString("user_nicename"));
                        SharePrefsUtils.put(OwnFragment.this.getContext(), "user", "user_level", userInfo.getString("user_level"));
                        SharePrefsUtils.put(OwnFragment.this.getContext(), "user", "avatar", userInfo.getString("avatar"));
                        MyApplication app = (MyApplication) getActivity().getApplication();
                        app.setBalance(userInfo.getString("balance"));
                        mTextUserNick.setText(userInfo.getString("user_nicename"));
                        mTextUserId.setText("ID:" + userInfo.getString("id"));
                        if (StringUtils.isNotEmpty(userInfo.getString("signature"))) {
                            mTextUserSignature.setText(userInfo.getString("signature"));
                        }
                        if ("1".equals(userInfo.getString("sex"))) {
                            mImageSex.setImageResource(R.drawable.userinfo_male);
                        }else {
                            mImageSex.setImageResource(R.drawable.userinfo_female);
                        }
                        if("1".equals(userInfo.getString("is_truename"))){
                            mImageReal.setVisibility(View.VISIBLE);
                        }
                        mTextUserLevel.setText(userInfo.getString("user_level"));
//                    mTextUserLevelLv.setText(userInfo.getString("user_level"));
                        int level = Integer.parseInt(userInfo.getString("user_level"));
                        if(level<5){
                            mTextUserLevel.setBackgroundResource(R.drawable.level1);
                        }
                        if(level>4 && level<9 ){
                            mTextUserLevel.setBackgroundResource(R.drawable.level2);
                        }
                        if(level>8 && level<13 ){
                            mTextUserLevel.setBackgroundResource(R.drawable.level3);
                        }
                        if(level>12 ){
                            mTextUserLevel.setBackgroundResource(R.drawable.level3);
                        }

//                    mTextUserSpendcoin.setText(userInfo.getString("total_spend"));
//                    mTextUserSidou.setText(userInfo.getString("sidou"));
                        mTextUserFans.setText(userInfo.getString("fans_num"));
                        mTextUserFollows.setText(userInfo.getString("attention_num"));
                        balance = userInfo.getString("balance");
//                    mTextUserBalance.setText(balance);
                        Glide.with(getActivity()).load(userInfo.getString("avatar"))
                                .error(R.drawable.icon_avatar_default)
                                .transform(new GlideCircleTransform(getActivity()))
                                .into(mImageOwnUserAvatar);
                        //CustomUserProvider provider = (CustomUserProvider) LCChatKit.getInstance().getProfileProvider();
                        //provider.getAllUsers().add(new LCChatKitUser(userInfo.getString("id"), userInfo.getString("user_nicename"), userInfo.getString("avatar")));
                    }

                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                    //openActivity(LoginActivity.class);
//                    Intent intent = new Intent(OwnFragment.this.getContext(),LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    OwnFragment.this.getActivity().finish();
                }
            });
            requestParams.put("limit_num", 4);
            for (int i = 0; i < 4; i++) { //初始ID为0
                SeatUserIDs[i] = "0";
            }
//            Api.getUserContributionList(this.getContext(), requestParams, new OnRequestDataListener() {
//                @Override
//                public void requestSuccess(int code, JSONObject data) {
//                    ArrayList<ImageView> temp = new ArrayList();
//                    temp.add(mImageFirst);
//                    temp.add(mImageSecond);
//                    temp.add(mImageThird);
//                    temp.add(mImageFourth);
//                    JSONArray list = data.getJSONArray("data");
//                    for (int i = 0; i < list.size(); i++) {
//                        ImageView tempImage = (ImageView) temp.get(i);
//                        JSONObject item = list.getJSONObject(i);
//                        SeatUserIDs[i] = item.getString("id");
//                        Glide.with(OwnFragment.this.getContext()).load(item.getString("avatar"))
//                                .error(R.drawable.icon_avatar_default)
//                                .transform(new GlideCircleTransform(OwnFragment.this.getContext()))
//                                .into(tempImage);
//                    }
//                }
//
//                @Override
//                public void requestFailure(int code, String msg) {
//
//                }
//            });

        } else {
            openActivity(LoginActivity.class);
            getActivity().finish();
        }
        getUnread();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getUnread();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
//
//    @OnClick(R.id.image_first) //查看第一个座位
//    public void showFirstSeat() {
//        if (SeatUserIDs[0] != "0") {
//            Bundle data = new Bundle();
//            data.putString("id", SeatUserIDs[0]);
//            openActivity(UserMainActivity.class, data);
//        }
//    }
//
//    @OnClick(R.id.image_second) //查看第二个座位
//    public void showSecondSeat() {
//        if (SeatUserIDs[1] != "0") {
//            Bundle data = new Bundle();
//            data.putString("id", SeatUserIDs[1]);
//            openActivity(UserMainActivity.class, data);
//        }
//    }
//
//    @OnClick(R.id.image_third) //查看第三个座位
//    public void showThirdSeat() {
//        if (SeatUserIDs[2] != "0") {
//            Bundle data = new Bundle();
//            data.putString("id", SeatUserIDs[2]);
//            openActivity(UserMainActivity.class, data);
//        }
//    }
    public void onEvent(LCIMIMTypeMessageEvent event) {
        getUnread();
    }

//    @OnClick(R.id.image_fourth) //查看第四个座位
//    public void showFourthSeat() {
//        if (SeatUserIDs[3] != "0") {
//            Bundle data = new Bundle();
//            data.putString("id", SeatUserIDs[3]);
//            openActivity(UserMainActivity.class, data);
//        }
//    }

    private void getUnread(){
        int num = 0;
        List<String> convIdList = LCIMConversationItemCache.getInstance().getSortedConversationList();
        for (String id :convIdList){
            AVIMConversation conversation = LCChatKit.getInstance().getClient().getConversation(id);
            if(conversation.getMembers().size() != 2)
                continue;
            num += LCIMConversationItemCache.getInstance().getUnreadCount(conversation.getConversationId());
        }
        mImageOwnUnread.setVisibility(View.GONE);
        if(num > 0){
            mImageOwnUnread.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_own;
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
}
