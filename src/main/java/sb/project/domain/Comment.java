package sb.project.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dateTimeComment;

    @Column(columnDefinition = "TEXT")
    private String commentText;

    @ManyToOne()
    @JoinColumn(name = "USERCMT_ID_F")
    private User commentUser;

    @ManyToOne()
    @JoinColumn(name = "GOODCMT_ID_F")
    private Item cmtGoods;

    private float rating;

    public Comment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateTimeComment() {
        return dateTimeComment;
    }

    public void setDateTimeComment(Date dateTimeComment) {
        this.dateTimeComment = dateTimeComment;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public User getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(User commentUser) {
        this.commentUser = commentUser;
    }

    public Item getCmtGoods() {
        return cmtGoods;
    }

    public void setCmtGoods(Item cmtGoods) {
        this.cmtGoods = cmtGoods;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Comment)) return false;
        Comment o = (Comment) obj;
        return o.id == this.id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("ID комментария", id)
                .append("Текст комментария", commentText)
                .append("Пользьзователь", commentUser)
                .append("Товар", cmtGoods)
                .append("Дата комментария", dateTimeComment)
                .append("Рейтинг товара", rating)
                .toString();
    }
}
