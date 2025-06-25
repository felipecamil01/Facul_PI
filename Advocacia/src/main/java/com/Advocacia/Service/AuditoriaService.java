package com.Advocacia.Service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Advocacia.Aud.CustomRevisionEntity;
import com.Advocacia.DTO.ClienteAudDTO;
import com.Advocacia.DTO.AgendaAudDTO;
import com.Advocacia.DTO.DespesaAudDTO;
import com.Advocacia.DTO.ProcessoAudDTO;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Agenda;
import com.Advocacia.Entity.Despesa;
import com.Advocacia.Entity.Processo;

import jakarta.persistence.EntityManager;

@Service
public class AuditoriaService {

    @Autowired
    private EntityManager entityManager;

    public List<ClienteAudDTO> getAuditoriaClientes() {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        List<Object> revisoes = reader.createQuery().forRevisionsOfEntity(Cliente.class, false, true).getResultList();

        List<ClienteAudDTO> resultados = new ArrayList<>();
        for (Object obj : revisoes) {
            Object[] row = (Object[]) obj;
            Cliente entity = (Cliente) row[0];
            CustomRevisionEntity rev = (CustomRevisionEntity) row[1];
            RevisionType tipo = (RevisionType) row[2];

            resultados.add(new ClienteAudDTO(
                entity.getId(), entity.getNome(), entity.getEmail(), entity.getCpf(), entity.getRg(),
                entity.getProfissao(), entity.getTelefone(), entity.getDataNascimento(),
                entity.getEstadoCivil().name(), entity.getStatus().name(),
                entity.getEndereco(), rev.getUsuario(), rev.getTimestamp(), tipo.name()
            ));
        }
        return resultados;
    }

    public List<AgendaAudDTO> getAuditoriaContatos() {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        List<Object> revisoes = reader.createQuery().forRevisionsOfEntity(Agenda.class, false, true).getResultList();

        List<AgendaAudDTO> resultados = new ArrayList<>();
        for (Object obj : revisoes) {
            Object[] row = (Object[]) obj;
            Agenda entity = (Agenda) row[0];
            CustomRevisionEntity rev = (CustomRevisionEntity) row[1];
            RevisionType tipo = (RevisionType) row[2];

            resultados.add(new AgendaAudDTO(
            	    entity.getId(),
            	    entity.getDataUltimoContato(),
            	    entity.getMeioContato(),
            	    entity.getNotasContato(),
            	    entity.getProximoPassos(),
            	    entity.getStatus().name(),
            	    entity.getCliente() != null ? entity.getCliente().getId() : 0,
            	    entity.getCliente() != null ? entity.getCliente().getNome() : null,
            	    rev.getUsuario(),
            	    rev.getTimestamp(),
            	    tipo.name()
            	));
        }
        return resultados;
    }

    public List<DespesaAudDTO> getAuditoriaDespesas() {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        List<Object> revisoes = reader.createQuery().forRevisionsOfEntity(Despesa.class, false, true).getResultList();

        List<DespesaAudDTO> resultados = new ArrayList<>();
        for (Object obj : revisoes) {
            Object[] row = (Object[]) obj;
            Despesa entity = (Despesa) row[0];
            CustomRevisionEntity rev = (CustomRevisionEntity) row[1];
            RevisionType tipo = (RevisionType) row[2];

            resultados.add(new DespesaAudDTO(
            	    entity.getId(),
            	    entity.getHonorario(),
            	    entity.getDespesasAdicionais(),
            	    entity.getDataVencimento(),
            	    entity.getCategoriaDespesa(),
            	    entity.getFormaPagamento(),
            	    entity.getStatusPagamento() != null ? entity.getStatusPagamento().name() : null,
            	    entity.getObservacao(),
            	    entity.getStatus().name(),
            	    entity.getCliente() != null ? entity.getCliente().getId() : 0,
            	    entity.getCliente() != null ? entity.getCliente().getNome() : null,
            	    rev.getUsuario(),
            	    rev.getTimestamp(),
            	    tipo.name()
            	));
        }
        return resultados;
    }

    public List<ProcessoAudDTO> getAuditoriaProcessos() {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        List<Object> revisoes = reader.createQuery().forRevisionsOfEntity(Processo.class, false, true).getResultList();

        List<ProcessoAudDTO> resultados = new ArrayList<>();
        for (Object obj : revisoes) {
            Object[] row = (Object[]) obj;
            Processo entity = (Processo) row[0];
            CustomRevisionEntity rev = (CustomRevisionEntity) row[1];
            RevisionType tipo = (RevisionType) row[2];

            resultados.add(new ProcessoAudDTO(
            	    entity.getId(),
            	    entity.getTipoCliente(),
            	    entity.getAreaAtuacao(),
            	    entity.getNumeroProcesso(),
            	    entity.getComarca(),
            	    entity.getDataInicio(),
            	    entity.getDescricao(),
            	    entity.getAndamento(),
            	    entity.getSituacaoAtual(),
            	    entity.getStatus().name(),
            	    entity.getPrazosImportantes(),
            	    entity.getCliente() != null ? entity.getCliente().getId() : 0,
            	    entity.getCliente() != null ? entity.getCliente().getNome() : null,
            	    rev.getUsuario(),
            	    rev.getTimestamp(),
            	    tipo.name()
            	));

        }
        return resultados;
    }
}
