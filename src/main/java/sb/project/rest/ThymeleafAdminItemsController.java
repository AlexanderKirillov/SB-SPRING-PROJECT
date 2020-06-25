package sb.project.rest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sb.project.domain.Categories;
import sb.project.domain.Items;
import sb.project.repositories.CategoriesRepository;
import sb.project.repositories.ItemsRepository;

import java.io.IOException;
import java.util.List;

@Controller
public class ThymeleafAdminItemsController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @GetMapping(value = "/admin-items")
    public String adminItemsPage(Model model, @RequestParam("selcat") Long selcat, @ModelAttribute("ctgSel") Categories ctgSel) {
        List<Categories> categoriesList = categoriesRepository.findAll();
        List<Items> itemsList;

        model.addAttribute("categories", categoriesList);

        if (selcat == 0) {
            itemsList = itemsRepository.findAll();
            model.addAttribute("items", itemsList);
            model.addAttribute("currentCategory", "отсутствует");
        } else {
            itemsList = categoriesRepository.findById(selcat).get().getItems();
            model.addAttribute("items", itemsList);
            model.addAttribute("currentCategory", categoriesRepository.findById(selcat).get().getName());
        }

        for (Items item : itemsList) {
            byte[] image = item.getImage();
            item.setImageString(Base64.encodeBase64String(image));
        }

        return "admin-items";
    }

    @RequestMapping(value = "/category/{categoryId}/items/{itemId}/delete")
    public String adminDeleteItem(Model model, @PathVariable long itemId, @PathVariable long categoryId) {
        Categories category = categoriesRepository.findById(categoryId);
        List<Items> items = category.getItems();

        items.removeIf(item -> item.getId() == itemId);
        itemsRepository.deleteById(itemId);

        return "redirect:/admin-items?selcat=0";
    }


    @GetMapping(value = {"/admin-items-add"})
    public String adminAddItemPage(Model model) {
        List<Categories> categoriesList = categoriesRepository.findAll();
        model.addAttribute("categories", categoriesList);

        Items item = new Items();
        model.addAttribute("item", item);

        return "admin-items-add";
    }

    @PostMapping(value = {"/admin-items-add"})
    public String adminAddItem(Model model, @ModelAttribute("item") Items item,
                               @RequestParam("img") MultipartFile file, @RequestParam("categoryId") Long catId) throws IOException {
        Categories ctg = categoriesRepository.findById(catId).get();

        item.setCategory(ctg);
        item.setImage(file.getBytes());

        itemsRepository.save(item);

        return "redirect:/admin-items?selcat=0";
    }


    @GetMapping(value = {"/items/{itemId}/edit"})
    public String adminEditItemPage(Model model, @PathVariable long itemId) {
        List<Categories> categoriesList = categoriesRepository.findAll();

        model.addAttribute("categories", categoriesList);
        model.addAttribute("item", itemsRepository.findById(itemId));

        return "admin-items-edit";
    }

    @PostMapping(value = {"/items/{itemId}/edit"})
    public String adminEditItem(Model model, @PathVariable long itemId, @ModelAttribute("item") Items item,
                                @RequestParam("img") MultipartFile file, @RequestParam("ctgid") long ctgid) throws IOException {
        Categories ctg = categoriesRepository.findById(ctgid);

        item.setId(itemId);
        item.setCategory(ctg);
        item.setImage(file.getBytes());
        itemsRepository.save(item);

        return "redirect:/admin-items?selcat=0";
    }


}