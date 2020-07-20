package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sb.project.domain.Order;
import sb.project.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    Optional<Order> findByOrderUser(User user);

    List<Order> findAll();

    List<Order> findAllByOrderByDateTimeOrderDesc();

    void deleteById(long id);

}
