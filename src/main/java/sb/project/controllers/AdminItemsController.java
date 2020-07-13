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
import java.math.BigDecimal;
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

        Item newItem = new Item();
        Item editItem = new Item();

        model.addAttribute("newItem", newItem);
        model.addAttribute("editItem", editItem);

        return "admin/admin-items";
    }

    @PostMapping(value = "/admin/category/{categoryId}/items/{itemId}/delete")
    public String adminDeleteItem(Model model, @PathVariable long itemId, @PathVariable long categoryId) {
        Category category = categoryRepository.findById(categoryId);
        List<Item> itemList = category.getItems();

        itemList.removeIf(item -> item.getId() == itemId);
        itemRepository.deleteById(itemId);

        return "redirect:/admin/items?selcat=0";
    }


    @PostMapping(value = {"/admin/items/add"})
    public String adminAddItem(Model model, @ModelAttribute("newItem") Item newItem,
                               @RequestParam("img") MultipartFile file, @RequestParam("categoryId") Long catId) throws IOException {
        Category ctg = categoryRepository.findById(catId).get();

        newItem.setCategory(ctg);
        newItem.setImage(file.getBytes());
        itemRepository.save(newItem);

        return "redirect:/admin/items?selcat=0";
    }


    @PostMapping(value = {"/admin/items/{itemId}/edit"})
    public String adminEditItem(Model model, @PathVariable long itemId, @ModelAttribute("editItem") Item editItem,
                                @RequestParam("img") MultipartFile file, @RequestParam("ctgid") long ctgid,
                                @RequestParam("articul") long articul, @RequestParam("name") String name,
                                @RequestParam("count") long count, @RequestParam("price") BigDecimal price,
                                @RequestParam("description") String description) throws IOException {
        Category ctg = categoryRepository.findById(ctgid);
        Item oldItem = itemRepository.findById(itemId);

        editItem.setId(itemId);
        editItem.setArticul(articul);
        editItem.setName(name);
        editItem.setCount(count);
        editItem.setPrice(price);
        editItem.setDescription(description);
        editItem.setCategory(ctg);
        if (!file.isEmpty()) {
            editItem.setImage(file.getBytes());
        } else {
            editItem.setImage(oldItem.getImage());
        }
        itemRepository.save(editItem);

        return "redirect:/admin/items?selcat=0";
    }
}