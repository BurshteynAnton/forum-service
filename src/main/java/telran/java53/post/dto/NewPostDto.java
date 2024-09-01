package telran.java53.post.dto;

import lombok.Getter;

import java.util.Set;

@Getter
public class NewPostDto {
    String title;
    String content;
    Set<String> tags;
}
