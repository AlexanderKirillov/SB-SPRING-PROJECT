package sb.project.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
public class ShoppingCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne()
    @JoinColumn(name = "SHOPPINGCART_ID_F", nullable = false)
    private ShoppingCart shoppingCart;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "GOODS_ID_F")
    private Item goods;

    private long quantity;

    public ShoppingCartItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Item getGoods() {
        return goods;
    }

    public void setGoods(Item goods) {
        this.goods = goods;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ShoppingCartItem)) return false;
        ShoppingCartItem o = (ShoppingCartItem) obj;
        return o.id == this.id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("ID элемента корзины", id)
                .append("Корзина, к которой относится элемент", shoppingCart)
                .append("Товар", goods)
                .append("Количество товара", quantity)
                .toString();
    }
}
