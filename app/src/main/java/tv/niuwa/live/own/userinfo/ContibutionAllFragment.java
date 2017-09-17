package tv.niuwa.live.own.userinfo;

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
public class ContibutionAllFragment extends BaseSiSiFragment implements OnRecyclerViewItemClickListener {
    @Bind(R.id.swipeRefreshLayout_all)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView_all)
    RecyclerView mRecyclerView;
    @Bind(R.id.noDataLayout_contribution_all)
    RelativeLayout mNodataView;

    private int mPosition;
    private ContributionListAdapter mContributionListAdapter;
    private ArrayList<ContributionItem> ContributionItems;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //ContributionItems.clear();
            getData(0, 20, mSwipeRefreshLayout);
        }
    };

    public static ContibutionAllFragment getInstance(int pos) {
        ContibutionAllFragment fragment = new ContibutionAllFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        fragment.setArguments(bundle);
        return fragment;
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

    private void getData(int limit_begin, int limit_num, final SwipeRefreshLayout mSwipeRefreshLayout) {
        String token = (String) SharePrefsUtils.get(this.getContext(), "user", "token", "");
        String userId = (String) SharePrefsUtils.get(this.getContext(), "user", "userId", "");
        if (StringUtils.isNotEmpty(((ContributionActivity) getActivity()).userId)) {
            userId = ((ContributionActivity) getActivity()).userId;
        }
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            requestParams.put("type", "all");
            requestParams.put("limit_begin", limit_begin);
            requestParams.put("limit_num", limit_num);
            Api.getUserContributionList(this.getContext(), requestParams, new OnRequestDataListener() {
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
                        ContributionItem.setSend_num(item.getString("send_num"));
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

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_contribution_all;
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        Bundle data = new Bundle();
        String id = (String) view.getTag();
        data.putString("id", id);
        openActivity(UserMainActivity.class, data);
    }

}
