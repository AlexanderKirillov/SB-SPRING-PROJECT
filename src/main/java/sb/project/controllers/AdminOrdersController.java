package sb.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sb.project.domain.Order;
import sb.project.domain.User;
import sb.project.repositories.OrderRepository;
import sb.project.repositories.UserRepository;
import sb.project.services.EmailService;

import java.util.List;
import java.util.Locale;

@Controller
public class AdminOrdersController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/admin/orders")
    public String adminOrdersPage(Model model) {
        List<Order> ordersList = orderRepository.findAllByOrderByDateTimeOrderDesc();

        model.addAttribute("orders", ordersList);

        return "admin/admin-orders";
    }

    @RequestMapping(value = "/admin/orders/{orderId}/cancel")
    public String adminCancelOrder(Model model, @PathVariable Long orderId, Authentication authentication, Locale locale) throws Exception {
        User currentUser = userRepository.findByUserName(authentication.getName()).get();
        Order order = orderRepository.findById(orderId).get();

        order.setOrderStatus("Отменен.");
        orderRepository.save(order);

        emailService.sendOrderStatusMail(currentUser.getEmail(), order, order.getShoppingCartOrder(), locale);

        return "redirect:/admin/orders";
    }

    @RequestMapping(value = "/admin/orders/{orderId}/info")
    public String adminOrderInfo(Model model, @PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId).get();

        model.addAttribute("order", order);

        return "admin/admin-order-info";
    }

    @PostMapping(value = "/admin/orders/{orderId}/delete")
    public String adminDeleteOrder(Model model, @PathVariable Long orderId) {
        orderRepository.deleteById(orderId);

        return "redirect:/admin/orders";
    }

    @RequestMapping(value = "/admin/orders/{orderId}/changestatus")
    public String adminChangeOrderStatus(Model model, @PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId).get();

        model.addAttribute("order", order);

        return "admin/admin-order-changestatus";
    }

    @PostMapping(value = "/admin/orders/{orderId}/changestatus")
    public String adminChangeOrderStatusProcess(Model model, @ModelAttribute("order") Order order, @PathVariable Long orderId, Authentication authentication, Locale locale) throws Exception {
        User currentUser = userRepository.findByUserName(authentication.getName()).get();
        Order uOrder = orderRepository.findById(orderId).get();

        uOrder.setOrderStatus(order.getOrderStatus());
        orderRepository.save(uOrder);

        emailService.sendOrderStatusMail(currentUser.getEmail(), uOrder, uOrder.getShoppingCartOrder(), locale);

        return "redirect:/admin/orders";
    }
}