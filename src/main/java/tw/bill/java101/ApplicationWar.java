package tw.bill.java101;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


//public class ApplicationWar extends SpringBootServletInitializer {
public class ApplicationWar {

	private static final Logger log = LoggerFactory.getLogger(ApplicationWar.class);

//    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationWar.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(ApplicationWar.class);
	}

//	public CommandLineRunner addTeams(TeamRepository teamRepository, UserRepository userRepository) {
//		return (args) -> {
//			// save a couple of topics
//			teamRepository.save(new Team("Chloe", "O'Brian"));
//			teamRepository.save(new Team("Kim", "Bauer"));
//			teamRepository.save(new Team("David", "Palmer"));
//			teamRepository.save(new Team("Michelle", "Dessler"));
//
//            Team team = new Team("Jack", "Bauer");
//            Set<Team> teams = new HashSet<>();
//            teams.add(team);
//
//            User user = new User("aaa", "aaa", new BCryptPasswordEncoder().encode("aaa"), Role.USER);
//
//            user.setTeams(teams);
//            team.setOwner(user);
//
//            userRepository.save(user);
//        };
//	}

}