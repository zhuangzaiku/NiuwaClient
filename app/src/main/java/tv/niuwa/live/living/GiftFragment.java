package tv.niuwa.live.living;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.smart.androidutils.utils.DensityUtils;
import com.smart.androidutils.utils.LogUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

import tv.niuwa.live.MyApplication;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiFragment;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.own.money.ChargeMoneyActivity;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.view.GiftViewPager;

/**
 * Created by Administrator on 2016/9/5.
 * Author: XuDeLong
 */
public class GiftFragment extends BaseSiSiFragment implements AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.gift_viewPager)
    GiftViewPager mGiftViewPager;
    @Bind(R.id.gift_balances)
    TextView mGiftbalance;
    @Bind(R.id.live_btn_send)
    Button mSendbtn;
    @Bind(R.id.tv_username)
    TextView tv_username;
    @Bind(R.id.tv_gift)
    TextView tv_gift;
    MyApplication app;
    Timer timer = new Timer();
    private GiftGridViewAdapter mGridAdapter;
    private GiftViewPagerAdapter mPagerAdapter;
    private ArrayList<GiftModel> giftItems;
    private ArrayList<LinearLayout> gridItems;
    private int giftCount;
    private ArrayList<GiftGridViewAdapter> mGiftGridViewAdapterList;
    private int mPage = 0;
    private int mPageCount = 0;
    private GiftModel model;//当前礼物
    private GiftModel lastGiftModel;//上一次送的礼物
    private int timeContainue = 0;//5s内送同一个礼物 相当于连送
    private int CONTAINUE_TIME = 5;
    private int containueNum = 1; //连送次数
    private int pageGiftCount = 4;//每页礼物数

    @OnClick(R.id.gift_charge)
    public void giftCharge(View view) {
        openActivity(ChargeMoneyActivity.class);
    }

    @OnClick(R.id.live_btn_send)
    public void liveBtnSend(View view) {
        if (model == null) {
            toast("请先选择要赠送的礼物");
//            ToastUtils.makeText(
//                    getContext(), "请先选择要赠送的礼物", ToastUtils.LENGTH_SHORT).show();
            return;
        }
        //上一次送的礼物 与当前要送的礼物是否同一个礼物--是
        //距离上次送的时间 是否大于CONTAINUE_TIME  -- 是
        //该礼物是否支持连送
        if (null != lastGiftModel && lastGiftModel.getGiftid().equals(model.getGiftid()) && timeContainue > 0 && "1".equals(model.getContinuous())) {
            containueNum++;
        } else {
            containueNum = 1;
        }
        timeContainue = CONTAINUE_TIME;
        JSONObject params = new JSONObject();
        final LivingActivity temp = (LivingActivity) this.getContext();
        params.put("token", temp.token);
        params.put("room_id", temp.mVideoItem.getRoom_id());
        params.put("giftid", model.getGiftid());
        params.put("number", "1");
        mSendbtn.setText("赠送中");
        mSendbtn.setBackgroundColor(0xFFBFBFBF);
        mSendbtn.setEnabled(false);
        Api.sendGift(this.getContext(), params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {

                // TODO: 2016/12/27 [用户]赠送一个[礼物]
                tv_username.setText(temp.user_nicename);
                tv_gift.setText(model.getGiftname());
                
                mSendbtn.setText("赠送");
                mSendbtn.setBackgroundColor(0xFFFF4081);
                mSendbtn.setEnabled(true);
                JSONObject result = data.getJSONObject("data");
                app.setBalance((result.getString("balance")));
                mGiftbalance.setText(result.getString("balance"));
                DanmuModel danmModel = new DanmuModel();
                danmModel.setType("2");
                danmModel.setUserLevel(temp.user_level);
                danmModel.setUserName(temp.user_nicename);
                danmModel.setAvatar(temp.avatar);
                danmModel.setUserId(temp.userId);
                danmModel.setContent("赠送1个" + model.getGiftname());
                JSONObject gift = new JSONObject();
                gift.put("gifticon", model.getGifticon());
                gift.put("giftname", model.getGiftname());
                gift.put("needcoin", model.getNeedcoin());
                gift.put("giftid", model.getGiftid());
                gift.put("continuous", model.getContinuous());
                gift.put("continuousNum", containueNum);
                JSONObject Other = new JSONObject();
                Other.put("giftInfo", gift);
                lastGiftModel = model;
                danmModel.setOther(Other);
                showGiftAnim1(temp, danmModel, model, containueNum);
                //showGiftAnim(temp, model, containueNum);
                temp.sendMessage(danmModel);
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
                mSendbtn.setText("赠送");
                mSendbtn.setBackgroundColor(0xFFFF4081);
                mSendbtn.setEnabled(true);
            }
        });
    }

    public boolean isHasScroll() {
        return mGiftViewPager == null ? false : mGiftViewPager.isHasScroll();
    }

    public void setHasScroll(boolean hasScroll) {
        if (mGiftViewPager != null) {
            mGiftViewPager.setHasScroll(hasScroll);
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

    public void showGiftAnim(final LivingActivity temp, GiftModel model, int num) {
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
        Glide.with(temp).load(temp.avatar)
                .error(R.drawable.icon_avatar_default)
                .into(giftAvatar);
        Glide.with(temp).load(model.getGifticon())
                .error(R.drawable.icon_avatar_default)
                .into(giftImage);
        if (Integer.parseInt(model.getNeedcoin()) > 200) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(giftImage.getLayoutParams());
            layoutParams.width = DensityUtils.dip2px(getContext(), 60);
            layoutParams.height = DensityUtils.dip2px(getContext(), 60);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.gravity = Gravity.RIGHT;
            giftImage.setLayoutParams(layoutParams);
        }
        giftUserName.setText(temp.user_nicename);
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
        giftPop.postDelayed(giftTimer, 3000);
        temp.mLiveGiftScroll.fullScroll(temp.mLiveGiftScroll.FOCUS_DOWN);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        timer.schedule(task, 1000, 1000);
    }

    private void init() {
        if (giftItems == null) {
            giftItems = new ArrayList<>();
        }
        if (gridItems == null) {
            gridItems = new ArrayList<>();
        }
        if (mGiftGridViewAdapterList == null) {
            mGiftGridViewAdapterList = new ArrayList<>();
        }
        giftItems.clear();
        gridItems.clear();
        app = (MyApplication) getActivity().getApplication();
        // mGiftbalance.setText("2");
//        mGiftGridViewAdapterList.clear();
        JSONObject params = new JSONObject();
        params.put("limit_begin", "0");
        params.put("limit_num", "50");
        Api.getGifts(this.getContext(), params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray gifts = data.getJSONArray("data");
                giftCount = gifts.size();
                for (int i = 0; i < gifts.size(); i++) {
                    JSONObject giftItem = gifts.getJSONObject(i);
                    GiftModel item = new GiftModel();
                    item.setGiftid(giftItem.getString("giftid"));
                    item.setContinuous(giftItem.getString("continuous"));
                    item.setGifticon(giftItem.getString("gifticon"));
                    item.setGiftname(giftItem.getString("giftname"));
                    item.setNeedcoin(giftItem.getString("needcoin"));
                    item.setChecked(false);
                    giftItems.add(item);
                }
                int pageNum = (giftCount % pageGiftCount) > 0 ? (giftCount / pageGiftCount) + 1 : (giftCount / pageGiftCount);
                mPageCount = pageNum;
                for (int i = 0; i < pageNum; i++) {
                    LinearLayout page = (LinearLayout) LayoutInflater.from(GiftFragment.this.getContext()).inflate(R.layout.gift_pager, null);
                    GridView gridView = (GridView) page.findViewById(R.id.gift_grid);
                    GiftGridViewAdapter giftGridViewAdapter = new GiftGridViewAdapter(GiftFragment.this.getContext(), giftItems.subList(i * pageGiftCount, ((i + 1) * pageGiftCount) > giftItems.size() ? giftItems.size() : (i + 1) * pageGiftCount));
                    gridView.setAdapter(giftGridViewAdapter);
                    gridView.setOnItemClickListener(GiftFragment.this);
                    mGiftGridViewAdapterList.add(giftGridViewAdapter);
                    gridItems.add(page);
                }
                mPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
//        for (int i = 0 ;i<giftCount;i++){
//            GiftModel item = new GiftModel();
//            item.s("gift"+ i);
//            item.setImageUrl("http://demo2.deerlive.com/data/upload/1440500099843411.png");
//            giftItems.add(item);
//        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPagerAdapter = new GiftViewPagerAdapter(gridItems);
        mGiftViewPager.setAdapter(mPagerAdapter);
        mGiftViewPager.addOnPageChangeListener(this);
        mPage = 0;
        mGiftViewPager.setCurrentItem(0);
        mGiftbalance.setText(app.getBalance());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_gift;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        model = null;
        GiftGridViewAdapter adapter = (GiftGridViewAdapter) adapterView.getAdapter();
        boolean status = adapter.isChecked(position);
        if (!status) {
            model = (GiftModel) adapter.getItem(position);
        }
        // String giftname = model.getGiftname();
        //String needcoin = model.getNeedcoin();
        for (int i = 0; i < mPageCount; i++) {
            GiftGridViewAdapter giftAdapter = mGiftGridViewAdapterList.get(i);
            if (i != mPage) {
                giftAdapter.resetAll();
            } else {
                giftAdapter.updateStatus(position, !status);
            }
        }
        //ToastUtils.makeText(getActivity(), giftname + "->" + needcoin, ToastUtils.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            LogUtils.i("-----", timeContainue + "");
            if (timeContainue > 0) {
                timeContainue--;
                if (isActive && lastGiftModel != null && "1".equals(lastGiftModel.getContinuous())) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (timeContainue > 0) {
                                mSendbtn.setText(timeContainue + "s");
                            } else {
                                mSendbtn.setText("赠送");
                            }
                        }
                    });
                }

            }
        }
    };
}
