package tv.niuwa.live.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smart.androidutils.utils.DensityUtils;
import com.smart.androidutils.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import tv.niuwa.live.R;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;
import tv.niuwa.live.home.model.VideoItem;

/**
 * Created by fengjh on 16/7/19.
 */
public class LatestRecyclerListAdapter extends RecyclerView.Adapter<LatestRecyclerListAdapter.VideoViewHolder> {

    private Context mContext;
    private ArrayList<VideoItem> mVideoItems;
    private int imageWidth;
    private int imageHeight;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public LatestRecyclerListAdapter(Context mContext, ArrayList<VideoItem> mVideoItems) {
        this.mContext = mContext;
        this.mVideoItems = mVideoItems;
        imageWidth = (DensityUtils.screenWidth(mContext) - 6) / 3;
        imageHeight = imageWidth;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_latest_list, parent, false);
        return new VideoViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {
        VideoItem item = mVideoItems.get(position);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageWidth, imageHeight);
//        holder.mLinearItemLatestContainer.setLayoutParams(params);
        holder.mImageHomeLatest.setLayoutParams(params);
        Glide.with(this.mContext).load(item.getAvatar())
                .error(R.drawable.icon_avatar_default)
                .into(holder.mImageHomeLatest);
        holder.mTextHomeLatestName.setText(item.getUser_nicename());
        if(StringUtils.isNotEmpty(item.getChannel_location()))
            holder.getmTextHomeLatestLocation.setText(item.getChannel_location());
        holder.mLatestVideoStatus.setText("直播");
        if (StringUtils.isNotEmpty(item.getPrice()) && Integer.parseInt(item.getPrice())>0){
            holder.mLatestVideoStatus.setText("付费"+item.getPrice());
        }
        if(StringUtils.isNotEmpty(item.getNeed_password()) && Integer.parseInt(item.getNeed_password())==1){
            holder.mLatestVideoStatus.setText("密码");
        }
        holder.mLinearItemLatestContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRecyclerViewItemClickListener != null) {
                    mOnRecyclerViewItemClickListener.onRecyclerViewItemClick(v, holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideoItems.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.linear_item_home_latest_container)
        LinearLayout mLinearItemLatestContainer;
        @Bind(R.id.image_home_latest)
        ImageView mImageHomeLatest;
        @Bind(R.id.text_home_latest_name)
        TextView mTextHomeLatestName;
        @Bind(R.id.text_home_latest_location)
        TextView getmTextHomeLatestLocation;
        @Bind(R.id.latest_video_status)
        TextView mLatestVideoStatus;
        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
