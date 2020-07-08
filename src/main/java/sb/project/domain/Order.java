package sb.project.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dateTimeOrder;

    private String orderStatus;
    private String name;
    private String surname;
    private String middlename;
    private String address;
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ORDER_ID_F")
    private User orderUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SHOPPING_CART_ORDER_ID_F")
    private ShoppingCart shoppingCartOrder;

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateTimeOrder() {
        return dateTimeOrder;
    }

    public void setDateTimeOrder(Date dateTimeOrder) {
        this.dateTimeOrder = dateTimeOrder;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(User orderUser) {
        this.orderUser = orderUser;
    }

    public ShoppingCart getShoppingCartOrder() {
        return shoppingCartOrder;
    }

    public void setShoppingCartOrder(ShoppingCart shoppingCartOrder) {
        this.shoppingCartOrder = shoppingCartOrder;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Order)) return false;
        Order o = (Order) obj;
        return o.id == this.id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("ID заказа", id)
                .append("Дата заказа", dateTimeOrder)
                .append("Статус заказа", orderStatus)
                .append("Фамилия заказчика", surname)
                .append("Имя заказчика", name)
                .append("Отчество заказчика", middlename)
                .append("Адрес заказчика", address)
                .append("Телефон заказчика", phone)
                .append("Заказчик", orderUser)
                .append("Товары в заказе", shoppingCartOrder)
                .toString();
    }
}
