package tv.niuwa.live.login;

import android.content.Context;
import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.NumUtils;
import com.smart.androidutils.utils.SharePrefsUtils;
import tv.niuwa.live.MainActivity;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiEditActivity;
import tv.niuwa.live.event.BroadCastEvent;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.utils.Api;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class LoginByPhoneActivity extends BaseSiSiEditActivity {


    @Bind(R.id.edit_input_mobile)
    EditText mEditInputMobile;
    @Bind(R.id.edit_input_password)
    EditText mEditInputPassword;

    @OnClick(R.id.image_back)
    public void back() {
        finish();
    }

    boolean isShowPwd = false;


    @Bind(R.id.iv_showpwd)
    ImageView iv_showpwd;


    @OnClick(R.id.iv_showpwd)
    public void showOrHidePwd() {

        if (isShowPwd) {
            //隐藏
            isShowPwd = false;
            iv_showpwd.setImageResource(R.drawable.zhibo_cancel_n);
            mEditInputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            //显示
            isShowPwd = true;
            iv_showpwd.setImageResource(R.drawable.zhibo_cancel_s);
            mEditInputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }

    }

    @OnClick(R.id.tv_forgetpwd)
    public void forgetPassword(View view) {
        openActivity(ForgetPasswordActivity.class);
    }


    @OnClick(R.id.tv_loginbyphone_register)
    public void jumpToRegister() {
        RegisterActivity.newInstance(this);
    }

    @OnClick(R.id.btn_login)
    public void doLogin(View view) {
        String mobile = mEditInputMobile.getText().toString();
        String password = mEditInputPassword.getText().toString();
        if ("".equals(mobile.trim()) || !NumUtils.isPhoneNumber(mobile)) {
            toast("请正确填写手机号");
            return;
        }
        if ("".equals(password.trim())) {
            toast("请输入密码");
            return;
        }
        JSONObject params = new JSONObject();
        params.put("mobile_num", mobile);
        params.put("password", password);
        Api.doLogin(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                String token = data.getString("token");
                JSONArray userInfo = data.getJSONArray("info");
                if (token != null) {
                    SharePrefsUtils.put(LoginByPhoneActivity.this, "user", "token", token);
                    if (userInfo.get(0) != null) {
                        JSONObject obj = (JSONObject) userInfo.getJSONObject(0);
                        String userId = obj.getString("id");
                        SharePrefsUtils.put(LoginByPhoneActivity.this, "user", "userId", userId);
                    }
                    toast("登录成功");
                    openActivity(MainActivity.class);
                    LoginByPhoneActivity.this.finish();
                    // 发送通知
                    EventBus.getDefault().post(new BroadCastEvent(LoginActivity.FINISH_EVENT));
                } else {
                    toast("token获取失败");
                }

            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }


    public static void newInstance(Context context) {
        context.startActivity(new Intent(context, LoginByPhoneActivity.class));
    }


    @Override
    public int getLayoutResource() {
        return R.layout.activity_login_by_phone;
    }
}
