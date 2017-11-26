package tv.niuwa.live.own;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.bumptech.glide.Glide;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.niuwa.live.MyApplication;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.home.model.VideoItem;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.living.LivingActivity;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.own.setting.SettingActivity;
import tv.niuwa.live.own.userinfo.MyDataActivity;
import tv.niuwa.live.own.userinfo.UserMenuItem;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.view.SFProgrssDialog;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 01/09/2017 16:10.
 * <p>
 * All copyright reserved.
 */

public class CommunityActivity extends BaseSiSiActivity {

    @Bind(R.id.text_top_title)
    TextView title;

    @Bind(R.id.image_top_back)
    ImageView leftIcon;


    @Bind(R.id.webview)
    WebView mWebView;


    private SFProgrssDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_community;
    }

    String token;
    String userId;

    private void initView() {

        token = (String) SharePrefsUtils.get(CommunityActivity.this, "user", "token", "");
        userId = (String) SharePrefsUtils.get(CommunityActivity.this, "user", "userId", "");

        title.setText(R.string.community);

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialog = SFProgrssDialog.show(this, "请稍后...");
        mWebView.loadUrl("http://zhibo.519wan.com/h5/book.html");
        mWebView.loadUrl("javascript:NativeCallH5('" + userId + "')");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebClient());

    }

    public class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dialog.dismiss();
        }
    }



    private void initData() {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
