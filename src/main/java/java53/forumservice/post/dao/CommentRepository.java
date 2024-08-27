package java53.forumservice.post.dao;

import java53.forumservice.post.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

public interface CommentRepository extends MongoRepository<Comment, String> {

}
