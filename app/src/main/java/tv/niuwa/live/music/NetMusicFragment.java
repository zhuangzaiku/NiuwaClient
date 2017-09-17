package tv.niuwa.live.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.LogUtils;
import com.smart.androidutils.utils.StringUtils;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiFragment;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.utils.Api;
import tv.niuwa.live.utils.DownloadService;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/22.
 * Author: XuDeLong
 */
public class NetMusicFragment extends BaseSiSiFragment implements OnRecyclerViewItemClickListener {
    public static final String ACTION_DOWNLOAD_PROGRESS = "my_download_progress";
    public static final String ACTION_DOWNLOAD_SUCCESS = "my_download_success";
    public static final String ACTION_DOWNLOAD_FAIL = "my_download_fail";
    @Bind(R.id.swipeRefreshLayout_net)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView_net)
    RecyclerView mRecyclerView;
    @Bind(R.id.noDataLayout_contribution_week)
    RelativeLayout mNodataView;
    @Bind(R.id.edit_input_keyword)
    EditText mEditInputKeyword;
    private MyReceiver receiver;
    private int mPosition;
    private MusicListAdapter mMusicListAdapter;
    private ArrayList<MusicItem> MusicItems;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    public static NetMusicFragment getInstance(int pos) {
        NetMusicFragment fragment = new NetMusicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DOWNLOAD_PROGRESS);
        filter.addAction(ACTION_DOWNLOAD_SUCCESS);
        filter.addAction(ACTION_DOWNLOAD_FAIL);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new MyReceiver();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPosition = bundle.getInt("pos");
        }
        if (MusicItems == null) {
            MusicItems = new ArrayList<>();
        }
        MusicItems.clear();
//        for (int i = 0; i < 20; i++) {
//            ContributionItem contributionItem = new ContributionItem();
//            MusicItems.add(contributionItem);
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(receiver);
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
                        //mMusicListAdapter.notifyDataSetChanged();
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

//    private void getData(int limit_begin, int limit_num, final SwipeRefreshLayout mSwipeRefreshLayout) {
//        String token = (String) SharePrefsUtils.get(this.getContext(), "user", "token", "");
//        String userId = (String) SharePrefsUtils.get(this.getContext(), "user", "userId", "");
//        if (StringUtils.isNotEmpty(((ContributionActivity) getActivity()).userId)) {
//            userId = ((ContributionActivity) getActivity()).userId;
//        }
//        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
//            JSONObject requestParams = new JSONObject();
//            requestParams.put("token", token);
//            requestParams.put("id", userId);
//            requestParams.put("type", "day");
//            requestParams.put("limit_begin", limit_begin);
//            requestParams.put("limit_num", limit_num);
//            Api.getUserContributionList(this.getContext(), requestParams, new OnRequestDataListener() {
//                @Override
//                public void requestSuccess(int code, JSONObject data) {
//                    if (mSwipeRefreshLayout != null) {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                        MusicItems.clear();
//                    }
//                    JSONArray list = data.getJSONArray("data");
//                    for (int i = 0; i < list.size(); i++) {
//                        JSONObject item = list.getJSONObject(i);
//                        MusicItem musicItem = new MusicItem();
//
//                        MusicItems.add(musicItem);
//                    }
//                    mMusicListAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void requestFailure(int code, String msg) {
//                    if (mSwipeRefreshLayout != null) {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//                    //toast(msg);
//                    //加载空数据图
//                    if (MusicItems.size() == 0) {
//                        mNodataView.setVisibility(View.VISIBLE);
//                    } else {
//                        mNodataView.setVisibility(View.GONE);
//                    }
//                }
//            });
//
//        } else {
//            openActivity(LoginActivity.class);
//        }
//    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_music_net;
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        Intent it = new Intent(getActivity(), DownloadService.class);
        MusicItem music = MusicItems.get(position);
        it.putExtra("music",music);
        it.putExtra("index", position+"");
        it.putExtra("path", getActivity().getFilesDir().getAbsolutePath() + "/");
        downLoadLrc(getActivity().getFilesDir().getAbsolutePath() + "/",music);
        getActivity().startService(it);
    }
    private void downLoadLrc(final String path,final MusicItem music){
        JSONObject params = new JSONObject();
        params.put("song_id",music.getHash());
        Api.getSongLrc(getContext(), params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                if(null != data.getJSONObject("data")){
                    String lyric = data.getJSONObject("data").getString("lyric");

                    try {
                        File temp = new File(path+music.getName()+".lrc");
                        if(temp.exists()){
                            temp.delete();
                        }
                        temp.createNewFile();
                        FileOutputStream outStream = new FileOutputStream(temp);
                        outStream.write(lyric.getBytes());
                        outStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    @OnClick(R.id.btn_search)
    public void btnSearchMusic(View v) {
        String keywords = mEditInputKeyword.getText().toString();
        if (StringUtils.isEmpty(keywords)) {
            toast("请输入要搜索的歌曲名称");
            return;
        }
        JSONObject params = new JSONObject();
        params.put("keyword",keywords);
        params.put("limit_num",30);
        Api.searchMusic1(getActivity(), params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray items = data.getJSONArray("data");
                MusicItems.clear();
                for (int i = 0; i < items.size(); i++) {
                    MusicItem item = new MusicItem();
                    item.setType("2");
                    item.setName(items.getJSONObject(i).getString("song_name"));
                    item.setSinger(items.getJSONObject(i).getString("artist_name"));
                    item.setHash(items.getJSONObject(i).getString("song_id"));
                    item.setUrl(items.getJSONObject(i).getString("mp3Url"));
                    MusicItems.add(item);
                }
                mMusicListAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.addHeader("apikey", "1bcab59b6ca7795269b4fbdb38a65dc1");
//        client.get(getActivity(), Api.SEARCH_MUSIC + "?s=" + keywords, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    String response = new String(responseBody, "UTF-8");
//                    JSONObject object = JSON.parseObject(response);
//                    int code = object.getIntValue("code");
//                    if (0 == code) {
//                        JSONArray items = object.getJSONObject("data").getJSONArray("data");
//                        MusicItems.clear();
//                        for (int i = 0; i < items.size(); i++) {
//                            MusicItem item = new MusicItem();
//                            item.setType("2");
//                            item.setName(items.getJSONObject(i).getString("filename"));
//                            item.setSinger(items.getJSONObject(i).getString("singername"));
//                            item.setTime(items.getJSONObject(i).getString("duration"));
//                            item.setHash(items.getJSONObject(i).getString("hash"));
//                            MusicItems.add(item);
//                        }
//                        mMusicListAdapter.notifyDataSetChanged();
//                    }
//
//                } catch (Exception e) {
//
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                try {
//                    String response = new String(responseBody, "UTF-8");
//                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(isActive){
                String action = intent.getAction();
                MusicItem music = (MusicItem) intent.getSerializableExtra("music");
                String index = music.getType();
                String hash = music.getHash();
                if (action.equals(ACTION_DOWNLOAD_PROGRESS)) {
                    int progress = Integer.parseInt(music.getProgress());
                    LogUtils.i("delong", index + "---" + progress+"----"+hash);
                    int tempIndex = Integer.parseInt(index);
                    if(MusicItems.size() > tempIndex && MusicItems.get(tempIndex).getHash().equals(hash)){
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putInt("pos",Integer.parseInt(index));
                        data.putInt("progress",progress);
                        msg.setData(data);
                        LogUtils.i("delong", index + "---" + progress+"----"+hash);
                        mMusicListAdapter.getHandler().sendMessage(msg);
                    }
                } else if (action.equals(ACTION_DOWNLOAD_SUCCESS)) {
                    toast("下载成功");
                    LogUtils.i("delong", index + "---" + ACTION_DOWNLOAD_SUCCESS);
                } else if (action.equals(ACTION_DOWNLOAD_FAIL)) {
                    toast("下载失败");
                    LogUtils.i("delong", index + "---" + ACTION_DOWNLOAD_FAIL);
                }
            }


        }

    }


}
