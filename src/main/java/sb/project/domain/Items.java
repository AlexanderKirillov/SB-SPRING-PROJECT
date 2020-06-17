package sb.project.domain;

import javax.persistence.*;

@Entity
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long item_id;
    private long articul;
    private String name;
    private long count;
    private float price;
    private String description;

    @ManyToOne()
    @JoinColumn(name = "CATEGORY_ID_F", nullable = false)
    private Categories ctg;

    protected Items() {
    }

    public Items(long articul, String name, long count, float price, String description, Categories category) {
        this.articul = articul;
        this.name = name;
        this.count = count;
        this.price = price;
        this.description = description;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescription(Categories category) {
        this.ctg = category;
    }

    public Categories getCategory() {
        return ctg;
    }

    public void setCategory(Categories ctg) {
        this.ctg = ctg;
    }

    @Override
    public String toString() {
        return String.format(
                "Items[id=%d, articul='%s', name='%s', count='%d', price='%f', description='%s', category='%d']",
                item_id, articul, name, count, price, description, ctg);
    }
}
