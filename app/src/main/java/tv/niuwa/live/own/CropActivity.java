package tv.niuwa.live.own;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.own.userinfo.MyDataActivity;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by FJC on 2016/12/18.
 */
public class CropActivity extends BaseSiSiActivity implements View.OnClickListener {


    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "face.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int SELECT_PIC_KITKAT = 3;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    @Bind(R.id.btn_done)
    Button btn_done;
    //@Bind(R.id.btn_select_pic)
   // Button btn_selectpic;
    @Bind(R.id.cropview)
    CropImageView cropview;
    private Intent intent;

    @OnClick(R.id.image_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_done)
    public void done() {
        cropview.startCrop(createSaveUri(), new CropCallback() {
            @Override
            public void onSuccess(Bitmap cropped) {

                Log.v("info","CropCallback,onSuccess,"+(cropped!=null));

            }

            @Override
            public void onError() {

            }
        }, new SaveCallback() {
            @Override
            public void onSuccess(Uri outputUri) {
                Log.v("info","SaveCallback,onSuccess,"+outputUri);
                Bundle bundle = new Bundle();
                bundle.putParcelable("uri", outputUri);
                intent.putExtras(bundle);
                finish();
            }

            @Override
            public void onError() {

            }
        });
    }

    AlertDialog dialog;

//    @OnClick(R.id.btn_select_pic)
//    public void showSettingFaceDialog() {
//
//        dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyAlertDialog)).create();
//        LinearLayout inflate = (LinearLayout) getLayoutInflater().inflate(R.layout.pic_dialog, null);
//        TextView temp1 = (TextView) inflate.findViewById(R.id.choose_pic);
//        TextView temp2 = (TextView) inflate.findViewById(R.id.choose_camera);
//        temp1.setOnClickListener(this);
//        temp2.setOnClickListener(this);
//        dialog.setView(inflate);
//        Window window = dialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        dialog.show();
//
//        WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.width = (int) (display.getWidth()); //设置宽度
//        dialog.getWindow().setAttributes(lp);
//    }

    public static void newInstanceForResult(Context context,Uri uri, int code) {
        Intent intent=new Intent(context, CropActivity.class);
        intent.setData(uri);
        ((Activity) context).startActivityForResult(intent, code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        cropview.startLoad(getIntent().getData(),mLoadCallback);
        cropview.setCropMode(CropImageView.CropMode.SQUARE);
        intent = getIntent();
        setResult(MyDataActivity.RESULT_REQUEST_CODE, intent);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_crop;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_pic:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    startActivityForResult(intent, SELECT_PIC_KITKAT);
                } else {
                    startActivityForResult(intent, IMAGE_REQUEST_CODE);
                }
                dialog.dismiss();
                break;
            case R.id.choose_camera:
                Intent intentFromCapture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                if (hasSdcard()) {
                    intentFromCapture.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment
                                    .getExternalStorageDirectory(),
                                    IMAGE_FILE_NAME)));
                }
                startActivityForResult(intentFromCapture,
                        CAMERA_REQUEST_CODE);
                dialog.dismiss();
                break;
        }
    }

    private boolean hasSdcard() {
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    cropview.startLoad(data.getData(), mLoadCallback);
//                    startPhotoZoom(data.getData());
                    break;
                case SELECT_PIC_KITKAT:
                    cropview.startLoad(data.getData(), mLoadCallback);
//                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (hasSdcard()) {
                        File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                        cropview.startLoad(Uri.fromFile(tempFile), mLoadCallback);
//                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        toast("未找到存储卡，无法存储照片！");
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
//                        setImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
        }

        @Override
        public void onError() {
        }
    };

    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
        }

        @Override
        public void onError() {

        }
    };


    public Uri createSaveUri() {
        return Uri.fromFile(new File(getCacheDir(), "cropped"));
    }


}
