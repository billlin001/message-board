package tw.bill.java101.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.bill.java101.domain.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
	List<Team> findByTitle(String title);
}
