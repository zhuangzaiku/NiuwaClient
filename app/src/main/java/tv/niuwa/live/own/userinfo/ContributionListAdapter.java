package tv.niuwa.live.own.userinfo;

import android.content.Context;
import android.graphics.Color;
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
public class ContributionListAdapter extends RecyclerView.Adapter<ContributionListAdapter.ContributionViewHolder> {

    private Context mContext;
    private ArrayList<ContributionItem> mContributionItems;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    public ContributionListAdapter(Context mContext, ArrayList<ContributionItem> mContributionItems) {
        this.mContext = mContext;
        this.mContributionItems = mContributionItems;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public ContributionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_contribution_list, parent, false);
        return new ContributionViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final ContributionViewHolder holder, int position) {
        ContributionItem item = mContributionItems.get(position);
        Glide.with(this.mContext).load(item.getAvatar())
                .error(R.id.image_own_user_avatar)
                .transform(new GlideCircleTransform(this.mContext))
                .into(holder.mContributionAvatar);
        holder.mItemContainer.setTag(item.getId());
        holder.mContributionNicename.setText(item.getUser_nicename());
        holder.mContributionNum.setText("NO."+(position+1));
        holder.mContributionLevel.setText(item.getUser_level());
        holder.mContributionAvatarBg.setVisibility(View.GONE);
        holder.mContributionNum.setTextColor(Color.BLACK);
        if(position == 0){
            holder.mContributionAvatarBg.setVisibility(View.VISIBLE);
           holder.mContributionAvatarBg.setImageResource(R.drawable.num1);
            holder.mContributionNum.setTextColor(Color.RED);
        }
        if(position == 1){
            holder.mContributionAvatarBg.setVisibility(View.VISIBLE);
            holder.mContributionAvatarBg.setImageResource(R.drawable.num2);
            holder.mContributionNum.setTextColor(mContext.getResources().getColor(R.color.num2));
        }
        if(position == 2){
            holder.mContributionAvatarBg.setVisibility(View.VISIBLE);
            holder.mContributionAvatarBg.setImageResource(R.drawable.num3);
            holder.mContributionNum.setTextColor(mContext.getResources().getColor(R.color.num3));
        }
        int level = Integer.parseInt(item.getUser_level());
        if(level<5){
            holder.mContributionLevel.setBackgroundResource(R.drawable.level1);
        }
        if(level>4 && level<9 ){
            holder.mContributionLevel.setBackgroundResource(R.drawable.level2);
        }
        if(level>8 && level<13 ){
            holder.mContributionLevel.setBackgroundResource(R.drawable.level3);
        }
        if(level>12 ){
            holder.mContributionLevel.setBackgroundResource(R.drawable.level3);
        }
        holder.mContributionSend.setText(item.getSend_num());
        if("1".equals(item.getSex())){
            holder.mContributionSex.setImageResource(R.drawable.userinfo_male);
        }
        if("1".equals(item.getIs_truename())){
            holder.mContributionReal.setVisibility(View.VISIBLE);
        }
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
        return mContributionItems.size();
    }

    class ContributionViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text_num)
        TextView mContributionNum;
        @Bind(R.id.contribution_avatar)
        ImageView mContributionAvatar;
        @Bind(R.id.contribution_level)
        TextView mContributionLevel;
        @Bind(R.id.contribution_nicename)
        TextView mContributionNicename;
        @Bind(R.id.contribution_sex)
        ImageView mContributionSex;
        @Bind(R.id.fancontribution_send)
        TextView mContributionSend;
        @Bind(R.id.contribution_real)
        ImageView mContributionReal;
        @Bind(R.id.contribution_item_container)
        LinearLayout mItemContainer;
        @Bind(R.id.contribution_avatar_bg)
        ImageView mContributionAvatarBg;
        public ContributionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
