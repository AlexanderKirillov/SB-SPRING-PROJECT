package sb.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sb.project.domain.Comment;
import sb.project.domain.Item;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    Comment findById(long id);

    List<Comment> findAll();

    List<Comment> findAllByCmtGoods(Item item);

    List<Comment> findAllByCmtGoodsOrderByDateTimeCommentDesc(Item item);

    void deleteById(long id);
}
