package tv.niuwa.live;

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

import java.util.ArrayList;
import java.util.List;

import tv.niuwa.live.home.model.HomeDataItem;
import tv.niuwa.live.home.model.VideoItem;
import tv.niuwa.live.living.LivingActivity;


/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 03/09/2017 00:01.
 * <p>
 * All copyright reserved.
 */

public class HomeListAdapter extends BaseAdapter {

    private Context mContext;
    private List<HomeDataItem> mUserMenuItems = new ArrayList<>();
    private LayoutInflater mInflater;

    public HomeListAdapter(Context context, List<HomeDataItem> data) {
        this.mContext = context;
        mUserMenuItems = data;
        mInflater = LayoutInflater.from(context);
    }

    public void addUserMenuItemList(List<HomeDataItem> data) {
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
        ViewHolder holder;
        final HomeDataItem data = (HomeDataItem) getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_home_data, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.channelTitle = (TextView) convertView.findViewById(R.id.channelTitle);
            holder.audienceNum = (TextView) convertView.findViewById(R.id.audienceNum);
            holder.previewImg = (ImageView) convertView.findViewById(R.id.previewImage);
            convertView.setTag(holder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoItem videoItem = new VideoItem();
                    videoItem.setRoom_id("17");
                    videoItem.setId("156277");
                    videoItem.setUser_nicename("\u7528\u6237156277");
                    videoItem.setAvatar("http://zhibo.519wan.com/data/upload/avatar/default.png");
                    videoItem.setChannel_creater("156277");
                    videoItem.setChannel_location(null);
                    videoItem.setChannel_title("\\u725b\\u54c7");
                    videoItem.setOnline_num("11");
                    videoItem.setSmeta("http://zhibo.519wan.com/data/upload/avatar/default.png");
                    videoItem.setChannel_status("2");
                    videoItem.setUser_level("1");
                    videoItem.setPrice("0");
                    videoItem.setNeed_password("0");
                    Bundle data = new Bundle();
                    data.putSerializable("videoItem", videoItem);
                    Intent intent = new Intent(mContext,LivingActivity.class);
                    intent.putExtras(data);
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(data.getTitle());
        if(TextUtils.isEmpty(data.getChannelTitle())) {
            holder.channelTitle.setVisibility(View.INVISIBLE);
        } else {
            holder.channelTitle.setText(data.getChannelTitle());
        }
        holder.audienceNum.setText(data.getAudienceNum());
        holder.previewImg.setImageResource(data.getPreviewImg());

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView channelTitle;
        TextView audienceNum;
        ImageView previewImg;
    }
}