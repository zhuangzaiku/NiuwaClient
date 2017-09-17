package tv.niuwa.live.own.userinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import tv.niuwa.live.R;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 01/09/2017 17:01.
 * <p>
 * All copyright reserved.
 */

public class UserMenuListAdapter extends BaseAdapter {
    private Context mContext;
    private List<UserMenuItem> mUserMenuItems = new ArrayList<>();
    private LayoutInflater mInflater;

    public UserMenuListAdapter(Context context, List<UserMenuItem> data) {
        this.mContext = context;
        mUserMenuItems = data;
        mInflater = LayoutInflater.from(context);
    }

    public void addUserMenuItemList(List<UserMenuItem> data) {
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
        final UserMenuItem menu = (UserMenuItem) getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.user_center_menu_item, parent, false);
            holder.paymentTip = (TextView) convertView.findViewById(R.id.menu_text);
            holder.paymentIcon = (ImageView) convertView.findViewById(R.id.menu_icon);
            convertView.setTag(holder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.paymentTip.setText(menu.getMenuName());
        holder.paymentIcon.setImageResource(menu.getMenuIconId());

        return convertView;
    }

    static class ViewHolder {
        TextView paymentTip;
        ImageView paymentIcon;
    }
}