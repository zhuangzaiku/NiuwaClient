package tv.niuwa.live.living;

/**
 * Created by Administrator on 2016/9/5.
 * Author: XuDeLong
 */
public class GiftModel {
    private String giftid;
    private String giftname;
    private String needcoin;
    private String gifticon;
    private String continuous;
    private boolean checked;

    public String getGiftid() {
        return giftid;
    }

    public void setGiftid(String giftid) {
        this.giftid = giftid;
    }

    public String getGiftname() {
        return giftname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public String getNeedcoin() {
        return needcoin;
    }

    public void setNeedcoin(String needcoin) {
        this.needcoin = needcoin;
    }

    public String getGifticon() {
        return gifticon;
    }

    public void setGifticon(String gifticon) {
        this.gifticon = gifticon;
    }

    public String getContinuous() {
        return continuous;
    }

    public void setContinuous(String continuous) {
        this.continuous = continuous;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
