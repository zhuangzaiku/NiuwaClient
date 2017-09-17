package tv.niuwa.live.home.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/10/9.
 * Author: XuDeLong
 */
public class GridLayoutManagerTopic extends GridLayoutManager {

    private int mChildPerLines;
    public GridLayoutManagerTopic(Context context, int spanCount) {
        super(context, spanCount);
        mChildPerLines=spanCount;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);
        int height = 0;
        for (int i = 0; i < getItemCount(); ) {
            height = height + 50;
            i = i + mChildPerLines;
        }
        if (height <= heightSize) {
            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }
        }
        setMeasuredDimension(widthSize, Math.min(heightSize, height));
    }

}