package tv.niuwa.live.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by fengjh on 16/7/19.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> mTopTabTitle;
    private ArrayList<Fragment> mFragments;

    public MainViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragments, ArrayList<String> mTopTabTitle) {
        super(fm);
        this.mFragments = mFragments;
        this.mTopTabTitle = mTopTabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTopTabTitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTopTabTitle.get(position);
    }
}
