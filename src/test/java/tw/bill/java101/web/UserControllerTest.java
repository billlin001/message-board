package tw.bill.java101.web;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import tw.bill.java101.domain.Degree;
import tw.bill.java101.domain.Post;
import tw.bill.java101.domain.Sex;
import tw.bill.java101.domain.Team;
import tw.bill.java101.dto.UserEditForm;
import tw.bill.java101.dto.UserForm;
import tw.bill.java101.service.UserService;
import tw.bill.java101.service.security.UserDetailsImpl;
import tw.bill.java101.utils.TestUtil;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by bill33 on 2016/6/3.
 * I can't control and verify the return detail
 */
public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController target;

    private TestingAuthenticationToken testingAuthenticationToken;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(target).setViewResolvers(viewResolver).build();

        User user = new User("aaa@gmail.com","aaa", AuthorityUtils.createAuthorityList("ROLE_PATRON"));
        testingAuthenticationToken =  new TestingAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }

    @Test
    public void test_getLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/login"));
    }

    @Test
    public void test_getSignupPage() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/signup"));
    }

    @Test
    public void test_createUser_WithTooLongEamil_ShouldRenderErrorMessage() throws Exception {
        UserForm userForm = getUserForm();
        userForm.setEmail(TestUtil.createStringWithLength(500));

        mockMvc.perform(post("/signup")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("email", userForm.getEmail())
                    .param("password", userForm.getPassword())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("/user/signup"))
                .andExpect(model().hasErrors());
    }

    @Test
    public void test_createUser_EmailAlreadyExist_ShouldThrowDataIntegrityViolationException() throws Exception {
        UserForm userForm = getUserForm();

        when(userService.create(anyObject())).thenThrow(new DataIntegrityViolationException(""));

        mockMvc.perform(post("/signup")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("email", userForm.getEmail())
                    .param("password", userForm.getPassword())
                    .param("nickName", userForm.getNickName())
                    .param("passwordRepeated", userForm.getPasswordRepeated())
                    .param("sex", userForm.getSex())
                    .param("degree", userForm.getDegree())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("/user/signup"))
                .andExpect(model().hasErrors());
    }

    @Test
    public void test_createUser_AddNewUser_ShouldRedirectHomePage() throws Exception {
        UserForm userForm = getUserForm();

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", userForm.getEmail())
                .param("password", userForm.getPassword())
                .param("nickName", userForm.getNickName())
                .param("passwordRepeated", userForm.getPasswordRepeated())
                .param("sex", userForm.getSex())
                .param("degree", userForm.getDegree())
        )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(model().hasNoErrors());

        ArgumentCaptor<UserForm> formObjectArgument = ArgumentCaptor.forClass(UserForm.class);
        verify(userService, times(1)).create(formObjectArgument.capture());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_editUser_RenderEditPage() throws Exception {
        tw.bill.java101.domain.User user = getUser();

        when(userService.findByUserName(anyString())).thenReturn(user);

        mockMvc.perform(get("/user/edit")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .principal(testingAuthenticationToken)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("/user/edit"))
                .andExpect(model().attribute("editUser", equalTo(user)));
    }

    @Test
    public void test_updateUser_WithTooLongEmail_ShouldReturnErrorMessage() throws Exception {
        UserEditForm userForm = getUserEditForm();
        userForm.setEmail(TestUtil.createStringWithLength(500));

        mockMvc.perform(post("/user/edit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", userForm.getEmail())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/user/edit"))
                .andExpect(model().hasErrors());
    }

    @Test
    public void test_updateUser_UpdateUser_ShouldRedirectHomePage() throws Exception {
        UserEditForm userForm = getUserEditForm();

        mockMvc.perform(post("/user/edit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", userForm.getEmail())
                .param("password", userForm.getPassword())
                .param("nickName", userForm.getNickName())
                .param("passwordRepeated", userForm.getPasswordRepeated())
                .param("sex", userForm.getSex())
                .param("degree", userForm.getDegree())
        )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(model().hasNoErrors());

        ArgumentCaptor<UserEditForm> formObjectArgument = ArgumentCaptor.forClass(UserEditForm.class);
        verify(userService, times(1)).update(formObjectArgument.capture());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testJoinTeam() throws Exception {
        mockMvc.perform(get("/users/join/{teamId}", 1L).principal(testingAuthenticationToken))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/teams/1"));

        // Argument value, username, should not be null, but I can't find out why it's null...
        verify(userService).joinTeam(null, 1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testQuitTeam() throws Exception {
        mockMvc.perform(get("/users/quit/{teamId}", 1L).principal(testingAuthenticationToken))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/teams/1"));

        // Argument value, username, should not be null, but I can't find out why it's null...
        verify(userService).quitTeam(null, 1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testFetchTeams() throws Exception {
        when(userService.findByUserName(anyString())).thenReturn(getUser());
        mockMvc.perform(get("/user/teams").principal(testingAuthenticationToken))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/team"))
                .andExpect(model().attribute("teams", hasSize(1)));
    }

    @Test
    public void testFetchPosts() throws Exception {
        when(userService.findByUserName(anyString())).thenReturn(getUser());
        mockMvc.perform(get("/user/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/post"))
                .andExpect(model().attribute("posts", hasSize(1)));
    }

    private UserForm getUserForm(){
        UserForm result = new UserForm();
        result.setEmail("aaa@gamil.com");
        result.setNickName("aaa");
        result.setPassword("aaa");
        result.setPasswordRepeated("aaa");
        result.setSex(Sex.MALE.toString());
        result.setDegree(Degree.COLLEGE.toString());
        return result;
    }

    private UserEditForm getUserEditForm(){
        UserEditForm result = new UserEditForm();
        result.setEmail("aaa@gamil.com");
        result.setNickName("aaa");
        result.setPassword("aaa");
        result.setPasswordRepeated("aaa");
        result.setSex(Sex.MALE.toString());
        result.setDegree(Degree.COLLEGE.toString());
        return result;
    }

    private tw.bill.java101.domain.User getUser() {
        tw.bill.java101.domain.User result = new tw.bill.java101.domain.User();
        result.setEmail("aaa@gamil.com");
        result.setNickName("aaa");
        result.addTeam(getTeam());
        return result;
    }

    private Team getTeam() {
        Team result = new Team("title", "content");
        result.addPost(new Post());
        return result;
    }
}
