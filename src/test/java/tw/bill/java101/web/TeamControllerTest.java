package tw.bill.java101.web;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tw.bill.java101.domain.Post;
import tw.bill.java101.domain.Team;
import tw.bill.java101.dto.TeamForm;
import tw.bill.java101.service.TeamService;
import tw.bill.java101.service.UserService;
import tw.bill.java101.utils.TestUtil;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by bill33 on 2016/5/30.
 */
public class TeamControllerTest {
    @Mock
    private TeamService teamService;
    @Mock
    private UserService userService;
    @InjectMocks
    private TeamController target;

    TestingAuthenticationToken testingAuthenticationToken;
    MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(target).build();

        User user = new User("aaa@gmail.com","aaa", AuthorityUtils.createAuthorityList("ROLE_PATRON"));
        testingAuthenticationToken =  new TestingAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }

    @Test
    public void test_listTeams() throws Exception {
        when(teamService.readAll())
                .thenReturn(Arrays.asList(new Team("title1", "content1"),
                        new Team("title2", "content2")));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("team/index"))
                .andExpect(model().attribute("teams", hasSize(2)))
                .andExpect(model().attribute("teams", hasItem(
                        allOf(
                                hasProperty("title", is("title1")),
                                hasProperty("description", is("content1"))
                        )
                )))
                .andExpect(model().attribute("teams", hasItem(
                        allOf(
                                hasProperty("title", is("title2")),
                                hasProperty("description", is("content2")
                        )
                ))));
    }

    @Test
    public void test_newTeam() throws Exception {
        mockMvc.perform(get("/teams/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("team/new"))
                .andExpect(model().attribute("teamForm", allOf(notNullValue())));
    }

    @Test
    public void test_showTeam() throws Exception {
        tw.bill.java101.domain.User user = getUserA();
        Team team = getTeam();
        when(userService.findByUserName(anyString())).thenReturn(user);
        when(teamService.readOne(1L)).thenReturn(team);

        mockMvc.perform(get("/teams/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("team/show"))
                .andExpect(model().attribute("team", sameInstance(team)))
                .andExpect(model().attribute("posts", hasSize(2)))
                .andExpect(model().attribute("isUserInTeam", is(true)));
    }

    @Test
    public void test_createTeam_hasTooLongTitle() throws Exception {
        Team team = getTeam();
        team.setTitle(TestUtil.createStringWithLength(201));

        mockMvc.perform(post("/teams/create")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("title", team.getTitle())
                    .param("description", team.getDescription())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("team/new"))
                .andExpect(model().attributeHasFieldErrors("teamForm", "title"));
    }

    @Test
    public void test_createTeam_addNewTeam() throws Exception {
        Team team = getTeam();

        when(teamService.create(any(), anyString())).thenReturn(team);

        mockMvc.perform(post("/teams/create")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("title", team.getTitle())
                    .param("description", team.getDescription())
        )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        verify(teamService, times(1)).create(any(), anyString());

    }

    @Test
    public void test_editTeam_RenderTeamEditPage() throws Exception {
        when(teamService.readOne(1L)).thenReturn(getTeam());

        mockMvc.perform(get("/teams/{id}/update", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("team/edit"))
                .andExpect(model().attribute("teamForm", allOf(notNullValue())));
    }

    @Test
    public void test_updateTeam_hasTooLongTitle_ShouldRenderErrorMessage() throws Exception {
        Team team = getTeam();
        team.setTitle(TestUtil.createStringWithLength(201));

        mockMvc.perform(put("/teams/{id}", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", team.getTitle())
                .param("description", team.getDescription())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("team/edit"))
                .andExpect(model().attributeHasFieldErrors("teamForm", "title"));
    }

    @Test
    public void test_updateTeam_AddNewTeam_ShouldRedirectToHomePage() throws Exception {
        Team team = getTeam();

        mockMvc.perform(put("/teams/{id}", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", team.getTitle())
                .param("description", team.getDescription())
                .principal(testingAuthenticationToken)
        )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        verify(teamService, times(1)).update(any());
    }

    @Test
    public void deleteTeam() throws Exception {
        Team team = getTeam();

        mockMvc.perform(
                delete("/teams/{teamId}", 1L)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", team.getTitle())
                        .param("description", team.getDescription())
                        .principal(testingAuthenticationToken)
        )
                .andExpect(status().isNoContent());

        verify(teamService, times(1)).delete(1L);
    }

    @Test
    public void listTeamsForPage() throws Exception {
        when(teamService.readAll())
                .thenReturn(Arrays.asList(new Team("title1", "content1"),
                        new Team("title2", "content2")));

        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(view().name("team/index"))
                .andExpect(model().attribute("teams", hasSize(2)))
                .andExpect(model().attribute("teams", hasItem(
                        allOf(
                                hasProperty("title", is("title1")),
                                hasProperty("description", is("content1"))
                        )
                )))
                .andExpect(model().attribute("teams", hasItem(
                        allOf(
                                hasProperty("title", is("title2")),
                                hasProperty("description", is("content2")
                                )
                        ))));
    }

    private tw.bill.java101.domain.User getUserA() {
        tw.bill.java101.domain.User result = new tw.bill.java101.domain.User("aaa@gmail.com", "aaa", "aaa");
        result.setId(1L);
        return result;
    }

    private tw.bill.java101.domain.User getUserB() {
        tw.bill.java101.domain.User result = new tw.bill.java101.domain.User("bbb@gmail.com", "bbb", "bbb");
        result.setId(2L);
        return result;
    }

    private Team getTeam() {
        Team result = new Team("title", "description");
        result.setUsers(Arrays.asList(getUserA(), getUserB()));
        result.setPosts(Arrays.asList(getPostA(), getPostB()));
        return result;
    }

    private Post getPostA() {
        return new Post("contentA");
    }

    private Post getPostB() {
        return new Post("contentB");
    }

}
