package sb.project.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import sb.project.domain.Category;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class CategoryRepositoryMockTest {

    @MockBean
    private CategoryRepository categoryRepository;

    private Category ctg1;
    private Category ctg2;

    @Before
    public void init() {
        Category ctg1 = new Category();
        ctg1.setId(1L);
        ctg1.setName("TestCategory1Name");
        ctg1.setDescription("TestCategory1Description");
        ctg1.setStatus(true);
        ctg1.setImage(null);
        ctg1.setItems(null);

        Category ctg2 = new Category();
        ctg2.setId(2L);
        ctg2.setName("TestCategory2Name");
        ctg2.setDescription("TestCategory2Description");
        ctg2.setStatus(true);
        ctg2.setImage(null);
        ctg2.setItems(null);

        List<Category> listCtg = new ArrayList<>();
        listCtg.add(ctg1);
        listCtg.add(ctg2);

        Mockito.when(categoryRepository.findByName(ctg1.getName()))
                .thenReturn(ctg1);

        Mockito.when(categoryRepository.findById(ctg1.getId()))
                .thenReturn(ctg1);

        Mockito.when(categoryRepository.findByName(ctg2.getName()))
                .thenReturn(ctg2);

        Mockito.when(categoryRepository.findById(ctg2.getId()))
                .thenReturn(ctg2);

        Mockito.when(categoryRepository.findAll())
                .thenReturn(listCtg);

    }

    @Test
    public void findByIdTest() {
        System.out.println("Starting CategoryRepository findById() test...");

        Category fCtg = categoryRepository.findById(ctg1.getId());

        try {
            assertThat(ctg1).isEqualTo(fCtg);
        } catch (AssertionError e) {
            System.out.println("\u001B[31m CategoryRepository findById() - NOT working.\u001B[0m");
            throw e;
        }
        System.out.println("\u001B[32mCategoryRepository findById() - success.\u001B[0m");
    }

    @Test
    public void findByNameTest() {
        System.out.println("Starting CategoryRepository findByName() test...");

        Category fCtg = categoryRepository.findByName(ctg1.getName());

        try {
            assertThat(ctg1).isEqualTo(fCtg);
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
            assertThat(listCtg).hasSize(2);
        } catch (AssertionError e) {
            System.out.println("\u001B[31m CategoryRepository findAll()  - NOT working.\u001B[0m");
            throw e;
        }
        System.out.println("\u001B[32mCategoryRepository findAll() - success.\u001B[0m");
    }

    @Test
    public void deleteByIdTest() {
        System.out.println("Starting CategoryRepository deleteById() test...");

        categoryRepository.deleteById(ctg1.getId());

        try {
            assertThat(categoryRepository.findAll().isEmpty());
        } catch (AssertionError e) {
            System.out.println("\u001B[31m CategoryRepository deleteById()  - NOT working.\u001B[0m");
            throw e;
        }
        System.out.println("\u001B[32mCategoryRepository deleteById() - success.\u001B[0m");
    }
}
