package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(
        //Not blank serve para validar que o dado nao pode ser nulo nem vazio e so pode ser usado para strings
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email,
        // Caso coloque este parâmetro de mensagem em validações do Bean, quando ocorrer uma exception referente a ela
        // está mensagem que será exibida no body da requisição
        @NotBlank(message = "Telefone é obrigatório")
        String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}") //esta anotação serve para validar usando expressão regular que o dado seja um digito de tamanho de 4 a 6 casa
        String crm,
        @NotNull
        Especialidade especialidade,
        @NotNull
        @Valid
        DadosEndereco endereco) {
}
