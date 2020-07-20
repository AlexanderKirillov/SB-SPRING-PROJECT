package sb.project.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import sb.project.domain.Category;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestEntityManager
public class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category ctg;

    @Before
    public void init() {
        ctg = new Category();
        ctg.setId(1L);
        ctg.setName("TestCategoryName");
        ctg.setDescription("TestCategoryDescription");
        ctg.setStatus(true);
        ctg.setImage(null);
        ctg.setItems(null);

        categoryRepository.save(ctg);
    }

    @Test
    public void findByIdTest() {
        System.out.println("Starting CategoryRepository findById() test...");

        Category fCtg = categoryRepository.findById(ctg.getId());

        try {
            assertThat(ctg).isEqualTo(fCtg);
        } catch (AssertionError e) {
            System.out.println("\u001B[31m CategoryRepository findById() - NOT working.\u001B[0m");
            throw e;
        }
        System.out.println("\u001B[32mCategoryRepository findById() - success.\u001B[0m");
    }

    @Test
    public void findByNameTest() {
        System.out.println("Starting CategoryRepository findByName() test...");

        Category fCtg = categoryRepository.findByName(ctg.getName());

        try {
            assertThat(ctg).isEqualTo(fCtg);
        } catch (AssertionError e) {
            System.out.println("\u001B[31m CategoryRepository findByName()  - NOT working.\u001B[0m");
            throw e;
        }
        System.out.println("\u001B[32mCategoryRepository findByName() - success.\u001B[0m");
    }

    @Test
    public void findAllTest() {
        System.out.println("Starting CategoryRepository findAll() test...");

        List<Category> listCtg = categoryRepository.findAll();

        try {
            assertThat(listCtg).hasSize(1);
        } catch (AssertionError e) {
            System.out.println("\u001B[31m CategoryRepository findAll()  - NOT working.\u001B[0m");
            throw e;
        }
        System.out.println("\u001B[32mCategoryRepository findAll() - success.\u001B[0m");
    }

    @Test
    public void deleteByIdTest() {
        System.out.println("Starting CategoryRepository deleteById() test...");

        categoryRepository.deleteById(ctg.getId());

        try {
            assertThat(categoryRepository.findAll().isEmpty());
        } catch (AssertionError e) {
            System.out.println("\u001B[31m CategoryRepository deleteById()  - NOT working.\u001B[0m");
            throw e;
        }
        System.out.println("\u001B[32mCategoryRepository deleteById() - success.\u001B[0m");
    }
}
