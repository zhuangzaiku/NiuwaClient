package tv.niuwa.live.own.userinfo;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.isseiaoki.simplecropview.util.Utils;
import com.loopj.android.http.RequestParams;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.DensityUtils;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

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
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.own.CropActivity;
import tv.niuwa.live.own.authorize.AuthorizeActivity;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.view.SFProgrssDialog;

public class MyDataActivity extends BaseSiSiActivity implements View.OnClickListener{

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    @Bind(R.id.frame_user_avatar_container)
    FrameLayout mFrameUserAvatarContainer;
    @Bind(R.id.image_user_avatar)
    ImageView mImageUserAvatar;
    @Bind(R.id.text_user_nick)
    TextView mTextUserNick;
    @Bind(R.id.text_user_signature)
    TextView mTextUserSingature;
    @Bind(R.id.radio_male)
    CheckBox mRadioMale;
    @Bind(R.id.radio_female)
    CheckBox mRadioFemale;
    @Bind(R.id.text_userdata_real)
    TextView mTextUserdataReal;
    @Bind(R.id.mobile_status)
    TextView mMobileStatus;
    @Bind(R.id.text_user_id)
    TextView mTextUserId;
    private int mAvatarContainerWidth;
    private int mAvatarContainerHeight;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;

    protected static Uri tempUri;
    String mobileStatus = "0";
    String token;
    String userId;
    @OnClick(R.id.image_top_back)
    public void back(View view) {
        setResult(RESULT_OK);
        MyDataActivity.this.finish();
    }

//    @OnClick(R.id.image_user_avatar)
//    public void avatar(View view) {
//        showChoosePicDialog();
//        showSettingFaceDialog();
//    }

    @OnClick(R.id.mydata_user_avatar)
    public void mydataUserAvatar(){
        showSettingFaceDialog();
    }

    @OnClick(R.id.linear_user_real_container)
    public void linearUserRealContainer(View view) {
        openActivity(AuthorizeActivity.class);
    }


    @OnClick(R.id.linear_user_nick_container)
    public void userNick(View view) {

    }

    @OnClick(R.id.linear_user_sex_container)
    public void userSex(View view) {

    }

    @OnClick(R.id.linear_user_email_container)
    public void userSignature(View view) {

    }

    @OnClick(R.id.radio_female)
    public void changeSex() {
        mRadioFemale.setChecked(true);
        mRadioMale.setChecked(false);
    }

    @OnClick(R.id.radio_male)
    public void changeSex1() {
        mRadioFemale.setChecked(false);
        mRadioMale.setChecked(true);
    }

    @OnClick(R.id.btn_save_and_back)
    public void saveAndBack(View view) {
        String avatar = "";
        String userNiceName = mTextUserNick.getText().toString();
        String signature = mTextUserSingature.getText().toString();
        String sex = mRadioFemale.isChecked() ? "2" : "1";
        JSONObject params = new JSONObject();
        params.put("token", (String) SharePrefsUtils.get(this, "user", "token", ""));
        params.put("avatar", avatar);
        params.put("user_nicename", userNiceName);
        params.put("sex", sex);
        params.put("signature", signature);
        Api.setUserData(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                toast(data.getString("descrp"));
                setResult(RESULT_OK);
                MyDataActivity.this.finish();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    @OnClick(R.id.linear_user_tel)
    public void linearUserTel(View v){
        if("0".equals(mobileStatus)){
            openActivity(MobileInActivity.class);
        }else{
            openActivity(MobileOutActivity.class);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("个人资料");
        mAvatarContainerWidth = DensityUtils.screenWidth(this);
        mAvatarContainerHeight = (mAvatarContainerWidth * 330) / 750;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mAvatarContainerWidth, mAvatarContainerHeight);
        mFrameUserAvatarContainer.setLayoutParams(params);
        initData();


    }



    private void initData() {
        token = (String) SharePrefsUtils.get(this, "user", "token", "");
        userId = (String) SharePrefsUtils.get(this, "user", "userId", "");
        if (!StringUtils.isEmpty(token)) {
            JSONObject params = new JSONObject();
            params.put("token", token);
            params.put("id",userId);
            Api.getUserInfo(this, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    JSONObject userInfo = data.getJSONObject("data");
                    Glide.with(MyDataActivity.this).load(userInfo.getString("avatar"))
                            .error(R.drawable.icon_avatar_default)
                            .transform(new GlideCircleTransform(MyDataActivity.this))
                            .into(mImageUserAvatar);
                    mTextUserNick.setText(userInfo.getString("user_nicename"));
                    mTextUserId.setText(userInfo.getString("id"));
                    if ("1".equals(userInfo.getString("sex"))) {
                        mRadioFemale.setChecked(false);
                        mRadioMale.setChecked(true);
                    }
                    if ("1".equals(userInfo.getString("is_truename"))) {
                        mTextUserdataReal.setText("已认证");
                    }
                    mobileStatus = userInfo.getString("mobile_status");
                    if("0".equals(mobileStatus)){
                        mMobileStatus.setText("未绑定");
                    }
                    mTextUserSingature.setText(userInfo.getString("signature"));

                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        } else {
            openActivity(LoginActivity.class);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_my_data;
    }

    /**
     * 显示修改头像的对话框
     */
//    protected void showChoosePicDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        //builder.setTitle("设置头像");
//        String[] items = {"选择照片", "拍摄照片"};
//        builder.setNegativeButton("取消", null);
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case CHOOSE_PICTURE: // 选择本地照片
//                        Intent openAlbumIntent = new Intent(
//                                Intent.ACTION_GET_CONTENT);
//                        openAlbumIntent.setType("image/*");
//                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
//                        break;
//                    case TAKE_PICTURE: // 拍照
//                        Intent openCameraIntent = new Intent(
//                                MediaStore.ACTION_IMAGE_CAPTURE);
//                        tempUri = Uri.fromFile(new File(Environment
//                                .getExternalStorageDirectory(), "image.jpg"));
//                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
//                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
//                        break;
//                }
//            }
//        });
//        builder.create().show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
//            switch (requestCode) {
//                case TAKE_PICTURE:
//                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
//                    break;
//                case CHOOSE_PICTURE:
//                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
//                    break;
//                case CROP_SMALL_PICTURE:
//                    if (data != null) {
//                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
//                    }
//                    break;
//            }
//        }
//    }
//
//    /**
//     * 裁剪图片方法实现
//     *
//     * @param uri
//     */
//    protected void startPhotoZoom(Uri uri) {
//        if (uri == null) {
//            LogUtils.i("tag", "The uri is not exist.");
//        }
//        tempUri = uri;
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // 设置裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, CROP_SMALL_PICTURE);
//    }
//
//    /**
//     * 保存裁剪之后的图片数据
//     *
//     * @param
//     * @param
//     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            uploadPic(photo);

            Uri uri = extras.getParcelable("uri");
            InputStream is = null;
            try {
                is = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = Utils.calculateInSampleSize(this, uri, 1024);
            options.inJustDecodeBounds = false;
            uploadPic(BitmapFactory.decodeStream(is, null, options));
        }
    }
    private String[] items = new String[] { "图库","拍照" };
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "face.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int SELECT_PIC_KITKAT = 3;
    private static final int CAMERA_REQUEST_CODE = 1;
    public static final int RESULT_REQUEST_CODE = 2;
    AlertDialog dialog;
    private void showSettingFaceDialog() {

        dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyAlertDialog)).create();
        LinearLayout inflate = (LinearLayout)getLayoutInflater().inflate(R.layout.pic_dialog,null);
        TextView temp1 = (TextView)inflate.findViewById(R.id.choose_pic);
        TextView temp2 = (TextView)inflate.findViewById(R.id.choose_camera);
        temp1.setOnClickListener(this);
        temp2.setOnClickListener(this);
        dialog.setView(inflate);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);

    }
    private void showSettingFaceDialog1() {

        new AlertDialog.Builder(this)
                .setTitle("图片来源")
                .setCancelable(true)
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Local Image
                                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    startActivityForResult(intent,SELECT_PIC_KITKAT);
                                } else {
                                    startActivityForResult(intent,IMAGE_REQUEST_CODE);
                                }
                                break;
                            case 1:// Take Picture
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
                                break;
                        }
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();

    }
    private boolean hasSdcard(){
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case SELECT_PIC_KITKAT:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (hasSdcard()) {
                        File tempFile = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        toast("未找到存储卡，无法存储照片！");
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        setImageToView(data);
                    }
                    break;

            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
            return;
        }

        CropActivity.newInstanceForResult(this,uri,RESULT_REQUEST_CODE);
//
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            String url=getPath(this,uri);
//            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
//        }else{
//            intent.setDataAndType(uri, "image/*");
//        }
//
//        // 设置裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 200);
//        intent.putExtra("outputY", 200);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, RESULT_REQUEST_CODE);


    }


//    private void setImageToView(Intent data,ImageView imageView) {
//        Bundle extras = data.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            Bitmap roundBitmap=ImageUtil.toRoundBitmap(photo);
//            imageView.setImageBitmap(roundBitmap);
//            saveBitmap(photo);
//        }
//    }

    public void saveBitmap(Bitmap mBitmap) {
        File f = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了

//        String imagePath = Utils.savePhoto(bitmap, Environment
//                .getExternalStorageDirectory().getAbsolutePath(), String
//                .valueOf(System.currentTimeMillis()));
//        if(imagePath != null){
//
//        }
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SFProgrssDialog dialog = SFProgrssDialog.show(this, "请稍后...");
        RequestParams params = new RequestParams();
        params.put("token", token);
        try {
            params.put("avatar", temp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Api.excuteUpload(Api.SET_USERDATA, this, params, dialog, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                Glide.with(MyDataActivity.this).load(temp).error(R.drawable.icon_avatar_default)
                        .transform(new GlideCircleTransform(MyDataActivity.this)).into(mImageUserAvatar);
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.choose_pic:
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    startActivityForResult(intent,SELECT_PIC_KITKAT);
                } else {
                    startActivityForResult(intent,IMAGE_REQUEST_CODE);
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
}
