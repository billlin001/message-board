package tw.bill.java101.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tw.bill.java101.conf.JpaIntegrationConfig;
import tw.bill.java101.domain.Post;
import tw.bill.java101.domain.Team;
import tw.bill.java101.domain.User;
import tw.bill.java101.dto.PostForm;
import tw.bill.java101.dto.TeamForm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created by bill33 on 2016/2/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(JpaIntegrationConfig.class)
public class PostServiceTestIT {
    @Autowired
    private TeamService teamService;

    @Autowired
    private PostService target;

    Team team;
    Post post;
    User user;

    TeamForm teamForm;
    PostForm postForm;

    @Before
    public void setup() {
        post = new Post();
        user = new User();
        user.setEmail("aaa");

        postForm = new PostForm();
        postForm.setId(1L);
        postForm.setContent("content");
    }

    @Test
    public void testReadOne() throws Exception {
        Post actual = target.readOne(1L);
        assertNotNull(actual);
    }

    @Test
    public void testCreate() throws Exception {
        Post actual = target.create(1L, postForm, "aaa");
        assertNotNull(actual);
    }

    @Test
    public void testUpdate() {
        String expected = "content";
        Post actual = target.update(postForm);
        assertEquals(expected, actual.getContent());
    }

    @Test
    public void testDelete() {
        Post savePost = target.create(1L, postForm, "aaa");
        Team team = teamService.readOne(1L);
        int expected = team.getPosts().size() - 1;
        target.delete(savePost.getId());
        team = teamService.readOne(1L);
        assertEquals(expected, team.getPosts().size());
    }


}