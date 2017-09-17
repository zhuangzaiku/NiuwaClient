package tv.niuwa.live.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import tv.niuwa.live.R;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.own.fans.FansItem;
import tv.niuwa.live.own.follow.FollowItem;
import tv.niuwa.live.utils.Api;

/**
 * Created by fengjh on 16/7/31.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.FansViewHolder> {

    private Context mContext;
    private ArrayList<FansItem> mFansItems;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    public SearchAdapter(Context mContext, ArrayList<FansItem> mFansItems) {
        this.mContext = mContext;
        this.mFansItems = mFansItems;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public FansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_fans_list, parent, false);
        return new FansViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final FansViewHolder holder, int position) {
       final FansItem item = mFansItems.get(position);
        Glide.with(this.mContext).load(item.getAvatar())
                .error(R.drawable.icon_avatar_default)
                .transform(new GlideCircleTransform(this.mContext))
                .into(holder.mFansAvatar);
        holder.mFansNicename.setText(item.getUser_nicename());
        if(StringUtils.isNotEmpty(item.getSignature()))
        holder.mFansSignature.setText(item.getSignature());
        holder.mFansLevel.setText(item.getUser_level());
        int level = Integer.parseInt(item.getUser_level());
        Drawable levelMoon1 = this.mContext.getResources().getDrawable(R.drawable.icon_small_star);
        levelMoon1.setBounds(0, 0, levelMoon1.getMinimumWidth(), levelMoon1.getMinimumHeight());
        if(level<4){
            holder.mFansLevel.setBackgroundResource(R.drawable.level1);
        }else if(level<16){
            holder.mFansLevel.setBackgroundResource(R.drawable.level2);
        }else{
            holder.mFansLevel.setBackgroundResource(R.drawable.level3);
        }
        holder.mItemContainer.setTag(item.getId());
        if("1".equals(item.getSex())){
            holder.mFansSex.setImageResource(R.drawable.userinfo_male);
        }
        if( !"0".equals(item.getIs_truename())){
            holder.mFansReal.setVisibility(View.VISIBLE);
        }
        holder.mIsLiving.setVisibility(View.GONE);
        if("2".equals(item.getChannel_status())){
            holder.mIsLiving.setVisibility(View.VISIBLE);
        }
        holder.mImageAddAttention.setVisibility(View.VISIBLE);
        holder.mFansBtnAttention.setText("关注");
        if("1".equals(item.getAttention_status())){
            //holder.mFansBtnAttention.setBackground(mContext.getResources().getDrawable(R.drawable.checkbox_follow_shape_blue));
            holder.mFansBtnAttention.setText("取消关注");
            holder.mFansBtnAttention.setTextColor(mContext.getResources().getColor(R.color.colorGrayFont));
            holder.mImageAddAttention.setVisibility(View.GONE);
        }
        holder.mFansBtnAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextView v = (TextView)view;
                if("取消关注".equals(v.getText())){
                    JSONObject params = new JSONObject();
                    params.put("token", SharePrefsUtils.get(mContext,"user","token",""));
                    params.put("userid",item.getId());
                    Api.cancelAttention(mContext, params, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            FollowItem tem = new FollowItem();
                            tem.setId(item.getId());
                            v.setText("关注");
                            v.setTextColor(mContext.getResources().getColor(R.color.home_tab1));
                            holder.mImageAddAttention.setVisibility(View.VISIBLE);
                            // mFollowItems.remove(tem);
                            // FollowListAdapter.this.notifyDataSetChanged();
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    JSONObject params = new JSONObject();
                    params.put("token", SharePrefsUtils.get(mContext,"user","token",""));
                    params.put("userid",item.getId());
                    Api.addAttention(mContext, params, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            FollowItem tem = new FollowItem();
                            tem.setId(item.getId());
                            v.setText("取消关注");
                            v.setTextColor(mContext.getResources().getColor(R.color.colorGrayFont));
                            holder.mImageAddAttention.setVisibility(View.GONE);
                            // mFollowItems.remove(tem);
                            // FollowListAdapter.this.notifyDataSetChanged();
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

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
        return mFansItems.size();
    }

    class FansViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.fans_avatar)
        ImageView mFansAvatar;
        @Bind(R.id.fans_level)
        TextView mFansLevel;
        @Bind(R.id.fans_nicename)
        TextView mFansNicename;
        @Bind(R.id.fans_sex)
        ImageView mFansSex;
        @Bind(R.id.fans_signature)
        TextView mFansSignature;
        @Bind(R.id.fans_real)
        ImageView mFansReal;
        @Bind(R.id.fans_btn_attention)
        TextView mFansBtnAttention;
        @Bind(R.id.item_container)
        LinearLayout mItemContainer;
        @Bind(R.id.is_living)
        TextView mIsLiving;
        @Bind(R.id.image_add_attention)
        ImageView mImageAddAttention;

        public FansViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
