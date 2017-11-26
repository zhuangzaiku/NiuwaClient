package tv.niuwa.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * @author Ronan.zhuang
 * @Date 4/18/17.
 * All copyright reserved.
 */

public class FadingScrollView extends ScrollView {

    private Scroller mScroller;
    private OnScrollListener mOnScrollListener;

    public FadingScrollView(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public FadingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public FadingScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    public void smoothScrollBy(int dx, int dy, int duration) {
        mScroller.startScroll(getScrollX(), getScrollY(), dx, dy, duration);
        invalidate();
    }


    public void smoothScrollBy(int dx, int dy, int duration,OnScrollListener onScrollListener) {
        mScroller.startScroll(getScrollX(), getScrollY(), dx, dy, duration);
        setOnScrollListener(onScrollListener);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }else {
            if(mOnScrollListener != null) {
                mOnScrollListener.onScrollFinished();
                mOnScrollListener = null;
            }
        }
        super.computeScroll();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public interface OnScrollListener {
        void onScrollFinished();
    }

    public OnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public void setOnScrollListener(OnScrollListener mOnScrollListener) {
        this.mOnScrollListener = mOnScrollListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}

