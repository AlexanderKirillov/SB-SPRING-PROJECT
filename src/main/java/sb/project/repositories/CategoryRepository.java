package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Category findById(long id);

    Category findByName(String name);

    List<Category> findAll();

    void deleteById(long category_id);

}
