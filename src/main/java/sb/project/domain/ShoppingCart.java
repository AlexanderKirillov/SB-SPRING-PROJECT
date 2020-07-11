package sb.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<ShoppingCartItem> cart_items;

    @ManyToOne()
    @JoinColumn(name = "USER_ID_F")
    private User user;

    private String status;
    private BigDecimal totalPrice;

    @OneToOne(mappedBy = "shoppingCartOrder")
    private Order order;

    public ShoppingCart() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ShoppingCart)) return false;
        ShoppingCart o = (ShoppingCart) obj;
        return o.id == this.id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("ID корзины", id)
                .append("Товары, находящиеся в корзине", cart_items)
                .append("Пользователь", user)
                .append("Статус (в корзине или уже в заказе)", status)
                .append("Общая цена", totalPrice)
                .toString();
    }
}
