package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.Item;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {

    Item findById(long item_id);

    Item findByName(String name);

    List<Item> findAll();

    void deleteById(long item_id);

}
