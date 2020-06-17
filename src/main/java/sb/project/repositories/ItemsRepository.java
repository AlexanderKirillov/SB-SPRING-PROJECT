package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.Items;

public interface ItemsRepository extends CrudRepository<Items, Long> {

    Items findByName(String name);

    void deleteById(long item_id);

    Items findById(long item_id);
}
