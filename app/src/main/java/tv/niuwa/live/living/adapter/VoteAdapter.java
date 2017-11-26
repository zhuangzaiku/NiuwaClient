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
    private int mTotalNum;

    public void setmTotalNum(int mTotalNum) {
        this.mTotalNum = mTotalNum;
    }

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
        if(view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_vote, null);
            viewHolder = new ViewHolder();
            viewHolder.bg = (ShapedImageView) view.findViewById(R.id.vote_bg);
            viewHolder.voteName = (TextView) view.findViewById(R.id.vote_name);
            viewHolder.voteNum = (TextView) view.findViewById(R.id.vote_num);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.bg.setImageResource(VOTE_BG[i]);
        Log.d(TAG,"num" + i + "->" + item.getVoteNum() + "----total->" + mTotalNum);
        float percent = 100;
        if(mTotalNum != 0) {
            percent = item.getVoteNum() * 100.f / mTotalNum;
        }

        Log.d(TAG,"percent->" + percent);
        viewHolder.bg.setPercent(percent);
        viewHolder.voteName.setText(item.getVoteOpName());
        viewHolder.voteNum.setText(String.format(mContext.getString(R.string.percents),String.valueOf((int)percent)));
        return view;
    }


}
