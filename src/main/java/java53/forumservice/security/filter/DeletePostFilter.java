package java53.forumservice.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java53.forumservice.accounting.dao.UserAccountRepository;
import java53.forumservice.post.dao.PostRepository;
import java53.forumservice.post.model.Post;
import java53.forumservice.security.model.UserAcc;
import java53.forumservice.accounting.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(60)
public class DeletePostFilter implements Filter {

    final PostRepository  postRepository;
    final UserAccountRepository userAccountRepository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndpoint(request.getMethod(), request.getServletPath())) {
            UserAcc userAcc = (UserAcc) request.getUserPrincipal();
            String[] parts = request.getServletPath().split("/");
            String postId = parts[parts.length - 1];
            Post post = postRepository.findById(postId).orElse(null);
            if (post == null) {
                response.sendError(404, "Not found");
                return;
            }
            if (!(userAcc.getName().equals(post.getAuthor())
                    || userAcc.getRoles().contains(Role.MODERATOR.name()))) {
                response.sendError(403, "You are not allowed to access this resource");
                return;
            }

        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return (HttpMethod.DELETE.matches(method) && path.matches("/forum/post/\\w+"));
    }


}