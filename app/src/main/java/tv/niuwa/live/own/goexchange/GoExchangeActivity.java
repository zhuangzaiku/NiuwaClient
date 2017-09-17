package tv.niuwa.live.own.goexchange;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.smart.androidutils.utils.SharePrefsUtils;

import butterknife.Bind;
import butterknife.OnClick;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.view.SFProgrssDialog;

public class GoExchangeActivity extends BaseSiSiActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    @Bind(R.id.exchange_webview)
    WebView mExchangeWebView;
    private SFProgrssDialog dialog;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        GoExchangeActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("兑换");
        String token = (String) SharePrefsUtils.get(this, "user", "token", "");
        mExchangeWebView.loadUrl(Api.WEB_EXCHANGE + "?token=" + token);
        dialog = SFProgrssDialog.show(this, "请稍后...");
        WebSettings webSettings = mExchangeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mExchangeWebView.setWebViewClient(new myWebClient());

    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_go_exchange;
    }

    public class myWebClient extends WebViewClient {
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
}
