package tv.niuwa.live.own.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tv.niuwa.live.HomeListAdapter;
import tv.niuwa.live.R;
import tv.niuwa.live.home.model.HomeDataItem;
import tv.niuwa.live.home.model.VideoItem;
import tv.niuwa.live.living.LivingActivity;
import tv.niuwa.live.own.bean.RewardItem;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 22/09/2017 14:17.
 * <p>
 * All copyright reserved.
 */

public class MyRewardAdapter extends BaseAdapter{

    private Context mContext;
    private List<RewardItem> mUserMenuItems = new ArrayList<>();
    private LayoutInflater mInflater;

    public MyRewardAdapter(Context context, List<RewardItem> data) {
        this.mContext = context;
        mUserMenuItems = data;
        mInflater = LayoutInflater.from(context);
    }

    public void addUserMenuItemList(List<RewardItem> data) {
        mUserMenuItems.clear();
        mUserMenuItems.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mUserMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyRewardAdapter.ViewHolder holder;
        final RewardItem data = (RewardItem) getItem(position);
        if (convertView == null) {
            holder = new MyRewardAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_my_reward, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
            convertView.setTag(holder);
        } else {
            holder = (MyRewardAdapter.ViewHolder) convertView.getTag();
        }

        holder.title.setText(data.getName());
//        holder.desc.setText(data.getAudienceNum());

//        holder.thumb.setImageResource(data.getAvatar());
        Glide.with(mContext).load(data.getAvatar()).into(holder.thumb);

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView desc;
        ImageView thumb;
    }
}
