package tv.niuwa.live.own;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.LogUtils;
import com.smart.androidutils.utils.SharePrefsUtils;

import butterknife.Bind;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.intf.OnRequestDataListener;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        String token = (String) SharePrefsUtils.get(MyRewardActivity.this, "user", "token", "");
        String userId = (String) SharePrefsUtils.get(MyRewardActivity.this, "user", "userId", "");
        JSONObject requestParams = new JSONObject();
        requestParams.put("token", token);
        requestParams.put("userId", userId);
        Api.getMyReward(this, requestParams, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                LogUtils.d(data.toString());

            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });
    }

    private void initView() {
        leftIcon.setImageResource(R.drawable.btn_fanhui);
        title.setText(R.string.my_reward);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_my_reward;
    }
}
