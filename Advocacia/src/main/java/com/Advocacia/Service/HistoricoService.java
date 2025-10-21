package com.Advocacia.Service;

import com.Advocacia.DTO.HIstoricoGeralDTO;
import com.Advocacia.DTO.HistoricoDTO;
import com.Advocacia.Entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import com.Advocacia.Auditoria.RevisionInfo;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HistoricoService {

  @PersistenceContext
  private EntityManager entityManager;

  public <T> List<HistoricoDTO> buscarHistorico(Class<T> tipo, Long id) {
    AuditReader reader = AuditReaderFactory.get(entityManager);
    List<Number> revisoes = reader.getRevisions(tipo, id);
    List<HistoricoDTO> lista = new ArrayList<>();

    for (Number rev : revisoes) {
      T entidade = reader.find(tipo, id, rev);
      RevisionInfo revisao = reader.findRevision(RevisionInfo.class, rev);
      Instant data = new Date(revisao.getRevisionDate().getTime()).toInstant();
      String usuario = revisao.getUsuario() != null ? revisao.getUsuario() : "desconhecido";
      lista.add(new HistoricoDTO(rev.intValue(), usuario, data, entidade));
    }

    return lista;
  }

  public List<HIstoricoGeralDTO> buscarTudo() {
    AuditReader reader = AuditReaderFactory.get(entityManager);
    List<HIstoricoGeralDTO> historico = new ArrayList<>();

    List<Class<?>> tipos = List.of(Cliente.class, Processo.class, Documento.class, Agenda.class);

    for (Class<?> tipo : tipos) {
      List<?> resultados = reader.createQuery()
        .forRevisionsOfEntity(tipo, false, true)
        .addOrder(AuditEntity.revisionNumber().desc())
        .getResultList();

      for (Object obj : resultados) {
        Object[] arr = (Object[]) obj;
        Object entidade = arr[0];
        RevisionInfo revisao = (RevisionInfo) arr[1];
        RevisionType acao = (RevisionType) arr[2];

        Long entidadeId = (Long) entityManager.getEntityManagerFactory()
          .getPersistenceUnitUtil().getIdentifier(entidade);

        Instant data = new Date(revisao.getRevisionDate().getTime()).toInstant();
        String usuario = revisao.getUsuario() != null ? revisao.getUsuario() : "desconhecido";

        historico.add(new HIstoricoGeralDTO(
          tipo.getSimpleName(),
          entidadeId,
          revisao.getId(),
          usuario,
          data,
          entidade
        ));
      }
    }

    return historico;
  }
}
