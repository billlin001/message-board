package tw.bill.java101.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bill33 on 2016/6/5.
 */
public class PageDataDto {
    private List<TeamForm> data = new ArrayList<>();

    public List<TeamForm> getData() {
        return data;
    }

    public void setData(List<TeamForm> data) {
        this.data = data;
    }

    public void addData(TeamForm data) {
        this.data.add(data);
    }

    public void removeData(TeamForm data) {
        this.data.remove(data);
    }
}
