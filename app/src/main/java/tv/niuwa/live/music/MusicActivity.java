package tv.niuwa.live.music;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.smart.androidutils.activity.BaseActivity;
import tv.niuwa.live.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/22.
 * Author: XuDeLong
 */
public class MusicActivity extends BaseActivity {

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @OnClick(R.id.image_back)
    public void back(View view) {
        MusicActivity.this.finish();
    }

    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTopTabTitle;
    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mFragments.clear();
        if (mTopTabTitle == null) {
            mTopTabTitle = new ArrayList<>();
        }
        mTopTabTitle.clear();
        mTopTabTitle.add("本地");
        mTopTabTitle.add("网络");
        for (int i = 0; i < mTopTabTitle.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mFragments.add(LocalMusicFragment.getInstance(1));
        mFragments.add(NetMusicFragment.getInstance(2));
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTopTabTitle.get(position);
            }
        });
        mViewPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_music;
    }

}
