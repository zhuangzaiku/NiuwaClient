package tv.niuwa.live.living;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import tv.niuwa.live.R;

/**
 * Created by Administrator on 2016/8/29.
 * Author: XuDeLong
 */
public class OnlineUserAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<UserModel> users;

    public OnlineUserAdapter(Context context, ArrayList<UserModel> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        LinearLayout live_online_users_container;
        CircleImageView live_online_user_avatar;
        TextView iv_level;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final UserModel userItem = (UserModel) getItem(i);
        ViewHolder viewHolder = null;
            view = LayoutInflater.from(context).inflate(R.layout.item_live_user, null);
            viewHolder = new ViewHolder();
            viewHolder.live_online_users_container = (LinearLayout) view.findViewById(R.id.live_online_users_container);
            viewHolder.live_online_user_avatar = (CircleImageView) view.findViewById(R.id.live_online_user_avatar);
            viewHolder.iv_level = (TextView) view.findViewById(R.id.iv_level);
            view.setTag(viewHolder);
        viewHolder.live_online_users_container.setTag(userItem.getId());
        Glide.with(context).load(userItem.getAvatar())
                .error(R.drawable.icon_avatar_default)
                .into(viewHolder.live_online_user_avatar);
        viewHolder.iv_level.setText(userItem.getLevel());

        if(null != userItem.getLevel()){
            int level = Integer.parseInt(userItem.getLevel());
            if(level<5){
                viewHolder.iv_level.setBackgroundResource(R.drawable.level1);
            }
            if(level>4 && level<9 ){
                viewHolder.iv_level.setBackgroundResource(R.drawable.level2);
            }
            if(level>8 && level<13 ){
                viewHolder.iv_level.setBackgroundResource(R.drawable.level3);
            }
            if(level>12 ){
                viewHolder.iv_level.setBackgroundResource(R.drawable.level3);
            }
        }
        return view;
    }

}
