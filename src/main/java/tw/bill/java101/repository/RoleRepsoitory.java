package tw.bill.java101.repository;

import org.springframework.data.repository.CrudRepository;
import tw.bill.java101.domain.security.Role;

/**
 * Created by jt on 12/21/15.
 */
public interface RoleRepsoitory extends CrudRepository<Role, Integer> {
}
