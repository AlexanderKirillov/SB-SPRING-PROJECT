package sb.project.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import sb.project.domain.Category;
import sb.project.domain.Item;
import sb.project.repositories.CategoryRepository;
import sb.project.repositories.ItemRepository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping(value = "/main")
    public String userMainPage(Model model, @RequestParam(value = "selcat", required = false) Long selcat, @ModelAttribute("ctgSel") Category ctgSel) {
        setCtgMenu(model);

        List<Item> itemsList;
        Set<Item> activeItems = new LinkedHashSet<Item>();

        if (selcat == null) {
            itemsList = itemRepository.findAll();
            for (Item item : itemsList) {
                if (item.getCategory().getStatus() && item.getStatus()) {
                    activeItems.add(item);
                }
            }
            model.addAttribute("currentCategory", "-");
        } else {
            itemsList = categoryRepository.findById(selcat).get().getItems();
            for (Item item : itemsList) {
                if (item.getStatus()) {
                    activeItems.add(item);
                }
            }
            model.addAttribute("currentCategory", categoryRepository.findById(selcat).get().getName());
        }

        model.addAttribute("items", activeItems);

        for (Item item : itemsList) {
            byte[] image = item.getImage();
            item.setImageString(Base64.encodeBase64String(image));
        }

        return "main/user-main";
    }

    @GetMapping(value = "/main/items/{itemId}")
    public String userItemPage(Model model, @PathVariable long itemId, @ModelAttribute("ctgSel") Category ctgSel) {
        setCtgMenu(model);

        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        byte[] image = item.getImage();
        item.setImageString(Base64.encodeBase64String(image));

        return "main/user-main-item";
    }

    public void setCtgMenu(Model model) {
        List<Category> categoryList = categoryRepository.findAll();
        List<Category> activeCategories = new ArrayList<Category>();

        for (Category ctg : categoryList) {
            if (ctg.getStatus()) {
                activeCategories.add(ctg);
            }
        }

        for (Category ctg : activeCategories) {
            byte[] img = ctg.getImage();
            ctg.setImageString(Base64.encodeBase64String(img));
        }

        model.addAttribute("categories", activeCategories);
    }
}