package tw.bill.java101.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.bill.java101.domain.User;

/**
 * Created by bill33 on 2016/2/14.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByEmail(String email);
}