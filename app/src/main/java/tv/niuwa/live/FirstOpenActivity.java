package tv.niuwa.live;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smart.androidutils.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.niuwa.live.utils.ViewPagerAdapter;

/**
 * Created by v7 on 2016/7/21.
 */
public class FirstOpenActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.indicator)
    LinearLayout linearLayout;

    @Bind(R.id.in)
    View in;

    boolean canIn=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // This work only for android 4.4+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        in.setVisibility(View.INVISIBLE);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getImageList());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0 ;i<linearLayout.getChildCount();i++) {
                    linearLayout.getChildAt(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_circle_nor));
                }
                linearLayout.getChildAt(position).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_circle_sel));
                in.setVisibility(View.INVISIBLE);
                if(position == 3){
                    in.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setIndicatorOnclickListener();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_welcome;
    }

    private void setIndicatorOnclickListener(){
        for (int i=0 ;i<linearLayout.getChildCount();i++) {
            final int finalI = i;
            linearLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(finalI,true);
                }
            });
        }
    }

    private ArrayList<View> getImageList(){

        ArrayList<View> arrayList = new ArrayList();
        arrayList.add(buildImageView(R.drawable.splash1,null));
        arrayList.add(buildImageView(R.drawable.splash2,null));
        arrayList.add(buildImageView(R.drawable.splash3,null));
        arrayList.add(buildImageView(R.drawable.splash4, new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FirstOpenActivity.this.finish();
           }
        }));

        return arrayList;
    }

    private ImageView buildImageView(int resId, View.OnClickListener onClickListener){
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageDrawable(getResources().getDrawable(resId));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

//        if (onClickListener!=null)
//            imageView.setOnClickListener(onClickListener);
        return imageView;
    }

    @OnClick(R.id.in)
    public void in(){
        this.finish();
        openActivity(MainActivity.class);
    }

}
