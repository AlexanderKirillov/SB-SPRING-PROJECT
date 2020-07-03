package sb.project.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sb.project.domain.Category;
import sb.project.repositories.CategoryRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class CategoriesRestController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping(value = "/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping(value = "/categories/{categoryId}")
    public Category getCategoryByID(@PathVariable Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);

        return category.get();
    }

    @DeleteMapping("/categories/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @PostMapping("/categories")
    public ResponseEntity<Object> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryRepository.save(category);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{categoryId}")
                .buildAndExpand(savedCategory.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Object> updateCategory(@RequestBody Category category, @PathVariable Long categoryId) {
        Optional<Category> categoriesOptional = categoryRepository.findById(categoryId);

        if (!categoriesOptional.isPresent())
            return ResponseEntity.notFound().build();
        category.setId(categoryId);
        categoryRepository.save(category);

        return ResponseEntity.noContent().build();
    }

}