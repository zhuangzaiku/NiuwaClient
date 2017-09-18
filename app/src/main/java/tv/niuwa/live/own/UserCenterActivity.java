package tv.niuwa.live.own;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.niuwa.live.MyApplication;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.home.model.VideoItem;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.living.LivingActivity;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.own.message.MessageActivity;
import tv.niuwa.live.own.setting.SettingActivity;
import tv.niuwa.live.own.userinfo.MyDataActivity;
import tv.niuwa.live.own.userinfo.UserMenuItem;
import tv.niuwa.live.own.userinfo.UserMenuListAdapter;
import tv.niuwa.live.search.SearchActivity;
import tv.niuwa.live.utils.Api;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 01/09/2017 16:10.
 * <p>
 * All copyright reserved.
 */

public class UserCenterActivity extends BaseSiSiActivity {

    private List<UserMenuItem> mMenuListData = new ArrayList<>();

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.left_icon)
    ImageView leftIcon;

    @Bind(R.id.loginRegister)
    Button loginRegister;

    @Bind(R.id.btn_my_gift)
    ImageView myGift;

    @Bind(R.id.btn_enter_living)
    ImageView enterLiving;


    @Bind(R.id.right_icon)
    ImageView rightIcon;


    @Bind(R.id.user_head)
    CircleImageView userHead;

    @Bind(R.id.user_nickname)
    TextView userNickname;

    @Bind(R.id.user_coin)
    TextView userCoin;

    @OnClick(R.id.left_icon)
    public void goMessages(View view) {
        ((ImageView)view).setImageResource(R.drawable.img_xinxi);
        startActivity(new Intent(this, SearchActivity.class));
    }

    @OnClick(R.id.right_icon)
    public void goSetting(View view) {
        ((ImageView)view).setImageResource(R.drawable.img_setting);
        startActivity(new Intent(this, SettingActivity.class));
    }
//
//    @Bind(R.id.user_center_menus)
//    ListView menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_user_center;
    }

    @OnClick(R.id.user_head)
    public void clickAvatarName(View view) {
        Intent i = new Intent(this, MyDataActivity.class);
        startActivityForResult(i, 1);
    }



    @Bind(R.id.nickname_edit)
    ImageView nicknameEdit;

    @OnClick(R.id.nickname_edit)
    public void clickAvatarName1(View view) {
        Intent i = new Intent(this, MyDataActivity.class);
        startActivityForResult(i, 1);
    }

    private void initView() {
        title.setText(R.string.user_center);

        userCoin.setText(Html.fromHtml(String.format(getResources().getString(R.string.user_coin), String.valueOf(54))));


        leftIcon.setImageResource(R.drawable.img_xinxi);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MessageActivity.class);
            }
        });
        rightIcon.setImageResource(R.drawable.img_setting);
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SettingActivity.class);
            }
        });

        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(LoginActivity.class);
            }
        });

        myGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        enterLiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoItem videoItem = new VideoItem();
                videoItem.setRoom_id("17");
                videoItem.setId("156277");
                videoItem.setUser_nicename("\u7528\u6237156277");
                videoItem.setAvatar("http://zhibo.519wan.com/data/upload/avatar/default.png");
                videoItem.setChannel_creater("156277");
                videoItem.setChannel_location(null);
                videoItem.setChannel_title("\\u725b\\u54c7");
                videoItem.setOnline_num("11");
                videoItem.setSmeta("http://zhibo.519wan.com/data/upload/avatar/default.png");
                videoItem.setChannel_status("2");
                videoItem.setUser_level("1");
                videoItem.setPrice("0");
                videoItem.setNeed_password("0");
                Bundle data = new Bundle();
                data.putSerializable("videoItem", videoItem);
                Intent intent = new Intent(UserCenterActivity.this,LivingActivity.class);
                intent.putExtras(data);
                UserCenterActivity.this.startActivity(intent);
            }
        });
    }

    private void initData() {
        String token = (String) SharePrefsUtils.get(UserCenterActivity.this, "user", "token", "");
        String userId = (String) SharePrefsUtils.get(UserCenterActivity.this, "user", "userId", "");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            Api.getUserInfo(UserCenterActivity.this, requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if(isActive){
                        JSONObject userInfo = data.getJSONObject("data");
                        String nickname = userInfo.getString("user_nicename");
                        SharePrefsUtils.put(UserCenterActivity.this, "user", "user_nicename", nickname);
                        SharePrefsUtils.put(UserCenterActivity.this, "user", "user_level", userInfo.getString("user_level"));
                        SharePrefsUtils.put(UserCenterActivity.this, "user", "avatar", userInfo.getString("avatar"));
                        MyApplication app = (MyApplication) UserCenterActivity.this.getApplication();
                        app.setBalance(userInfo.getString("balance"));

                        userCoin.setText(String.format(getString(R.string.user_coin),app.getBalance()));
                        userNickname.setVisibility(View.VISIBLE);
                        userNickname.setText(nickname);
                        nicknameEdit.setVisibility(View.VISIBLE);

                        Glide.with(UserCenterActivity.this).load(userInfo.getString("avatar"))
                                .error(R.drawable.icon_avatar_default)
                                .transform(new GlideCircleTransform(UserCenterActivity.this))
                                .into(userHead);

                    }

                }

                @Override
                public void requestFailure(int code, String msg) {
                }
            });
            loginRegister.setVisibility(View.GONE);

        } else {
            userCoin.setText(String.format(getString(R.string.user_coin),"0"));
            loginRegister.setVisibility(View.VISIBLE);
            userNickname.setVisibility(View.INVISIBLE);
            nicknameEdit.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                initData();
        }
    }
}
