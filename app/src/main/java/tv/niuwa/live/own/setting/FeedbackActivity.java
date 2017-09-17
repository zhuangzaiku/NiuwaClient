package tv.niuwa.live.own.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiEditActivity;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.utils.Api;

public class FeedbackActivity extends BaseSiSiEditActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    @Bind(R.id.edit_input_advice)
    EditText mEditInputAdvice;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        FeedbackActivity.this.finish();
    }

    @OnClick(R.id.btn_submit)
    public void submit(View view) {
        String advice = mEditInputAdvice.getText().toString();
        if (StringUtils.isEmpty(advice)) {
            toast("请填写您的意见");
            return;
        }
        JSONObject params = new JSONObject();
        String token = (String) SharePrefsUtils.get(this, "user", "token", "");
        if (StringUtils.isNotEmpty(token)) {
            params.put("token", token);
            params.put("content", advice);
            Api.feedBack(this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    toast(data.getString("descrp"));
                    finish();
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        } else {
            openActivity(LoginActivity.class);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("意见反馈");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_feedback;
    }
}
