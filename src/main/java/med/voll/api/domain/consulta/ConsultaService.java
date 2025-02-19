package med.voll.api.domain.consulta;

import jakarta.validation.Valid;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public void agendar(DadosAgendamentoConsulta dados){
        if(!pacienteRepository.existsById(dados.idPaciente())){
            throw  new ValidacaoException("Id do paciente informado não encontrado");
        }
        if(dados.idMedico() != null && !medicoRepository.existsById((dados.idMedico()))){
            throw  new ValidacaoException("Id do médico informado não encontrado");
        }

        var medico = escolherMedico(dados);

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());

        var consulta = new Consulta(null, medico, paciente, dados.data(), null, true);

        consultaRepository.save(consulta);
    }

    public void cancelar(DadosCancelamentoConsulta dados){
        if(!consultaRepository.existsById(dados.idConsulta())){
            throw new ValidacaoException("Id da consulta informada não encontrada");
        }

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());

        consulta.cancelar(dados);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if(dados.idMedico() != null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }
        if(dados.especialidade() == null){
            throw  new ValidacaoException("Espciealidade é obrigatória quando médico não for escolhido");
        }

        return  medicoRepository.escolherMedicoAletorioLivre(dados.especialidade(), dados.data());
    }
}
