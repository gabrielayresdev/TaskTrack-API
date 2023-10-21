package br.com.todoist.todoist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.todoist.todoist.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class FilterTaskAuth extends OncePerRequestFilter{


    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        
        var servletPath = request.getServletPath();

        if(servletPath.startsWith("/tasks/")) {
            // Pegar email e senha
            var authorization = request.getHeader("Authorization"); // Retorna uma string encriptada no formato Basic [código]
            var authEncoded = authorization.substring("Basic".length()).trim(); // Remove o Basic da string e retira os espaços, sobrando apenas o código
            
            byte[] authDecode = Base64.getDecoder().decode(authEncoded); // Decodifica da sequencia escrita em base 64

            var authString = new String(authDecode); // Transforma os bytes decodificados em uma string no modelo [email]:[senha]

            String[] credentials = authString.split(":"); // Separa email e senha em um array
            String email = credentials[0];
            String password = credentials[1];
            System.out.println(email);

            // Verificar se o usuário existe
            var user = this.userRepository.findByEmail(email);
            /* if(user == null) {
                response.sendError(401, "Non-Authorized User");
            } else {
                // Validar senha 
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if(passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401, "Non-Authorized User");
                }
            } */
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }

    }

}
