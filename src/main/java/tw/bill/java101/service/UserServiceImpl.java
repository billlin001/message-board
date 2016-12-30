package tw.bill.java101.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tw.bill.java101.domain.*;
import tw.bill.java101.dto.UserEditForm;
import tw.bill.java101.dto.UserForm;
import tw.bill.java101.helper.EncryptionHelper;
import tw.bill.java101.repository.PostRepository;
import tw.bill.java101.repository.TeamRepository;
import tw.bill.java101.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by bill33 on 2016/2/14.
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EncryptionHelper encryptionHelper;

    @Override
    public User getUserById(long id) {
        return userRepository.findOne(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("email"));
    }

    @Override
    public User create(UserForm userForm) {
        User user = new User();
        user.setEmail(userForm.getEmail());
        user.setNickName(userForm.getNickName());
        user.setPasswordHash(encryptionHelper.encryptString(userForm.getPassword()));
        user.setSex(Sex.forName(userForm.getSex()));
        user.setDegree(Degree.forName(userForm.getDegree()));
        user.setIntroduce(userForm.getIntroduce());
        user.setHobbies(userForm.getHobbies());
        user.setBirthday(userForm.getBirthday());
        return userRepository.save(user);
    }

    @Override
    public boolean canUpdateAndDeleteTeam(UserDetails userDetails, Long teamId) {
        Team team = teamRepository.findOne(teamId);
        if (team.getOwner().getEmail().equals(userDetails.getUsername())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canUpdateAndDeletePost(UserDetails userDetails, Long postId) {
        Post post = postRepository.findOne(postId);
        if (post.getOwner().getEmail().equals(userDetails.getUsername())) {
            return true;
        }
        return false;
    }

    @Override
    public void joinTeam(String userName, long teamId) {
        User user = userRepository.findOneByEmail(userName);
        Team team = teamRepository.findOne(teamId);
        user.addTeam(team);
        userRepository.save(user);
    }

    @Override
    public void quitTeam(String userName, long teamId) {
        User user = userRepository.findOneByEmail(userName);
        Team team = teamRepository.findOne(teamId);
        user.removeTeam(team);
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findOneByEmail(username);
    }

    @Override
    public void update(UserEditForm userEditForm) {
        User user = findByUserName(userEditForm.getEmail());
        user.setSex(Sex.forName(userEditForm.getSex()));
        user.setDegree(Degree.forName(userEditForm.getDegree()));
        user.setIntroduce(userEditForm.getIntroduce());
        user.setHobbies(userEditForm.getHobbies());
        user.setBirthday(userEditForm.getBirthday());
        userRepository.save(user);
    }

}
