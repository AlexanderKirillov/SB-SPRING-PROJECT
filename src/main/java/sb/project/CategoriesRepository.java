package sb.project;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoriesRepository extends CrudRepository<Categories, Long> {

    List<Categories> findByName(String name);
}
