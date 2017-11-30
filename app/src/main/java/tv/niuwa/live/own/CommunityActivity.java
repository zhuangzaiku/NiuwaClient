package tv.niuwa.live.own;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.androidutils.utils.SharePrefsUtils;

import butterknife.Bind;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
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

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("http://zhibo.519wan.com/h5/book.html");

    }

    public class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mWebView.loadUrl("javascript:NativeCallH5('" + userId + "')");
            dialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
