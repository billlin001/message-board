package tw.bill.java101.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import tw.bill.java101.domain.Post;
import tw.bill.java101.domain.Team;
import tw.bill.java101.domain.User;
import tw.bill.java101.dto.PostForm;
import tw.bill.java101.dto.TeamForm;
import tw.bill.java101.repository.PostRepository;
import tw.bill.java101.repository.TeamRepository;
import tw.bill.java101.repository.UserRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


/**
 * Created by bill33 on 2016/2/21.
 */
@Configuration
public class PostServiceTest {
    @Mock
    TeamRepository teamRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    PostService target;

    TeamForm teamForm;
    PostForm postForm;

    Team team;
    Post post;
    User user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        team = new Team("title", "description");
        team.setId(1L);
        post = new Post();
        post.setContent("content");
        post.setId(1L);
        team.addPost(post);
        post.setTeam(team);

        user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        teamForm = new TeamForm();
        postForm = new PostForm();
        BeanUtils.copyProperties(team, teamForm);
        BeanUtils.copyProperties(post, postForm);
    }

    @Test
    public void testCreate() throws Exception {
        when(teamRepository.findOne(1L)).thenReturn(team);
        target.create(1L, postForm, user.getEmail());
        verify(teamRepository, times(1)).findOne(1L);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void testFindByTeamId() throws Exception {
        when(teamRepository.findOne(1L)).thenReturn(team);
        List<Post> actuals = target.findByTeamId(1L);
        assertEquals(1, actuals.size());
    }

    @Test
    public void testReadOne() throws Exception {
        when(postRepository.findOne(1L)).thenReturn(post);
        Post actual = target.readOne(1L);
        assertEquals(post, actual);
    }

    @Test
    public void testUpdate() throws Exception {
        when(postRepository.findOne(1L)).thenReturn(post);
        target.update(postForm);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    public void testDelete() throws Exception {
        when(postRepository.findOne(1L)).thenReturn(post);
        target.delete(1L);
        verify(postRepository, times(1)).delete(any(Post.class));
    }
}