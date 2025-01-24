package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional //Por ser um método de escrita(mas caso fosse update ou delete eu teria que adicionar o transactional
    // tenho que ter a anotação de transactional para o banco de dados rodar
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        var medico = new Medico(dados);
        repository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        //Pageable é uma classe propria do springboot para trabalhar com paginação dos dados da sua API, e quando passamos
        // esta anotação serve para modificar parãmetros padrão da classe pageable
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico :: new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    // Este método atualiza as informações do médico com base nos dados recebidos via requisição PUT.
// A JPA gerencia automaticamente a atualização no banco de dados ao final da transação, sem necessidade de comandos adicionais.
    // o getReferenceByID como se fosse um ponteiro, então quando atualizamos o objeto a JPA faz o trabalho de atualizar o objeto diretamente no banco.
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return  ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        medico.excluir();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }
}
