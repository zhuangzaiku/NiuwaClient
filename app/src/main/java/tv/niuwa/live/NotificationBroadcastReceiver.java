package tv.niuwa.live;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import tv.niuwa.live.utils.AVImClientManager;

/**
 * Created by wli on 15/9/8.
 * 因为 notification 点击时，控制权不在 app，此时如果 app 被 kill 或者上下文改变后，
 * 有可能对 notification 的响应会做相应的变化，所以此处将所有 notification 都发送至此类，
 * 然后由此类做分发。
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    if (AVImClientManager.getInstance().getClient() == null) {

    } else {

    }
  }

}
