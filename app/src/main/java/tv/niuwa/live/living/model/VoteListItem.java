package tv.niuwa.live.living.model;

/**
 * @author Ronan.zhuang
 * @email zaiku.zhuang@melot.cn
 * @Date 29/09/2017 11:13.
 * <p>
 * All copyright reserved.
 */

public class VoteListItem {

    private String voteName;
    private String voteOpName;
    private String voteOpId;
    private int voteNum;

    public VoteListItem(String voteName, String voteOpName, String voteOpId) {
        this.voteName = voteName;
        this.voteOpName = voteOpName;
        this.voteOpId = voteOpId;
    }

    public String getVoteName() {
        return voteName;
    }

    public void setVoteName(String voteName) {
        this.voteName = voteName;
    }

    public String getVoteOpName() {
        return voteOpName;
    }

    public void setVoteOpName(String voteOpName) {
        this.voteOpName = voteOpName;
    }

    public String getVoteOpId() {
        return voteOpId;
    }

    public void setVoteOpId(String voteOpId) {
        this.voteOpId = voteOpId;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
