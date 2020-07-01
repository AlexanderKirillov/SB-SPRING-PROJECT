package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.Basket;
import sb.project.domain.BasketItem;
import sb.project.domain.Items;

import java.util.Optional;

public interface BasketItemRepository extends CrudRepository<BasketItem, Long> {

    Optional<BasketItem> findById(long id);

    Optional<BasketItem> findByBitem(Items item);

    Optional<BasketItem> findByBitemAndBasket(Items item, Basket bs);

    void deleteById(long id);
}
