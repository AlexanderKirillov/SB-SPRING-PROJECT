package sb.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long shoppingCart_id;

    @JsonIgnore
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<ShoppingCartItem> cart_items;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID_F")
    private User user;

    private BigDecimal totalPrice;

    public ShoppingCart() {
    }

    public long getId() {
        return shoppingCart_id;
    }

    public void setId(long shoppingCart_id) {
        this.shoppingCart_id = shoppingCart_id;
    }

    public List<ShoppingCartItem> getCartItems() {
        return cart_items;
    }

    public void setCartItems(List<ShoppingCartItem> cart_items) {
        this.cart_items = cart_items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice() {
        this.totalPrice = BigDecimal.valueOf(0);
        List<ShoppingCartItem> items = this.getCartItems();
        for (ShoppingCartItem item : items) {
            this.totalPrice = this.totalPrice.add(item.getGoods().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
    }

}
