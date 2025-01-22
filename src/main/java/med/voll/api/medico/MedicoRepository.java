package med.voll.api.medico;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//A JpaReporsitory pede que pass generics sendo dois objetos o primeiro é qual é o tipo da entidade e o segundo é o
// tipo do atributo da chave primaria da entidade então logo temos o Medico e o tipo da chave primaria que é o ID e long
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Page<Medico> findAllByAtivoTrue(Pageable paginacao);
}
