package tv.niuwa.live.own;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.avos.avoscloud.im.v2.AVIMClient;
//import com.avos.avoscloud.im.v2.AVIMException;
//import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.bumptech.glide.Glide;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.DensityUtils;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.utils.LCIMConstants;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;
import tv.niuwa.live.home.model.VideoItem;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.lean.Chat;
import tv.niuwa.live.living.LivingActivity;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.own.follow.FollowItem;
import tv.niuwa.live.own.follow.FollowListAdapterother;
import tv.niuwa.live.own.userinfo.ContributionActivity;
import tv.niuwa.live.utils.Api;

public class UserMainActivity extends BaseSiSiActivity implements OnRecyclerViewItemClickListener {

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
    @Bind(R.id.text_user_spendcoin)
    TextView mTextUserSpendcoin;
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
    @Bind(R.id.btn_live_status)
    Button mBtnLiveStatus;
    @Bind(R.id.user_main_recycleview)
    RecyclerView mRecyclerView;
    @Bind(R.id.noDataLayout_user_main)
    RelativeLayout mNodataView;
    @Bind(R.id.linear_own_fans_container)
    LinearLayout mLinerOwnFansContainer;
    @Bind(R.id.linear_own_follow_container)
    LinearLayout mLinerOwnFollowContainer;
    @Bind(R.id.user_main_attention)
    LinearLayout mUserMainAttention;
//    @Bind(R.id.user_main_message)
//    LinearLayout mUserMainMessage;


    @Bind(R.id.text_user_fans)
    TextView mTextUserFans;
    @Bind(R.id.text_user_follows)
    TextView mTextUserFollows;

    private int mAvatarContainerWidth;
    private int mAvatarContainerHeight;
    private int current_type = 1;
    private FollowListAdapterother mFollowListAdapter;
    private ArrayList<FollowItem> mFollowItems;
    private String userId;
    private String roomId;
    private String token;
    private String current_user;
    private String[] SeatUserIDs = {"0", "0", "0", "0"};

    @OnClick(R.id.user_main_attention)
    public void userMainAttention(View v){
        mUserMainAttention.setEnabled(false);
        final TextView view = (TextView) mUserMainAttention.findViewById(R.id.user_main_text_attention);
        JSONObject requestParams = new JSONObject();
        requestParams.put("token", token);
        requestParams.put("userid", userId);
        if("关注".equals(view.getText())){
            Api.addAttention(UserMainActivity.this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {

                    view.setText("已关注");
                    mUserMainAttention.setEnabled(true);
                }

                @Override
                public void requestFailure(int code, String msg) {
                    mUserMainAttention.setEnabled(true);
                    toast(msg);
                }
            });
        }else{
            Api.cancelAttention(UserMainActivity.this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    view.setText("关注");
                    mUserMainAttention.setEnabled(true);
                }

                @Override
                public void requestFailure(int code, String msg) {
                    mUserMainAttention.setEnabled(true);
                    toast(msg);
                }
            });
        }
    }
    @OnClick(R.id.iv_user_main_msg)
    public void userMainMessage(View v){
        LCChatKit.getInstance().open(current_user, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    Intent intent = new Intent(UserMainActivity.this, Chat.class);
                    intent.putExtra(LCIMConstants.PEER_ID, userId);
                    startActivity(intent);
                } else {
                    toast(e.toString());
                }
            }
        });
    }


    @OnClick(R.id.image_back)
    public void back(View view) {
        UserMainActivity.this.finish();
    }

    @OnClick(R.id.text_contribution)
    public void contribution(View view) {
        Bundle data = new Bundle();
        data.putString("id", userId);
        openActivity(ContributionActivity.class, data);
    }

    @OnClick(R.id.linear_own_follow_container)
    public void getAttentionList(View view) {
        mFollowItems.clear();
        current_type = 1;
        mFollowListAdapter.notifyDataSetChanged();
        getAttentionList(token, userId, 0, 20);
    }

    @OnClick(R.id.linear_own_fans_container)
    public void getFansList(View view) {
        mFollowItems.clear();
        current_type = 2;
        mFollowListAdapter.notifyDataSetChanged();
        getFansList(token, userId, 0, 20);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            userId = data.getString("id");
            if (data.getInt("type") == 2) { //是否直接跳转到粉丝列表
                current_type = 2;
            }
        }
        if (mFollowItems == null) {
            mFollowItems = new ArrayList<>();
        }
        mFollowItems.clear();
        mAvatarContainerWidth = DensityUtils.screenWidth(this);
        mAvatarContainerHeight = (mAvatarContainerWidth * 440) / 750;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mAvatarContainerWidth, mAvatarContainerHeight);
//        mFrameOwnAvatarContainer.setLayoutParams(params);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
      //  mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mFollowListAdapter = new FollowListAdapterother(this, mFollowItems);
        mFollowListAdapter.setOnRecyclerViewItemClickListener(this);
        mRecyclerView.setAdapter(mFollowListAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("MainViewPagerFragment", "--------onScrollStateChanged");
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItemPosition == (totalItemCount - 1) && isSlidingToLast) {
                        //toast("没有更多数据了~~");
                        if (current_type == 2) {
                            getFansList(token, userId, totalItemCount, 20);
                        } else {
                            getAttentionList(token, userId, totalItemCount, 20);
                        }

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("MainViewPagerFragment", "--------onScrolled=dx=" + dx + "---dy=" + dy);
                if (dy > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }
            }
        });
        initData();

    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_user_main;
    }

    private void initData() {
        token = (String) SharePrefsUtils.get(this, "user", "token", "");
        current_user = (String)SharePrefsUtils.get(this, "user", "userId", "");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            Api.getUserInfo(this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    final JSONObject userInfo = data.getJSONObject("data");
                    mTextUserNick.setText(userInfo.getString("user_nicename"));
                    mTextUserId.setText("ID  " + userInfo.getString("id"));
                    roomId = userInfo.getString("room_id");
                    if (StringUtils.isNotEmpty(userInfo.getString("signature"))) {
                        mTextUserSignature.setText(userInfo.getString("signature"));
                    }
                    if ("1".equals(userInfo.getString("sex"))) {
                        mImageSex.setImageResource(R.drawable.userinfo_male);
                    }
                    if ("2".equals(userInfo.getString("channel_status"))) {
                        mBtnLiveStatus.setText("直播中");
                        mBtnLiveStatus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle data = new Bundle();
                                VideoItem temp = new VideoItem();
                                temp.setRoom_id(roomId);
                                data.putSerializable("videoItem", temp);
                                openActivity(LivingActivity.class, data);
                            }
                        });
                    }

                    mTextUserFans.setText(userInfo.getString("fans_num"));
                    mTextUserFollows.setText(userInfo.getString("attention_num"));
                    mTextUserLevel.setText(userInfo.getString("user_level"));
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
                    mTextUserSpendcoin.setText(userInfo.getString("total_spend"));
                    Glide.with(UserMainActivity.this).load(userInfo.getString("avatar"))
                            .error(R.drawable.icon_avatar_default)
                            .transform(new GlideCircleTransform(UserMainActivity.this))
                            .into(mImageOwnUserAvatar);
                    if("1".equals(userInfo.getString("attention_status"))){
                        TextView v = (TextView) mUserMainAttention.findViewById(R.id.user_main_text_attention);
                        v.setText("已关注");
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
            requestParams.put("limit_num", 4);
            for (int i = 0; i < 4; i++) { //初始ID为0
                SeatUserIDs[i] = "0";
            }
//            Api.getUserContributionList(UserMainActivity.this, requestParams, new OnRequestDataListener() {
//                @Override
//                public void requestSuccess(int code, JSONObject data) {
//                    ArrayList<ImageView> temp = new ArrayList();
////                    temp.add(mImageFirst);
////                    temp.add(mImageSecond);
////                    temp.add(mImageThird);
////                    temp.add(mImageFourth);
//                    JSONArray list = data.getJSONArray("data");
//                    for (int i = 0; i < list.size(); i++) {
//                        ImageView tempImage = (ImageView) temp.get(i);
//                        JSONObject item = list.getJSONObject(i);
//                        SeatUserIDs[i] = item.getString("id");
//                        Glide.with(UserMainActivity.this).load(item.getString("avatar"))
//                                .error(R.drawable.icon_avatar_default)
//                                .transform(new GlideCircleTransform(UserMainActivity.this))
//                                .into(tempImage);
//                    }
//                }
//
//                @Override
//                public void requestFailure(int code, String msg) {
//
//                }
//            });
            if (current_type == 2) {
                getAttentionList(token, userId, 0, 20);
            } else {
                getFansList(token, userId, 0, 20);
            }

        } else {
            toast("数据有误");
        }
    }

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
//
//    @OnClick(R.id.image_fourth) //查看第四个座位
//    public void showFourthSeat() {
//        if (SeatUserIDs[3] != "0") {
//            Bundle data = new Bundle();
//            data.putString("id", SeatUserIDs[3]);
//            openActivity(UserMainActivity.class, data);
//        }
//    }

    private void getAttentionList(String token, String userId, int limit_begin, int limit_num) {

        if (!StringUtils.isEmpty(token)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            requestParams.put("limit_begin", limit_begin);
            requestParams.put("limit_num", limit_num);
            Api.getUserAttentionList(this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    JSONArray list = data.getJSONArray("data");
                    for (int i = 0; i < list.size(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        FollowItem followItem = new FollowItem();
                        followItem.setId(item.getString("id"));
                        followItem.setAttention(item.getString("attention_status"));
                        followItem.setAvatar(item.getString("avatar"));
                        followItem.setIs_truename(item.getString("is_truename"));
                        followItem.setSex(item.getString("sex"));
                        followItem.setSignature(item.getString("signature"));
                        followItem.setUser_level(item.getString("user_level"));
                        followItem.setUser_nicename(item.getString("user_nicename"));
                        mFollowItems.add(followItem);
                    }
                    mFollowListAdapter.notifyDataSetChanged();

                }

                @Override
                public void requestFailure(int code, String msg) {
                    //加载空数据图
                    if (mFollowItems.size() == 0) {
                        mNodataView.setVisibility(View.VISIBLE);
                    } else {
                        mNodataView.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            openActivity(LoginActivity.class);
            finish();
        }

    }

    private void getFansList(String token, String userId, int limit_begin, int limit_num) {

        if (!StringUtils.isEmpty(token)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            requestParams.put("limit_begin", limit_begin);
            requestParams.put("limit_num", limit_num);
            Api.getUserFansList(this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    JSONArray list = data.getJSONArray("data");
                    for (int i = 0; i < list.size(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        FollowItem followItem = new FollowItem();
                        followItem.setId(item.getString("id"));
                        followItem.setAttention(item.getString("attention_status"));
                        followItem.setAvatar(item.getString("avatar"));
                        followItem.setIs_truename(item.getString("is_truename"));
                        followItem.setSex(item.getString("sex"));
                        followItem.setSignature(item.getString("signature"));
                        followItem.setUser_level(item.getString("user_level"));
                        followItem.setUser_nicename(item.getString("user_nicename"));
                        mFollowItems.add(followItem);
                    }
                    mFollowListAdapter.notifyDataSetChanged();
                    //加载空数据图
                    if (mFollowItems.size() == 0) {
                        mNodataView.setVisibility(View.VISIBLE);
                    } else {
                        mNodataView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {

                }
            });

        } else {
            openActivity(LoginActivity.class);
            finish();
        }
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        Bundle data = new Bundle();
        String id = (String) view.getTag();
        data.putString("id", id);
        openActivity(UserMainActivity.class, data);
    }

}
