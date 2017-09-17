package tv.niuwa.live.intf;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2016/8/10.
 * Author: XuDeLong
 */
public interface OnRequestDataListener {
    void requestSuccess(int code, JSONObject data);

    void requestFailure(int code,String msg);
}
