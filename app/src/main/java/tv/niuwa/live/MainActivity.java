package tv.niuwa.live;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.smart.androidui.widget.menu.BottomTabMenu;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.NumUtils;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import cn.leancloud.chatkit.LCChatKit;
import de.greenrobot.event.EventBus;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.event.BroadCastEvent;
import tv.niuwa.live.home.model.HomeDataItem;
import tv.niuwa.live.home.model.VideoItem;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.living.LivingActivity;
import tv.niuwa.live.living.PublishActivity;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.own.PushActivity;
import tv.niuwa.live.own.UserCenterActivity;
import tv.niuwa.live.own.userinfo.UserMenuItem;
import tv.niuwa.live.search.SearchActivity;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.utils.DisplayUtil;

public class MainActivity extends BaseSiSiActivity {

    //    @Bind(R.id.bottomTabMenu)
    BottomTabMenu mBottomTabMenu;
    private Fragment[] mTabFragment = new Fragment[2];
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    //    @OnClick(R.id.image_start_living)
    public void startLiving(View view) {
        openActivity(PublishActivity.class);
    }

    private IndicatorViewPager indicatorViewPager;

    @Bind(R.id.moretab_viewPager)
    ViewPager viewPager;

    private Integer[] images = {R.drawable.img_banner01, R.drawable.img_banner02, R.drawable.img_banner03,
            R.drawable.img_banner04};

    @Bind(R.id.title)
    TextView title;


    @Bind(R.id.left_icon)
    ImageView leftIcon;


    @Bind(R.id.right_icon)
    ImageView rightIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //电视端
//        goLiving();

        //客户端
        if ("".equals(SharePrefsUtils.get(this, "user", "token", "")) || "".equals(SharePrefsUtils.get(this, "user", "userId", ""))) {
            // openActivity(LoginActivity.class);
//            goLiving();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }
//        else {
//            openActivity(PublishActivity.class);
//        }
        onCreateRest();
    }

    private void onCreateRest() {
        PushService.setDefaultPushCallback(this, PushActivity.class);

        setDoubleBack(true);


        ScrollIndicatorView scrollIndicatorView = (ScrollIndicatorView) findViewById(R.id.moretab_indicator);

        float unSelectSize = 14;
        float selectSize = unSelectSize * 1.1f;
        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(Color.BLACK, Color.DKGRAY).setSize(selectSize, unSelectSize));
        ColorBar colorBar = new ColorBar(this, 0xFFf4da33, 5, ScrollBar.Gravity.BOTTOM);
        colorBar.setWidth(DisplayUtil.dipToPix(this, 25));
        View slideView = colorBar.getSlideView();
//        slideView.setPadding(slideView.getPaddingLeft(), -DisplayUtil.dipToPix(this, 3),slideView.getPaddingRight(),slideView.getPaddingBottom());
        scrollIndicatorView.setScrollBar(colorBar);

        title.setText(R.string.app_name_short);
        viewPager.setOffscreenPageLimit(2);
        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
        indicatorViewPager.setAdapter(new MyAdapter());

        leftIcon.setImageResource(R.drawable.img_sousuo);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SearchActivity.class);
            }
        });
        rightIcon.setImageResource(R.drawable.img_me);
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(UserCenterActivity.class);
            }
        });


        initData();
        //设置翻页的效果，不需要翻页效果可用不设
        //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
//        convenientBanner.setManualPageable(false);//设置不能手动影响
//        mFragmentManager = getSupportFragmentManager();
//        mTabFragment[0] = mFragmentManager.findFragmentById(R.id.fragment_home);
//        mTabFragment[1] = mFragmentManager.findFragmentById(R.id.fragment_own);
//        mTransaction = mFragmentManager.beginTransaction();
//        mTransaction.hide(mTabFragment[0]).hide(mTabFragment[1]);
//        mTransaction.show(mTabFragment[0]).commit();
//        mBottomTabMenu.setHasMiddleImage(true);
//        mBottomTabMenu.addTextImageTab(R.drawable.zhibo_shou, R.drawable.zhibo_shou_s, "首页", true);
//        mBottomTabMenu.addTextImageTab(R.drawable.zhibo_me, R.drawable.zhibo_me_s, "我的", false);
//        mBottomTabMenu.changeImageTab(0);
//        mBottomTabMenu.setOnTabClickListener(new OnTabClickListener() {
//            @Override
//            public void onTabClick(View view, int position) {
//                mTransaction = mFragmentManager.beginTransaction();
//                mTransaction.hide(mTabFragment[0]).hide(mTabFragment[1]);
//                mTransaction.show(mTabFragment[position]).commit();
//            }
//        });
        LCChatKit.getInstance().open((String) SharePrefsUtils.get(this, "user", "userId", ""), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {

            }
        });
        JSONObject params = new JSONObject();
        try {
            String versionCode = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            params.put("ver_num", versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Api.checkUpdate(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONObject info = data.getJSONObject("data");
                if (StringUtils.isNotEmpty(info.getString("package"))) {
                    checkUpgrade(info.getString("package"), info.getString("description"));
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                //toast(msg);
            }
        });
    }

    private void initData() {
        String token = (String) SharePrefsUtils.get(MainActivity.this, "user", "token", "");
        String userId = (String) SharePrefsUtils.get(MainActivity.this, "user", "userId", "");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            Api.getUserInfo(MainActivity.this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if(isActive){
                        JSONObject userInfo = data.getJSONObject("data");
                        SharePrefsUtils.put(MainActivity.this, "user", "user_nicename", userInfo.getString("user_nicename"));
                        SharePrefsUtils.put(MainActivity.this, "user", "user_level", userInfo.getString("user_level"));
                        SharePrefsUtils.put(MainActivity.this, "user", "avatar", userInfo.getString("avatar"));
                        MyApplication app = (MyApplication) MainActivity.this.getApplication();
                        app.setBalance(userInfo.getString("balance"));
                    }

                }

                @Override
                public void requestFailure(int code, String msg) {
                }
            });

        }
    }

    private void goLiving() {
        String mobile = "18073185008";
        String password = "111qqq";
        if ("".equals(mobile.trim()) || !NumUtils.isPhoneNumber(mobile)) {
            toast("请正确填写手机号");
            return;
        }
        if ("".equals(password.trim())) {
            toast("请输入密码");
            return;
        }
        JSONObject params = new JSONObject();
        params.put("mobile_num", mobile);
        params.put("password", password);
        Api.doLogin(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                String token = data.getString("token");
                JSONArray userInfo = data.getJSONArray("info");
                if (token != null) {
                    SharePrefsUtils.put(MainActivity.this, "user", "token", token);
                    if (userInfo.get(0) != null) {
                        JSONObject obj = (JSONObject) userInfo.getJSONObject(0);
                        String userId = obj.getString("id");
                        SharePrefsUtils.put(MainActivity.this, "user", "userId", userId);
                    }
                    toast("登录成功");
                    openActivity(PublishActivity.class);
//                    onCreateRest();
                    finish();
                    // 发送通知
                    EventBus.getDefault().post(new BroadCastEvent(LoginActivity.FINISH_EVENT));
                } else {
                    toast("token获取失败");
                }

            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    AlertDialog tipsAlertDialog;

    private void checkUpgrade(final String downloadUrl, String mes) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        tipsAlertDialog = builder.setTitle("提示")
                .setMessage(mes)
                .setNegativeButton("再等一下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (tipsAlertDialog.isShowing()) {
                            tipsAlertDialog.dismiss();
                        }
                    }
                })
                .setPositiveButton("更新下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri uri = Uri.parse(downloadUrl);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .create();
        tipsAlertDialog.show();
        tipsAlertDialog.setCancelable(false);
        tipsAlertDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {
        private String[] versions = {"热门", "综艺", "新闻", "演唱会", "明星会", "娱乐", "时尚", "时尚", "时尚", "时尚"};
        private String[] names = {"纸杯蛋糕", "甜甜圈", "闪电泡芙", "冻酸奶", "姜饼", "蜂巢", "时尚", "时尚", "时尚", "时尚"};

        @Override
        public int getCount() {
            return versions.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(versions[position]);

            int witdh = getTextWidth(textView);
            int padding = DisplayUtil.dipToPix(getApplicationContext(), 8);
            //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
            //1.3f是根据上面字体大小变化的倍数1.3f设置
            textView.setWidth((int) (witdh * 1.3f) + padding);

            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.layout_main_homepage, null);
            }
            if (position == 0) {
                initHomePage(convertView);
            } else {
                initOtherPage(convertView);
            }

            RefreshLayout refreshLayout = (RefreshLayout) convertView.findViewById(R.id.refreshLayout);
//            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//
//                }
//
//                @Override
//                public void onRefresh(RefreshLayout refreshlayout) {
//                    refreshlayout.finishRefresh(2000);
//                }
//            });
            refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    refreshlayout.finishLoadmore(2000);
                }
            });
            return convertView;
        }


        @Override
        public int getItemPosition(Object object) {
            //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
            // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
            return PagerAdapter.POSITION_UNCHANGED;
        }

        private int getTextWidth(TextView textView) {
            if (textView == null) {
                return 0;
            }
            Rect bounds = new Rect();
            String text = textView.getText().toString();
            Paint paint = textView.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int width = bounds.left + bounds.width();
            return width;
        }

    }

    private void initHomePage(View convertView) {
        convertView.findViewById(R.id.banner_layout).setVisibility(View.VISIBLE);
        convertView.findViewById(R.id.tagLayout).setVisibility(View.GONE);
        ConvenientBanner convenientBanner = (ConvenientBanner) convertView.findViewById(R.id.convenientBanner);
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, Arrays.asList(images))
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_LEFT)
                .startTurning(5000);
        initData(convertView, true);
    }

    private void initOtherPage(View convertView) {
        convertView.findViewById(R.id.banner_layout).setVisibility(View.GONE);
        convertView.findViewById(R.id.tagLayout).setVisibility(View.VISIBLE);
        initData(convertView, false);

    }

    private void initData(View convertView, boolean isHomePage) {
        List<HomeDataItem> data = new ArrayList<>();
        String[] audienceNum = new String[]{"12.4万", "4.4万", "5.6万", "45万", "23.3万", "2.7万", "11.1万", "6.2万"};
        String[] titleStrs = getResources().getStringArray(R.array.titles);
        String[] channelTitles = getResources().getStringArray(R.array.channelTitles);
        int[] imgIds = new int[]{R.drawable.img_01, R.drawable.img_02, R.drawable.img_03, R.drawable.img_04,
                R.drawable.img_05, R.drawable.img_06, R.drawable.img_07, R.drawable.img_08};
        for (int i = 0; i < audienceNum.length; i++) {
            data.add(new HomeDataItem(isHomePage ? channelTitles[i] : null, titleStrs[i], audienceNum[i], imgIds[i]));
        }
        GridView gridView = (GridView) convertView.findViewById(R.id.gridView);
        gridView.setAdapter(new HomeListAdapter(this, data));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                VideoItem videoItem = new VideoItem();
//                videoItem.setRoom_id("16");
//                videoItem.setId("156276");
//                videoItem.setUser_nicename("\u7528\u6237156276");
//                videoItem.setAvatar("http://zhibo.519wan.com/data/upload/avatar/default.png");
//                videoItem.setChannel_creater("156276");
//                videoItem.setChannel_location(null);
//                videoItem.setChannel_title("niuwa");
//                videoItem.setOnline_num("11");
//                videoItem.setSmeta("http://zhibo.519wan.com/data/upload/avatar/default.png");
//                videoItem.setChannel_status("2");
//                videoItem.setUser_level("1");
//                videoItem.setPrice("0");
//                videoItem.setNeed_password("0");
//                Bundle data = new Bundle();
//                data.putSerializable("videoItem", videoItem);
//                openActivity(LivingActivity.class,data);
            }
        });
    }


    public class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
            imageView.setImageResource(data);
        }
    }


}
