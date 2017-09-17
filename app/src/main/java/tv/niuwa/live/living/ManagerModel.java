package tv.niuwa.live.living;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2016/8/29.
 * Author: XuDeLong
 */
public class ManagerModel {
    private JSONObject other;
    private String userName;
    private String userLevel;
    private String type;
    private String content;
    private String userId;
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public JSONObject getOther() {
        return other;
    }

    public void setOther(JSONObject other) {
        this.other = other;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
