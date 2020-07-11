package sb.project.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sb.project.domain.Category;
import sb.project.domain.ShoppingCart;
import sb.project.domain.ShoppingCartItem;
import sb.project.domain.User;
import sb.project.repositories.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ShoppingCartController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/main/shoppingCart")
    public String shoppingCartPage(Model model, Authentication authentication) {
        setCtgMenu(model);

        List<ShoppingCartItem> shoppingCartItems = null;
        ShoppingCart shoppingCart = null;

        if (authentication != null) {
            User user = userRepository.findByUserName(authentication.getName()).get();
            Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUserAndStatus(user, "in_cart");

            if (shoppingCartOptional.isPresent()) {
                shoppingCart = shoppingCartOptional.get();
                shoppingCartItems = shoppingCart.getCartItems();
                shoppingCart.setTotalPrice();
            } else {
                shoppingCart = new ShoppingCart();
                shoppingCart.setUser(user);
                shoppingCart.setStatus("in_cart");
                shoppingCartItems = shoppingCart.getCartItems();
            }

            shoppingCartRepository.save(shoppingCart);
            model.addAttribute("shoppingCartItems", shoppingCartItems);
            model.addAttribute("shoppingCart", shoppingCart);
            model.addAttribute("totalPrice", shoppingCart.getTotalPrice());
        }

        return "main/shopping-cart";
    }

    @GetMapping(value = "/main/items/{itemId}/addToCart")
    public String addToShoppingCart(Model model, Authentication authentication, @PathVariable long itemId, RedirectAttributes redirectAttributes) {
        User user = null;
        ShoppingCart shoppingCart = null;
        ShoppingCartItem shoppingCartItem = null;

        if (authentication != null) {
            user = userRepository.findByUserName(authentication.getName()).get();
            Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUserAndStatus(user, "in_cart");

            if (shoppingCartOptional.isEmpty()) {
                shoppingCart = new ShoppingCart();
                shoppingCart.setUser(user);
                shoppingCart.setStatus("in_cart");
                shoppingCartRepository.save(shoppingCart);

                shoppingCartItem = new ShoppingCartItem();
                shoppingCartItem.setGoods(itemRepository.findById(itemId));
                shoppingCartItem.setShoppingCart(shoppingCart);
                shoppingCartItem.setQuantity(1);

                shoppingCartItemRepository.save(shoppingCartItem);

            } else {
                shoppingCart = shoppingCartRepository.findByUserAndStatus(user, "in_cart").get();

                List<ShoppingCartItem> shoppingCartItemList = shoppingCart.getCartItems();
                Optional<ShoppingCartItem> basItem = shoppingCartItemRepository.findByGoodsAndShoppingCart(itemRepository.findById(itemId), shoppingCart);

                if (basItem.isPresent() && shoppingCartItemList.contains(basItem.get())) {
                    ShoppingCartItem ShoppingCart_item = shoppingCartItemRepository.findByGoodsAndShoppingCart(itemRepository.findById(itemId), shoppingCart).get();
                    if (ShoppingCart_item.getQuantity() < ShoppingCart_item.getGoods().getCount()) {
                        ShoppingCart_item.setQuantity(ShoppingCart_item.getQuantity() + 1);
                        shoppingCartItemRepository.save(ShoppingCart_item);
                    } else {
                        redirectAttributes.addFlashAttribute("error", "Произошла ошибка при увеличении количества товара в корзине. На складе недостаточно товара!");
                    }
                } else {
                    shoppingCartItem = new ShoppingCartItem();
                    shoppingCartItem.setGoods(itemRepository.findById(itemId));
                    shoppingCartItem.setShoppingCart(shoppingCart);
                    shoppingCartItem.setQuantity(1);
                    shoppingCartItemRepository.save(shoppingCartItem);
                }
            }
        }

        return "redirect:/main/shoppingCart";
    }

    @PostMapping(value = "/main/shoppingCart/items/{itemId}/delete")
    public String shoppingCartDeleteItem(Model model, @PathVariable long itemId, Authentication authentication) {
        User user = null;
        Optional<ShoppingCart> shoppingCartOptional;
        ShoppingCart shoppingCart = null;
        List<ShoppingCartItem> shoppingCartItems = null;

        if (authentication != null) {
            user = userRepository.findByUserName(authentication.getName()).get();
            shoppingCartOptional = shoppingCartRepository.findByUserAndStatus(user, "in_cart");

            if (shoppingCartOptional.isPresent()) {
                shoppingCart = shoppingCartOptional.get();
                shoppingCartItems = shoppingCart.getCartItems();
                shoppingCartItems.removeIf(item -> item.getId() == itemId);
                shoppingCart.setCartItems(shoppingCartItems);
                shoppingCartRepository.save(shoppingCart);
            }
        }

        shoppingCartItemRepository.deleteById(itemId);

        return "redirect:/main/shoppingCart";
    }

    @RequestMapping(value = "/main/shoppingCart/items/{itemId}/addq")
    public String shoppingCartAddQuantity(@Valid Model model, @PathVariable long itemId, Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = null;
        Optional<ShoppingCart> shoppingCartOptional;
        ShoppingCart shoppingCart = null;

        if (authentication != null) {
            user = userRepository.findByUserName(authentication.getName()).get();

            shoppingCartOptional = shoppingCartRepository.findByUserAndStatus(user, "in_cart");

            if (shoppingCartOptional.isPresent()) {
                shoppingCart = shoppingCartOptional.get();
                ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findByIdAndShoppingCart(itemId, shoppingCart).get();
                if (shoppingCartItem.getQuantity() < shoppingCartItem.getGoods().getCount()) {
                    shoppingCartItem.setQuantity(shoppingCartItem.getQuantity() + 1);
                    shoppingCartItemRepository.save(shoppingCartItem);
                } else {
                    redirectAttributes.addFlashAttribute("error", "Произошла ошибка при увеличении количества товара в корзине. На складе недостаточно товара!");
                }
            }

        }

        return "redirect:/main/shoppingCart";
    }

    @RequestMapping(value = "/main/shoppingCart/items/{itemId}/remq")
    public String shoppingCartRemoveQuantity(Model model, @PathVariable long itemId, Authentication authentication) {
        User user = null;
        Optional<ShoppingCart> shoppingCartOptional;
        ShoppingCart shoppingCart = null;

        if (authentication != null) {
            user = userRepository.findByUserName(authentication.getName()).get();

            shoppingCartOptional = shoppingCartRepository.findByUserAndStatus(user, "in_cart");

            if (shoppingCartOptional.isPresent()) {
                shoppingCart = shoppingCartOptional.get();
                ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findByIdAndShoppingCart(itemId, shoppingCart).get();
                if (shoppingCartItem.getQuantity() == 1) {
                    shoppingCartDeleteItem(model, itemId, authentication);
                } else {
                    shoppingCartItem.setQuantity(shoppingCartItem.getQuantity() - 1);
                    shoppingCartItemRepository.save(shoppingCartItem);
                }
            }

        }

        return "redirect:/main/shoppingCart";
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