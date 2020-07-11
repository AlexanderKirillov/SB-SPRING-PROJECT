package sb.project.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sb.project.domain.Category;
import sb.project.repositories.CategoryRepository;

import java.io.IOException;
import java.util.List;

@Controller
public class AdminCategoriesController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping(value = "/admin/categories")
    public String adminCategoriesPage(Model model) {
        List<Category> categoryList = categoryRepository.findAll();

        model.addAttribute("categories", categoryList);

        for (Category category : categoryList) {
            byte[] image = category.getImage();
            category.setImageString(Base64.encodeBase64String(image));
        }

        return "admin/admin-categories";
    }

    @PostMapping(value = "/admin/categories/{categoryId}/delete")
    public String adminDeleteCategory(Model model, @PathVariable Long categoryId) {
        categoryRepository.deleteById(categoryId);

        return "redirect:/admin/categories";
    }

    @GetMapping(value = {"/admin/categories/add"})
    public String adminAddCategoryPage(Model model) {
        Category ctg = new Category();

        model.addAttribute("category", ctg);

        return "admin/admin-categories-add";
    }

    @PostMapping(value = {"/admin/categories/add"})
    public String adminAddCategory(Model model, @ModelAttribute("category") Category ctg,
                                   @RequestParam("img") MultipartFile file) throws IOException {

        ctg.setImage(file.getBytes());
        categoryRepository.save(ctg);

        return "redirect:/admin/categories";
    }

    @GetMapping(value = {"/admin/categories/{categoryId}/edit"})
    public String adminEditCategoryPage(Model model, @PathVariable long categoryId) {
        Category category = categoryRepository.findById(categoryId);

        model.addAttribute("category", category);

        return "admin/admin-categories-edit";
    }

    @PostMapping(value = {"/admin/categories/{categoryId}/edit"})
    public String adminEditCategory(Model model, @PathVariable long categoryId,
                                    @ModelAttribute("category") Category category, @RequestParam("img") MultipartFile file) throws IOException {
        category.setId(categoryId);
        Category oldCategory = categoryRepository.findById(categoryId);

        if (!file.isEmpty()) {
            category.setImage(file.getBytes());
        } else {
            category.setImage(oldCategory.getImage());
        }

        categoryRepository.save(category);

        return "redirect:/admin/categories";
    }
}