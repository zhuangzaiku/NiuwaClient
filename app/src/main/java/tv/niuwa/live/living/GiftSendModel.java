package tv.niuwa.live.living;

public class GiftSendModel {

    private int giftCount;
    private String userAvatarRes;
    private String nickname;
    private String sig;
    private String giftRes;
    private String gift_id;
    private String userId;
    private String bigAnim;

    public String getBigAnim() {
        return bigAnim;
    }

    public void setBigAnim(String bigAnim) {
        this.bigAnim = bigAnim;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GiftSendModel(int giftCount) {
        this.giftCount = giftCount;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    public String getUserAvatarRes() {
        return userAvatarRes;
    }

    public void setUserAvatarRes(String userAvatarRes) {
        this.userAvatarRes = userAvatarRes;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getGiftRes() {
        return giftRes;
    }

    public void setGiftRes(String giftRes) {
        this.giftRes = giftRes;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    @Override
    public String toString() {
        return "GiftSendModel{" +
                "giftCount=" + giftCount +
                ", userAvatarRes='" + userAvatarRes + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sig='" + sig + '\'' +
                ", giftRes='" + giftRes + '\'' +
                ", gift_id='" + gift_id + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}