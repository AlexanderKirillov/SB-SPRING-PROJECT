package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.Categories;

import java.util.List;

public interface CategoriesRepository extends CrudRepository<Categories, Long> {

    Categories findById(long category_id);

    Categories findByName(String name);

    List<Categories> findAll();

    void deleteById(long category_id);

}
