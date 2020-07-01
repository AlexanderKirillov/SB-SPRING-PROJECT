package sb.project.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sb.project.domain.Basket;
import sb.project.domain.BasketItem;
import sb.project.domain.Categories;
import sb.project.domain.Users;
import sb.project.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BasketController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private BasketItemRepository basketItemRepository;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping(value = "/main/basket")
    public String basketPage(Model model, @RequestParam(value = "selcat", required = false) Long selcat, @ModelAttribute("ctgSel") Categories ctgSel, Authentication authentication) {
        setCtgMenu(model);

        List<BasketItem> b_items = null;
        Basket bs = null;

        if (authentication != null) {
            Users user = usersRepository.findByUserName(authentication.getName()).get();
            Optional<Basket> basketOptional = basketRepository.findByUser(user);

            if (basketRepository.findByUser(user).isPresent()) {
                bs = basketOptional.get();
                b_items = bs.getItems();
                bs.setTotalPrice();
            } else {
                bs = new Basket();
                bs.setUser(user);
                b_items = bs.getItems();
            }

            basketRepository.save(bs);
            model.addAttribute("bitems", b_items);
            model.addAttribute("basket", bs);
            model.addAttribute("totPrice", bs.getTotalPrice());
        }

        return "basket";
    }

    @GetMapping(value = "/main/items/{itemId}/addtobasket")
    public String addToBasket(Model model, Authentication authentication, @PathVariable long itemId) {
        Users user = null;
        Basket bs = null;
        BasketItem b_item = null;

        if (authentication != null) {
            user = usersRepository.findByUserName(authentication.getName()).get();

            if (basketRepository.findByUser(user).isEmpty()) {
                bs = new Basket();
                bs.setUser(user);
                basketRepository.save(bs);

                b_item = new BasketItem();
                b_item.setBitem(itemsRepository.findById(itemId));
                b_item.setBasket(bs);
                b_item.setQuantity(1);

                basketItemRepository.save(b_item);

            } else {
                bs = basketRepository.findByUser(user).get();

                List<BasketItem> basketItemList = bs.getItems();
                Optional<BasketItem> basItem = basketItemRepository.findByBitemAndBasket(itemsRepository.findById(itemId), bs);

                if (basItem.isPresent() && basketItemList.contains(basItem.get())) {
                    BasketItem basket_item = basketItemRepository.findByBitemAndBasket(itemsRepository.findById(itemId), bs).get();
                    basket_item.setQuantity(basket_item.getQuantity() + 1);
                    basketItemRepository.save(basket_item);
                } else {
                    b_item = new BasketItem();
                    b_item.setBitem(itemsRepository.findById(itemId));
                    b_item.setBasket(bs);
                    b_item.setQuantity(1);
                    basketItemRepository.save(b_item);
                }
            }
        }

        return "redirect:/main/basket";
    }

    @RequestMapping(value = "/main/basket/items/{itemId}/delete")
    public String basketDeleteItem(Model model, @PathVariable long itemId, Authentication authentication) {
        Users user = null;
        Optional<Basket> basketOptional;
        Basket bs = null;
        List<BasketItem> b_items = null;

        if (authentication != null) {
            user = usersRepository.findByUserName(authentication.getName()).get();
            basketOptional = basketRepository.findByUser(user);

            if (basketOptional.isPresent()) {
                bs = basketOptional.get();
                b_items = bs.getItems();
                b_items.removeIf(item -> item.getId() == itemId);
                bs.setItems(b_items);
                basketRepository.save(bs);
            }
        }

        basketItemRepository.deleteById(itemId);

        return "redirect:/main/basket";
    }

    @RequestMapping(value = "/main/basket/items/{itemId}/addq")
    public String basketAddQuantity(Model model, @PathVariable long itemId, Authentication authentication) {

        BasketItem b_item = basketItemRepository.findById(itemId).get();

        if (b_item.getQuantity() < b_item.getBitem().getCount()) {
            b_item.setQuantity(b_item.getQuantity() + 1);
            basketItemRepository.save(b_item);
        }

        return "redirect:/main/basket";
    }

    @RequestMapping(value = "/main/basket/items/{itemId}/remq")
    public String basketRemoveQuantity(Model model, @PathVariable long itemId, Authentication authentication) {

        BasketItem b_item = basketItemRepository.findById(itemId).get();

        if (b_item.getQuantity() == 1) {
            basketDeleteItem(model, itemId, authentication);
        } else {
            b_item.setQuantity(b_item.getQuantity() - 1);
            basketItemRepository.save(b_item);
        }

        return "redirect:/main/basket";
    }

    public void setCtgMenu(Model model) {
        List<Categories> categoriesList = categoriesRepository.findAll();
        List<Categories> activeCategories = new ArrayList<Categories>();

        for (Categories ctg : categoriesList) {
            if (ctg.getStatus()) {
                activeCategories.add(ctg);
            }
        }

        for (Categories ctg : activeCategories) {
            byte[] img = ctg.getImage();
            ctg.setImageString(Base64.encodeBase64String(img));
        }

        model.addAttribute("categories", activeCategories);
    }
}