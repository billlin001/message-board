package tw.bill.java101;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tw.bill.java101.conf.SecurityTestConfig;

/**
 * Created by bill33 on 2016/5/29.
 */
@Configuration
@ComponentScan("tw.bill.java101.conf")
@EnableWebMvc
@Import({SecurityTestConfig.class })
public class AppConfig {
}
