package java53.forumservice.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Setter
    String user;
    @Setter
    String message;
    LocalDateTime dateCreated = LocalDateTime.now();
    int likes;

    public Comment(String user,String message) {
        this.user = user;
        this.message = message;
    }

    public void addLike() {
        likes++;
    }

}
