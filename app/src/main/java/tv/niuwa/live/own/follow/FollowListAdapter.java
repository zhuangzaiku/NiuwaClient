package tv.niuwa.live.own.follow;

import android.content.Context;
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
import tv.niuwa.live.utils.Api;

/**
 * Created by fengjh on 16/7/31.
 */
public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.FollowViewHolder> {

    private Context mContext;
    private ArrayList<FollowItem> mFollowItems;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    public FollowListAdapter(Context mContext, ArrayList<FollowItem> mFollowItems) {
        this.mContext = mContext;
        this.mFollowItems = mFollowItems;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public FollowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_follow_list1, parent, false);
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
        if(StringUtils.isNotEmpty(item.getSignature()))
        holder.mFollowSignature.setText(item.getSignature());
        holder.mIsLiving.setVisibility(View.GONE);
        if("2".equals(item.getChannel_status())){
            holder.mIsLiving.setVisibility(View.VISIBLE);
        }
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
        holder.mItemContainer.setTag(item.getId());
        holder.mItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRecyclerViewItemClickListener != null) {
                    mOnRecyclerViewItemClickListener.onRecyclerViewItemClick(v, holder.getLayoutPosition());
                }
            }
        });
        holder.mFollowBtnCancel.setVisibility(View.VISIBLE);
        holder.mFollowBtnCancel.setOnClickListener(new View.OnClickListener() {
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
        @Bind(R.id.follow_btn_cancel)
        TextView mFollowBtnCancel;
        @Bind(R.id.is_living)
        TextView mIsLiving;
        @Bind(R.id.image_add_attention)
        ImageView mImageAddAttention;
        public FollowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
