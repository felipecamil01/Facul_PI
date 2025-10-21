package com.Advocacia.Repository;

import com.Advocacia.Entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda,Long> {
  List<Agenda> findByParcelaId(Long parcelaId);
  void deleteByParcelaId(Long parcelaId);
}
