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

        Category newCtg = new Category();
        Category editCtg = new Category();

        model.addAttribute("newCtg", newCtg);
        model.addAttribute("editCtg", editCtg);

        return "admin/admin-categories";
    }

    @PostMapping(value = "/admin/categories/{categoryId}/delete")
    public String adminDeleteCategory(Model model, @PathVariable Long categoryId) {
        categoryRepository.deleteById(categoryId);

        return "redirect:/admin/categories";
    }


    @PostMapping(value = {"/admin/categories/add"})
    public String adminAddCategory(Model model, @ModelAttribute("newCtg") Category newCtg,
                                   @RequestParam("img") MultipartFile file) throws IOException {

        newCtg.setImage(file.getBytes());
        categoryRepository.save(newCtg);

        return "redirect:/admin/categories";
    }

    @PostMapping(value = {"/admin/categories/{categoryId}/edit"})
    public String adminEditCategory(Model model, @PathVariable long categoryId,
                                    @ModelAttribute("editCtg") Category editCtg, @RequestParam("img") MultipartFile file,
                                    @RequestParam("name") String name, @RequestParam("description") String description) throws IOException {
        editCtg.setId(categoryId);
        editCtg.setName(name);
        editCtg.setDescription(description);
        Category oldCategory = categoryRepository.findById(categoryId);

        if (!file.isEmpty()) {
            editCtg.setImage(file.getBytes());
        } else {
            editCtg.setImage(oldCategory.getImage());
        }

        categoryRepository.save(editCtg);

        return "redirect:/admin/categories";
    }
}