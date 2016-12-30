package tw.bill.java101.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;
import tw.bill.java101.domain.Post;
import tw.bill.java101.domain.Team;
import tw.bill.java101.domain.User;
import tw.bill.java101.dto.TeamForm;
import tw.bill.java101.repository.PostRepository;
import tw.bill.java101.repository.TeamRepository;
import tw.bill.java101.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@Configuration
public class TeamServiceTest {
    @Mock
    TeamRepository teamRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    TeamService target;

    TeamForm teamForm;

    Team team;
    Post post;
    User user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        teamForm = new TeamForm();
        teamForm.setTitle("title");
        teamForm.setDescription("description");

        team = new Team("title", "description");
        post = new Post();
        post.setContent("content");
        team.addPost(post);
        post.setTeam(team);

        user = new User();
        user.setEmail("aaa");
        user.addTeam(team);
        team.addUser(user);
        team.setOwner(user);
    }

    @Test
    public void testReadAll() throws Exception {
        when(teamRepository.findAll()).thenReturn(new ArrayList<Team>());
        List<Team> actuals = target.readAll();
        assertNotNull(actuals);
    }

    @Test
    public void testCreate() throws Exception {
        when(userRepository.findOneByEmail("aaa")).thenReturn(user);
        Team team = new Team();
        target.create(teamForm, user.getEmail());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    public void testReadOne() throws Exception {
        when(teamRepository.findOne(1L)).thenReturn(team);
        Team actual = target.readOne(1L);
        assertEquals("title", actual.getTitle());
    }

    @Test
    public void testUpdate() throws Exception {
        when(teamRepository.findOne(anyLong())).thenReturn(team);
        System.out.println(teamForm);
        System.out.println(teamForm.getTitle());
        target.update(teamForm);
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    public void testDelete() throws Exception {
        when(teamRepository.findOne(anyLong())).thenReturn(team);
        target.delete(1L);
//        verify(postRepository, times(1)).delete(any(Set.class));
        verify(teamRepository, times(1)).delete(any(Team.class));
    }
}