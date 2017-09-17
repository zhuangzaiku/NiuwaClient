package tv.niuwa.live.living;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tv.niuwa.live.R;

/**
 * Created by Administrator on 2016/9/5.
 * Author: XuDeLong
 */
public class GiftGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<GiftModel> mGifts;

    public GiftGridViewAdapter(Context context, List mGifts) {
        this.context = context;
        this.mGifts = mGifts;
    }

    @Override
    public int getCount() {
        return mGifts.size();
    }

    @Override
    public Object getItem(int i) {
        return mGifts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GiftModel giftModel = (GiftModel) getItem(position);
        GiftViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gift, null);
            viewHolder = new GiftViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.gift_name);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.gift_image);
            viewHolder.mFramLayout = (FrameLayout) convertView.findViewById(R.id.gift_item_image_container);
            viewHolder.mNeedCoin = (TextView) convertView.findViewById(R.id.gift_need_coin);
            viewHolder.mIfContainue = (CheckBox) convertView.findViewById(R.id.gift_containue);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GiftViewHolder) convertView.getTag();
        }
        Glide.with(this.context).load(giftModel.getGifticon())
                .error(R.drawable.icon_avatar_default)
                .into(viewHolder.mImageView);
        viewHolder.mTextView.setText(giftModel.getGiftname());
        viewHolder.mNeedCoin.setText(giftModel.getNeedcoin());
        if (!"1".equals(giftModel.getContinuous())) {
            viewHolder.mIfContainue.setVisibility(View.GONE);
        }
        if (giftModel.isChecked()) {
            viewHolder.mIfContainue.setVisibility(View.VISIBLE);
        }
        viewHolder.mIfContainue.setChecked(giftModel.isChecked());
        return convertView;
    }

    public boolean isChecked(int position) {
        if (getCount() > position) {
            return mGifts.get(position).isChecked();
        }
        return false;
    }

    public void updateStatus(int position, boolean checked) {
        for (int i = 0; i < getCount(); i++) {
            mGifts.get(i).setChecked(false);
        }
        mGifts.get(position).setChecked(checked);
        notifyDataSetChanged();
    }

    public void resetAll() {
        for (int i = 0; i < getCount(); i++) {
            mGifts.get(i).setChecked(false);
        }
        notifyDataSetChanged();
    }

    class GiftViewHolder {
        public ImageView mImageView;
        public TextView mTextView;
        public FrameLayout mFramLayout;
        public TextView mNeedCoin;
        public CheckBox mIfContainue;
    }
}
