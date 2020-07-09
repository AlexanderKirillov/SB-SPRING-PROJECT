package sb.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sb.project.domain.Category;
import sb.project.domain.Item;
import sb.project.domain.Order;
import sb.project.domain.User;
import sb.project.repositories.CategoryRepository;
import sb.project.repositories.ItemRepository;
import sb.project.repositories.OrderRepository;
import sb.project.repositories.UserRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.List;

@Controller
public class AdminStatsController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping(value = "/admin/stats")
    public String adminUsersPage(Model model) throws Exception {
        List<Category> allCategories = categoryRepository.findAll();
        long showingCategoriesQuantity = 0;
        long hiddenCategoriesQuantity = 0;

        List<Item> allItems = itemRepository.findAll();
        long showingItemsQuantity = 0;
        long hiddenItemsQuantity = 0;
        BigDecimal itemFullPrice = BigDecimal.valueOf(0);
        BigDecimal minItemPrice = allItems.get(1).getPrice();
        BigDecimal maxItemPrice = allItems.get(1).getPrice();

        for (Item item : allItems) {
            minItemPrice = minItemPrice.min(item.getPrice());
            maxItemPrice = maxItemPrice.max(item.getPrice());
            itemFullPrice = itemFullPrice.add(item.getPrice());
        }

        List<User> allUsers = userRepository.findAll();
        long activeUsersQuantity = 0;
        long mGenderUsersQuantity = 0;
        long wGenderUsersQuantity = 0;
        long inActiveUsersQuantity = 0;

        List<Order> allOrders = orderRepository.findAll();
        long activeOrdersQuantity = 0;
        long completedOrdersQuantity = 0;
        long canceledOrdersQuantity = 0;
        BigDecimal orderFullPrice = BigDecimal.valueOf(0);
        BigDecimal minOrderPrice = allOrders.get(1).getShoppingCartOrder().getTotalPrice();
        BigDecimal maxOrderPrice = allOrders.get(1).getShoppingCartOrder().getTotalPrice();
        BigDecimal allOrdersPrice = BigDecimal.valueOf(0);

        for (Category ctg : allCategories) {
            if (ctg.getStatus()) {
                showingCategoriesQuantity++;
            } else {
                hiddenCategoriesQuantity++;
            }
        }

        for (Item item : allItems) {
            if (item.getStatus()) {
                showingItemsQuantity++;
            } else {
                hiddenItemsQuantity++;
            }
        }

        for (User user : allUsers) {
            if (user.isActive()) {
                activeUsersQuantity++;
            } else {
                inActiveUsersQuantity++;
            }
            if (user.getGender().equals("мужской")) {
                mGenderUsersQuantity++;
            }
            if (user.getGender().equals("женский")) {
                wGenderUsersQuantity++;
            }
        }

        for (Order order : allOrders) {
            minOrderPrice = minOrderPrice.min(order.getShoppingCartOrder().getTotalPrice());
            maxOrderPrice = maxOrderPrice.max(order.getShoppingCartOrder().getTotalPrice());
            orderFullPrice = orderFullPrice.add(order.getShoppingCartOrder().getTotalPrice());
            allOrdersPrice = allOrdersPrice.add(order.getShoppingCartOrder().getTotalPrice());

            if (order.getOrderStatus().equals("Отменен.")) {
                canceledOrdersQuantity++;
            } else if (order.getOrderStatus().equals("Завершен.")) {
                completedOrdersQuantity++;
            } else {
                activeOrdersQuantity++;
            }
        }

        model.addAttribute("categoryQuantity", categoryRepository.findAll().size());
        model.addAttribute("hiddenCategoriesQuantity", hiddenCategoriesQuantity);
        model.addAttribute("showingCategoriesQuantity", showingCategoriesQuantity);

        model.addAttribute("itemsQuantity", itemRepository.findAll().size());
        model.addAttribute("hiddenItemsQuantity", hiddenItemsQuantity);
        model.addAttribute("showingItemsQuantity", showingItemsQuantity);
        model.addAttribute("avgItemsPrice", itemFullPrice.divide(BigDecimal.valueOf(allItems.size()), 2, RoundingMode.HALF_UP));
        model.addAttribute("minItemPrice", minItemPrice);
        model.addAttribute("maxItemPrice", maxItemPrice);

        model.addAttribute("usersQuantity", allUsers.size());
        model.addAttribute("mGenderQuantity", mGenderUsersQuantity);
        model.addAttribute("wGenderQuantity", wGenderUsersQuantity);
        model.addAttribute("activeUsersQuantity", activeUsersQuantity);
        model.addAttribute("inActiveUsersQuantity", inActiveUsersQuantity);

        model.addAttribute("ordersQuantity", allOrders.size());
        model.addAttribute("activeOrdersQuantity", activeOrdersQuantity);
        model.addAttribute("completedOrdersQuantity", completedOrdersQuantity);
        model.addAttribute("canceledOrdersQuantity", canceledOrdersQuantity);
        model.addAttribute("avgOrdersPrice", orderFullPrice.divide(BigDecimal.valueOf(allOrders.size()), 2, RoundingMode.HALF_UP));
        model.addAttribute("minOrderPrice", minOrderPrice);
        model.addAttribute("maxOrderPrice", maxOrderPrice);
        model.addAttribute("allOrdersPrice", allOrdersPrice);

        URL url = new URL("https://api.ipify.org/");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        model.addAttribute("currentIP", br.readLine());

        return "admin/admin-stats";
    }
}