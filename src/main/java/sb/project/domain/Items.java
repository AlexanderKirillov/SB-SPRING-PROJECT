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

    @Transient
    private String ctgName;

    @Transient
    private long ctgId;

    @Lob
    private byte[] image;
    @Transient
    private String imageString;

    public Items() {
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
    public String toString() {
        return String.format(
                "Items[id=%d, articul='%s', name='%s', count='%d', price='%f', description='%s']",
                item_id, articul, name, count, price, description);
    }
}
