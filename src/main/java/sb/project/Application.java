package sb.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import sb.project.domain.Categories;
import sb.project.domain.Items;
import sb.project.repositories.CategoriesRepository;
import sb.project.repositories.ItemsRepository;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        CategoriesRepository categoriesRepository = context.getBean(CategoriesRepository.class);
        ItemsRepository itemsRepository = context.getBean(ItemsRepository.class);

        Categories videokarts = new Categories("Видеокарты", "Раздел с различными видеокартами");
        Categories processors = new Categories("Процессоры", "Раздел с различными процессорами");
        Categories ram = new Categories("Оперативная память", "Раздел с различными вариантами оперативной памяти");

        categoriesRepository.save(videokarts);
        categoriesRepository.save(processors);
        categoriesRepository.save(ram);

        Items gtx_2080 = new Items(764648, "Nvidia GTX 2080 Ti", 10, 65990, "Топовая видеокарта от Nvidia", videokarts);
        Items intel_i7_9700k = new Items(834544, "Intel Core i7 9700k", 50, 35990, "Хороший процессор от Intel", processors);
        Items gtx_2060 = new Items(756453, "Nvidia GTX 2060", 5, 45990, "Хорошая видюха от Nvidia", videokarts);

        itemsRepository.save(gtx_2080);
        itemsRepository.save(intel_i7_9700k);
        itemsRepository.save(gtx_2060);

      /*System.out.println();
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
        System.out.println();*/

        /*List<Categories> categories3 = repository.findByName("Видеокарты");
        System.out.println("Все доступные категории товаров с именем \"Видеокарты\":");
        System.out.println("--------------------------------------------");
        for (Categories ctg : categories3) {
            System.out.println(ctg);
        }
        System.out.println();
        System.out.println();*/

    }
}