package sb.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sb.project.domain.Users;
import sb.project.repositories.UsersRepository;

import java.util.List;

@Controller
public class AdminUsersController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping(value = "/admin/users")
    public String adminUsersPage(Model model, Authentication authentication) {
        List<Users> userList = usersRepository.findAll();

        if (authentication != null) {
            Users currentUser = usersRepository.findByUserName(authentication.getName()).get();
            userList.removeIf(user -> user.getId() == (currentUser.getId()));
        }
        model.addAttribute("users", userList);

        return "admin-users";
    }

    @RequestMapping(value = "/admin/users/{userId}/delete")
    public String adminDeleteUser(Model model, @PathVariable Long userId) {
        usersRepository.deleteById(userId);

        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/admin/users/{userId}/setStatus")
    public String adminDeactivateUser(Model model, @PathVariable Long userId) {
        Users user = usersRepository.findById(userId).get();

        if (user.isActive()) {
            user.setActive(false);
        } else if (!user.isActive()) {
            user.setActive(true);
        }

        usersRepository.save(user);

        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/admin/users/{userId}/setRole")
    public String adminSetUserRole(Model model, @PathVariable Long userId) {
        Users user = usersRepository.findById(userId).get();

        if (user.getRoles().equals("ROLE_USER")) {
            user.setRoles("ROLE_ADMIN");
        } else if (user.getRoles().equals("ROLE_ADMIN")) {
            user.setRoles("ROLE_USER");
        }

        usersRepository.save(user);

        return "redirect:/admin/users";
    }
}