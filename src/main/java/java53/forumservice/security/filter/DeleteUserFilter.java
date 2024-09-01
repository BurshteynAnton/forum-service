package java53.forumservice.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java53.forumservice.accounting.dao.UserAccountRepository;
import java53.forumservice.accounting.model.Role;
import java53.forumservice.accounting.model.User;
import java53.forumservice.security.model.UserAcc;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(40)
public class DeleteUserFilter implements Filter {

    final UserAccountRepository userAccountRepository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndpoint(request.getMethod(), request.getServletPath())) {
            String principal = request.getUserPrincipal().getName();
            User userAcc = userAccountRepository.findById(principal).get();
            String[] arr = request.getServletPath().split("/");
            String owner = arr[arr.length - 1];
            if (!(userAcc.getRoles().contains(Role.ADMINISTRATOR) || principal.equalsIgnoreCase(owner))) {
                response.sendError(403, "Permission denied");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return HttpMethod.DELETE.matches(method) && path.matches("/account/user/\\w+");
    }

}
