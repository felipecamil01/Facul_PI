package com.Advocacia.Repository;

import com.Advocacia.Entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda,Long> {
  List<Agenda> findByParcelaId(Long parcelaId);
  void deleteByParcelaId(Long parcelaId);
  List<Agenda> findByProcessoId(Long processoId);

  @Query("""
      SELECT ag
      FROM Agenda ag
      LEFT JOIN FETCH ag.processo proc
      LEFT JOIN FETCH proc.cliente cliProc
      LEFT JOIN FETCH ag.parcela parc
      LEFT JOIN FETCH parc.pagamento pag
      LEFT JOIN FETCH pag.cliente cliPag
      LEFT JOIN FETCH ag.cliente cliDireto
      WHERE (:clienteId IS NULL)
         OR (cliProc.id = :clienteId)
         OR (cliPag.id = :clienteId)
         OR (cliDireto.id = :clienteId)
      """)
  List<Agenda> findByClienteVinculado(@Param("clienteId") Long clienteId);
}

