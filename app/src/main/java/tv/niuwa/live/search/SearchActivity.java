package tv.niuwa.live.search;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import tv.niuwa.live.core.BaseSiSiEditActivity;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.own.UserMainActivity;
import tv.niuwa.live.own.fans.FansItem;
import tv.niuwa.live.utils.Api;

public class SearchActivity extends BaseSiSiEditActivity implements OnRecyclerViewItemClickListener {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.edit_input_keyword)
    EditText mEditInputKeyword;
    @Bind(R.id.noDataLayout_search)
    RelativeLayout mNodataView;
    @Bind(R.id.search_key)
    TextView mSearchKey;
    private String keyWords;
    private ArrayList<FansItem> result;
    private SearchAdapter mSearchAdapter;

    @OnClick(R.id.image_back)

    public void back(View view) {
        SearchActivity.this.finish();
    }

    @OnClick(R.id.btn_search)
    public void search(View view) {
        keyWords = mEditInputKeyword.getText().toString();
        if (StringUtils.isEmpty(keyWords)) {
            toast("请输入要搜索的用户名");
            return;
        }
        if (result != null) {
            result.clear();
        }
        mNodataView.setVisibility(View.GONE);
        getData(keyWords, 0, 20);
    }

    private void getData(final String keyWords, int limit_begin, int limit_num) {
        String token = (String) SharePrefsUtils.get(this, "user", "token", "");
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("keyword", keyWords);
        params.put("limit_begin", limit_begin);
        params.put("limit_num", limit_num);
        Api.doSearch(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray list = data.getJSONArray("data");
                for (int i = 0; i < list.size(); i++) {
                    JSONObject item = list.getJSONObject(i);
                    FansItem fansItem = new FansItem();
                    fansItem.setId(item.getString("id"));
                    fansItem.setAttention_status(item.getString("attention_status"));
                    fansItem.setAvatar(item.getString("avatar"));
                    fansItem.setIs_truename(item.getString("is_truename"));
                    fansItem.setChannel_status(item.getString("channel_status"));
                    fansItem.setSex(item.getString("sex"));
                    fansItem.setSignature(item.getString("signature"));
                    fansItem.setUser_level(item.getString("user_level"));
                    fansItem.setUser_nicename(item.getString("user_nicename"));
                    result.add(fansItem);
                }
                mSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailure(int code, String msg) {
                if(result.size()==0){
                    mSearchAdapter.notifyDataSetChanged();
                    //toast(msg);
                    //加载空数据图
                    if (result.size() == 0) {
                        mNodataView.setVisibility(View.VISIBLE);
                        mSearchKey.setText("\""+keyWords+"\"");
                    } else {
                        mNodataView.setVisibility(View.GONE);
                    }
                }else{
                    toast("没有更多数据了");
                }

            }
        });

    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isActive)
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        result = new ArrayList<>();
        if (mSearchAdapter == null) {
            final LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
            //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            mSearchAdapter = new SearchAdapter(this, result);
            mRecyclerView.setAdapter(mSearchAdapter);
            mSearchAdapter.setOnRecyclerViewItemClickListener(this);
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
                            getData(keyWords, lastVisibleItemPosition, 20);
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
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_search;
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        Bundle data = new Bundle();
        String id = (String) view.getTag();
        data.putString("id", id);
        openActivity(UserMainActivity.class, data);
    }
}
