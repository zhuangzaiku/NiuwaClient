package tv.niuwa.live.living;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smart.androidutils.images.GlideCircleTransform;
import tv.niuwa.live.R;
import tv.niuwa.live.view.StrokeTextView;


/**
 * ${Author}: 徐坤
 * ${CreateTime}:16/5/26
 * ${Description}:送礼的动画
 * ${EMail}:xukun1007@163.com
 */
public class GiftFrameLayout extends FrameLayout {

    private LayoutInflater mInflater;

    RelativeLayout anim_rl;
    ImageView anim_gift, anim_light, anim_header;
    TextView anim_nickname, anim_sign;
    StrokeTextView anim_num;

    /**
     * 礼物数量的起始值
     */
    int starNum = 1;
    int repeatCount = 0;
    private boolean isShowing = false;
    GiftSendModel sendModel ;
    public GiftFrameLayout(Context context) {
        this(context, null);
    }

    public GiftFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        initView();
    }


    private void initView() {
        View view = mInflater.inflate(R.layout.gift_item_animation, this, false);
        anim_rl = (RelativeLayout) view.findViewById(R.id.animation_person_rl);
        anim_gift = (ImageView) view.findViewById(R.id.animation_gift);
        anim_light = (ImageView) view.findViewById(R.id.animation_light);
        anim_num = (StrokeTextView) view.findViewById(R.id.animation_num);
        anim_header = (ImageView) view.findViewById(R.id.gift_userheader_iv);
        anim_nickname = (TextView) view.findViewById(R.id.gift_usernickname_tv);
        anim_sign = (TextView) view.findViewById(R.id.gift_usersign_tv);
        this.addView(view);
    }

    public void hideView() {
        anim_gift.setVisibility(INVISIBLE);
        anim_light.setVisibility(INVISIBLE);
        anim_num.setVisibility(INVISIBLE);
    }

    public void setModel(GiftSendModel model){
        sendModel = model;
        if (0!=model.getGiftCount()) {
            this.repeatCount = model.getGiftCount()-1;
        }
        if (!TextUtils.isEmpty(model.getNickname())) {
            anim_nickname.setText(model.getNickname());
        }
        if (!TextUtils.isEmpty(model.getSig())) {
            anim_sign.setText(model.getSig());
        }
        Glide.with(getContext()).load(model.getUserAvatarRes()).transform(new GlideCircleTransform(getContext())).into(anim_header);

    }

    public GiftSendModel getSendModel() {
        return sendModel;
    }

    public boolean isShowing(){
        return isShowing;
    }
    ObjectAnimator scaleGiftNum;
    public AnimatorSet startAnimation() {
        hideView();
        //布局飞入
        ObjectAnimator flyFromLtoR = GiftAnimationUtil.createFlyFromLtoR(anim_rl, -getWidth(), 0, 400,new OvershootInterpolator());
        flyFromLtoR.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                GiftFrameLayout.this.setVisibility(View.VISIBLE);
                GiftFrameLayout.this.setAlpha(1f);
                isShowing = true;
                anim_num.setText("x " + starNum);
                Log.i("TAG", "flyFromLtoR A start");
            }
        });
        //礼物飞入
        ObjectAnimator flyFromLtoR2 = GiftAnimationUtil.createFlyFromLtoR(anim_gift, -getWidth(), 0, 400,new DecelerateInterpolator());
        flyFromLtoR2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                anim_gift.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(GiftFrameLayout.this.getSendModel().getGiftRes()).transform(new GlideCircleTransform(getContext())).into(anim_gift);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                GiftAnimationUtil.startAnimationDrawable(anim_light);
                anim_num.setVisibility(View.VISIBLE);
            }
        });
        //数量增加
        scaleGiftNum = GiftAnimationUtil.scaleGiftNum(anim_num, repeatCount);
        scaleGiftNum.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                anim_num.setText("x " + (++starNum));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //isShowing = false;
                super.onAnimationEnd(animation);
            }
        });
        //向上渐变消失
        ObjectAnimator fadeAnimator = GiftAnimationUtil.createFadeAnimator(GiftFrameLayout.this, 0, -100, 100, 0);
        fadeAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                GiftFrameLayout.this.setVisibility(View.INVISIBLE);
            }
        });
        // 复原
        ObjectAnimator fadeAnimator2 = GiftAnimationUtil.createFadeAnimator(GiftFrameLayout.this, 100, 0, 10, 0);

        AnimatorSet animatorSet = GiftAnimationUtil.startAnimation(flyFromLtoR, flyFromLtoR2, scaleGiftNum, fadeAnimator, fadeAnimator2);
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                starNum = 1;
                isShowing = false;
            }

        });
        return animatorSet;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        if(null != scaleGiftNum)
            scaleGiftNum.setRepeatCount(repeatCount);
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }
}
