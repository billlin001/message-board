package tw.bill.java101;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tw.bill.java101.domain.Post;
import tw.bill.java101.domain.Team;
import tw.bill.java101.domain.User;
import tw.bill.java101.domain.security.Role;
import tw.bill.java101.helper.EncryptionHelper;
import tw.bill.java101.repository.RoleRepsoitory;
import tw.bill.java101.repository.TeamRepository;
import tw.bill.java101.repository.UserRepository;

@SpringBootApplication
public class Application{

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}


	@Bean
	public CommandLineRunner loadData(TeamRepository teamRepository, UserRepository userRepository, RoleRepsoitory roleRepsoitory, EncryptionHelper encryptionHelper) {
		return (args) -> {
            log.info("load role data");
            Role role = new Role();
            role.setRole("CUSTOMER");
            roleRepsoitory.save(role);

            Role adminRole = new Role();
            adminRole.setRole("ADMIN");
//            adminRole = roleRepsoitory.save(adminRole);

//            adminRole = roleRepsoitory.findOne(adminRole.getId());


            log.info("load team data");
			teamRepository.save(new Team("Chloesss", "O'Brian"));
			teamRepository.save(new Team("Kim", "Bauer"));
			teamRepository.save(new Team("David", "Palmer"));
            for(int i = 0; i < 1; i++) teamRepository.save(new Team("Michelle", "Dessler"));

            log.info("load team data");
            Post post = new Post();
            post.setContent("test");
            Team team = new Team("Jack", "Bauer");
            team.addPost(post);
            User user = new User("aaa", "aaa", encryptionHelper.encryptString("aaa"));
            team.setOwner(user);
            user.addRole(adminRole);
            user.addTeam(team);

            userRepository.save(user);
        };
	}

}