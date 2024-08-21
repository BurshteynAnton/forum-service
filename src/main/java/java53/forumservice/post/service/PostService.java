package java53.forumservice.post.service;

import java53.forumservice.post.dto.DatePeriodDto;
import java53.forumservice.post.dto.NewCommentDto;
import java53.forumservice.post.dto.NewPostDto;
import java53.forumservice.post.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto addNewPost(String author, NewPostDto newPostDto);

    PostDto findPostById(String id);

    PostDto removePost(String id);

    PostDto updatePost(String id, NewPostDto newPostDto);

    PostDto addComment(String id, String author, NewCommentDto newCommentDto);

    void addLike(String id);

    Iterable<PostDto> findPostsByAuthor(String author);

    Iterable<PostDto> findPostsByTags(List<String> tags);

    Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto);

}
