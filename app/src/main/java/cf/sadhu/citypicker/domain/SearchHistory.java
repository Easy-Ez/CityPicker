package cf.sadhu.citypicker.domain;

/**
 * Created by sadhu on 2017/7/9.
 * 描述:
 */
public class SearchHistory extends City {
    public long operateTime;

    public SearchHistory(String name, String pinYin, long operateTime) {
        super(name, pinYin);
        this.operateTime = operateTime;
    }

    public long getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(long operateTime) {
        this.operateTime = operateTime;
    }
}
