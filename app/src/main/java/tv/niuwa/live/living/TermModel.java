package tv.niuwa.live.living;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/9.
 * Author: XuDeLong
 */
public class TermModel implements Serializable{
    private String term_id;
    private String name;

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
