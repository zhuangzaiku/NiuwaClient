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
import com.smart.androidutils.utils.SharePrefsUtils;

import butterknife.Bind;
import butterknife.OnClick;

import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiEditActivity;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.utils.TimeCountUtile;

public class RegisterActivity extends BaseSiSiEditActivity {

    //    @Bind(R.id.text_top_title)
//    TextView mTextTopTitle;
    @Bind(R.id.edit_input_mobile)
    EditText mInputMobile;
    @Bind(R.id.edit_input_password)
    EditText mInputPassword;
    @Bind(R.id.edit_input_code)
    EditText mInputCode;


    @Bind(R.id.iv_showpwd)
    ImageView iv_showpwd;
    boolean isPwdShown=false;

    @OnClick(R.id.iv_showpwd)
    public void showPwd() {
        if(isPwdShown){
            isPwdShown=false;
            iv_showpwd.setImageResource(R.drawable.zhibo_cancel_n);
            mInputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }else{
            isPwdShown=true;
            iv_showpwd.setImageResource(R.drawable.zhibo_cancel_s);
            mInputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        }
    }

    @OnClick(R.id.image_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_register_code)
    public void getCode(final View view) {
        final Button btn = (Button) view;
        String mobileNum = mInputMobile.getText().toString();
        if (mobileNum.trim() == "" || !NumUtils.isPhoneNumber(mobileNum)) {
            toast("请输入正确的手机号");
            return;
        }

        JSONObject params = new JSONObject();
        params.put("mobile_num", mobileNum);
        params.put("status", "register0");
        Api.getVarCode(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                toast(data.getString("descrp"));
                TimeCountUtile timer = new TimeCountUtile(RegisterActivity.this, 60000, 1000, btn);
                timer.start();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    @OnClick(R.id.btn_register)
    public void register(View view) {
        String mobileNum = mInputMobile.getText().toString();
        if (mobileNum.trim() == "" || !NumUtils.isPhoneNumber(mobileNum)) {
            toast("请输入正确的手机号");
            return;
        }
        String password = mInputPassword.getText().toString();
        String code = mInputCode.getText().toString();
        if (password.trim().length() == 0) {
            toast("请输入密码");
            return;
        }
        if (code.trim().length() == 0) {
            toast("请输入验证码");
            return;
        }
        JSONObject params = new JSONObject();
        params.put("mobile_num", mobileNum);
        //params.put("user_nicename", mobileNum);
        params.put("password", password);
        params.put("repassword", password);
        params.put("varcode", code);
        Api.doRegister(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                String token = data.getString("token");
                if (token != null) {
                    toast(data.getString("descrp"));
                    SharePrefsUtils.put(RegisterActivity.this, "user", "token", token);
                    openActivity(LoginActivity.class);
                    RegisterActivity.this.finish();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

//    @OnClick(R.id.image_top_back)
//    public void back(View view) {
//        RegisterActivity.this.finish();
//    }


    public static void newInstance(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mTextTopTitle.setText("注册");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_register;
    }


}
