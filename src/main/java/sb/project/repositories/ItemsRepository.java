package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.Items;

import java.util.List;

public interface ItemsRepository extends CrudRepository<Items, Long> {

    Items findById(long item_id);

    Items findByName(String name);

    List<Items> findAll();

    void deleteById(long item_id);

}
