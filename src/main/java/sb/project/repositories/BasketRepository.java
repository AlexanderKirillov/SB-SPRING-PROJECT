package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.Basket;
import sb.project.domain.Users;

import java.util.Optional;

public interface BasketRepository extends CrudRepository<Basket, Long> {
    Optional<Basket> findByUser(Users user);

    void deleteById(long id);
}
