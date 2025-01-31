package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.voll.api.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    private static final String ISSUER = "API Voll.med";

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(User user) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    // Identifica quem foi responsável pelga geração do token
                    .withIssuer(ISSUER)
                    // Identica quem é a pessoa relacionado a este usuario
                    .withSubject(user.getUsername())
                    // Este withClaim é uma maneira de retornar alguma informação adicional ao token, como o id do usuario
                    // ou perfil, seja o que desejar, funciona como chave valor
                    //.withClaim("id", user.gerId())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar nosso JWT", exception);
        }

    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    //Este verify vai validar se o algoritimo fornecido bate com o passado no verify, e validar o Issuer
                    // se ambos são iguais se for Ok continua e retorna o subject que é o usuário se não retorna erro
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
