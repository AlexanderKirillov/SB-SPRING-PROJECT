package sb.project.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sb.project.domain.Categories;
import sb.project.domain.Items;
import sb.project.repositories.CategoriesRepository;
import sb.project.repositories.ItemsRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class ItemsController {

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @GetMapping(value = "/categories/{categoryId}/items")
    public List<Items> getAllItems(@PathVariable Long categoryId) {
        Optional<Categories> categoryOptional = categoriesRepository.findById(categoryId);
        Categories category = categoryOptional.get();

        return category.getItems();
    }

    @GetMapping(value = "/categories/{categoryId}/items/{itemId}")
    public Items getItemByID(@PathVariable Long itemId, @PathVariable Long categoryId) {
        Optional<Items> item = itemsRepository.findById(itemId);

        Categories category = item.get().getCategory();
        if (category.getId() == categoryId) {
            return item.get();
        } else {
            ResponseEntity.notFound().build();
            return null;
        }
    }

    @DeleteMapping("/categories/{categoryId}/items/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        itemsRepository.deleteById(itemId);
    }

    @PostMapping("/categories/{categoryId}/items")
    public ResponseEntity<Object> createItem(@RequestBody Items item) {
        Items savedItem = itemsRepository.save(item);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{itemId}")
                .buildAndExpand(savedItem.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/categories/{categoryId}/items/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody Items item, @PathVariable Long itemId) {

        Optional<Items> itemOptional = itemsRepository.findById(itemId);

        if (!itemOptional.isPresent())
            return ResponseEntity.notFound().build();

        item.setId(itemId);

        itemsRepository.save(item);

        return ResponseEntity.noContent().build();
    }
}