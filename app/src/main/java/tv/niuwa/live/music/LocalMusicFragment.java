package tv.niuwa.live.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.smart.androidutils.utils.LogUtils;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiFragment;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import butterknife.Bind;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2016/8/22.
 * Author: XuDeLong
 */
public class LocalMusicFragment extends BaseSiSiFragment implements OnRecyclerViewItemClickListener {
    @Bind(R.id.swipeRefreshLayout_local)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView_local)
    RecyclerView mRecyclerView;
    @Bind(R.id.noDataLayout_contribution_week)
    RelativeLayout mNodataView;

    private int mPosition;
    private MusicListAdapter mMusicListAdapter;
    private ArrayList<MusicItem> MusicItems;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getLocalMusic();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    public static LocalMusicFragment getInstance(int pos) {
        LocalMusicFragment fragment = new LocalMusicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPosition = bundle.getInt("pos");
        }
        if (MusicItems == null) {
            MusicItems = new ArrayList<>();
        }
        MusicItems.clear();
//        for (int i = 0; i < 20; i++) {
//            MusicItem MusicItem = new MusicItem();
//            MusicItems.add(MusicItem);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocalMusic();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);
        //final GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        //mRecyclerView.addItemDecoration(new SpaceItemDecoration(6));
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mSwipeRefreshLayout.setRefreshing(false);
        MusicItems.clear();
        mMusicListAdapter = new MusicListAdapter(getActivity(), MusicItems);
        mRecyclerView.setAdapter(mMusicListAdapter);
        mMusicListAdapter.setOnRecyclerViewItemClickListener(this);
        getLocalMusic();
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("MainViewPagerFragment", "--------onScrollStateChanged");
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItemPosition == (totalItemCount - 1) && isSlidingToLast) {
                        //toast("没有更多数据了~~");
                        //getData(totalItemCount, 20, null);
                        mMusicListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("MainViewPagerFragment", "--------onScrolled=dx=" + dx + "---dy=" + dy);
                if (dy > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }
            }
        });
        mMusicListAdapter.setOnRecyclerViewItemClickListener(this);
    }

    public void getLocalMusic() {
        MusicItems.clear();
        File file = getActivity().getFilesDir();
        String[] songs = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                LogUtils.i(s);
                if (s.contains(".mp3"))
                    return true;
                return false;
            }
        });
        for (int i = 0; i < songs.length; i++) {
            MusicItem temp = new MusicItem();
            temp.setName(songs[i]);
            temp.setType("1");
            temp.setUrl(file.getAbsolutePath() + "/" + songs[i]);
            MusicItems.add(temp);
        }
        mMusicListAdapter.notifyDataSetChanged();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_music_local;
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        Intent mIntent = new Intent();
        mIntent.putExtra("path",MusicItems.get(position).getUrl() );
        getActivity().setResult(RESULT_OK, mIntent);
        getActivity().finish();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        getLocalMusic();
        super.onHiddenChanged(hidden);
    }
}
