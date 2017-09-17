package tv.niuwa.live.own.publishrecord;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import tv.niuwa.live.R;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;

/**
 * Created by fengjh on 16/7/31.
 */
public class PublishRecordListAdapter extends RecyclerView.Adapter<PublishRecordListAdapter.FansViewHolder> {

    private Context mContext;
    private ArrayList<PublishRecordItem> mPublishRecordItems;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    public PublishRecordListAdapter(Context mContext, ArrayList<PublishRecordItem> mPublishRecordItems) {
        this.mContext = mContext;
        this.mPublishRecordItems = mPublishRecordItems;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public FansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_publish_record_list, parent, false);
        return new FansViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final FansViewHolder holder, int position) {
        PublishRecordItem item = mPublishRecordItems.get(position);
        holder.mPublishRecordEarn.setText(item.getEarn());
        holder.mPublishRecordHits.setText(item.getHits());
        holder.mPublishRecordTime.setText(item.getAdd_time());
        holder.mPublishRecordTitle.setText(item.getVideo_title());
    }

    @Override
    public int getItemCount() {
        return mPublishRecordItems.size();
    }

    class FansViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.publis_record_earn)
        TextView mPublishRecordEarn;
        @Bind(R.id.publis_record_hits)
        TextView mPublishRecordHits;
        @Bind(R.id.publis_record_time)
        TextView mPublishRecordTime;
        @Bind(R.id.publis_record_title)
        TextView mPublishRecordTitle;
        public FansViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
