package sb.project.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sb.project.domain.Category;
import sb.project.domain.Item;
import sb.project.repositories.CategoryRepository;
import sb.project.repositories.ItemRepository;

import java.io.IOException;
import java.util.List;

@Controller
public class AdminItemsController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping(value = "/admin/items")
    public String adminItemsPage(Model model, @RequestParam("selcat") Long selcat, @ModelAttribute("ctgSel") Category ctgSel) {
        List<Category> categoryList = categoryRepository.findAll();
        List<Item> itemList;

        model.addAttribute("categories", categoryList);

        if (selcat == 0) {
            itemList = itemRepository.findAll();
            model.addAttribute("items", itemList);
            model.addAttribute("currentCategory", "отсутствует");
        } else {
            itemList = categoryRepository.findById(selcat).get().getItems();
            model.addAttribute("items", itemList);
            model.addAttribute("currentCategory", categoryRepository.findById(selcat).get().getName());
        }

        for (Item item : itemList) {
            byte[] image = item.getImage();
            item.setImageString(Base64.encodeBase64String(image));
        }

        return "admin/admin-items";
    }

    @RequestMapping(value = "/admin/category/{categoryId}/items/{itemId}/delete")
    public String adminDeleteItem(Model model, @PathVariable long itemId, @PathVariable long categoryId) {
        Category category = categoryRepository.findById(categoryId);
        List<Item> itemList = category.getItems();

        itemList.removeIf(item -> item.getId() == itemId);
        itemRepository.deleteById(itemId);

        return "redirect:/admin/items?selcat=0";
    }


    @GetMapping(value = {"/admin/items/add"})
    public String adminAddItemPage(Model model) {
        List<Category> categoryList = categoryRepository.findAll();
        Item item = new Item();

        model.addAttribute("categories", categoryList);
        model.addAttribute("item", item);

        return "admin/admin-items-add";
    }

    @PostMapping(value = {"/admin/items/add"})
    public String adminAddItem(Model model, @ModelAttribute("item") Item item,
                               @RequestParam("img") MultipartFile file, @RequestParam("categoryId") Long catId) throws IOException {
        Category ctg = categoryRepository.findById(catId).get();

        item.setCategory(ctg);
        item.setImage(file.getBytes());
        itemRepository.save(item);

        return "redirect:/admin/items?selcat=0";
    }


    @GetMapping(value = {"/admin/items/{itemId}/edit"})
    public String adminEditItemPage(Model model, @PathVariable long itemId) {
        List<Category> categoryList = categoryRepository.findAll();

        model.addAttribute("categories", categoryList);
        model.addAttribute("item", itemRepository.findById(itemId));

        return "admin/admin-items-edit";
    }

    @PostMapping(value = {"/admin/items/{itemId}/edit"})
    public String adminEditItem(Model model, @PathVariable long itemId, @ModelAttribute("item") Item item,
                                @RequestParam("img") MultipartFile file, @RequestParam("ctgid") long ctgid) throws IOException {
        Category ctg = categoryRepository.findById(ctgid);
        Item oldItem = itemRepository.findById(itemId);

        item.setId(itemId);
        item.setCategory(ctg);
        if (!file.isEmpty()) {
            item.setImage(file.getBytes());
        } else {
            item.setImage(oldItem.getImage());
        }
        itemRepository.save(item);

        return "redirect:/admin/items?selcat=0";
    }
}