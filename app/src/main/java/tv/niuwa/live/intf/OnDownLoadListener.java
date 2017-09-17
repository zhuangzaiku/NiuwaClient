package tv.niuwa.live.intf;

/**
 * Created by Administrator on 2016/8/10.
 * Author: XuDeLong
 */
public interface OnDownLoadListener {
    void OnSuccess(byte[] binaryData);

    void onFailure();

    void onProgress(int count);
}
