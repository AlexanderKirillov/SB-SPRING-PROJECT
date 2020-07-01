package sb.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<BasketItem> items;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID_F")
    private Users user;

    private BigDecimal totalPrice;

    public Basket() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void setItems(List<BasketItem> items) {
        this.items = items;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice() {
        this.totalPrice = BigDecimal.valueOf(0);
        List<BasketItem> items = this.getItems();
        for (BasketItem item : items) {
            this.totalPrice = this.totalPrice.add(item.getBitem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
    }

}
