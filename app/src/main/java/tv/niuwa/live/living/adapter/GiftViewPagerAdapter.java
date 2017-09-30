package tv.niuwa.live.living.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/5.
 * Author: XuDeLong
 */
public class GiftViewPagerAdapter extends PagerAdapter {
    private ArrayList<LinearLayout> mGridViews;

    public GiftViewPagerAdapter(ArrayList<LinearLayout> mGridViews) {
        this.mGridViews = mGridViews;
    }

    @Override
    public int getCount() {
        return mGridViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mGridViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mGridViews.get(position));
        return mGridViews.get(position);
    }
}
