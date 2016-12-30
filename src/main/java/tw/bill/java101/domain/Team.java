package tw.bill.java101.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team extends AbstractDomainClass{
    private String title;
    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="team")
    private List<Post> posts = new ArrayList<>();

    @OneToOne
    private User owner;

    @ManyToMany
    @JoinTable
    private List<User> users = new ArrayList<>();

    public Team() {}

    public Team(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setTeam(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setTeam(null);
    }


    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user){
        if(!this.users.contains(user)){
            this.users.add(user);
        }

        if(!user.getTeams().contains(this)){
            user.getTeams().add(this);
        }
    }

    public void removeUser(User user){
        this.users.remove(user);
        user.getTeams().remove(this);
    }
}
