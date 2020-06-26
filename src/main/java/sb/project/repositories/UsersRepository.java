package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.Users;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<Users, Long> {
    Optional<Users> findByUserName(String userName);

    Optional<Users> findByToken(String token);

    List<Users> findAll();

    void deleteById(long id);
}
