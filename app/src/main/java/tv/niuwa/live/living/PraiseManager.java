package tv.niuwa.live.living;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import tv.niuwa.live.R;
import tv.niuwa.live.living.model.DanmuModel;
import tv.niuwa.live.utils.DisplayUtil;
import tv.niuwa.live.view.FadingScrollView;

/**
 * @author Ronan.zhuang
 * @Date 4/11/17.
 * All copyright reserved.
 */

public class PraiseManager {

    private static final String TAG = PraiseManager.class.getSimpleName();
    private Context mContext;

    private ArrayList<DanmuModel> mMsgList = new ArrayList<>();
    private FadingScrollView mScrollView;
    private LinearLayout mParent;
    private int mColorIndex = 0;
    private String[] mColors = new String[]{"#03e913", "#38f3ff", "#ffdc38", "#8d41ff"};

    public PraiseManager(Context mContext, FadingScrollView scrollView) {
        this.mContext = mContext;
        this.mScrollView = scrollView;
        mParent = (LinearLayout) mScrollView.findViewById(R.id.praise_parent);
        mScrollView.setOnTouchListener(null);
        mParent.setOnTouchListener(null);
    }

    public void setmColors(String[] mColors) {
        this.mColors = mColors;
    }

    public void addDanmaView(DanmuModel msg) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.praise_list_item, mParent, false);

        TextView content = (TextView)view.findViewById(R.id.content);
        content.setTextColor(Color.parseColor(mColors[mColorIndex % 4]));
        content.setText(msg.getUserName() + msg.getContent());
        view.setAlpha(1.0f);
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.praise_out);
        view.startAnimation(anim);

        mParent.addView(view, mParent.getChildCount());
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight(); // 获取高度
        mScrollView.smoothScrollBy(0, height, 200, new FadingScrollView.OnScrollListener() {
            @Override
            public void onScrollFinished() {
                int size = mParent.getChildCount();
                for (int i = size - 1; i > 0; i--) {
                    View child = mParent.getChildAt(i);
                    if(child.getVisibility() == View.INVISIBLE)
                        mParent.removeView(child);
                }
            }
        });
        int size = mParent.getChildCount();
        int totalHeight = 0;
        int margin = DisplayUtil.dipToPix(mContext, 13);
        int maxHeight = DisplayUtil.dipToPix(mContext, 120);
        for (int i = size - 1; i > 0; i--) {
            View child = mParent.getChildAt(i);
            totalHeight += child.getMeasuredHeight() + margin;
            if (totalHeight > maxHeight) {
                child.clearAnimation();
                child.setVisibility(View.INVISIBLE);
            }
        }

        mColorIndex++;
    }

    public void clear() {
        mMsgList.clear();
        int size = mParent.getChildCount();
        for (int i = size - 1; i > 0; i--) {
            View child = mParent.getChildAt(i);
            mParent.removeView(child);
        }
        mColorIndex= 0;
    }
}
