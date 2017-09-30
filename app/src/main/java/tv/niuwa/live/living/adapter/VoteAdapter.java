package tv.niuwa.live.living.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tv.niuwa.live.R;
import tv.niuwa.live.living.model.VoteListItem;
import tv.niuwa.live.utils.DisplayUtil;
import tv.niuwa.live.view.ShapedImageView;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 29/09/2017 11:04.
 * <p>
 * All copyright reserved.
 */

public class VoteAdapter extends BaseAdapter {
    private static final String TAG = VoteAdapter.class.getSimpleName();

    private Context mContext;
    private List<VoteListItem> mVoteList = new ArrayList<>();
    private static final int[] VOTE_BG = new int[]{R.drawable.img_piao01, R.drawable.img_piao02, R.drawable.img_piao03,
            R.drawable.img_piao04, R.drawable.img_piao05, };

    public void setVoteList(List<VoteListItem> data) {
        mVoteList.clear();
        mVoteList.addAll(data);
        notifyDataSetChanged();
    }

    public VoteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mVoteList.size();
    }

    @Override
    public Object getItem(int position) {
        return mVoteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        ShapedImageView bg;
        TextView voteNum;
        TextView voteName;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final VoteListItem item = (VoteListItem) getItem(i);
        ViewHolder viewHolder = null;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_vote, null);
        viewHolder = new ViewHolder();
        viewHolder.bg = (ShapedImageView) view.findViewById(R.id.vote_bg);
        viewHolder.voteName = (TextView) view.findViewById(R.id.vote_name);
        viewHolder.voteNum = (TextView) view.findViewById(R.id.vote_num);
        view.setTag(viewHolder);

        float[] percents = getPercents();
        Log.d(TAG,i + "---" + VOTE_BG[i] + "--" + percents[i] + item.getVoteOpName());
        viewHolder.bg.setImageResource(VOTE_BG[i]);
        viewHolder.bg.setPercent(percents[i]);
        viewHolder.voteName.setText(item.getVoteOpName());
        if(item.getVoteNum() > 999) {
            viewHolder.voteNum.setTextSize(8);
        }
        viewHolder.voteNum.setText(String.valueOf(item.getVoteNum()));
        return view;
    }

    private float[] getPercents() {
        int length = mVoteList.size();
        float[] ret = new float[length];
        int max = 0;
        for(int i = 0;i < length; i++) {
            VoteListItem item = mVoteList.get(i);
            if(item.getVoteNum() > max) {
                max = item.getVoteNum();
            }
        }
        if(max != 0 ) {
            for (int i = 0; i < length; i++) {
                VoteListItem item = mVoteList.get(i);
                ret[i] = (float)item.getVoteNum() / max * 100;
            }
        }
        return ret;
    }
}
