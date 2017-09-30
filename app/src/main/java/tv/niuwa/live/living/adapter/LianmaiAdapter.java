package tv.niuwa.live.living.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smart.androidutils.images.GlideCircleTransform;
import tv.niuwa.live.R;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;
import tv.niuwa.live.living.model.DanmuModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/29.
 * Author: XuDeLong
 */
public class LianmaiAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DanmuModel> managers;

    public LianmaiAdapter(Context context, ArrayList<DanmuModel> managers) {
        this.context = context;
        this.managers = managers;
    }

    @Override
    public int getCount() {
        return managers.size();
    }

    @Override
    public Object getItem(int i) {
        return managers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        ImageView avatar;
        TextView nicename;
        Button but;
    }
    private OnRecyclerViewItemClickListener mOnRequestDataListener;

    public void setmOnRequestDataListener(OnRecyclerViewItemClickListener mOnRequestDataListener) {
        this.mOnRequestDataListener = mOnRequestDataListener;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final DanmuModel managersItem = (DanmuModel) getItem(i);
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_lianmai, null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = (ImageView) view.findViewById(R.id.manager_list_avatar);
            viewHolder.nicename = (TextView) view.findViewById(R.id.manager_list_nicename);
            viewHolder.but = (Button) view.findViewById(R.id.manager_list_cancel);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.nicename.setText(managersItem.getUserName());
        Glide.with(context).load(managersItem.getAvatar()).transform(new GlideCircleTransform(context)).error(R.drawable.icon_avatar_default).into(viewHolder.avatar);
        viewHolder.but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(null != mOnRequestDataListener){
                    mOnRequestDataListener.onRecyclerViewItemClick(view,i);
                }
            }
        });
        return view;
    }

}
