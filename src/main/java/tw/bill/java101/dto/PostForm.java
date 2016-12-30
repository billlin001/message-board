package tw.bill.java101.dto;

import javax.validation.constraints.Size;

/**
 * Created by bill33 on 2016/3/25.
 */
public class PostForm {
    private long id;
    private int version;
    @Size(min=2, max=200)
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
