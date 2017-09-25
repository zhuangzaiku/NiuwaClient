package tv.niuwa.live.utils;

import android.os.Looper;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 25/09/2017 11:45.
 * <p>
 * All copyright reserved.
 */

public class Util {

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
