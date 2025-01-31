package med.voll.api.domain.medico;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

//A JpaReporsitory pede que pass generics sendo dois objetos o primeiro é qual é o tipo da entidade e o segundo é o
// tipo do atributo da chave primaria da entidade então logo temos o Medico e o tipo da chave primaria que é o ID e long
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    //Está consulta realiza uma busca na tabela medico onde estejam ativos, a especialidade seja igual ao parâmetro passado onde
    // :especialidade significa que ele puxará a especialidade do parâmetro, junto de uma subquery, que busca uma lista de medicos que possuem
    // uma consulta marcada para aquela data, e a gente pega exatamente os medicos que nao estão nesta data com o not in usando um order
    // by rand() para sempre pegar um medico que foi organizado de forma aleatória e pegando o primeiro registro
    @Query("""
            SELECT m FROM Medico m
            WHERE 
            m.ativo = true and
            m.especialidade = :especialidade and
            m.id not in(
                select c.medico.id from Consulta c
                where 
                c.data = :data
            )
            order by rand()
            limit 1
            """)
    Medico escolherMedicoAletorioLivre(Especialidade especialidade, @NotNull @Future LocalDateTime data);

    @Query("""
            SELECT m.ativo FROM Medico m
            WHeRE
            m.id = :id
            """)
    Boolean findAtivoById(Long id);
}
