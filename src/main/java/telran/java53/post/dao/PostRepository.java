package telran.java53.post.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import telran.java53.post.model.Post;

public interface PostRepository extends JpaRepository<Post, String> {
	Stream<Post> findByAuthorIgnoreCase(String author);


	Stream<Post> findByDateCreatedBetween(LocalDate from, LocalDate to);

	@Query("SELECT p FROM Post p JOIN p.tags t WHERE LOWER(t.name) IN :tagNames")
	Stream<Post> findByTagsNameInIgnoreCase(List<String> tagNames);
}
