package sb.project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long articul;
    private String name;
    private long count;
    private float price;
    private String description;
    private long category;

    protected Items() {
    }

    public Items(long articul, String name, long count, float price, String description, long category) {
        this.articul = articul;
        this.name = name;
        this.count = count;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public long getArticul() {
        return articul;
    }

    public String getName() {
        return name;
    }

    public long getCount() {
        return count;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public long getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format(
                "Items[id=%d, articul='%s', name='%s', count='%d', price='%f', description='%s', category='%d']",
                id, articul, name, count, price, description, category);
    }
}
