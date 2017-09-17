package com.smart.loginsharesdk.share.onekeyshare;

/**
 * Created by fengjh on 2014/9/29.
 */

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.loginsharesdk.R;
import com.smart.loginsharesdk.utils.ILog;
import com.mob.tools.FakeActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

import static com.mob.tools.utils.BitmapHelper.getBitmap;

//import static cn.sharesdk.framework.utils.BitmapHelper.getBitmap;

/** 执行图文分享的页面，此页面不支持微信平台的分享 */
public class EditPage extends FakeActivity implements OnClickListener, TextWatcher {
    private static final int MAX_TEXT_COUNT = 140;
    private static final int DIM_COLOR = 0x7f323232;
    private HashMap<String, Object> reqData;
    //当前要分享的平台
    private Platform curPlatform;
    private Button button_back;
    private Button button_publish;
    private TextView text_title;
    private TextView text_counter;
    private EditText edit_content;
    private ImageView imageView_image;
//    private CheckBox checkBox_image;

    public void setShareData(HashMap<String, Object> data) {
        reqData = data;
    }

    public void onCreate() {
        if (reqData == null) {
            finish();
            return;
        }
        customContentView();
        onTextChanged(edit_content.getText(), 0, edit_content.length(), 0);
    }

    /**
     *自定义分享内容页面的布局
     */
    private void customContentView(){
        activity.setContentView(R.layout.share_content_layout);
        //初始化组件
        initView();
        //初始化监听器
        initListener();
    }
    private void initView(){
        button_back = (Button) activity.findViewById(R.id.button_back);
        button_publish = (Button) activity.findViewById(R.id.button_publish);
        text_title = (TextView) activity.findViewById(R.id.text_title);
        text_counter = (TextView) activity.findViewById(R.id.text_counter);
        edit_content = (EditText) activity.findViewById(R.id.edit_content);
//        checkBox_image = (CheckBox) activity.findViewById(R.id.checkBox_image);
        imageView_image = (ImageView) activity.findViewById(R.id.imageView_image);
        text_title.setText(getPlatformName()+"分享");
        edit_content.setText(String.valueOf(reqData.get("text")));
        String imagePath = (String)reqData.get("imagePath");
        Object imageUrl = reqData.get("imageUrl");
        if(!TextUtils.isEmpty(imagePath)&&new File(imagePath).exists()){
            share_image = getImage();
            if(share_image!=null){
//            checkBox_image.setVisibility(View.VISIBLE);
                imageView_image.setVisibility(View.VISIBLE);
                imageView_image.setImageBitmap(share_image);
//            checkBox_image.setText("无图分享");
            }else{
//            checkBox_image.setVisibility(View.INVISIBLE);
                imageView_image.setVisibility(View.INVISIBLE);
            }
        }else{
            if(imageUrl!=null&&!TextUtils.isEmpty(reqData.get("imageUrl").toString())){
                    new BitmapDownloadTask(imageView_image).execute(imageUrl.toString());
            }
        }
    }
    private void initListener(){
        button_back.setOnClickListener(this);
        button_publish.setOnClickListener(this);
        edit_content.addTextChangedListener(this);
        //图片双击放大预览
        imageView_image.setOnTouchListener(new onDoubleClickListener());
//        checkBox_image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
//                //用于操作有图、无图分享
//                if(bool){
//                    //无图分享
//                    imageView_image.setVisibility(View.INVISIBLE);
//                    checkBox_image.setText("有图分享");
//
//                }else{
//                    //有图分享
//                    imageView_image.setVisibility(View.VISIBLE);
//                    checkBox_image.setText("无图分享");
//                }
//            }
//        });
    }
    private Bitmap share_image;

    private Bitmap getImage(){

        Bitmap bm = null;

        Object imageUrl = reqData.get("imageUrl");
        String imagePath = (String)reqData.get("imagePath");
        if(imageUrl!=null&&!TextUtils.isEmpty(reqData.get("imageUrl").toString())){

        }else if(!TextUtils.isEmpty(imagePath)&&new File(imagePath).exists()){
            try{
                bm = getBitmap(imagePath);
            }catch(Throwable t){
                System.gc();
                try{
                    bm = getBitmap(imagePath,2);
                }catch(Throwable t1){
                    t1.printStackTrace();
                }
            }
        }

//        if(reqData.containsKey("bitmap")){
//            Bitmap bm = (Bitmap) reqData.get("bitmap");
//            if(bm!=null){
//                Log.d("Edit","====bm="+bm.toString());
//                return bm;
//            }else{
//                Log.d("Edit","====bm==null");
//                return null;
//            }
//        }
      return bm;
    }

    /**
     * 获取分享平台名称
     */
    public String getPlatformName(){
        String platform = String.valueOf(reqData.get("platform"));
        curPlatform = ShareSDK.getPlatform(platform);
        if("TencentWeibo".equals(platform)||"TencentWeibo"==platform){
            platform = "腾讯微博";
        }else if("SinaWeibo".equals(platform)||"SinaWeibo"==platform){
            platform = "新浪微博";
        }
        ILog.d("EditPage", "====platform===" + platform);
        return platform;
    }

    public void onClick(View v) {

        /**
         * 自定义布局中的按钮
         */
        if(v.equals(button_back)){
            Platform plat = null;
            String platform = String.valueOf(reqData.get("platform"));
            plat = ShareSDK.getPlatform(platform);
            //取消分享的统计
            if(plat!=null){
                ShareSDK.logDemoEvent(5, plat);
            }
            finish();
            return ;
        }
        if(v.equals(button_publish)){
            String text = edit_content.getText().toString();
            ILog.d("EditPage","====etContent="+text);
            reqData.put("text", text);
            ILog.d("EditPage","====publish=imagePath="+reqData.get("imagePath"));
            ILog.d("EditPage","====publish=imageUrl="+reqData.get("imageUrl"));

            ILog.d("EditPage","====publish=imagePath="+reqData.get("imagePath"));
            ILog.d("EditPage","====publish=imageUrl="+reqData.get("imageUrl"));
            HashMap<Platform, HashMap<String, Object>> editRes
                    = new HashMap<Platform, HashMap<String,Object>>();
            editRes.put(curPlatform, reqData);
            ILog.d("EditPage","=====即将调用");
            HashMap<String, Object> res = new HashMap<String, Object>();
            res.put("editRes", editRes);
            setResult(res);
            finish();
            return;
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int remain = MAX_TEXT_COUNT - edit_content.length();
        if(remain>0){
            String counter = "您还可以输入"+String.valueOf(remain)+"个字";
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(counter);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.GRAY),0,6,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED),6,counter.length()-2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.GRAY),counter.length()-2,counter.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            text_counter.setText(spannableStringBuilder);
        }
    }
    public void afterTextChanged(Editable s) {

    }
    private void hideSoftInput() {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_content.getWindowToken(), 0);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean onFinish() {
        hideSoftInput();
        return super.onFinish();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSoftInput();
            Window win = activity.getWindow();
            win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } else {
            hideSoftInput();
            Window win = activity.getWindow();
            win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }
    //图片双击放大监听器
    class onDoubleClickListener implements View.OnTouchListener{
        int count = 0;
        long firstClick;
        long secondClick;
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(MotionEvent.ACTION_DOWN == motionEvent.getAction()){
                count ++;
                if(count == 1){
                    firstClick =  System.currentTimeMillis();
                }else if(count == 2){
                    secondClick = System.currentTimeMillis();
                    if(secondClick - firstClick < 1000){
                        if(share_image!=null&&!share_image.isRecycled()){
                            PicViewer pv = new PicViewer();
                            pv.setImageBitmap(share_image);
                            pv.show(activity,null);
                        }
                    }
                    count = 0;
                    firstClick = 0;
                    secondClick = 0;
                }
            }
            return true;
        }
    }

    class BitmapDownloadTask extends AsyncTask<String,Void,Bitmap>{

        private  WeakReference<ImageView> imgWeakReference = null;

        public BitmapDownloadTask(ImageView imageView){
            imgWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            URL imageUrl = null;
            try {
                imageUrl  = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
           if(isCancelled()){
               bitmap = null;
           }
          if(imgWeakReference!=null){
              ImageView imageView = imgWeakReference.get();
              share_image = bitmap;
              imageView.setOnTouchListener(new onDoubleClickListener());
              if(imageView!=null){
                  imageView.setVisibility(View.VISIBLE);
                  imageView.setImageBitmap(bitmap);
              }else{
                  imageView.setVisibility(View.INVISIBLE);
              }
          }
        }
    }
}
