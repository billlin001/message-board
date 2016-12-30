package tw.bill.java101.service;

import org.springframework.security.core.userdetails.UserDetails;
import tw.bill.java101.domain.User;
import tw.bill.java101.dto.UserEditForm;
import tw.bill.java101.dto.UserForm;

import java.util.Collection;

/**
 * Created by bill33 on 2016/2/14.
 */
public interface UserService {
    User getUserById(long id);
    Collection<User> getAllUsers();
    User create(UserForm form);
    boolean canUpdateAndDeleteTeam(UserDetails userDetails, Long teamId);
    boolean canUpdateAndDeletePost(UserDetails userDetails, Long postId);
    void joinTeam(String userName, long teamId);
    void quitTeam(String userName, long teamId);
    User findById(Long id);
    User findByUserName(String username);
    void update(UserEditForm userForm);
}

