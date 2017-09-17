package tv.niuwa.live.own;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import tv.niuwa.live.MainActivity;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.home.model.VideoItem;
import tv.niuwa.live.living.LivingActivity;
import tv.niuwa.live.view.SFProgrssDialog;

public class PushActivity extends BaseSiSiActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    @Bind(R.id.exchange_webview)
    WebView mExchangeWebView;
    private SFProgrssDialog dialog;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        openActivity(MainActivity.class);
        PushActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Bundle data = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        try {
            JSONObject json = JSON.parseObject(getIntent().getExtras().getString("com.avos.avoscloud.Data"));
            switch (json.getString("type")){
                case "LIVE":
                    Bundle data = new Bundle();
                    VideoItem temp = new VideoItem();
                    temp.setRoom_id(json.getString("roomId"));
                    data.putSerializable("videoItem", temp);
                    openActivity(LivingActivity.class, data);
                    break;
                case "URL":
                    Bundle data1 = new Bundle();
                    data1.putString("title",json.getString("alert"));
                    data1.putString("jump",json.getString("url"));
                    openActivity(WebviewActivity.class,data1);
                    finish();
                    break;
                default:
                    Bundle data2 = new Bundle();
                    data2.putString("title","");
                    data2.putString("jump","");
                    openActivity(WebviewActivity.class,data2);
                    finish();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_webview;
    }

}
