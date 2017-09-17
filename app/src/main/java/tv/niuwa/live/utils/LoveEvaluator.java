package tv.niuwa.live.utils;

import android.animation.TypeEvaluator;
import android.graphics.Point;


public class LoveEvaluator implements TypeEvaluator<Point> {
    //private Point dirPoint;
    private Point centerPoint;
    public LoveEvaluator(Point dirPoint){
        //this.dirPoint = dirPoint ;
        this.centerPoint = centerPoint;
    }
    @Override
    public Point evaluate(float t, Point startValue, Point endValue) {
        //(1 - t)^2 P0 + 2 t (1 - t) P1 + t^2 P2;
//        int x = (int)(Math.pow((1-t),2)*startValue.x+2*t*(1-t)*dirPoint.x+Math.pow(t,2)*endValue.x);
//        int y = (int)(Math.pow((1-t),2)*startValue.y+2*t*(1-t)*dirPoint.y+Math.pow(t,2)*endValue.y);
//        return new Point(x,y);
        int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * centerPoint.x + t * t * endValue.x);
        int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * centerPoint.y + t * t * endValue.y);
        return new Point(x, y);
    }
}
