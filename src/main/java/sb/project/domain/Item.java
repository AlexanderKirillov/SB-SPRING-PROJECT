package sb.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long articul;
    private String name;
    private long count;
    private BigDecimal price;
    private String description;
    private boolean status;
    @Lob
    private byte[] image;
    @Transient
    private String imageString;

    @JsonIgnore
    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    private List<ShoppingCartItem> shoppingCartItem;

    @JsonIgnore
    @OneToMany(mappedBy = "cmtGoods", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @ManyToOne()
    @JoinColumn(name = "CATEGORY_ID_F", nullable = false)
    private Category ctg;
    @Transient
    private String ctgName;
    @Transient
    private long ctgId;

    public Item() {
    }

    public Item(long articul, String name, long count, BigDecimal price, String description, boolean status, Category category) {
        this.articul = articul;
        this.name = name;
        this.count = count;
        this.price = price;
        this.description = description;
        this.status = status;
        this.ctg = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArticul() {
        return articul;
    }

    public void setArticul(long articul) {
        this.articul = articul;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Category getCategory() {
        return ctg;
    }

    public void setCategory(Category ctg) {
        this.ctg = ctg;
    }

    public String getCategoryName() {
        return ctg.getName();
    }

    public void setCategoryName(String ctgName) {
        this.ctgName = ctgName;
    }

    public long getCategoryId() {
        return ctg.getId();
    }

    public void setCategoryId(long ctgId) {
        this.ctgId = ctgId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public List<ShoppingCartItem> getShoppingCartItem() {
        return shoppingCartItem;
    }

    public void setShoppingCartItem(List<ShoppingCartItem> shoppingCartItem) {
        this.shoppingCartItem = shoppingCartItem;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Item)) return false;
        Item o = (Item) obj;
        return o.id == this.id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("ID товара", id)
                .append("Артикул", articul)
                .append("Название", name)
                .append("Описание", description)
                .append("Количество", count)
                .append("Цена", price)
                .append("Категория", ctgName)
                .toString();
    }
}
