package tw.bill.java101.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.bill.java101.domain.Post;
import tw.bill.java101.domain.Team;
import tw.bill.java101.dto.PostForm;
import tw.bill.java101.repository.PostRepository;
import tw.bill.java101.repository.TeamRepository;
import tw.bill.java101.repository.UserRepository;

import java.util.List;

/**
 * Created by bill33 on 2016/2/9.
 */

@Service
public class PostService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public Post create(long teamId, PostForm postForm, String userName) {
        Team team = teamRepository.findOne(teamId);
        Post post = new Post();
        post.setContent(postForm.getContent());
        post.setTeam(team);
        post.setOwner(userRepository.findOneByEmail(userName));
        return postRepository.save(post);
    }

    public List<Post> findByTeamId(long teamId) {
        Team team = teamRepository.findOne(teamId);
        return team.getPosts();
    }

    public Post readOne(long postId) {
        return postRepository.findOne(postId);
    }

    public Post update(PostForm postForm) {
        Post post = postRepository.findOne(postForm.getId());
        post.setContent(postForm.getContent());
        return postRepository.save(post);
    }

    public void delete(long postId) {
        Post post = postRepository.findOne(postId);
        post.setTeam(null);
        postRepository.delete(post);
    }
}
