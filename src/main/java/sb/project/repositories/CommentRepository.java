package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sb.project.domain.Comment;
import sb.project.domain.Item;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    Comment findById(long id);

    List<Comment> findAll();

    List<Comment> findAllByCmtGoods(Item item);

    List<Comment> findAllByCmtGoodsOrderByDateTimeCommentDesc(Item item);

    void deleteById(long id);
}
