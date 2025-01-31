package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Usamos o component quando queremos que o spring carregue uma classe mas não tem uma definição certa para ela
//desta forma o spring carregue esta classe como um component genérico
@Component
public class SecurityFilter extends OncePerRequestFilter {

    //Pelo component se tratar também de uma classe do Spring ele peermite também que eu injete dependencias aqui
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

//        System.out.println(tokenJWT);

        if( tokenJWT != null){
            var subject = tokenService.getSubject(tokenJWT);

            var user = repository.findByLogin(subject);

            //Este authentication mais o securityContextHolder serve para forçar uma autenticação do spring, de forma que libere
            // este usuário que foi validado manualmente pelo token verificando o subject e o Issuer para realizar esta requisição
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null){
            return authorizationHeader.split(" ")[1];
        }

        return  null;
    }
}
