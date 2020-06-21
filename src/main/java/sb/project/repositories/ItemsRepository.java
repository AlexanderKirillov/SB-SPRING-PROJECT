package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.Items;

import java.util.List;

public interface ItemsRepository extends CrudRepository<Items, Long> {

    Items findByName(String name);

    List<Items> findAll();

    Items findById(long item_id);

    void deleteById(long item_id);


}
