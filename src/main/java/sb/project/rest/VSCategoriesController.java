package sb.project.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sb.project.domain.Categories;
import sb.project.repositories.CategoriesRepository;

import java.util.List;


@Controller
public class VSCategoriesController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @GetMapping(value = "/admin-categories")
    public String adminCategoriesGet(Model model) {
        List<Categories> categoriesList = categoriesRepository.findAll();

        model.addAttribute("categories", categoriesList);

        return "admin-categories";
    }

    @GetMapping(value = "/admin")
    public String adminPage(Model model) {

        return "admin";
    }

    @RequestMapping(value = "/categories/{categoryId}/delete")
    public String deleteCategory(Model model, @PathVariable Long categoryId) {
        categoriesRepository.deleteById(categoryId);
        return "redirect:/admin-categories";
    }

    @RequestMapping(value = {"/admin-categories-add"}, method = RequestMethod.GET)
    public String showAddPersonPage(Model model) {

        Categories ctg = new Categories();
        model.addAttribute("category", ctg);

        return "admin-categories-add";
    }

    @RequestMapping(value = {"/admin-categories-add"}, method = RequestMethod.POST)
    public String savePerson(Model model,
                             @ModelAttribute("category") Categories ctg) {

        String name = ctg.getName();
        String description = ctg.getDescription();

        if (name != null && name.length() > 0
                && description != null && description.length() > 0) {
            Categories newPerson = new Categories(name, description);
            categoriesRepository.save(newPerson);

            return "redirect:/admin-categories";
        }

        model.addAttribute("errorMessage", "error");
        return "admin-categories";
    }

}