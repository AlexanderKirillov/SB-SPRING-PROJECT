package sb.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sb.project.domain.User;
import sb.project.repositories.UserRepository;

import java.util.List;

@Controller
public class AdminUsersController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/admin/users")
    public String adminUsersPage(Model model, Authentication authentication) {
        List<User> userList = userRepository.findAll();

        if (authentication != null) {
            User currentUser = userRepository.findByUserName(authentication.getName()).get();
            userList.removeIf(user -> user.getId() == (currentUser.getId()));
        }
        model.addAttribute("users", userList);

        return "admin/admin-users";
    }

    @PostMapping(value = "/admin/users/{userId}/delete")
    public String adminDeleteUser(Model model, @PathVariable Long userId) {
        userRepository.deleteById(userId);

        return "redirect:/admin/users";
    }

    @PostMapping(value = "/admin/users/{userId}/setStatus")
    public String adminDeactivateUser(Model model, @PathVariable Long userId) {
        User user = userRepository.findById(userId).get();

        if (user.isActive()) {
            user.setActive(false);
        } else if (!user.isActive()) {
            user.setActive(true);
        }

        userRepository.save(user);

        return "redirect:/admin/users";
    }

    @PostMapping(value = "/admin/users/{userId}/setRole")
    public String adminSetUserRole(Model model, @PathVariable Long userId) {
        User user = userRepository.findById(userId).get();

        if (user.getRoles().equals("ROLE_USER")) {
            user.setRoles("ROLE_ADMIN");
        } else if (user.getRoles().equals("ROLE_ADMIN")) {
            user.setRoles("ROLE_USER");
        }

        userRepository.save(user);

        return "redirect:/admin/users";
    }
}