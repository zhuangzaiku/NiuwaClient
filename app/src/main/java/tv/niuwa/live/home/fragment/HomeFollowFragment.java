package tv.niuwa.live.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import tv.niuwa.live.MyApplication;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiFragment;
import tv.niuwa.live.home.adapter.FollowRecyclerListAdapter;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;
import tv.niuwa.live.home.model.VideoItem;
import tv.niuwa.live.intf.OnCustomClickListener;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.living.LivingActivity;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.utils.DialogEnsureUtiles;

/**
 * Created by fengjh on 16/7/19.
 */
public class HomeFollowFragment extends BaseSiSiFragment implements OnRecyclerViewItemClickListener {

    @Bind(R.id.swipeRefreshLayout_video)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView_video)
    RecyclerView mRecyclerView;
    @Bind(R.id.noDataLayout_follow_home)
    RelativeLayout mNodataView;

    private int mPosition;
    private ArrayList<VideoItem> mVideoItems;
    private FollowRecyclerListAdapter mFollowRecyclerListAdapter;

    public static HomeFollowFragment getInstance(int pos) {
        HomeFollowFragment fragment = new HomeFollowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
//            }, 2000);
            //mVideoItems.clear();
            getData(0, 10, mSwipeRefreshLayout);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPosition = bundle.getInt("pos");
        }
        if (mVideoItems == null) {
            mVideoItems = new ArrayList<>();
        }
        mVideoItems.clear();
//        for (int i = 0; i < 10; i++) {
//            VideoItem videoItem = new VideoItem();
//            mVideoItems.add(videoItem);
//        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mSwipeRefreshLayout.setRefreshing(true);
        mVideoItems.clear();
        getData(0, 10, mSwipeRefreshLayout);
        mFollowRecyclerListAdapter = new FollowRecyclerListAdapter(getActivity(), mVideoItems);
        mRecyclerView.setAdapter(mFollowRecyclerListAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
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
                        getData(totalItemCount, 10, null);
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
        mFollowRecyclerListAdapter.setOnRecyclerViewItemClickListener(this);
    }

    private void getData(int limit_begin, int limit_num, final SwipeRefreshLayout mSwipeRefreshLayout) {
        String token = (String) SharePrefsUtils.get(this.getContext(), "user", "token", "");
        String userId = (String) SharePrefsUtils.get(this.getContext(), "user", "userId", "");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            //requestParams.put("type",1);
            requestParams.put("limit_begin", limit_begin);
            requestParams.put("limit_num", limit_num);
            Api.getAttentionChannelList(this.getContext(), requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if (isActive){
                        if (mSwipeRefreshLayout != null) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            mVideoItems.clear();
                        }
                        JSONArray list = data.getJSONArray("info");
                        for (int i = 0; i < list.size(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            VideoItem videoItem = new VideoItem();
                            videoItem.setRoom_id(item.getString("room_id"));
                            videoItem.setId(item.getString("id"));
                            videoItem.setUser_nicename(item.getString("user_nicename"));
                            videoItem.setAvatar(item.getString("avatar"));
                            videoItem.setChannel_creater(item.getString("channel_creater"));
                            videoItem.setChannel_location(item.getString("channel_location"));
                            videoItem.setChannel_title(item.getString("channel_title"));
                            videoItem.setOnline_num(item.getString("online_num"));
                            videoItem.setSmeta(item.getString("smeta"));
                            videoItem.setChannel_status(item.getString("channel_status"));
                            videoItem.setUser_level(item.getString("user_level"));
                            videoItem.setPrice(item.getString("price"));
                            videoItem.setNeed_password(item.getString("need_password"));
                            mVideoItems.add(videoItem);
                        }
                        mFollowRecyclerListAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void requestFailure(int code, String msg) {
                    if (isActive){
                        if (mSwipeRefreshLayout != null) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        //toast(msg);
                        //加载空数据图
                        if (mVideoItems.size() == 0) {
                            mNodataView.setVisibility(View.VISIBLE);
                        } else {
                            mNodataView.setVisibility(View.GONE);
                        }
                    }

                }
            });

        } else {
            openActivity(LoginActivity.class);
            this.getActivity().finish();
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_follow_home;
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        final VideoItem item = mVideoItems.get(position);
        if(StringUtils.isNotEmpty(item.getPrice()) && Integer.parseInt(item.getPrice())>0){
            DialogEnsureUtiles.showConfirm(getActivity(), "该房间需要收费" + item.getPrice(), new OnCustomClickListener() {
                @Override
                public void onClick(String value) {
                    Bundle data = new Bundle();
                    data.putSerializable("videoItem", item);
                    openActivity(LivingActivity.class, data);
                }
            });

        }else if(StringUtils.isNotEmpty(item.getNeed_password()) && Integer.parseInt(item.getNeed_password())==1){
            DialogEnsureUtiles.showInfo(getActivity(), new OnCustomClickListener() {
                @Override
                public void onClick(final String value) {
                    JSONObject params = new JSONObject();
                    params.put("room_id", item.getRoom_id());
                    params.put("token",(String)SharePrefsUtils.get(getContext(),"user","token",""));
                    params.put("room_password",value);
                    Api.checkPass(getContext(), params, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            MyApplication app = (MyApplication) getActivity().getApplication();
                            if(Integer.parseInt(app.getBalance()) < Integer.parseInt(item.getPrice())){
                                toast("余额不足，请充值");
                                return;
                            }
                            Bundle data1 = new Bundle();
                            data1.putSerializable("videoItem", item);
                            data1.putString("password",value);
                            openActivity(LivingActivity.class, data1);
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            toast(msg);
                        }
                    });
                }
            },"","该房间需要密码");
        }else{
            Bundle data = new Bundle();
            data.putSerializable("videoItem", item);
            openActivity(LivingActivity.class, data);
        }
    }
}
