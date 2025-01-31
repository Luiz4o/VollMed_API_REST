package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.user.DadosAutenticacao;
import med.voll.api.domain.user.User;
import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        //Convertendo os dados recebidos para o tipo/DTO esperado para a validação do manager
        var authToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());

        System.out.println(authToken);

        // o retorno de authentication é quem representa o usuario autenticado no sistema
        var authentication = manager.authenticate(authToken);

        var tokenJWT = tokenService.gerarToken((User) authentication.getPrincipal());


        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
