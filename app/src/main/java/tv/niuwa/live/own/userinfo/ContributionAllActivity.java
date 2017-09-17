package tv.niuwa.live.own.userinfo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;

/**
 * Created by Administrator on 2016/8/22.
 * Author: XuDeLong
 */
public class ContributionAllActivity extends BaseSiSiActivity {

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @OnClick(R.id.image_back)
    public void back(View view) {
        ContributionAllActivity.this.finish();
    }

    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTopTabTitle;
    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            userId = data.getString("id");
        }
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mFragments.clear();
        if (mTopTabTitle == null) {
            mTopTabTitle = new ArrayList<>();
        }
        mTopTabTitle.clear();
        mTopTabTitle.add("收益榜");
        mTopTabTitle.add("打赏榜");
        for (int i = 0; i < mTopTabTitle.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mFragments.add(BenefitFragment.getInstance(1));
        mFragments.add(ContibutionFragment.getInstance(2));
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
        return R.layout.activity_contribution_all;
    }

}
