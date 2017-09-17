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
import com.smart.androidutils.images.GlideCircleTransform;
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
public class FollowRecyclerListAdapter extends RecyclerView.Adapter<FollowRecyclerListAdapter.VideoViewHolder> {

    private Context mContext;
    private ArrayList<VideoItem> mVideoItems;
    private int imageWidth;
    private int imageHeight;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public FollowRecyclerListAdapter(Context mContext, ArrayList<VideoItem> mVideoItems) {
        this.mContext = mContext;
        this.mVideoItems = mVideoItems;
        imageWidth = DensityUtils.screenWidth(mContext);
        imageHeight = (imageWidth * 650) / 750;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_video_list, parent, false);
        return new VideoViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageWidth, imageHeight);
        VideoItem item = mVideoItems.get(position);
        Glide.with(this.mContext).load(item.getAvatar())
                .error(R.drawable.icon_avatar_default)
                .transform(new GlideCircleTransform(this.mContext))
                .into(holder.mImageLiveUserAvatar);
        Glide.with(this.mContext).load(item.getSmeta())
                .error(R.drawable.default_bg)
                .into(holder.mImageVideoPreview);
        holder.mTextTitle.setText(item.getChannel_title());
        holder.mTextUserLevel.setText(item.getUser_level());
        holder.mTextLiveOnlineNum.setText(item.getOnline_num());
        holder.mTextLiveTitle.setText(item.getChannel_title());
        if(StringUtils.isNotEmpty(item.getChannel_location()))
        holder.mTextLiveUserLocation.setText(item.getChannel_location());
        holder.mTextLiveUserNicename.setText(item.getUser_nicename());
        holder.mImageVideoPreview.setLayoutParams(params);
        if (StringUtils.isNotEmpty(item.getPrice()) && Integer.parseInt(item.getPrice())>0){
            holder.mTextLiveStatus.setText("付费"+item.getPrice());
        }
        if(StringUtils.isNotEmpty(item.getNeed_password()) && Integer.parseInt(item.getNeed_password())==1){
            holder.mTextLiveStatus.setText("密码");
        }

        int level = Integer.parseInt(item.getUser_level());
        if(level<5){
            holder.mTextUserLevel.setBackgroundResource(R.drawable.level1);
        }
        if(level>4 && level<9 ){
            holder.mTextUserLevel.setBackgroundResource(R.drawable.level2);
        }
        if(level>8 && level<13 ){
            holder.mTextUserLevel.setBackgroundResource(R.drawable.level3);
        }
        if(level>12 ){
            holder.mTextUserLevel.setBackgroundResource(R.drawable.level3);
        }

        holder.mLinearVideoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRecyclerViewItemClickListener != null) {
                    mOnRecyclerViewItemClickListener.onRecyclerViewItemClick(v, holder.getLayoutPosition());
                }
            }
        });
        //TODO 用户等级
    }

    @Override
    public int getItemCount() {
        return mVideoItems.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_linear_video_container)
        LinearLayout mLinearVideoContainer;
        @Bind(R.id.item_image_video_preview)
        ImageView mImageVideoPreview;
        @Bind(R.id.image_live_user_avatar)
        ImageView mImageLiveUserAvatar;
        @Bind(R.id.text_live_user_nicename)
        TextView mTextLiveUserNicename;
        @Bind(R.id.text_live_user_location)
        TextView mTextLiveUserLocation;
        @Bind(R.id.text_live_online_num)
        TextView mTextLiveOnlineNum;
        @Bind(R.id.text_live_status)
        TextView mTextLiveStatus;
        @Bind(R.id.text_live_title)
        TextView mTextLiveTitle;
        @Bind(R.id.tv_user_level)
        TextView mTextUserLevel;
        @Bind(R.id.text_title)
        TextView mTextTitle;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
