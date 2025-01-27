package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.user.DadosAutenticacao;
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

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        //Convertendo os dados recebidos para o tipo esperado para a validação do manager
        var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());

        System.out.println(token);

        // o retorno de authentication é quem representa o usuario autenticado no sistema
        var authentication = manager.authenticate(token);

        return ResponseEntity.ok().build();
    }
}
