package tv.niuwa.live.own.userinfo;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 01/09/2017 17:03.
 * <p>
 * All copyright reserved.
 */

public class UserMenuItem {
    private String menuName;
    private int menuIconId;

    public UserMenuItem(String menuName, int menuIconId) {
        this.menuName = menuName;
        this.menuIconId = menuIconId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuIconId() {
        return menuIconId;
    }

    public void setMenuIconId(int menuIconId) {
        this.menuIconId = menuIconId;
    }
}
