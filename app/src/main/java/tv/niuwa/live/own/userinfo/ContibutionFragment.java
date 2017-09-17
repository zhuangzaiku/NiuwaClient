package tv.niuwa.live.own.userinfo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiFragment;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.own.UserMainActivity;
import tv.niuwa.live.utils.Api;

/**
 * Created by Administrator on 2016/8/22.
 * Author: XuDeLong
 */
public class ContibutionFragment extends BaseSiSiFragment implements OnRecyclerViewItemClickListener {
    @Bind(R.id.swipeRefreshLayout_all)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView_all)
    RecyclerView mRecyclerView;
    @Bind(R.id.noDataLayout_contribution_all)
    RelativeLayout mNodataView;
    @Bind(R.id.contribution_day)
    TextView contributionDay;
    @Bind(R.id.contribution_week)
    TextView contributionWeek;
    @Bind(R.id.contribution_month)
    TextView contributionMonth;
    @Bind(R.id.contribution_all)
    TextView contributionAll;
    private int mPosition;
    private ContributionListAdapter mContributionListAdapter;
    private ArrayList<ContributionItem> ContributionItems;

    public static ContibutionFragment getInstance(int pos) {
        ContibutionFragment fragment = new ContibutionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //ContributionItems.clear();
            getData(0, 20, mSwipeRefreshLayout);
        }
    };

    private void changeType(int index){
        TextView[] ids = {contributionDay,contributionWeek,contributionMonth,contributionAll};
        for (int i = 0;i<ids.length;i++){
            ids[i].setBackgroundResource(R.drawable.btn_shape_main);
            ids[i].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        ids[index].setBackgroundResource(R.drawable.btn_shape);
        ids[index].setTextColor(Color.WHITE);
    }
    @OnClick(R.id.contribution_day)
    public void benefitDay(View v){
        order = "day";
        ContributionItems.clear();
        mContributionListAdapter.notifyDataSetChanged();
        changeType(0);
        getData(0,20,mSwipeRefreshLayout);
    }

    @OnClick(R.id.contribution_week)
    public void benefitWeek(View v){
        order = "week";
        ContributionItems.clear();
        mContributionListAdapter.notifyDataSetChanged();
        changeType(1);
        getData(0,20,mSwipeRefreshLayout);
    }

    @OnClick(R.id.contribution_month)
    public void benefitMonth(View v){
        order = "month";
        ContributionItems.clear();
        mContributionListAdapter.notifyDataSetChanged();
        changeType(2);
        getData(0,20,mSwipeRefreshLayout);
    }

    @OnClick(R.id.contribution_all)
    public void benefitAll(View v){
        order = "all";
        ContributionItems.clear();
        mContributionListAdapter.notifyDataSetChanged();
        changeType(3);
        getData(0,20,mSwipeRefreshLayout);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPosition = bundle.getInt("pos");
        }
        if (ContributionItems == null) {
            ContributionItems = new ArrayList<>();
        }
        ContributionItems.clear();
//        for (int i = 0; i < 20; i++) {
//            ContributionItem contributionItem = new ContributionItem();
//            ContributionItems.add(contributionItem);
//        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);
        //final GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        //mRecyclerView.addItemDecoration(new SpaceItemDecoration(6));
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mSwipeRefreshLayout.setRefreshing(true);
        ContributionItems.clear();
        getData(0, 20, mSwipeRefreshLayout);
        mContributionListAdapter = new ContributionListAdapter(getActivity(), ContributionItems);
        mRecyclerView.setAdapter(mContributionListAdapter);
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
                        getData(totalItemCount, 20, null);
                        mContributionListAdapter.notifyDataSetChanged();
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
        mContributionListAdapter.setOnRecyclerViewItemClickListener(this);
    }
    String order = "all";
    private void getData(int limit_begin, int limit_num, final SwipeRefreshLayout mSwipeRefreshLayout) {
        String token = (String) SharePrefsUtils.get(this.getContext(), "user", "token", "");
        String userId = (String) SharePrefsUtils.get(this.getContext(), "user", "userId", "");
//        if (StringUtils.isNotEmpty(((ContributionActivity) getActivity()).userId)) {
//            userId = ((ContributionActivity) getActivity()).userId;
//        }
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("type", "contribution");
            requestParams.put("order", order);
            requestParams.put("limit_begin", limit_begin);
            requestParams.put("limit_num", limit_num);
            Api.getSystemRankList(this.getContext(), requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        ContributionItems.clear();
                    }
                    JSONArray list = data.getJSONArray("data");
                    for (int i = 0; i < list.size(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        ContributionItem ContributionItem = new ContributionItem();
                        ContributionItem.setId(item.getString("id"));
                        ContributionItem.setSend_num(item.getString("money_num"));
                        ContributionItem.setAvatar(item.getString("avatar"));
                        ContributionItem.setIs_truename(item.getString("is_truename"));
                        ContributionItem.setSex(item.getString("sex"));
                        ContributionItem.setSignature(item.getString("signature"));
                        ContributionItem.setUser_level(item.getString("user_level"));
                        ContributionItem.setUser_nicename(item.getString("user_nicename"));
                        ContributionItems.add(ContributionItem);
                    }
                    mContributionListAdapter.notifyDataSetChanged();
                }

                @Override
                public void requestFailure(int code, String msg) {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    //toast(msg);
                    //加载空数据图
                    if (ContributionItems.size() == 0) {
                        mNodataView.setVisibility(View.VISIBLE);
                    } else {
                        mNodataView.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            openActivity(LoginActivity.class);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_contribution;
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        Bundle data = new Bundle();
        String id = (String) view.getTag();
        data.putString("id", id);
        openActivity(UserMainActivity.class, data);
    }

}
