package tv.niuwa.live.music;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smart.androidutils.utils.LogUtils;
import tv.niuwa.live.R;
import tv.niuwa.live.home.intf.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fengjh on 16/7/31.
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicViewHolder> {

    private Context mContext;
    private ArrayList<MusicItem> mMusicItems;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    //private View mHeaderView;
    public MusicListAdapter(Context mContext, ArrayList<MusicItem> mMusicItems) {
        this.mContext = mContext;
        this.mMusicItems = mMusicItems;
    }


    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }


    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
            return new MusicViewHolder(inflate);

    }

    @Override
    public void onBindViewHolder(final MusicViewHolder holder, final int position) {
        final MusicItem item = mMusicItems.get(position);
        if(null != holder.mMusicName){
            holder.mMusicName.setText(item.getName());
            holder.mBtnDownload.setText("下载");
            holder.mSingerName.setText(item.getSinger());
            if(null == item.getProgress()){
                holder.progress.setVisibility(View.INVISIBLE);
            }else{
                if(0 < Integer.parseInt(item.getProgress())){
                    holder.progress.setVisibility(View.VISIBLE);
                    holder.progress.setProgress(Integer.parseInt(item.getProgress()));
                }
            }
            if("1".equals(item.getType())){
                holder.mBtnDownload.setText("使用");
            }
            holder.mBtnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnRecyclerViewItemClickListener.onRecyclerViewItemClick(view,position);
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return mMusicItems.size();
    }



    class MusicViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.music_name)
        TextView mMusicName;
        @Bind(R.id.btn_download)
        Button mBtnDownload;
        @Bind(R.id.singer_name)
        TextView mSingerName;
        @Bind(R.id.progress)
        ProgressBar progress;
        public MusicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg)
        {
            LogUtils.i("2");
            Bundle data = msg.getData();
            mMusicItems.get(data.getInt("pos")).setProgress(data.getInt("progress")+"");
            MusicListAdapter.this.notifyItemChanged(data.getInt("pos"));


        }
    };
    public Handler getHandler(){
        return mHandler;
    }


}
