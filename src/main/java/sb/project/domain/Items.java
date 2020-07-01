package sb.project.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long item_id;
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

    @ManyToOne()
    @JoinColumn(name = "CATEGORY_ID_F", nullable = false)
    private Categories ctg;

    @OneToOne(mappedBy = "bitem")
    private BasketItem basketItem;

    @Transient
    private String ctgName;
    @Transient
    private long ctgId;

    public Items() {

    }

    public Items(long articul, String name, long count, BigDecimal price, String description, boolean status, Categories category) {
        this.articul = articul;
        this.name = name;
        this.count = count;
        this.price = price;
        this.description = description;
        this.status = status;
        this.ctg = category;
    }

    public long getId() {
        return item_id;
    }

    public void setId(long item_id) {
        this.item_id = item_id;
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

    public Categories getCategory() {
        return ctg;
    }

    public void setCategory(Categories ctg) {
        this.ctg = ctg;
    }

    public String getCategoryName() {
        return ctg.getName();
    }

    public long getCategoryId() {
        return ctg.getId();
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Items)) return false;
        Items o = (Items) obj;
        return o.item_id == this.item_id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("ID товара", item_id)
                .append("Артикул", articul)
                .append("Название", name)
                .append("Описание", description)
                .append("Количество", count)
                .append("Цена", price)
                .append("Категория", ctgName)
                .toString();
    }
}
