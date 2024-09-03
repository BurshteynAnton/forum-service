package telran.java53.post.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	private String title;

	@Setter
	private String content;

	@Setter
	private String
			author;

	private LocalDateTime
			dateCreated = LocalDateTime.now();

	@ManyToMany
	private Set<Tag> tags = new HashSet<>();

	private int likes;

	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();

	public Post(String title, String content, String author, Set<Tag> tags) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.tags = tags;

	}

	public void addLike() {
		likes++;
	}

	public boolean addTag(Tag tag) {
		return tags.add(tag);
	}

	public boolean removeTag(Tag tag) {
		return tags.remove(tag);
	}

	public void addComment(Comment comment) {
		comment.setPost(this);
		comments.add(comment);
	}

	public boolean removeComment(Comment comment) {
		return comments.remove(comment);
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
}