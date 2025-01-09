package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional //Por ser um método de escrita(mas caso fosse update ou delete eu teria que adicionar o transactional
    // tenho que ter a anotação de transactional para o banco de dados rodar
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
        repository.save(new Medico(dados));
    }

    @GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        //Pageable é uma classe propria do springboot para trabalhar com paginação dos dados da sua API, e quando passamos
        // esta anotação serve para modificar parãmetros padrão da classe pageable
        return repository.findAll(paginacao).map(DadosListagemMedico :: new);
    }

    @PutMapping
    @Transactional
    // Este método atualiza as informações do médico com base nos dados recebidos via requisição PUT.
// A JPA gerencia automaticamente a atualização no banco de dados ao final da transação, sem necessidade de comandos adicionais.
    // o getReferenceByID como se fosse um ponteiro, então quando atualizamos o objeto a JPA faz o trabalho de atualizar o objeto diretamente no banco.
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
    }
}
