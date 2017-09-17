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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tv.niuwa.live.R;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;
import tv.niuwa.live.home.model.VideoItem;

/**
 * Created by fengjh on 16/7/19.
 */
public class HotRecyclerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;

    private static final int TYPE_FOOTER = 1;

    private static final int TYPE_ITEM = 2;

    private static final int TYPE_EMPTY = 3;

    private View mHeaderView;

    private View mFooterView;

    private View mEmptyView;

    private Context mContext;
    private ArrayList<VideoItem> mItems;
    private int imageWidth;
    private int imageHeight;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public HotRecyclerListAdapter(Context mContext, ArrayList<VideoItem> mVideoItems) {
        this.mContext = mContext;
        this.mItems = mVideoItems;
        imageWidth = DensityUtils.screenWidth(mContext);
        imageHeight = (imageWidth * 650) / 750;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        int size = mItems.size();
        if (size == 0 && null != mEmptyView) {
            return TYPE_EMPTY;
        } else if (position < getHeadViewSize()) {
            return TYPE_HEADER;
        } else if (position >= size + getHeadViewSize()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_video_list, parent, false);
            return new VideoViewHolder(inflate);
        } else if (viewType == TYPE_HEADER) {
            View v = mHeaderView;
            return new HeaderHolder(v);
        } else if (viewType == TYPE_FOOTER) {
            View v = mFooterView;
            return new FooterHolder(v);
        } else if (viewType == TYPE_EMPTY) {
            View v = mEmptyView;
            return new EmptyHolder(v);
        }
        throw new RuntimeException("there is no type which matches this type " + viewType + ",please make sure your are using type correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoViewHolder) {
            VideoItem item = getItem(position);
            final VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageWidth, imageHeight);
            videoViewHolder.mImageVideoPreview.setLayoutParams(params);
            Glide.with(this.mContext).load(item.getAvatar())
                    .error(R.drawable.icon_avatar_default)
                    .transform(new GlideCircleTransform(this.mContext))
                    .into(videoViewHolder.mImageLiveUserAvatar);
            Glide.with(this.mContext).load(item.getSmeta())
                    .error(R.drawable.default_bg)
                    .into(videoViewHolder.mImageVideoPreview);
            videoViewHolder.mTextTitle.setText(item.getChannel_title());
            videoViewHolder.mTextUserLevel.setText(item.getUser_level());

            int level = Integer.parseInt(item.getUser_level());
            if(level<5){
                videoViewHolder.mTextUserLevel.setBackgroundResource(R.drawable.level1);
            }
            if(level>4 && level<9 ){
                videoViewHolder.mTextUserLevel.setBackgroundResource(R.drawable.level2);
            }
            if(level>8 && level<13 ){
                videoViewHolder.mTextUserLevel.setBackgroundResource(R.drawable.level3);
            }
            if(level>12 ){
                videoViewHolder.mTextUserLevel.setBackgroundResource(R.drawable.level3);
            }

            videoViewHolder.mTextLiveOnlineNum.setText(item.getOnline_num());
            videoViewHolder.mTextLiveTitle.setText(item.getChannel_title());
            if(StringUtils.isNotEmpty(item.getChannel_location()))
            videoViewHolder.mTextLiveUserLocation.setText(item.getChannel_location());
            videoViewHolder.mTextLiveUserNicename.setText(item.getUser_nicename());
            videoViewHolder.mImageVideoPreview.setLayoutParams(params);
            if (StringUtils.isNotEmpty(item.getPrice()) && Integer.parseInt(item.getPrice())>0){
                videoViewHolder.mTextLiveStatus.setText("付费"+item.getPrice());
            }
            if(StringUtils.isNotEmpty(item.getNeed_password()) && Integer.parseInt(item.getNeed_password())==1){
                videoViewHolder.mTextLiveStatus.setText("密码");
            }
            videoViewHolder.mLinearVideoContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerViewItemClickListener != null) {
                        int layoutPosition = videoViewHolder.getLayoutPosition() - getHeadViewSize();
                        mOnRecyclerViewItemClickListener.onRecyclerViewItemClick(v, layoutPosition);
                    }
                }
            });
        } else if (holder instanceof HeaderHolder) {

        } else if (holder instanceof FooterHolder) {

        } else if (holder instanceof EmptyHolder) {

        }
    }

    private VideoItem getItem(int position) {
        return mItems.get(position - getHeadViewSize());
    }

    private int getHeadViewSize() {
        return mHeaderView == null ? 0 : 1;
    }

    private int getFooterViewSize() {
        return mFooterView == null ? 0 : 1;
    }

    public void addHeader(View header) {
        mHeaderView = header;
        notifyItemInserted(0);
    }

    public void removeHeader() {
        notifyItemRemoved(0);
        mHeaderView = null;
    }

    public void addFooter(View footer) {
        mFooterView = footer;
        notifyItemInserted(getHeadViewSize() + mItems.size());
    }

    public void removeFooter() {
        notifyItemRemoved(getHeadViewSize() + mItems.size());
        mFooterView = null;
    }

    public void addDataList(List<VideoItem> data) {
        mItems.addAll(data);
        notifyItemInserted(getHeadViewSize() + mItems.size() - 1);
    }

    public void refreshData(List<VideoItem> data) {
        mItems.clear();
        addDataList(data);
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        int size = mItems.size();
        if (size == 0 && null != mEmptyView) {
            count = 1;
        } else {
            count = getHeadViewSize() + size + getFooterViewSize();
        }
        return count;
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
        @Bind(R.id.tv_user_level)
        TextView mTextUserLevel;
        @Bind(R.id.text_live_user_location)
        TextView mTextLiveUserLocation;
        @Bind(R.id.text_live_online_num)
        TextView mTextLiveOnlineNum;
        @Bind(R.id.text_live_status)
        TextView mTextLiveStatus;
        @Bind(R.id.text_live_title)
        TextView mTextLiveTitle;
        @Bind(R.id.text_title)
        TextView mTextTitle;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    class EmptyHolder extends RecyclerView.ViewHolder {

        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }

}
