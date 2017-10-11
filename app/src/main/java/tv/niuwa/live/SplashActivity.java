package tv.niuwa.live;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.smart.androidutils.activity.BaseSplashActivity;
import com.smart.androidutils.utils.SharePrefsUtils;

import butterknife.Bind;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.own.UserCenterActivity;
import tv.niuwa.live.own.UserMainActivity;
import tv.niuwa.live.own.WebviewActivity;
import tv.niuwa.live.own.userinfo.MyDataActivity;
import tv.niuwa.live.utils.Api;

public class SplashActivity extends BaseSplashActivity {

    private boolean destroy = false;
    boolean firstOpen;
    @Bind(R.id.lauch_screen)
    ImageView mLauchScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstOpen();
        if(!firstOpen){
            Api.getLaunchScreen(this, new JSONObject(), new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, final JSONObject data) {
                    Glide.with(SplashActivity.this).load(data.getString("info")).into(mLauchScreen);
                    mLauchScreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(null != data.getString("url") && null != data.getString("title")){
                                Bundle temp = new Bundle();
                                temp.putString("title",data.getString("title"));
                                temp.putString("jump",data.getString("url"));
                                openActivity(WebviewActivity.class,temp);
                                SplashActivity.this.finish();
                            }
                        }
                    });
                }

                @Override
                public void requestFailure(int code, String msg) {

                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!destroy) {
                        openActivity(MainActivity.class);
                        SplashActivity.this.finish();
                    }
                }
            }, 400);// TODO: 28/09/2017  
        }


    }
    private void isFirstOpen(){
        firstOpen = (boolean) SharePrefsUtils.get(SplashActivity.this, "system", "isFirstOpen", true);
        if (firstOpen){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,FirstOpenActivity.class));
                    finish();
                    SharePrefsUtils.put(SplashActivity.this, "system", "isFirstOpen", false);
                }
            },3000);
        }
    }
    @Override
    public int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        destroy = true;
        super.onDestroy();
    }
}
