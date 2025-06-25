package com.Advocacia.Repository;

import com.Advocacia.Entity.Agenda;
import com.Advocacia.Entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda,Long> {
    List<Agenda> findByClienteId(Long clienteId);
    
    @Query("SELECT a FROM Agenda a WHERE a.status = :status")
    List<Agenda> findAllAtivos(Status status);
    
}
