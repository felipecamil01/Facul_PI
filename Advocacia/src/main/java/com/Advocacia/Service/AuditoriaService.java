package com.Advocacia.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.*;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditoriaService {

  @PersistenceContext
  private EntityManager entityManager;

  public List<Object> buscarRevisoes(
    Class<?> entidade,
    Object idFiltro,
    LocalDateTime de,
    LocalDateTime ate
  ) {
    AuditReader reader = AuditReaderFactory.get(entityManager);
    List<Object> revisoes = new ArrayList<>();

    List<?> resultados = reader.createQuery() // Usar List<?> é uma boa prática aqui
      .forRevisionsOfEntity(entidade, false, true)
      .addOrder(AuditEntity.revisionNumber().desc())
      .add(idFiltro != null ? AuditEntity.id().eq(idFiltro) : null)
      .getResultList();


    for (Object item : resultados) {

      Object[] linha = (Object[]) item;


      Object entidadeAuditada = linha[0];
      DefaultRevisionEntity revisao = (DefaultRevisionEntity) linha[1];

      LocalDateTime dataRevisao = LocalDateTime.ofInstant(
        revisao.getRevisionDate().toInstant(),
        java.time.ZoneId.systemDefault()
      );

      if ((de == null || !dataRevisao.isBefore(de)) && (ate == null || !dataRevisao.isAfter(ate))) {
        revisoes.add(entidadeAuditada);
      }
    }

    return revisoes;
  }
}
