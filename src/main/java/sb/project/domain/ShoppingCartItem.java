package sb.project.domain;

import javax.persistence.*;

@Entity
public class ShoppingCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long shoppingCartItem_id;

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
        return shoppingCartItem_id;
    }

    public void setId(long shoppingCartItem_id) {
        this.shoppingCartItem_id = shoppingCartItem_id;
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
}
