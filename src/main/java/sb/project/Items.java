package sb.project;

public class Items {
    private final long id;
    private final long articul;
    private final String name;
    private final long count;
    private final float price;
    private final String description;
    private final long category;

    public Items(long id, long articul, String name, long count,float price,String description,long category) {
        this.id = id;
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
}
