package tv.niuwa.live.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author Ronan.zhuang
 * @Date 06/06/2017.
 * All copyright reserved.
 */

public class ShapedImageView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = ShapedImageView.class.getSimpleName();
    private Path mPath = new Path();
    private RectF mInnerRectF = new RectF();
    private RectF mOuterRectF = new RectF();
    private RectF mBitmapRectF = new RectF();
    private PorterDuffXfermode mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
    private float mPercent = 0;
    private static final float START_ANGLE = 90f;
    private static final float CIRCLE_ANGLE = 359.9999f;
    private static final double RADIAN = Math.PI / 180;


    public ShapedImageView(Context context) {
        super(context);
    }

    public ShapedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public ShapedImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable &&  mPercent < 100) {
            Paint paint = ((BitmapDrawable) drawable).getPaint();
            final int color = 0xff000000;
            Rect bitmapBounds = drawable.getBounds();
            mBitmapRectF.setEmpty();
            mBitmapRectF.set(bitmapBounds);
            int saveCount = canvas.saveLayer(mBitmapRectF, null,
                    Canvas.ALL_SAVE_FLAG);
            getImageMatrix().mapRect(mBitmapRectF);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setStrokeWidth(5);
            paint.setColor(color);
            int offset = 2;
            float width = getMeasuredWidth() + offset;
            mPath.moveTo(width / 2, width);
            mOuterRectF.setEmpty();
            mOuterRectF.set(-offset, -offset, width, width);
            float deltaAngle = mPercent * CIRCLE_ANGLE / 100;
            float currentAngle = START_ANGLE + deltaAngle;
            double currentRadian = currentAngle * RADIAN;
            mPath.arcTo(mOuterRectF, START_ANGLE, deltaAngle - CIRCLE_ANGLE);
            mPath.lineTo((float)(1 + 0.4 * Math.cos(currentRadian)) * width / 2, (float)(1 + 0.4 * Math.sin(currentRadian)) * width / 2);
            mInnerRectF.setEmpty();
            mInnerRectF.set(0.31f * width, 0.31f * width, 0.69f * width, 0.69f * width);
            mPath.arcTo(mInnerRectF, currentAngle, -deltaAngle + CIRCLE_ANGLE);
            mPath.lineTo(width / 2, 0.69f * width);

            canvas.drawPath(mPath, paint);
            Xfermode xfermode = paint.getXfermode();
            paint.setXfermode(mPorterDuffXfermode);
            super.onDraw(canvas);
            paint.setXfermode(xfermode);
            canvas.restoreToCount(saveCount);
        } else {
            super.onDraw(canvas);
        }
    }

    public void setPercent(float percent) {
        mPercent = percent;
        invalidate();
    }
}
