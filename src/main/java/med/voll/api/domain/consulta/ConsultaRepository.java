package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByMedico_IdAndData(Long id, LocalDateTime data);

    boolean existsByPaciente_IdAndDataBetween(@NotNull Long id, LocalDateTime primeiroHorario, LocalDateTime ultimoHorario);
}
