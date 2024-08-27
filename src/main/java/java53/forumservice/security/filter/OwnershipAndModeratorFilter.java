package java53.forumservice.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java53.forumservice.accounting.dao.UserAccountRepository;
import java53.forumservice.accounting.model.User;
import java53.forumservice.post.dao.CommentRepository;
import java53.forumservice.post.dao.PostRepository;
import java53.forumservice.post.model.Comment;
import java53.forumservice.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;


@Component
@RequiredArgsConstructor
@Order(20)
public class OwnershipAndModeratorFilter implements Filter {

    final UserAccountRepository userRepository;
    final PostRepository postRepository;
    final CommentRepository commentRepository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if (checkEndpoint(request)) {
            try {
                User user = getAuthenticatedUser(request);

                if (isPostRequest(request)) {
                    Post post = getPost(request);
                    verifyAccess(user, post.getOwner(), user.getRoles().contains("MODERATOR"));
                } else if (isCommentRequest(request)) {
                    Comment comment = getComment(request);
                    verifyAccess(user, comment.getAuthor(), false);
                }

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private User getAuthenticatedUser(HttpServletRequest request) {
        Principal userPrincipal = request.getUserPrincipal();
        if (userPrincipal == null) {
            throw new RuntimeException("User not authenticated");
        }

        return userRepository.findByLogin(userPrincipal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Post getPost(HttpServletRequest request) {
        String postId = extractId(request.getServletPath(), 3);
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    private Comment getComment(HttpServletRequest request) {
        String commentId = extractId(request.getServletPath(), 5);
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    private void verifyAccess(User user, String resourceOwner, boolean isModerator) {
        if (!user.getLogin().equals(resourceOwner) && !isModerator) {
            throw new RuntimeException("User not authorized");
        }
    }

    private boolean checkEndpoint(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getServletPath();
        return (("PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))
                && (path.startsWith("/forum/post/") || path.startsWith("/forum/comment/")));
    }

    private boolean isPostRequest(HttpServletRequest request) {
        return request.getServletPath().startsWith("/forum/post/") && !request.getServletPath().contains("/comment/");
    }

    private boolean isCommentRequest(HttpServletRequest request) {
        return request.getServletPath().contains("/comment/");
    }

    private String extractId(String path, int index) {
        return path.split("/")[index];
    }
}
