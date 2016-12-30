package tw.bill.java101.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Created by Bill Lin on 2016/2/9.
 */

@Entity
public class Post extends AbstractDomainClass{
    private String content;

    @ManyToOne
    private Team team;

    @OneToOne
    private User owner;

    public Post() {
    }

    public Post(String content) {
        super();
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Post{" +
                "content='" + content + '\'' +
                ", team=" + team +
                ", owner=" + owner +
                '}';
    }
}
