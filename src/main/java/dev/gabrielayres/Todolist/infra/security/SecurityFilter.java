package dev.gabrielayres.Todolist.infra.security;


import dev.gabrielayres.Todolist.users.UserModel;
import dev.gabrielayres.Todolist.users.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var servletPath = request.getServletPath();

        if (!servletPath.startsWith("/auth/register") && !servletPath.startsWith("/auth/login") && !servletPath.startsWith("/h2-console")) {
            System.out.println("==================>");

            var token = this.recoverToken(request);


            if(token != null) {
                var username = tokenService.validateToken(token);
                System.out.println(username);
                UserDetails user = repository.findByUsername(username);
                System.out.println(user);

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                System.out.println(authentication);


                //UserModel userAttributes = (UserModel) user;
                //request.setAttribute("userId", userAttributes.getId());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

            filterChain.doFilter(request, response);
        } else {
            System.out.println("!=================>");
            filterChain.doFilter(request, response);
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
            return null;

        }
        return authorizationHeader.replace("Basic ", "").trim();
    }
}
