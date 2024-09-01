package java53.forumservice.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java53.forumservice.accounting.dao.UserAccountRepository;
import java53.forumservice.accounting.model.User;
import java53.forumservice.accounting.model.Role;
import java53.forumservice.security.model.UserAcc;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Order(10)
public class AuthenticationFilter implements Filter {

    final UserAccountRepository repository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndpoint(request.getMethod(), request.getServletPath())) {
            try {
                String[] credentials = getCredentials(request.getHeader("Authorization"));
                User user = repository.findById(credentials[0]).orElseThrow(RuntimeException::new);
                if (!BCrypt.checkpw(credentials[1], user.getPassword())) {
                    throw new RuntimeException();
                }
                Set<String> roles = user.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.toSet());
                request = new WrappedRequest(request, user.getLogin(), roles);
            } catch (Exception e) {
                response.sendError(401);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return !(
                (HttpMethod.POST.matches(method) && path.matches("/account/register"))
                        || HttpMethod.GET.matches(method) && path.matches("/forum/posts.+")
        );
    }

    private String[] getCredentials(String header) {
        String token = header.split(" ")[1];
        String decode = new String(Base64.getDecoder().decode(token));
        return decode.split(":");
    }

    private class WrappedRequest extends HttpServletRequestWrapper {
        private String login;
        private Set<String> roles;

        public WrappedRequest(HttpServletRequest request, String login, Set<String> roles) {
            super(request);
            this.login = login;
            this.roles = roles;
        }

        @Override
        public Principal getUserPrincipal() {
            return new UserAcc(login, roles);
        }
    }
}