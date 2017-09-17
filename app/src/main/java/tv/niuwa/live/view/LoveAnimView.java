package tv.niuwa.live.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import java.util.Random;

import tv.niuwa.live.R;
import tv.niuwa.live.utils.LoveEvaluator;


public class LoveAnimView extends ImageView implements ValueAnimator.AnimatorUpdateListener{
    private Point mStartPoint;
    private Point mEndPoint;
    private int[] colors = new int[]{Color.YELLOW,Color.BLACK,Color.BLUE,Color.RED,Color.GREEN};
    public LoveAnimView(Context context) {
        this(context,null);
    }
    public LoveAnimView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public LoveAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        VectorDrawableCompat a = VectorDrawableCompat.create(getResources(), R.drawable.love_drawable,
                getResources().newTheme());
        Random random = new Random();
        int round =  random.nextInt(5);
        a.setTint(colors[round]);
        //DrawableCompat.setTint(a,colors[round]);
        VectorDrawableCompat boardVdc = VectorDrawableCompat.create(getResources(), R.drawable.border_drawable,
                getResources().newTheme());
        Drawable[] drawable = new Drawable[2];
        drawable[0] = a;
        drawable[1] = boardVdc;
        LayerDrawable layerDrawable = new LayerDrawable(drawable);
        setImageDrawable(layerDrawable);
    }
    public void setStartPosition(Point startPosition) {
        startPosition.y -= 10;
        this.mStartPoint = startPosition;
    }

    public void setEndPosition(Point endPosition) {
        this.mEndPoint = endPosition;
    }
    public void startLoveAnimation(){
        if(mStartPoint==null||mEndPoint==null)
            throw new IllegalArgumentException("mStartPoint is not null or mEndPoint is not null");
        int dirPointX = (int)(Math.random()*330);
        int dirPointY = (mStartPoint.y+mEndPoint.y)/2;
        Point dirPoint = new Point(dirPointX,dirPointY);
        LoveEvaluator loveEvaluator = new LoveEvaluator(dirPoint);
        ValueAnimator animator = ValueAnimator.ofObject(loveEvaluator,mStartPoint,mEndPoint);
        animator.addUpdateListener(this);
        animator.setDuration(2000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup viewGroup = (ViewGroup) getParent();
                setAlpha(0f);
                viewGroup.removeView(LoveAnimView.this);
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }
    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        Point point = (Point) valueAnimator.getAnimatedValue();
        setX(point.x);
        setY(point.y);
        float value = point.y*1.0f/mStartPoint.y;
        setAlpha(value);
        invalidate();
    }
}
