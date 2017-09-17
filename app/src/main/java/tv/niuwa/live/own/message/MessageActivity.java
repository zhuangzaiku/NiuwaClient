package tv.niuwa.live.own.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationListFragment;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;

public class MessageActivity extends BaseSiSiActivity {
    private ViewPager viewPager;
    private String userId;
    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        MessageActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("会话列表");
        viewPager = (ViewPager) findViewById(R.id.pager);
        userId = (String) SharePrefsUtils.get(this, "user", "userId", "");
        if (null == LCChatKit.getInstance().getClient()) {
            LCChatKit.getInstance().open(userId, new AVIMClientCallback() {

                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    final Fragment[] fragmentList = new Fragment[]{new LCIMConversationListFragment()};
                    String[] tabList = new String[]{"会话列表"};
                    TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager(),
                            Arrays.asList(fragmentList), Arrays.asList(tabList));
                    viewPager.setAdapter(adapter);
                }
            });
        } else {
            final Fragment[] fragmentList = new Fragment[]{new LCIMConversationListFragment()};
            String[] tabList = new String[]{"会话列表"};
            TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager(),
                    Arrays.asList(fragmentList), Arrays.asList(tabList));
            viewPager.setAdapter(adapter);
        }

    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_message;
    }

    public class TabFragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mTitles;

        public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }

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
            return mTitles.get(position);
        }
    }
}
