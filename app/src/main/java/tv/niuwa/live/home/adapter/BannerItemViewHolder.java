package tv.niuwa.live.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.smart.androidutils.utils.DensityUtils;
import com.smart.androidutils.utils.StringUtils;

import tv.niuwa.live.R;
import tv.niuwa.live.home.model.BannerItem;
import tv.niuwa.live.home.model.VideoItem;
import tv.niuwa.live.living.LivingActivity;
import tv.niuwa.live.own.WebviewActivity;

/**
 * Created by fengjh on 16/7/31.
 */
public class BannerItemViewHolder implements Holder<BannerItem>, View.OnClickListener {

    private Context mContext;
    private View mRootView;
    private int bannerWidth;
    private int bannerHeight;

    @Override
    public View createView(Context context) {
        this.mContext = context;
        bannerWidth = DensityUtils.screenWidth(context);
        bannerHeight = (bannerWidth * 270) / 720;
        mRootView = LayoutInflater.from(context).inflate(R.layout.layout_home_banner_item, null);
        return mRootView;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerItem data) {
        ImageView imageView = (ImageView) mRootView.findViewById(R.id.image_banner);
        if (data != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bannerWidth, bannerHeight);
            imageView.setLayoutParams(params);
            String imageUrl = data.getPic();
            if (StringUtils.isNotEmpty(imageUrl)) {
                Glide.with(context).load(imageUrl).into(imageView);
            }
            imageView.setTag(data);
            imageView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        BannerItem item = (BannerItem) view.getTag();
        if (item != null) {
            String type = item.getType();
            switch (type) {
                case "1":
                    Bundle data = new Bundle();
                    VideoItem vItem = new VideoItem();
                    vItem.setRoom_id(item.getJump());
                    data.putSerializable("videoItem", vItem);
                    openActivity(LivingActivity.class, data);
                    break;
                case "2":
                    Bundle data1 = new Bundle();
                    data1.putString("title", item.getTitle());
                    data1.putString("jump", item.getJump());
                    openActivity(WebviewActivity.class, data1);
                    break;
            }
        }
    }

    public void openActivity(Class<?> cls) {
        openActivity(cls, null);
    }

    public void openActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}
