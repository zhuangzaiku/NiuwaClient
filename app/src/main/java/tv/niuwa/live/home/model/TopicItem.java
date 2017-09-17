package tv.niuwa.live.home.model;

import java.io.Serializable;

/**
 * Created by fengjh on 16/7/19.
 */
public class TopicItem implements Serializable {
    private String name;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
