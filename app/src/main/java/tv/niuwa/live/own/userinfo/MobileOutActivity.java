package tv.niuwa.live.own.userinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.NumUtils;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiEditActivity;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.utils.TimeCountUtile;
import tv.niuwa.live.utils.Util;

public class MobileOutActivity extends BaseSiSiEditActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    @Bind(R.id.edit_input_old_tel)
    EditText mEditInputOldTel;
    @Bind(R.id.edit_input_new_tel)
    EditText mEditInputNewTel;
    @Bind(R.id.edit_input_code)
    EditText mEditInputCode;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        MobileOutActivity.this.finish();
    }

    @OnClick(R.id.btn_save)
    public void save(View view) {
        String oldTel = mEditInputOldTel.getText().toString();
        String newTel = mEditInputNewTel.getText().toString();
        String varcode = mEditInputCode.getText().toString();
        if (StringUtils.isEmpty(oldTel) || StringUtils.isEmpty(newTel) || StringUtils.isEmpty(varcode)) {
            toast("请将信息填写完整");
            return;
        }
        JSONObject params = new JSONObject();
        params.put("token", (String) SharePrefsUtils.get(this, "user", "token", ""));
        params.put("old_mobile_num", oldTel);
        params.put("mobile_num", newTel);
        params.put("varcode", varcode);
        Api.changeMobile(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                toast(data.getString("descrp"));
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    @OnClick(R.id.btn_get_code)
    public void getCode(final View view) {
        final Button btn = (Button) view;
        String mobileNum = mEditInputNewTel.getText().toString();
        if (mobileNum.trim() == "" || !Util.isPhoneNumber(mobileNum)) {
            toast("请输入正确的手机号");
            return;
        }

        JSONObject params = new JSONObject();
        params.put("mobile_num", mobileNum);
        params.put("status", "binding0");
        Api.getVarCode(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                toast(data.getString("descrp"));
                TimeCountUtile timer = new TimeCountUtile(MobileOutActivity.this, 60000, 1000, btn);
                timer.start();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("更换绑定");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_mobile_out;
    }
}
