package tv.niuwa.live.lean;

import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;
import tv.niuwa.live.MyApplication;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.utils.Api;

/**
 * Created by wli on 15/12/4.
 * 实现自定义用户体系
 */
public class CustomUserProvider implements LCChatProfileProvider {

  private static CustomUserProvider customUserProvider;

  public synchronized static CustomUserProvider getInstance() {
    if (null == customUserProvider) {
      customUserProvider = new CustomUserProvider();
    }
    return customUserProvider;
  }

  private CustomUserProvider() {
  }

  private static List<LCChatKitUser> partUsers = new ArrayList<LCChatKitUser>();

  // 此数据均为 fake，仅供参考
//  static {
//    partUsers.add(new LCChatKitUser("Tom", "Tom", "http://www.avatarsdb.com/avatars/tom_and_jerry2.jpg"));
//    partUsers.add(new LCChatKitUser("Jerry", "Jerry", "http://www.avatarsdb.com/avatars/jerry.jpg"));
//    partUsers.add(new LCChatKitUser("Harry", "Harry", "http://www.avatarsdb.com/avatars/young_harry.jpg"));
//    partUsers.add(new LCChatKitUser("William", "William", "http://www.avatarsdb.com/avatars/william_shakespeare.jpg"));
//    partUsers.add(new LCChatKitUser("Bob", "Bob", "http://www.avatarsdb.com/avatars/bath_bob.jpg"));
//  }

  @Override
  public void fetchProfiles(List<String> list, final LCChatProfilesCallBack callBack) {
    final List<LCChatKitUser> userList = new ArrayList<LCChatKitUser>();
    for (final String userId : list) {

//      for (LCChatKitUser user : partUsers) {
//        if (user.getUserId().equals(userId)) {
//          userList.add(user);
//          break;
//        }
        //如果没有  则去请求网络
        final JSONObject params = new JSONObject();
        params.put("token", SharePrefsUtils.get(MyApplication.getGlobalContext(),"user","token",""));
        params.put("id",userId);
        Api.getUserData1(MyApplication.getGlobalContext(), params, new OnRequestDataListener() {
          @Override
          public void requestSuccess(int code, JSONObject data) {
            JSONObject userInfo = data.getJSONObject("data");
            LCChatKitUser user = new LCChatKitUser(userId,userInfo.getString("user_nicename"),userInfo.getString("avatar"));
            userList.add(user);
            //partUsers.add(user);
            callBack.done(userList, null);
          }

          @Override
          public void requestFailure(int code, String msg) {

          }
        });
      //end
      }
    //}

  }

  public List<LCChatKitUser> getAllUsers() {
    return partUsers;
  }
}
