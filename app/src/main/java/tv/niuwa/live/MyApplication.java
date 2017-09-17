package tv.niuwa.live;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tencent.bugly.crashreport.CrashReport;

import cn.leancloud.chatkit.LCChatKit;

import tv.niuwa.live.lean.CustomUserProvider;

/**
 * Created by Administrator on 2016/9/1.
 * Author: XuDeLong
 */
public class MyApplication extends Application {

    public LocationService locationService;
    private final String APP_ID = "vJwCUIAvzPlvT8JtKvjnRzYa-gzGzoHsz";
    private final String APP_KEY = "vxr7hKt603J9SQUMd3IpNHnt";
    String balance;
    private static Context mContext;

    public static Context getGlobalContext() {
        return mContext;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        MultiDex.install(this);
        //AVOSCloud.initialize(this,APP_ID,APP_KEY);
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        //AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(this));
        locationService = new LocationService(getApplicationContext());
//        SDKInitializer.initialize(getApplicationContext());
        CrashReport.initCrashReport(getApplicationContext(), "1ada2b4c05", true);
    }
}
