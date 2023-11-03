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
        var token = this.recoverToken(request);
        var servletPath = request.getServletPath();


        if(servletPath.startsWith("/task")) {
            if(token != null) {
                var username = tokenService.validateToken(token);
                System.out.println(username);
                UserDetails user = repository.findByUsername(username);
                System.out.println(user);

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                System.out.println(authentication);


                // Sets in request the user who made the request
                UserModel userAttributes = (UserModel) user;
                request.setAttribute("userId", userAttributes.getId());

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

            filterChain.doFilter(request, response);

        } else {
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
