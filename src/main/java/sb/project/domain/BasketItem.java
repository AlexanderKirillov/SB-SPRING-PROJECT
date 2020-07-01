package sb.project.domain;

import javax.persistence.*;

@Entity
public class BasketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne()
    @JoinColumn(name = "BASKET_ID_F", nullable = false)
    private Basket basket;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ITEM_ID_F")
    private Items bitem;

    private long quantity;

    public BasketItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public Items getBitem() {
        return bitem;
    }

    public void setBitem(Items bitem) {
        this.bitem = bitem;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
