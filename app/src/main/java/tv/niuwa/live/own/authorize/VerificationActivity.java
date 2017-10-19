package tv.niuwa.live.own.authorize;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.LogUtils;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.own.userinfo.MyDataActivity;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.view.SFProgrssDialog;

public class VerificationActivity extends BaseSiSiActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    private SFProgrssDialog dialog;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    private final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

    private String token;
    private String userId;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        VerificationActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("实名认证");
        LogUtils.i("token=" + token);
        token = (String) SharePrefsUtils.get(this, "user", "token", "");
        userId = (String) SharePrefsUtils.get(this, "user", "userId", "");
//        dialog = SFProgrssDialog.show(this, "请稍后...");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_wanju01);
        uploadPic(bitmap,bitmap,bitmap);
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "选择证件照"), FILECHOOSER_RESULTCODE);
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "选择证件照");
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_authorize;
    }


    private void uploadPic(Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3) {
        // 上传至服务器
        final File temp = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis() + ".jpg"));
        if (!temp.exists()) {
            try {
                temp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(temp));
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SFProgrssDialog dialog = SFProgrssDialog.show(this, "请稍后...");
        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("uid", userId);
        try {
            params.put("photo1", temp);
            params.put("photo2", temp);
            params.put("photo3", temp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LogUtils.d("zzk", params.toString());
        Api.excuteUpload(Api.SET_USERDATA, this, params, dialog, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

}
