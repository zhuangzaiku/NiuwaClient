package tv.niuwa.live.core;

import android.widget.Toast;

import com.smart.androidutils.activity.BaseEditActivity;

import tv.niuwa.live.R;
import tv.niuwa.live.utils.ToastHelper;

/**
 * Created by fengjh on 16/9/12.
 */
public abstract class BaseSiSiEditActivity extends BaseEditActivity {

    @Override
    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
