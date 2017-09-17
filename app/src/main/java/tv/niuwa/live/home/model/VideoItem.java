package tv.niuwa.live.home.model;

import java.io.Serializable;

/**
 * Created by fengjh on 16/7/19.
 */
public class VideoItem  implements Serializable {
    private String room_id;
    private String id;
    private String channel_creater;
    private String avatar;
    private String user_nicename;
    private String channel_location;
    private String online_num;
    private String channel_status;
    private String channel_title;
    private String smeta;
    private String user_level;
    private String price;
    private String need_password;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNeed_password() {
        return need_password;
    }

    public void setNeed_password(String need_password) {
        this.need_password = need_password;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel_creater() {
        return channel_creater;
    }

    public void setChannel_creater(String channel_creater) {
        this.channel_creater = channel_creater;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getChannel_location() {
        return channel_location;
    }

    public void setChannel_location(String channel_location) {
        this.channel_location = channel_location;
    }

    public String getOnline_num() {
        return online_num;
    }

    public void setOnline_num(String online_num) {
        this.online_num = online_num;
    }

    public String getChannel_status() {
        return channel_status;
    }

    public void setChannel_status(String channel_status) {
        this.channel_status = channel_status;
    }

    public String getChannel_title() {
        return channel_title;
    }

    public void setChannel_title(String channel_title) {
        this.channel_title = channel_title;
    }

    public String getSmeta() {
        return smeta;
    }

    public void setSmeta(String smeta) {
        this.smeta = smeta;
    }
}
