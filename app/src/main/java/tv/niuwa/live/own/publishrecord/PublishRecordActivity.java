package tv.niuwa.live.own.publishrecord;

import android.os.Bundle;
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
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.own.UserMainActivity;
import tv.niuwa.live.utils.Api;

public class PublishRecordActivity extends BaseSiSiActivity implements OnRecyclerViewItemClickListener {

    @Bind(R.id.swipeRefreshLayout_publish)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView_publish)
    RecyclerView mRecyclerView;
    @Bind(R.id.noDataLayout_fans)
    RelativeLayout mNodataView;
    private PublishRecordListAdapter mFansListAdapter;
    private ArrayList<PublishRecordItem> mPublishRecordItems;

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        PublishRecordActivity.this.finish();
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          /*  new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);*/
           // mPublishRecordItems.clear();
            getData(0, 20, mSwipeRefreshLayout);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPublishRecordItems == null) {
            mPublishRecordItems = new ArrayList<>();
        }
        mPublishRecordItems.clear();
//       for (int i = 0; i < 20; i++) {
//            PublishRecordItem PublishRecordItem = new PublishRecordItem();
//            mPublishRecordItems.add(PublishRecordItem);
//        }
        mSwipeRefreshLayout.setRefreshing(true);
        getData(0, 20, mSwipeRefreshLayout);
        mTextTopTitle.setText("我的发布");
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
       // mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mFansListAdapter = new PublishRecordListAdapter(this, mPublishRecordItems);
        mRecyclerView.setAdapter(mFansListAdapter);
        mFansListAdapter.setOnRecyclerViewItemClickListener(this);
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
                        // toast("没有更多数据了~~");
                        //getData(totalItemCount, 20, null);
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
    }

    private void getData(int limit_begin, int limit_num, final SwipeRefreshLayout mSwipeRefreshLayout) {
        String token = (String) SharePrefsUtils.get(this, "user", "token", "");
        String userId = (String) SharePrefsUtils.get(this, "user", "userId", "");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            requestParams.put("limit_begin", limit_begin);
            requestParams.put("limit_num", limit_num);
            Api.getPublishRecord(this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mPublishRecordItems.clear();
                    }
                    JSONArray list = data.getJSONArray("data");
                    for (int i = 0; i < list.size(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        PublishRecordItem PublishRecordItem = new PublishRecordItem();
                        PublishRecordItem.setId(item.getString("id"));
                        PublishRecordItem.setAdd_time(item.getString("add_time"));
                        PublishRecordItem.setEarn(item.getString("earn"));
                        PublishRecordItem.setHits(item.getString("hits"));
                        PublishRecordItem.setVideo_title(item.getString("video_title"));
                        mPublishRecordItems.add(PublishRecordItem);
                    }
                    mFansListAdapter.notifyDataSetChanged();
                }

                @Override
                public void requestFailure(int code, String msg) {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    //toast(msg);
                    //加载空数据图
                    if (mPublishRecordItems.size() == 0) {
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

    @Override
    public int getLayoutResource() {
        return R.layout.activity_publish_record;
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        Bundle data = new Bundle();
        String id = (String) view.getTag();
        data.putString("id", id);
        openActivity(UserMainActivity.class, data);
    }
}
