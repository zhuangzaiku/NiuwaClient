package tv.niuwa.live.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.smart.androidutils.fragment.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import tv.niuwa.live.R;
import tv.niuwa.live.home.adapter.MainViewPagerAdapter;
import tv.niuwa.live.lean.ConversationListActivity;
import tv.niuwa.live.own.userinfo.ContributionAllActivity;
import tv.niuwa.live.search.SearchActivity;

/**
 * Created by fengjh on 16/7/19.
 */
public class HomeFragment extends BaseFragment {

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    private ArrayList<String> mTopTabTitle;
    private ArrayList<Fragment> mFragments;
    private MainViewPagerAdapter mAdapter;

    @OnClick(R.id.image_home_search)
    public void homeSearch(View view) {
        openActivity(SearchActivity.class);
    }

    @OnClick(R.id.image_home_message)
    public void homeMessage(View view) {
        //openActivity(MessageActivity.class);
        openActivity(ConversationListActivity.class);
    }

    @OnClick(R.id.image_home_rank)
    public void imageHomeRank(View v){
        openActivity(ContributionAllActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mFragments.clear();
        if (mTopTabTitle == null) {
            mTopTabTitle = new ArrayList<>();
        }
        mTopTabTitle.clear();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTopTabTitle.add("关注");
        mTopTabTitle.add("热门");
        mTopTabTitle.add("最新");
        for (int i = 0; i < mTopTabTitle.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        HomeFollowFragment fragment1 = HomeFollowFragment.getInstance(0);
        HomeHotFragment fragment2 = HomeHotFragment.getInstance(1);
        HomeLatestFragment fragment3 = HomeLatestFragment.getInstance(2);
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragments.add(fragment3);
        mAdapter = new MainViewPagerAdapter(getChildFragmentManager(), mFragments, mTopTabTitle);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home;
    }
}
