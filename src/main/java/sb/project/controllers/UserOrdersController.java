package sb.project.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sb.project.domain.Category;
import sb.project.domain.Order;
import sb.project.domain.User;
import sb.project.repositories.CategoryRepository;
import sb.project.repositories.OrderRepository;
import sb.project.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserOrdersController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping(value = "/myOrders")
    public String userOrdersPage(Model model, Authentication authentication) {
        setCtgMenu(model);

        User currentUser = userRepository.findByUserName(authentication.getName()).get();

        List<Order> allOrders = orderRepository.findAll();
        List<Order> userOrders = new ArrayList<Order>();

        for (Order order : allOrders) {
            if (order.getOrderUser().equals(currentUser)) {
                userOrders.add(order);
            }
        }

        model.addAttribute("userOrders", userOrders);

        return "main/user-orders";
    }

    @RequestMapping(value = "/myOrders/{orderId}/info")
    public String userOrderInfo(Model model, @PathVariable long orderId, Authentication authentication) {
        setCtgMenu(model);

        User currentUser = userRepository.findByUserName(authentication.getName()).get();

        List<Order> allOrders = orderRepository.findAll();
        List<Order> userOrders = new ArrayList<Order>();

        for (Order order : allOrders) {
            if (order.getOrderUser().equals(currentUser)) {
                userOrders.add(order);
            }
        }

        if (userOrders.contains(orderRepository.findById(orderId).get())) {
            model.addAttribute("order", orderRepository.findById(orderId).get());

            return "main/user-order-info";

        } else {

            return "error/access-denied-page";
        }
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