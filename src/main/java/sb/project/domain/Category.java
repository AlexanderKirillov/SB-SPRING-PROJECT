package sb.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {

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
    private List<Item> items;

    public Category() {

    }

    public Category(String name, String description, boolean status) {
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
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
        if (!(obj instanceof Category)) return false;
        Category o = (Category) obj;
        return o.category_id == this.category_id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("ID категории", category_id)
                .append("Название", name)
                .append("Описание", description)
                .append("Статус", status)
                .toString();
    }
}
