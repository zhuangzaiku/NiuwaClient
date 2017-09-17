package tv.niuwa.live.own.follow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smart.androidutils.images.GlideCircleTransform;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import tv.niuwa.live.R;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;

/**
 * Created by fengjh on 16/7/31.
 */
public class FollowListAdapterother extends RecyclerView.Adapter<FollowListAdapterother.FollowViewHolder> {

    private Context mContext;
    private ArrayList<FollowItem> mFollowItems;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    public FollowListAdapterother(Context mContext, ArrayList<FollowItem> mFollowItems) {
        this.mContext = mContext;
        this.mFollowItems = mFollowItems;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public FollowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_follow_list, parent, false);
        return new FollowViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final FollowViewHolder holder, int position) {

        final FollowItem item = mFollowItems.get(position);
        Glide.with(this.mContext).load(item.getAvatar())
                .error(R.drawable.icon_avatar_default)
                .transform(new GlideCircleTransform(this.mContext))
                .into(holder.mFollowAvatar);
        holder.mFollowNicename.setText(item.getUser_nicename());
        holder.mFollowSignature.setText(item.getSignature());
        holder.mFollowLevel.setText(item.getUser_level());
        int level = Integer.parseInt(item.getUser_level());
        if(level<5){
            holder.mFollowLevel.setBackgroundResource(R.drawable.level1);
        }
        if(level>4 && level<9 ){
            holder.mFollowLevel.setBackgroundResource(R.drawable.level2);
        }
        if(level>8 && level<13 ){
            holder.mFollowLevel.setBackgroundResource(R.drawable.level3);
        }
        if(level>12 ){
            holder.mFollowLevel.setBackgroundResource(R.drawable.level3);
        }
        if("1".equals(item.getSex())){
            holder.mFollowSex.setImageResource(R.drawable.userinfo_male);
        }
        if("1".equals(item.getIs_truename())){
            holder.mFollowReal.setVisibility(View.VISIBLE);
        }
        holder.mItemContainer.setTag(item.getId());
        holder.mItemContainer.setOnClickListener(new View.OnClickListener() {
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
        return mFollowItems.size();
    }

    class FollowViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.follow_avatar)
        ImageView mFollowAvatar;
        @Bind(R.id.follow_level)
        TextView mFollowLevel;
        @Bind(R.id.follow_nicename)
        TextView mFollowNicename;
        @Bind(R.id.follow_sex)
        ImageView mFollowSex;
        @Bind(R.id.follow_signature)
        TextView mFollowSignature;
        @Bind(R.id.follow_real)
        ImageView mFollowReal;
        @Bind(R.id.item_container)
        LinearLayout mItemContainer;
        public FollowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
