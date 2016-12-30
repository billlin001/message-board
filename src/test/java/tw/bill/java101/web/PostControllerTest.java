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
import tw.bill.java101.dto.PostForm;
import tw.bill.java101.service.PostService;
import tw.bill.java101.utils.TestUtil;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by bill33 on 2016/3/20.
 */
public class PostControllerTest {
    @Mock
    private PostService postService;
    @InjectMocks
    private PostController target;

    TestingAuthenticationToken testingAuthenticationToken;

    MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(target).build();

        User user = new User("aaa","", AuthorityUtils.createAuthorityList("ROLE_PATRON"));
        testingAuthenticationToken =  new TestingAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }

    @Test
    public void newPost_NewPage() throws Exception {
        mockMvc.perform(get("/teams/{teamId}/posts/create", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("post/new"))
                .andExpect(model().attribute("teamId", is(1L)))
                .andExpect(model().attribute("postForm", allOf(isA(PostForm.class), notNullValue())));
    }

    @Test
    public void editPost_EditPage() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setContent("test");

        when(postService.readOne(1L)).thenReturn(post);

        mockMvc.perform(get("/teams/{teamId}/posts/{postId}/update", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("post/edit"))
                .andExpect(model().attribute("postForm", hasProperty("id", is(1L))))
                .andExpect(model().attribute("postForm", hasProperty("content", is("test"))));
    }

    @Test
    public void createPost_ContentIsTooLong_ShouldRenderFormView_AndReutnValidateErrorForContent() throws Exception {
        String content = TestUtil.createStringWithLength(201);

        mockMvc.perform(
                    post("/teams/{teamId}/posts/create", 1L)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("content", content)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("post/new"))
                .andExpect(model().attributeHasFieldErrors("postForm", "content"))
                .andExpect(model().attribute("postForm", hasProperty("id", is(0L))))
                .andExpect(model().attribute("postForm", hasProperty("content", is(content))));
    }

    @Test
    public void createPost_NewPostEntry_ShouldAddPostEntry_AndRenderViewPostsOfTeamView() throws Exception {
        PostForm postForm = new PostForm();
        postForm.setId(1L);
        postForm.setContent("test");

        when(postService.create(1L, postForm, "aaa")).thenReturn(new Post());

        mockMvc.perform(
                    post("/teams/{teamId}/posts/create", 1L)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("content", "test")
                    .principal(testingAuthenticationToken)
                )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/teams/1"))
                .andExpect(redirectedUrl("/teams/1"));
    }

    @Test
    public void updatePost_ContentIsTooLong_ShouldRenderFormView_AndReutnValidateErrorForContent() throws Exception {
        String content = TestUtil.createStringWithLength(201);

        mockMvc.perform(
                put("/teams/{teamId}/posts/{postId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("content", content)
                        .principal(testingAuthenticationToken)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("post/edit"))
                .andExpect(model().attributeHasFieldErrors("postForm", "content"))
                .andExpect(model().attribute("postForm", hasProperty("id", is(0L))))
                .andExpect(model().attribute("postForm", hasProperty("content", is(content))));
    }

    @Test
    public void updatePost_ModifyPostEntry_ShouldUpdatePostEntry_AndRenderViewPostsOfTeamView() throws Exception {
        PostForm postForm = new PostForm();
        postForm.setId(1L);
        postForm.setContent("test");

        when(postService.update(postForm)).thenReturn(new Post());

        mockMvc.perform(
                    put("/teams/{teamId}/posts/{postId}", 1L, 1L)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("content", "test")
                    .principal(testingAuthenticationToken)
                 )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/teams/1"))
                .andExpect(redirectedUrl("/teams/1"));

        verify(postService, times(1)).update(any());
    }

    @Test
    public void deletePost() throws Exception {
        mockMvc.perform(
                delete("/teams/{teamId}/posts/{postId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("content", "test")
                        .principal(testingAuthenticationToken)
                )
                .andExpect(status().isNoContent());
    }

}
