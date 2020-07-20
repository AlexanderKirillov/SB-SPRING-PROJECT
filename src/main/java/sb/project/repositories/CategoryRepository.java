package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sb.project.domain.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    Category findById(long id);

    Category findByName(String name);

    List<Category> findAll();

    void deleteById(long id);

}
