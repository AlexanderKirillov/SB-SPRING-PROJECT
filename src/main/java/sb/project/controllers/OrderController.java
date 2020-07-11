package sb.project.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sb.project.domain.*;
import sb.project.repositories.*;
import sb.project.services.EmailService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class OrderController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/main/shoppingCart/checkout")
    public String checkoutPage(Model model, Authentication authentication) {
        setCtgMenu(model);

        User currentUser = userRepository.findByUserName(authentication.getName()).get();

        if (shoppingCartRepository.findByUserAndStatus(currentUser, "in_cart").get().getCartItems().isEmpty()) {
            return "error/error-checkout";
        } else {
            Order newOrder = new Order();
            newOrder.setOrderUser(currentUser);
            newOrder.setShoppingCartOrder(shoppingCartRepository.findByUserAndStatus(currentUser, "in_cart").get());
            newOrder.setName(currentUser.getName());
            newOrder.setSurname(currentUser.getSurname());

            model.addAttribute("newOrder", newOrder);
            model.addAttribute("shoppingCart", newOrder.getShoppingCartOrder());
            model.addAttribute("shoppingCartItems", newOrder.getShoppingCartOrder().getCartItems());

            return "main/order-form";
        }
    }

    @PostMapping(value = {"/main/shoppingCart/checkout"})
    public String checkoutPageForm(Model model, @ModelAttribute("newOrder") Order newOrder, Authentication authentication, Locale locale) throws Exception {
        setCtgMenu(model);

        User currentUser = userRepository.findByUserName(authentication.getName()).get();

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserAndStatus(currentUser, "in_cart").get();

        List<ShoppingCartItem> shpCartItemList = shoppingCart.getCartItems();
        List<Item> listItems = new ArrayList<Item>();
        List<Long> quantityList = new ArrayList<Long>();

        for (ShoppingCartItem shpCartItem : shpCartItemList) {
            listItems.add(shpCartItem.getGoods());
            quantityList.add(shpCartItem.getQuantity());
        }

        for (int i = 0; i < listItems.size(); i++) {
            listItems.get(i).setCount(listItems.get(i).getCount() - quantityList.get(i));
            itemRepository.save(listItems.get(i));
        }
        shoppingCart.setStatus("in_order");

        newOrder.setOrderStatus("Принят в обработку.");
        newOrder.setOrderUser(currentUser);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        newOrder.setDateTimeOrder(timestamp);
        newOrder.setShoppingCartOrder(shoppingCart);

        orderRepository.save(newOrder);

        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(currentUser);
        newShoppingCart.setStatus("in_cart");

        shoppingCartRepository.save(shoppingCart);
        shoppingCartRepository.save(newShoppingCart);

        model.addAttribute("order", newOrder);

        emailService.sendOrderMail(currentUser.getEmail(), newOrder, shoppingCart, locale);

        return "other/order-successful";
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