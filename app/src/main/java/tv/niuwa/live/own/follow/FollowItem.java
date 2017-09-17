package tv.niuwa.live.own.follow;

/**
 * Created by fengjh on 16/7/31.
 */
public class FollowItem {
    private String id;
    private String user_nicename;
    private String is_truename;
    private String avatar;
    private String sex;
    private String signature;
    private String user_level;
    private String attention;
    private String channel_status;

    public String getChannel_status() {
        return channel_status;
    }

    public void setChannel_status(String channel_status) {
        this.channel_status = channel_status;
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

    public String getIs_truename() {
        return is_truename;
    }

    public void setIs_truename(String is_truename) {
        this.is_truename = is_truename;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    @Override
    public boolean equals(Object obj) {
        FollowItem item = (FollowItem)obj;
        if(this.getId() == item.getId()){
            return true;
        }
        return super.equals(obj);
    }
}
