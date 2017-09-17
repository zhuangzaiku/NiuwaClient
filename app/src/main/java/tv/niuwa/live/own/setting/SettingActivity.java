package tv.niuwa.live.own.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.login.ModifyPasswordActivity;
import tv.niuwa.live.utils.Api;

public class SettingActivity extends BaseSiSiActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        SettingActivity.this.finish();
    }

    @OnClick(R.id.linear_about_container)
    public void about(View view) {
        openActivity(AboutActivity.class);
    }

    @OnClick(R.id.linear_feedback_container)
    public void feedback(View view) {
        openActivity(FeedbackActivity.class);
    }

    @OnClick(R.id.linear_modify_password_container)
    public void modifyPassword(View view) {
        openActivity(ModifyPasswordActivity.class);
    }

    @OnClick(R.id.linear_version_container)
    public void checkUpdate(){
        JSONObject params = new JSONObject();
        try {
            String versionCode = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            params.put("ver_num",versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Api.checkUpdate(this,params , new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONObject info = data.getJSONObject("data");
                if(StringUtils.isNotEmpty(info.getString("package"))){
                    checkUpgrade(info.getString("package"),info.getString("description"));
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }
    AlertDialog tipsAlertDialog;
    private void checkUpgrade(final String downloadUrl,String mes) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        tipsAlertDialog = builder.setTitle("提示")
                .setMessage(mes)
                .setNegativeButton("再等一下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (tipsAlertDialog.isShowing()) {
                            tipsAlertDialog.dismiss();
                        }
                    }
                })
                .setPositiveButton("更新下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri uri = Uri.parse(downloadUrl);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .create();
        tipsAlertDialog.show();
        tipsAlertDialog.setCancelable(false);
        tipsAlertDialog.setCanceledOnTouchOutside(false);
    }

    @OnClick(R.id.btn_sign_out)
    public void signOut(View view) {
        SharePrefsUtils.clear(this, "user");
//        openActivity(LoginActivity.class);
//        SettingActivity.this.finish();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefsUtils.get(this, "user", "token", "").equals("")) {
            openActivity(LoginActivity.class);
            SettingActivity.this.finish();
        }
        mTextTopTitle.setText("系统设置");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_setting;
    }
}
