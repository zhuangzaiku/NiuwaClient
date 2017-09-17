package tv.niuwa.live.living;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tv.niuwa.live.R;

/**
 * Created by Administrator on 2016/8/29.
 * Author: XuDeLong
 */
public class DanmuAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DanmuModel> danmu;

    public DanmuAdapter(Context context, ArrayList<DanmuModel> danmu) {
        this.context = context;
        this.danmu = danmu;
    }

    @Override
    public int getCount() {
        return danmu.size();
    }

    @Override
    public Object getItem(int i) {
        return danmu.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        TextView textview_content;
        TextView textview_user_level;
        TextView textview_user_name;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final DanmuModel danmuItem = (DanmuModel) getItem(i);
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_danmu, null);
            viewHolder = new ViewHolder();
            viewHolder.textview_user_level = (TextView) view.findViewById(R.id.danmu_text_user_level);
            viewHolder.textview_user_name = (TextView) view.findViewById(R.id.danmu_user_name);
            viewHolder.textview_content = (TextView) view.findViewById(R.id.danmu_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textview_user_level.setVisibility(View.VISIBLE);
        viewHolder.textview_user_name.setTextColor(Color.YELLOW);
        SpannableString spannableString = new SpannableString("");
        switch (danmuItem.getType()){
            //普通消息
            case "1":
                spannableString = new SpannableString("            "+danmuItem.getUserName()+":" + danmuItem.getContent());
                //viewHolder.textview_user_name.setTextColor(Color.YELLOW);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.WHITE);
                spannableString.setSpan(colorSpan, 12+danmuItem.getUserName().length(),12+danmuItem.getUserName().length()+danmuItem.getContent().length()+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                viewHolder.textview_user_level.setText(danmuItem.getUserLevel());
                int level = Integer.parseInt(danmuItem.getUserLevel());
                if(level<5){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level1);
                }
                if(level>4 && level<9 ){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level2);
                }
                if(level>8 && level<13 ){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level3);
                }
                if(level>12 ){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level3);
                }
                break;
            //礼物消息
            case "2":
                spannableString = new SpannableString("            "+danmuItem.getUserName()+":" + danmuItem.getContent());
                ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.RED);
                spannableString.setSpan(colorSpan1, 12+danmuItem.getUserName().length(),12+danmuItem.getUserName().length()+danmuItem.getContent().length()+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                //viewHolder.textview_content.setTextColor(context.getResources().getColor(R.color.danmu_notice));
                viewHolder.textview_user_level.setText(danmuItem.getUserLevel());
                int level1 = Integer.parseInt(danmuItem.getUserLevel());
                if(level1<5){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level1);
                }
                if(level1>4 && level1<9 ){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level2);
                }
                if(level1>8 && level1<13 ){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level3);
                }
                if(level1>12 ){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level3);
                }
                break;
            //系统消息
            case "3":
                spannableString = new SpannableString(danmuItem.getUserName()+":" + danmuItem.getContent());
                ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.RED);
                spannableString.setSpan(colorSpan2, danmuItem.getUserName().length(),danmuItem.getUserName().length()+danmuItem.getContent().length()+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                //viewHolder.textview_content.setTextColor(context.getResources().getColor(R.color.danmu_notice));
                //viewHolder.textview_user_name.setTextColor(Color.YELLOW);
                viewHolder.textview_user_level.setVisibility(View.GONE);
                break;
            //点亮❤
            case "4":
                spannableString = new SpannableString("            "+danmuItem.getUserName()+":" + danmuItem.getContent());
                ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.RED);
                spannableString.setSpan(colorSpan3, 12+danmuItem.getUserName().length(),12+danmuItem.getUserName().length()+danmuItem.getContent().length()+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                viewHolder.textview_user_level.setText(danmuItem.getUserLevel());
                int level2 = Integer.parseInt(danmuItem.getUserLevel());
                if(level2<5){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level1);
                }
                if(level2>4 && level2<9 ){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level2);
                }
                if(level2>8 && level2<13 ){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level3);
                }
                if(level2>12 ){
                    viewHolder.textview_user_level.setBackgroundResource(R.drawable.level3);
                }
                break;
            //系统消息=  离开房间
            case "5":
                spannableString = new SpannableString(danmuItem.getUserName()+":" + danmuItem.getContent());
                ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.RED);
                spannableString.setSpan(colorSpan4, danmuItem.getUserName().length(),danmuItem.getUserName().length()+danmuItem.getContent().length()+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                //viewHolder.textview_content.setTextColor(context.getResources().getColor(R.color.danmu_notice));
                viewHolder.textview_user_level.setVisibility(View.GONE);
                break;
            //系统消息=  进入房间
            case "6":
                spannableString = new SpannableString(danmuItem.getUserName()+":" + danmuItem.getContent());
                ForegroundColorSpan colorSpan5 = new ForegroundColorSpan(Color.RED);
                spannableString.setSpan(colorSpan5, danmuItem.getUserName().length(),danmuItem.getUserName().length()+danmuItem.getContent().length()+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                //viewHolder.textview_content.setTextColor(context.getResources().getColor(R.color.danmu_notice));
                viewHolder.textview_user_level.setVisibility(View.GONE);
                break;
            //系统消息=  申请连麦  //7弹幕
            case "8":
                spannableString = new SpannableString(danmuItem.getUserName()+":" + danmuItem.getContent());
                ForegroundColorSpan colorSpan6 = new ForegroundColorSpan(Color.RED);
                spannableString.setSpan(colorSpan6, danmuItem.getUserName().length(),danmuItem.getUserName().length()+danmuItem.getContent().length()+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                //viewHolder.textview_user_name.setTextColor(Color.YELLOW);
                viewHolder.textview_content.setTextColor(context.getResources().getColor(R.color.danmu_notice));
                viewHolder.textview_user_level.setVisibility(View.GONE);
                break;
            //发送禁言
            case "11":
                spannableString = new SpannableString(danmuItem.getUserName()+":" + danmuItem.getContent());
                ForegroundColorSpan colorSpan7 = new ForegroundColorSpan(Color.RED);
                spannableString.setSpan(colorSpan7, danmuItem.getUserName().length(),danmuItem.getUserName().length()+danmuItem.getContent().length()+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                //viewHolder.textview_content.setTextColor(context.getResources().getColor(R.color.danmu_notice));
                viewHolder.textview_user_name.setText("系统消息");
                //viewHolder.textview_user_name.setTextColor(Color.YELLOW);
                viewHolder.textview_user_level.setVisibility(View.GONE);
                break;
        }
        //viewHolder.textview_content.setText(danmuItem.getContent());
         viewHolder.textview_user_name.setText(spannableString);

        return view;
    }

}
