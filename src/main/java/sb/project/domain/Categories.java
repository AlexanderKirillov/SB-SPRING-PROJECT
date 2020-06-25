package sb.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long category_id;
    private String name;
    private String description;
    private boolean status;

    @Lob
    private byte[] image;
    @Transient
    private String imageString;

    @JsonIgnore
    @OneToMany(mappedBy = "ctg", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Items> items;

    public Categories() {
    }

    public Categories(String name, String description, boolean status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public long getId() {
        return category_id;
    }

    public void setId(long category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Items> getItems() {
        return items;
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
                "Categories[id=%d, name='%s', description='%s']",
                category_id, name, description);
    }
}
