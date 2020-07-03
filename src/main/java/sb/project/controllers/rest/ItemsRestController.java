package sb.project.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sb.project.domain.Category;
import sb.project.domain.Item;
import sb.project.repositories.CategoryRepository;
import sb.project.repositories.ItemRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class ItemsRestController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping(value = "/categories/{categoryId}/items")
    public List<Item> getAllItems(@PathVariable Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        Category category = categoryOptional.get();

        return category.getItems();
    }

    @GetMapping(value = "/categories/{categoryId}/items/{itemId}")
    public Item getItemByID(@PathVariable Long itemId, @PathVariable Long categoryId) {
        Optional<Item> item = itemRepository.findById(itemId);

        Category category = item.get().getCategory();
        if (category.getId() == categoryId) {
            return item.get();
        } else {
            ResponseEntity.notFound().build();
            return null;
        }
    }

    @DeleteMapping("/categories/{categoryId}/items/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @PostMapping("/categories/{categoryId}/items")
    public ResponseEntity<Object> createItem(@RequestBody Item item) {
        Item savedItem = itemRepository.save(item);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{itemId}")
                .buildAndExpand(savedItem.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/categories/{categoryId}/items/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody Item item, @PathVariable Long itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);

        if (!itemOptional.isPresent())
            return ResponseEntity.notFound().build();
        item.setId(itemId);
        itemRepository.save(item);

        return ResponseEntity.noContent().build();
    }
}