package tv.niuwa.live.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tv.niuwa.live.R;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 12/10/2017 10:28.
 * <p>
 * All copyright reserved.
 */

public class LotteryDialog extends Dialog {

    public LotteryDialog(Context context) {
        super(context);
    }

    public LotteryDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private View contentView;

        public Builder(Context context) {
            this.context = context;
        }


        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public LotteryDialog create(String users) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final LotteryDialog dialog = new LotteryDialog(context, R.style.Dialog);
            Window window = dialog.getWindow();
//            if(window != null) {
//                window.clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND | WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                WindowManager.LayoutParams  attrs = window.getAttributes();
//                if(attrs != null) {
//                    attrs.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
//                }
//            }
            View layout = inflater.inflate(R.layout.dialog_lottery, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            final TextView lotteryUsers = (TextView) layout.findViewById(R.id.lottery_users);
            lotteryUsers.setText(users);

            RelativeLayout bg = (RelativeLayout) layout.findViewById(R.id.lottery_bg);
            AnimationDrawable animationDrawable = (AnimationDrawable) bg.getBackground();
            animationDrawable.start();

            int duration = 0;
            for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
                duration += animationDrawable.getDuration(i);
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    lotteryUsers.setVisibility(View.VISIBLE);
                }
            }, duration + 100);
            if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            }
            dialog.setCancelable(true);
            dialog.setContentView(layout);
            return dialog;
        }
    }
}