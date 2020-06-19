package sb.project.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sb.project.domain.Categories;
import sb.project.repositories.CategoriesRepository;

import java.util.List;

@Controller
public class ThymeleafCategoriesController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @GetMapping(value = "/admin")
    public String adminMainPage(Model model) {
        return "admin";
    }

    @GetMapping(value = "/admin-categories")
    public String adminCategoriesPage(Model model) {
        List<Categories> categoriesList = categoriesRepository.findAll();

        model.addAttribute("categories", categoriesList);

        return "admin-categories";
    }

    @RequestMapping(value = "/categories/{categoryId}/delete")
    public String adminDeleteCategory(Model model, @PathVariable Long categoryId) {
        categoriesRepository.deleteById(categoryId);

        return "redirect:/admin-categories";
    }

    @GetMapping(value = {"/admin-categories-add"})
    public String adminAddCategoryPage(Model model) {
        Categories ctg = new Categories();

        model.addAttribute("category", ctg);

        return "admin-categories-add";
    }

    @PostMapping(value = {"/admin-categories-add"})
    public String adminAddCategory(Model model, @ModelAttribute("category") Categories ctg) {
        String name = ctg.getName();
        String description = ctg.getDescription();

        if (name != null && name.length() > 0 && description != null && description.length() > 0) {
            Categories newCategory = new Categories(name, description);
            categoriesRepository.save(newCategory);
        }

        return "redirect:/admin-categories";
    }

    @GetMapping(value = {"/categories/{categoryId}/edit"})
    public String adminEditCategoryPage(Model model, @PathVariable long categoryId) {
        Categories category = categoriesRepository.findById(categoryId);

        model.addAttribute("category", category);

        return "admin-categories-edit";
    }

    @PostMapping(value = {"/categories/{categoryId}/edit"})
    public String adminEditCategory(Model model, @PathVariable long categoryId, @ModelAttribute("category") Categories category) {
        category.setId(categoryId);
        categoriesRepository.save(category);

        return "redirect:/admin-categories";
    }

}