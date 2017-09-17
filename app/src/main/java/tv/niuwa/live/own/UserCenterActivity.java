package tv.niuwa.live.own;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import tv.niuwa.live.R;
import tv.niuwa.live.core.BaseSiSiActivity;
import tv.niuwa.live.home.model.VideoItem;
import tv.niuwa.live.living.LivingActivity;
import tv.niuwa.live.login.LoginActivity;
import tv.niuwa.live.own.message.MessageActivity;
import tv.niuwa.live.own.setting.SettingActivity;
import tv.niuwa.live.own.userinfo.UserMenuItem;
import tv.niuwa.live.own.userinfo.UserMenuListAdapter;
import tv.niuwa.live.search.SearchActivity;

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
    Button myGift;

    @Bind(R.id.btn_enter_living)
    Button enterLiving;


    @Bind(R.id.right_icon)
    ImageView rightIcon;
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
        startActivity(new Intent(this, UserMainActivity.class));
    }
//
//    @Bind(R.id.user_center_menus)
//    ListView menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_user_center;
    }

    private void initView() {
        title.setText(R.string.user_center);

//        String[] menuStrs = getResources().getStringArray(R.array.user_center_menu);
//        int[] menuResIds = new int[]{R.drawable.img_icon01, R.drawable.img_icon02, R.drawable.img_icon03,
//                R.drawable.img_icon04, R.drawable.img_icon05, R.drawable.img_icon06, };
//        for(int i = 0;i < menuStrs.length; i++) {
//            mMenuListData.add(new UserMenuItem(menuStrs[i], menuResIds[i]));
//        }
//        menuList.setAdapter(new UserMenuListAdapter(this,mMenuListData));
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

}
