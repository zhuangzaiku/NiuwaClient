package tv.niuwa.live.living;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiFragment;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.living.adapter.VoteAdapter;
import tv.niuwa.live.living.model.VoteListItem;
import tv.niuwa.live.utils.Api;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 29/09/2017 09:50.
 * <p>
 * All copyright reserved.
 */

public class VoteFragment extends BaseSiSiFragment {

    @Bind(R.id.voteList)
    GridView mVoteListView;

    private VoteAdapter mVoteAdapter;
    private List<VoteListItem> mVoteList = new ArrayList<>();


    private void init() {
        mVoteAdapter = new VoteAdapter(getContext());
        mVoteListView.setAdapter(mVoteAdapter);
        mVoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doVote(mVoteList.get(position).getVoteOpId());
            }
        });

        
       
        Api.getVote(getContext(), new JSONObject(), new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {

                JSONArray votes = data.getJSONArray("data");
                mVoteList.clear();
                for (int i = 0; i < votes.size(); i++) {
                    JSONObject jo = votes.getJSONObject(i);
                    VoteListItem item = new VoteListItem(jo.getString("votename"), jo.getString("vote_opname"), jo.getString("vote_opid"));
                    mVoteList.add(item);
                }
                int length = votes.size();
                if (length > 0) {
                    mVoteListView.setNumColumns(length);
                    mVoteAdapter.setVoteList(mVoteList);
                }

                getVoteResult();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    private void doVote(String voteId) {

        JSONObject params = new JSONObject();
        params.put("userid", (String) SharePrefsUtils.get(getContext(), "user", "userId", ""));
        params.put("option", voteId);
        Api.doVote(getContext(), params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                String desc = data.getString("descrp");
                Toast.makeText(getContext(), desc, Toast.LENGTH_SHORT).show();
                getVoteResult();

            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }


    private void getVoteResult() {
        Api.voteResult(getContext(), new JSONObject(), new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray votes = data.getJSONArray("data");
                int listSize = mVoteList.size();
                for (int i = 0; i < listSize; i++) {
                    VoteListItem item = mVoteList.get(i);
                    for(int j = 0; j < listSize; j++) {
                        JSONObject jo = votes.getJSONObject(j);
                        if(!TextUtils.isEmpty(item.getVoteOpName()) && item.getVoteOpName().equals(jo.getString("vote_option"))) {
                            item.setVoteNum(jo.getIntValue("vote_num"));
                        }
                    }
                }
                mVoteAdapter.setVoteList(mVoteList);
            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_vote;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }
}
