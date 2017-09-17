package tv.niuwa.live.home.model;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 03/09/2017 00:09.
 * <p>
 * All copyright reserved.
 */

public class HomeDataItem {

    private String channelTitle;
    private String title;
    private String audienceNum;
    private int previewImg;

    public HomeDataItem(String channelTitle, String title, String audienceNum, int previewImg) {
        this.channelTitle = channelTitle;
        this.title = title;
        this.audienceNum = audienceNum;
        this.previewImg = previewImg;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudienceNum() {
        return audienceNum;
    }

    public void setAudienceNum(String audienceNum) {
        this.audienceNum = audienceNum;
    }

    public int getPreviewImg() {
        return previewImg;
    }

    public void setPreviewImg(int previewImg) {
        this.previewImg = previewImg;
    }
}
