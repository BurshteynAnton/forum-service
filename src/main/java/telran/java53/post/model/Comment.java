package telran.java53.post.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Post post;

	@Column(name = "username")
	@Setter
	private String user;

	@Setter
	private String message;

	private LocalDateTime dateCreated = LocalDateTime.now();

	private int likes;

	public Comment(String user, String message) {
		this.user = user;
		this.message = message;
	}

	public void addLike() {
		likes++;
	}

	public void setPost(Post post) {
		this.post = post;
	}
}
