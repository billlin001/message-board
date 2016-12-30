package tw.bill.java101.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.bill.java101.domain.Post;
import tw.bill.java101.domain.Team;
import tw.bill.java101.domain.User;
import tw.bill.java101.dto.TeamForm;
import tw.bill.java101.repository.PostRepository;
import tw.bill.java101.repository.TeamRepository;
import tw.bill.java101.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
	private TeamRepository teamRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Team> readAll() {
        return teamRepository.findAll();
    }

    public Team create(TeamForm teamForm, String userName) {
        Team team = new Team();
        BeanUtils.copyProperties(teamForm, team);
        User owner = userRepository.findOneByEmail(userName);
        team.setOwner(owner);
        team.addUser(owner);
        return teamRepository.save(team);
    }

    public Team readOne(long id) {
        return teamRepository.findOne(id);
    }

    public Team update(TeamForm teamForm) {
        Team team = teamRepository.findOne(teamForm.getId());
        team.setTitle(teamForm.getTitle());
        team.setDescription(teamForm.getDescription());
        return teamRepository.save(team);
    }

    public void delete(long id) {
        Team team = teamRepository.findOne(id);
        team.getPosts().forEach(item -> item.setTeam(null));
        team.getOwner().removeTeam(team);
        teamRepository.delete(team);
    }
}
