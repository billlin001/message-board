package tw.bill.java101.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import tw.bill.java101.domain.Hobby;
import tw.bill.java101.domain.Post;
import tw.bill.java101.domain.Team;
import tw.bill.java101.domain.User;
import tw.bill.java101.dto.UserEditForm;
import tw.bill.java101.dto.UserForm;
import tw.bill.java101.helper.EncryptionHelper;
import tw.bill.java101.repository.PostRepository;
import tw.bill.java101.repository.TeamRepository;
import tw.bill.java101.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Created by bill33 on 2016/2/21.
 */
public class UserServiceImplTest {
    @Mock
    TeamRepository teamRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    EncryptionHelper encryptionHelper;

    @InjectMocks
    UserServiceImpl target;

    Team team;
    Post post;
    User user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        team = new Team("title", "description");
        post = new Post();
        post.setContent("content");
        team.addPost(post);
        post.setTeam(team);

        user = new User();
        user.setEmail("aaa");
        user.addTeam(team);
        team.addUser(user);
    }

    @Test
    public void testGetUserById() throws Exception {
        when(userRepository.findOne(1L)).thenReturn(user);
        User actual = target.getUserById(1L);
        assertNotNull(actual);
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        when(userRepository.findOneByEmail(user.getEmail())).thenReturn(user);
        User actual = target.findByUserName(user.getEmail());
        assertNotNull(actual);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(new ArrayList<User>());
        Collection<User> actuals = target.getAllUsers();
        assertNotNull(actuals);
    }

    @Test
    public void testCreate() throws Exception {
        UserForm userForm = new UserForm();
        userForm.setBirthday(new Date());
        userForm.setDegree("DOCTOR");
        userForm.setEmail("aaa@gmail.com");
        userForm.setHobbies(Arrays.asList(new Hobby[]{Hobby.MOVIE}));
        userForm.setIntroduce("");
        userForm.setNickName("bbb");
        userForm.setPassword("1234");
        userForm.setPasswordRepeated("1234");
        userForm.setSex("MALE");

        target.create(userForm);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testJoinTeam() throws Exception {
        when(userRepository.findOneByEmail(user.getEmail())).thenReturn(user);
        when(teamRepository.findOne(1L)).thenReturn(team);
        target.joinTeam(user.getEmail(), 1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testQuitTeam() throws Exception {
        when(userRepository.findOneByEmail(user.getEmail())).thenReturn(user);
        when(teamRepository.findOne(1L)).thenReturn(team);
        target.quitTeam(user.getEmail(), 1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testFindById() {
        when(userRepository.findOne(1L)).thenReturn(user);
        User user = target.findById(1L);
        assert user != null;
    }

    @Test
    public void testCanUpdateAndDeleteTeamReturnTrue() throws Exception {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("aaa@gmail.com");
        Long teamId = 1L;
        boolean expected = true;

        Team team = new Team();
        User owner = new User();
        owner.setEmail("aaa@gmail.com");
        team.setOwner(owner);
        when(teamRepository.findOne(1L)).thenReturn(team);

        boolean actual = target.canUpdateAndDeleteTeam(userDetails, teamId);

        assertEquals(expected, actual);
    }

    @Test
    public void testCanUpdateAndDeleteTeamReturnFalse() throws Exception {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("aaa@gmail.com");
        Long teamId = 1L;
        boolean expected = false;

        Team team = new Team();
        User owner = new User();
        owner.setEmail("bbb@gmail.com");
        team.setOwner(owner);
        when(teamRepository.findOne(1L)).thenReturn(team);

        boolean actual = target.canUpdateAndDeleteTeam(userDetails, teamId);

        assertEquals(expected, actual);
    }

    @Test
    public void testCanUpdateAndDeletePostReturnTrue() throws Exception {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("aaa@gmail.com");
        Long postId = 1L;
        boolean expected = true;

        Post post = new Post();
        User owner = new User();
        owner.setEmail("aaa@gmail.com");
        post.setOwner(owner);
        when(postRepository.findOne(1L)).thenReturn(post);

        boolean actual = target.canUpdateAndDeletePost(userDetails, postId);

        assertEquals(expected, actual);
    }

    @Test
    public void testCanUpdateAndDeletePostReturnFalse() throws Exception {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("aaa@gmail.com");
        Long postId = 1L;
        boolean expected = false;

        Post post = new Post();
        User owner = new User();
        owner.setEmail("bbb@gmail.com");
        post.setOwner(owner);
        when(postRepository.findOne(1L)).thenReturn(post);

        boolean actual = target.canUpdateAndDeletePost(userDetails, postId);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdate() throws Exception {
        UserEditForm userForm = new UserEditForm();
        userForm.setBirthday(new Date());
        userForm.setDegree("DOCTOR");
        userForm.setEmail("aaa");
        userForm.setHobbies(Arrays.asList(new Hobby[]{Hobby.MOVIE}));
        userForm.setIntroduce("");
        userForm.setNickName("bbb");
        userForm.setPassword("1234");
        userForm.setPasswordRepeated("1234");
        userForm.setSex("MALE");

        when(userRepository.findOneByEmail(user.getEmail())).thenReturn(user);
        target.update(userForm);

        verify(userRepository, times(1)).save(any(User.class));
    }
}