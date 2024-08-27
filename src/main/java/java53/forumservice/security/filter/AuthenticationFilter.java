package java53.forumservice.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java53.forumservice.accounting.dao.UserAccountRepository;
import java53.forumservice.accounting.model.User;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

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
                request = new WrappedRequest(request, user.getLogin());
            } catch (Exception e) {
                response.sendError(401);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return !("POST".equalsIgnoreCase(method) && path.matches("/account/register"));
    }

    private String[] getCredentials(String header) {
        String token = header.split(" ")[1];
        String decode = new String(Base64.getDecoder().decode(token));
        return decode.split(":");
    }

    private class WrappedRequest extends HttpServletRequestWrapper {
        private String login;

        public WrappedRequest(HttpServletRequest request, String login) {
            super(request);
            this.login = login;
        }

        @Override
        public Principal getUserPrincipal() {
            return () -> login;
        }
    }
}