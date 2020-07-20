package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sb.project.domain.ShoppingCart;
import sb.project.domain.User;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByUser(User user);

    Optional<ShoppingCart> findByUserAndStatus(User user, String status);

    void deleteById(long id);
}
