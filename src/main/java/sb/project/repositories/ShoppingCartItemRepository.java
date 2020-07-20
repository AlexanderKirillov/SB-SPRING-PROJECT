package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sb.project.domain.Item;
import sb.project.domain.ShoppingCart;
import sb.project.domain.ShoppingCartItem;

import java.util.Optional;

@Repository
public interface ShoppingCartItemRepository extends CrudRepository<ShoppingCartItem, Long> {

    Optional<ShoppingCartItem> findById(long id);

    Optional<ShoppingCartItem> findByGoods(Item item);

    Optional<ShoppingCartItem> findByGoodsAndShoppingCart(Item item, ShoppingCart shoppingCart);

    Optional<ShoppingCartItem> findByIdAndShoppingCart(long id, ShoppingCart shoppingcart);

    void deleteById(long id);
}
