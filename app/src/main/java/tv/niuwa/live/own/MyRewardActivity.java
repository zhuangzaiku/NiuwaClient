package tv.niuwa.live.own;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smart.androidutils.utils.LogUtils;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.own.adapter.MyRewardAdapter;
import tv.niuwa.live.own.bean.RewardItem;
import tv.niuwa.live.own.message.MessageActivity;
import tv.niuwa.live.utils.Api;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 19/09/2017 22:51.
 * <p>
 * All copyright reserved.
 */

public class MyRewardActivity extends BaseSiSiActivity {

    @Bind(R.id.left_icon)
    ImageView leftIcon;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.listView)
    ListView mRewardListView;

    @Bind(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    private MyRewardAdapter mRewardAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        mRewardAdapter = new MyRewardAdapter(MyRewardActivity.this);
        mRewardListView.setAdapter(mRewardAdapter);
        getRewardList(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getRewardList(true);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getRewardList(false);
            }
        });
    }

    private void getRewardList(final boolean needClear) {
        String token = (String) SharePrefsUtils.get(MyRewardActivity.this, "user", "token", "");
        String userId = (String) SharePrefsUtils.get(MyRewardActivity.this, "user", "userId", "");
        JSONObject requestParams = new JSONObject();
        requestParams.put("token", token);
        requestParams.put("userId", userId);
        Api.getMyReward(this, requestParams, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                LogUtils.d(data.toString());
                JSONArray dataArr = data.getJSONArray("data");
                List<RewardItem> rewardItemList = new ArrayList<RewardItem>();
                int length = data.size();
                for (int i = 0; i < length; i++) {
                    JSONObject jo = dataArr.getJSONObject(i);
                    String name = jo.getString("name");
                    String avatar = jo.getString("avatar");
                    String desc = jo.getString("desc");
                    int status = jo.getInteger("status");
                    RewardItem item = new RewardItem(name, avatar, desc, status);
                    rewardItemList.add(item);
                }
                mRewardAdapter.addUserMenuItemList(rewardItemList, needClear);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }

            @Override
            public void requestFailure(int code, String msg) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
                toast(msg);
            }
        });
    }

    private void initView() {
        title.setText(R.string.my_reward);
        leftIcon.setImageResource(R.drawable.btn_fanhui);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_my_reward;
    }
}
