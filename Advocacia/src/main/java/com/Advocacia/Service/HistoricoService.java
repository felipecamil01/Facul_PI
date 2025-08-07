package com.Advocacia.Service;

/*
 * import com.Advocacia.Auditoria.AuditoriaEntity;
 * import com.Advocacia.DTO.HIstoricoGeralDTO;
 * import com.Advocacia.DTO.HistoricoDTO;
 * import com.Advocacia.Entity.*;
 * import jakarta.persistence.EntityManager;
 * import org.hibernate.envers.AuditReader;
 * import org.hibernate.envers.AuditReaderFactory;
 * import org.hibernate.envers.query.AuditEntity;
 * import org.springframework.stereotype.Service;
 * 
 * import java.util.ArrayList;
 * import java.util.Date;
 * import java.util.List;
 * 
 * @Service
 * public class HistoricoService {
 * private final EntityManager entityManager;
 * 
 * public HistoricoService(EntityManager entityManager) {
 * this.entityManager = entityManager;
 * }
 * 
 * public <T> List<HistoricoDTO> buscarHistorico(Class<T> tipo, Long id) {
 * var reader = AuditReaderFactory.get(entityManager);
 * var revisoes = reader.getRevisions(tipo, id);
 * var lista = new ArrayList<HistoricoDTO>();
 * 
 * for (Number rev : revisoes) {
 * T entidade = reader.find(tipo, id, rev);
 * var revisionEntity = reader.findRevision(AuditoriaEntity.class, rev);
 * 
 * lista.add(new HistoricoDTO(
 * rev.intValue(),
 * revisionEntity.getUsuario(),
 * new Date(revisionEntity.getTimestamp()).toInstant(),
 * entidade
 * ));
 * }
 * 
 * return lista;
 * }
 * public List<HIstoricoGeralDTO> buscarTudo() {
 * AuditReader reader = AuditReaderFactory.get(entityManager);
 * List<HIstoricoGeralDTO> historico = new ArrayList<>();
 * 
 * var tipos = List.of(Cliente.class, Despesa.class, Processo.class,
 * Endereco.class, Documento.class, Contato.class);
 * 
 * for (Class<?> tipo : tipos) {
 * List<Number> revisoes = reader.createQuery()
 * .forRevisionsOfEntity(tipo, false, true)
 * .getResultList()
 * .stream()
 * .map(rev -> ((Object[]) rev)[1])
 * .map(obj -> ((AuditoriaEntity) obj).getId())
 * .distinct()
 * .toList();
 * 
 * for (Number revId : revisoes) {
 * List<?> revisoesEntidade = reader.createQuery()
 * .forRevisionsOfEntity(tipo, false, true)
 * .add(AuditEntity.revisionNumber().eq(revId))
 * .getResultList();
 * 
 * for (Object obj : revisoesEntidade) {
 * Object[] arr = (Object[]) obj;
 * Object entidade = arr[0];
 * AuditoriaEntity revisionEntity = (AuditoriaEntity) arr[1];
 * Long entidadeId = (Long) entityManager.getEntityManagerFactory()
 * .getPersistenceUnitUtil().getIdentifier(entidade);
 * 
 * historico.add(new HIstoricoGeralDTO(
 * tipo.getSimpleName(),
 * entidadeId,
 * revId.intValue(),
 * revisionEntity.getUsuario(),
 * new Date(revisionEntity.getTimestamp()).toInstant(),
 * entidade
 * ));
 * }
 * }
 * }
 * 
 * return historico;
 * }
 * }
 */
