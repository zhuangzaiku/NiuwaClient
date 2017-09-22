package tv.niuwa.live.own.bean;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 22/09/2017 14:20.
 * <p>
 * All copyright reserved.
 */

public class RewardItem {

    private String name;
    private String avatar;
    private int status;

    public RewardItem(String name, String avatar, int status) {
        this.name = name;
        this.avatar = avatar;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
