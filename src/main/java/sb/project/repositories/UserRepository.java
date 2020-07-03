package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByToken(String token);

    List<User> findAll();

    void deleteById(long id);

}
