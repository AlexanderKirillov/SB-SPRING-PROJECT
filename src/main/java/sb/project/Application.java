package sb.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Optional;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        CategoriesRepository repository = context.getBean(CategoriesRepository.class);

        repository.save(new Categories("Видеокарты", "Раздел с различными видеокартами"));
        repository.save(new Categories("Процессоры", "Раздел с различными процессорами"));
        repository.save(new Categories("Оперативная память", "Раздел с различными вариантами оперативной памяти"));

        System.out.println();
        System.out.println();

        Iterable<Categories> categories1 = repository.findAll();
        System.out.println("Все доступные категории товаров (комплектующих для ПК):");
        System.out.println("-------------------------------");
        for (Categories ctg : categories1) {
            System.out.println(ctg);
        }
        System.out.println();
        System.out.println();

        Optional<Categories> categories2 = repository.findById(2L);
        System.out.println("Категория товаров с идентификатором 2:");
        System.out.println("--------------------------------");
        System.out.println(categories2);
        System.out.println();
        System.out.println();

        List<Categories> categories3 = repository.findByName("Видеокарты");
        System.out.println("Все доступные категории товаров с именем \"Видеокарты\":");
        System.out.println("--------------------------------------------");
        for (Categories ctg : categories3) {
            System.out.println(ctg);
        }
        System.out.println();
        System.out.println();

    }
}