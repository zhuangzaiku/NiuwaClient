package tv.niuwa.live.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by fengjh on 16/9/28.
 */

public class GiftViewPager extends ViewPager {

    private static final String TAG = GiftViewPager.class.getName();

    public GiftViewPager(Context context) {
        super(context);
    }

    public GiftViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean hasScroll = false;

    public boolean isHasScroll() {
        return hasScroll;
    }

    public void setHasScroll(boolean hasScroll) {
        this.hasScroll = hasScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            setHasScroll(true);
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_UP) {
            setHasScroll(false);
        }
        return super.onTouchEvent(ev);
    }
}
