package tw.bill.java101.repository;

import org.springframework.data.repository.CrudRepository;
import tw.bill.java101.domain.Post;

/**
 * Created by bill33 on 2016/2/9.
 */
public interface PostRepository extends CrudRepository<Post, Long> {
    void deleteByTeamId(long id);
}
