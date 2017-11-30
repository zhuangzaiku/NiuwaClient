package tv.niuwa.live.core;

import android.widget.Toast;

import com.smart.androidutils.activity.BaseActivity;


/**
 * Created by fengjh on 16/9/12.
 */
public abstract class BaseSiSiActivity extends BaseActivity {

    @Override
    public void toast(String s) {
//        super.toast(s);
//        ToastUtils.makeText(this, s).show();
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
