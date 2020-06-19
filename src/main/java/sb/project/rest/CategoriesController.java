package sb.project.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sb.project.domain.Categories;
import sb.project.repositories.CategoriesRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class CategoriesController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @GetMapping(value = "/categories")
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    @GetMapping(value = "/categories/{categoryId}")
    public Categories getCategoryByID(@PathVariable Long categoryId) {
        Optional<Categories> category = categoriesRepository.findById(categoryId);

        return category.get();
    }

    @DeleteMapping("/categories/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoriesRepository.deleteById(categoryId);
    }

    @PostMapping("/categories")
    public ResponseEntity<Object> createCategory(@RequestBody Categories category) {
        Categories savedCategory = categoriesRepository.save(category);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{categoryId}")
                .buildAndExpand(savedCategory.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Object> updateCategory(@RequestBody Categories category, @PathVariable Long categoryId) {
        Optional<Categories> categoriesOptional = categoriesRepository.findById(categoryId);

        if (!categoriesOptional.isPresent())
            return ResponseEntity.notFound().build();
        category.setId(categoryId);
        categoriesRepository.save(category);

        return ResponseEntity.noContent().build();
    }

}