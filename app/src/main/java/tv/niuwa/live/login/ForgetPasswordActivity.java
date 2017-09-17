package tv.niuwa.live.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.NumUtils;
import com.smart.androidutils.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;

import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiEditActivity;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.utils.TimeCountUtile;

public class ForgetPasswordActivity extends BaseSiSiEditActivity {

//    @Bind(R.id.text_top_title)
//    TextView mTextTopTitle;

    @Bind(R.id.edit_input_mobile)
    EditText mInputMobile;

    @Bind(R.id.edit_input_new_password)
    EditText mInputNewPassword;

    @Bind(R.id.edit_input_code)
    EditText mInputCode;


    @Bind(R.id.iv_showpwd)
    ImageView iv_showpwd;
    boolean isShowPwd;

    @OnClick(R.id.iv_showpwd)
    public void showOrHidePwd() {

        if (isShowPwd) {
            //隐藏
            isShowPwd = false;
            iv_showpwd.setImageResource(R.drawable.zhibo_cancel_n);
            mInputNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            //显示
            isShowPwd = true;
            iv_showpwd.setImageResource(R.drawable.zhibo_cancel_s);
            mInputNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }

    }


    @OnClick(R.id.image_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_get_code)
    public void getCode(final View view) {
        String mobileNum = mInputMobile.getText().toString();
        if (mobileNum.trim() == "" || !NumUtils.isPhoneNumber(mobileNum)) {
            toast("请输入正确的手机号");
            return;
        }

        JSONObject params = new JSONObject();
        params.put("mobile_num", mobileNum);
        params.put("status", "forgetPassword0");
        Api.getVarCode(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                toast(data.getString("descrp"));
                Button btn = (Button) view;
                TimeCountUtile timer = new TimeCountUtile(ForgetPasswordActivity.this, 60000, 1000, btn);
                timer.start();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    @OnClick(R.id.btn_edit_password)
    public void editPassword(View view) {

        String mobileNum = mInputMobile.getText().toString();
        if (mobileNum.trim() == "" || !NumUtils.isPhoneNumber(mobileNum)) {
            toast("请输入正确的手机号");
            return;
        }
        String password = mInputNewPassword.getText().toString();
        String code = mInputCode.getText().toString();
        if (StringUtils.isEmpty(password)) {
            toast("请输入密码");
            return;
        }
        if (StringUtils.isEmpty(code)) {
            toast("请输入验证码");
            return;
        }
        JSONObject params = new JSONObject();
        params.put("mobile_num", mobileNum);
        params.put("repassword", password);
        params.put("password", password);
        params.put("varcode", code);
        Api.forgetPassword(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                String token = data.getString("token");
                if (token != null) {
                    toast(data.getString("descrp"));
                    openActivity(LoginActivity.class);
                    ForgetPasswordActivity.this.finish();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });

    }


    public static void newInstance(Context context) {
        context.startActivity(new Intent(context, ForgetPasswordActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_forget_password;
    }
}
