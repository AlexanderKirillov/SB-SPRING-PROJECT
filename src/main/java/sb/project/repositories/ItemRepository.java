package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.Category;
import sb.project.domain.Item;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {

    Item findById(long id);

    Item findByName(String name);

    List<Item> findAll();

    List<Item> findAllByCtg(Category ctg);


    List<Item> findAllByOrderByPriceDesc();

    List<Item> findAllByCtgOrderByPriceDesc(Category ctg);

    List<Item> findAllByOrderByPriceAsc();

    List<Item> findAllByCtgOrderByPriceAsc(Category ctg);

    List<Item> findAllByOrderByRatingDesc();

    List<Item> findAllByCtgOrderByRatingDesc(Category ctg);

    List<Item> findAllByOrderByRatingAsc();

    List<Item> findAllByCtgOrderByRatingAsc(Category ctg);


    void deleteById(long item_id);
}
