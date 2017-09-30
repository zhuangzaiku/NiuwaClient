package tv.niuwa.live.living.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.util.ArrayList;

import tv.niuwa.live.R;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.living.model.ManagerModel;
import tv.niuwa.live.utils.Api;

/**
 * Created by Administrator on 2016/8/29.
 * Author: XuDeLong
 */
public class ManagerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ManagerModel> managers;

    public ManagerAdapter(Context context, ArrayList<ManagerModel> managers) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ManagerModel managersItem = (ManagerModel) getItem(i);
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_manager, null);
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
                final Button but = (Button)view;
                but.setEnabled(false);
                final JSONObject temp1 = new JSONObject();
                temp1.put("token", SharePrefsUtils.get(context,"user","token",""));
                temp1.put("id", managersItem.getUserId());
                if("设为场控".equals(but.getText())){
                    Api.setManager(context, temp1, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            but.setEnabled(true);
                            but.setText("取消场控");
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            but.setEnabled(true);
                        }
                    });
                }else{
                    Api.cancelManager(context, temp1, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            but.setEnabled(true);
                            but.setText("设为场控");
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            but.setEnabled(true);
                        }
                    });
                }
            }
        });
        return view;
    }

}
