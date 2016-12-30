package tw.bill.java101.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import tw.bill.java101.Application;

import static org.junit.Assert.*;

/**
 * Created by bill33 on 2016/5/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TeamRestControllerTest {

    @Test
    public void testFindOne() throws Exception {

    }
}