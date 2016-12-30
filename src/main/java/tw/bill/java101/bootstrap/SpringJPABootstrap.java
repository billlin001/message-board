package tw.bill.java101.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import tw.bill.java101.repository.TeamRepository;
import tw.bill.java101.repository.UserRepository;

/**
 * Created by jt on 12/9/15.
 */
@Component
@Profile("develop")
public class SpringJPABootstrap implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        loadData();
    }

    private void loadData() {
//        teamRepository.save(new Team("Chloe", "O'Brian"));
//        teamRepository.save(new Team("Kim", "Bauer"));
//        teamRepository.save(new Team("David", "Palmer"));
//        teamRepository.save(new Team("Michelle", "Dessler"));
//
//        Team team = new Team("Jack", "Bauer");
//        Set<Team> teams = new HashSet<>();
//        teams.add(team);
//
//        User user = new User("aaa", "aaa", passwordEncoder.encodePassword("aaa", null));
//
//        user.setTeams(teams);
//        team.setOwner(user);
//
//        userRepository.save(user);
    }

}
