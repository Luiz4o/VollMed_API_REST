package med.voll.api.domain.consulta.validations;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public class ValidadorMedicoComConsultaMarcadaMesmaHora {

    private ConsultaRepository repository;

    public void validar(DadosAgendamentoConsulta dados){
        var medicoPossuiOutraConsultaNestaHora = repository.existsByMedico_IdAndData(dados.idMedico(),dados.data());
        if(medicoPossuiOutraConsultaNestaHora) {
            throw new ValidacaoException("Médico já possui outra consulta neste horário marcado");
        }
    }
}
