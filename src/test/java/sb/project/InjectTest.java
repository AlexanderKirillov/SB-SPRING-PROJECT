package sb.project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sb.project.repositories.*;
import sb.project.services.EmailService;
import sb.project.services.UserDetailsServiceImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InjectTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void injectTest() {
        assertNotNull(categoryRepository);
        assertNotNull(itemRepository);
        assertNotNull(orderRepository);
        assertNotNull(shoppingCartRepository);
        assertNotNull(shoppingCartItemRepository);
        assertNotNull(userRepository);
        assertNotNull(commentRepository);
        assertNotNull(emailService);
        assertNotNull(userDetailsService);
    }
}
