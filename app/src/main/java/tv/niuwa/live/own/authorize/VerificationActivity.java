package tv.niuwa.live.own.authorize;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.isseiaoki.simplecropview.util.Utils;
import com.loopj.android.http.RequestParams;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.LogUtils;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.OnClick;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.own.userinfo.MyDataActivity;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.view.SFProgrssDialog;

public class VerificationActivity extends BaseSiSiActivity{

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    private SFProgrssDialog mProgressDialog;
    private final static int TYPE_POSITIVE = 1;
    private final static int TYPE_NEGATIVE = 2;
    private final static int TYPE_POSITIVE_HOLDING = 3;

    private static final int IMAGE_REQUEST_CODE = 4;
    private static final int SELECT_PIC_KITKAT = 7;
    private static final int CAMERA_REQUEST_CODE = 5;
    public static final int RESULT_REQUEST_CODE = 6;

    private int mCurrentChosen = 0;
    private String mCurrentFile;

    private static final String FILE_NAME_POSITIVE = "photo1.jpg";
    private static final String FILE_NEGATIVE = "photo2.jpg";
    private static final String FILE_POSITIVE_HOLDING = "photo3.jpg";

    private File mTemp1,mTemp2,mTemp3;

    private String token;
    private String userId;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        VerificationActivity.this.finish();
    }

    @OnClick(R.id.positiveButton)
    public void uploadPositive(View view) {
        mCurrentChosen = TYPE_POSITIVE;
        mCurrentFile = FILE_NAME_POSITIVE;
        showSettingFaceDialog();
    }

    @OnClick(R.id.negativeButton)
    public void uploadNegative(View view) {
        mCurrentChosen = TYPE_NEGATIVE;
        mCurrentFile = FILE_NEGATIVE;
        showSettingFaceDialog();
    }

    @OnClick(R.id.positiveHoldingButton)
    public void uploadPositiveHolding(View view) {
        mCurrentChosen = TYPE_POSITIVE_HOLDING;
        mCurrentFile = FILE_POSITIVE_HOLDING;
        showSettingFaceDialog();
    }

    @OnClick(R.id.commit)
    public void commit(View view) {
        if(mTemp1 == null || mTemp2 == null || mTemp3 == null) {
            Toast.makeText(this,R.string.upload_choose_tip, Toast.LENGTH_SHORT).show();
            return;
        }
        LogUtils.d(mTemp1.getAbsolutePath() + "\n" + mTemp2.getAbsolutePath() + "\n" + mTemp3.getAbsolutePath());
        uploadPic(mTemp1, mTemp2, mTemp3);

    }

    @Bind(R.id.photo1)
    ImageView mPhoto1;

    @Bind(R.id.photo2)
    ImageView mPhoto2;

    @Bind(R.id.photo3)
    ImageView mPhoto3;

    @Bind(R.id.positiveFilled)
    ImageView mPositiveFilled;

    @Bind(R.id.negativeFilled)
    ImageView mNegativeFilled;

    @Bind(R.id.positiveHoldingFilled)
    ImageView mPositiveHoldingFilled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("实名认证");
        LogUtils.i("token=" + token);
        token = (String) SharePrefsUtils.get(this, "user", "token", "");
        userId = (String) SharePrefsUtils.get(this, "user", "userId", "");
    }

    AlertDialog mPickDialog;
    private void showSettingFaceDialog() {
        mPickDialog = new AlertDialog.Builder(this, R.style.MyAlertDialog).create();
        LinearLayout inflate = (LinearLayout)getLayoutInflater().inflate(R.layout.pic_dialog,null);
        TextView temp1 = (TextView)inflate.findViewById(R.id.choose_pic);
        TextView temp2 = (TextView)inflate.findViewById(R.id.choose_camera);
        temp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    startActivityForResult(intent,SELECT_PIC_KITKAT);
                } else {
                    startActivityForResult(intent,IMAGE_REQUEST_CODE);
                }
                mPickDialog.dismiss();
            }
        });
        temp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromCapture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                if (hasSdcard()) {
                    intentFromCapture.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment
                                    .getExternalStorageDirectory(),
                                    mCurrentFile)));
                }
                startActivityForResult(intentFromCapture,
                        CAMERA_REQUEST_CODE);
                mPickDialog.dismiss();
            }
        });
        mPickDialog.setView(inflate);
        Window window = mPickDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        mPickDialog.show();

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mPickDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()); //设置宽度
        mPickDialog.getWindow().setAttributes(lp);

    }

    private boolean hasSdcard() {
        return Environment.isExternalStorageEmulated();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    setImageToView(data.getData());
                    break;
                case SELECT_PIC_KITKAT:
                    setImageToView(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (hasSdcard()) {
                        File tempFile = new File(Environment.getExternalStorageDirectory(),mCurrentFile);
                        setImageToView(Uri.fromFile(tempFile));
                    } else {
                        toast("未找到存储卡，无法存储照片！");
                    }

                    break;
                default:
                    break;

            }


        }
    }

    protected void setImageToView(Uri uri) {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = sdcard + File.separator +  mCurrentFile;
        ImageView tmp = null;
        File tmpFile = new File(path);

        InputStream is = null;
        try {
            // check image size
            is = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = Utils.calculateInSampleSize(this, uri, 1024);
            options.inJustDecodeBounds = false;

            if(tmpFile.exists()) {
                tmpFile.delete();
            }
            tmpFile.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile));
            LogUtils.d("size" + is.available());
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            LogUtils.d("new file " + mCurrentFile + tmpFile.canRead());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (mCurrentChosen) {
            case TYPE_POSITIVE:
                mTemp1 = tmpFile;
                tmp = mPhoto1;
                mPositiveFilled.setVisibility(View.VISIBLE);
                break;
            case TYPE_NEGATIVE:
                mTemp2 = tmpFile;
                tmp = mPhoto2;
                mNegativeFilled.setVisibility(View.VISIBLE);
                break;
            case TYPE_POSITIVE_HOLDING:
                mTemp3 = tmpFile;
                tmp = mPhoto3;
                mPositiveHoldingFilled.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        Glide.with(VerificationActivity.this).load(uri).error(R.drawable.icon_avatar_default).into(tmp);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_verification;
    }


    private void uploadPic(File bitmap1, File bitmap2, File bitmap3) {
        SFProgrssDialog dialog = SFProgrssDialog.show(this, "请稍后...");
        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("uid", userId);
        try {
            params.put("photo1", bitmap1);
            params.put("photo2", bitmap2);
            params.put("photo3", bitmap3);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LogUtils.d("zzk", params.toString());
        Api.excuteUpload(Api.CERT_UPLOAD, this, params, dialog, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                Toast.makeText(VerificationActivity.this,data.getString("descrp"), Toast.LENGTH_SHORT).show();
                clear();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    private void clear() {
        mTemp1 = null;
        mTemp2 = null;
        mTemp3 = null;
        mPhoto1.setImageDrawable(null);
        mPhoto2.setImageDrawable(null);
        mPhoto3.setImageDrawable(null);
        mPositiveFilled.setVisibility(View.GONE);
        mNegativeFilled.setVisibility(View.GONE);
        mPositiveHoldingFilled.setVisibility(View.GONE);
    }

}
