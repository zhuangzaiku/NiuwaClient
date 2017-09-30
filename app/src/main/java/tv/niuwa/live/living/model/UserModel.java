package tv.niuwa.live.living.model;

/**
 * Created by Administrator on 2016/9/5.
 * Author: XuDeLong
 */
public class UserModel {
    private String id;
    private String user_nicename;
    private String avatar;
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object obj) {
       UserModel tem = (UserModel)obj;
        if(this.id.equals(tem.getId())){
            return true;
        }
        return false;
    }
}
