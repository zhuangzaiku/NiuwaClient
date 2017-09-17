package tv.niuwa.live.login;

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
import tv.niuwa.live.utils.Api;

public class ModifyPasswordActivity extends BaseSiSiEditActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    @Bind(R.id.edit_input_old_password)
    EditText mEditInputOldPassword;
    @Bind(R.id.edit_input_new_password)
    EditText mEditInputNewPassword;
    @Bind(R.id.edit_input_repassword)
    EditText mEditInputRepassword;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        ModifyPasswordActivity.this.finish();
    }

    @OnClick(R.id.btn_save)
    public void save(View view) {
        String oldPassword = mEditInputOldPassword.getText().toString();
        String newPassword = mEditInputNewPassword.getText().toString();
        String rePassword = mEditInputRepassword.getText().toString();
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(rePassword)) {
            toast("请将信息填写完整");
            return;
        }
        if (newPassword.length() < 6) {
            toast("密码需大于六位");
            return;
        }
        if (newPassword.length() > 20) {
            toast("密码长度应小于20位");
            return;
        }
        if (newPassword.equals(oldPassword)) {
            toast("新密码与老密码相同");
            return;
        }
        if (!newPassword.equals(rePassword)) {
            toast("重复密码不一样");
            return;
        }
        JSONObject params = new JSONObject();
        params.put("token", (String) SharePrefsUtils.get(this, "user", "token", ""));
        params.put("oldpassword", oldPassword);
        params.put("password", newPassword);
        params.put("repassword", rePassword);
        Api.changePassword(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                toast(data.getString("descrp"));
                SharePrefsUtils.clear(ModifyPasswordActivity.this, "user");
                openActivity(LoginActivity.class);
                finish();
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
        mTextTopTitle.setText("修改密码");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_modify_password;
    }
}
