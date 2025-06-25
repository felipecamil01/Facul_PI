package com.Advocacia.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Status;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long> {
  
	@Query("SELECT c FROM Cliente c WHERE c.nome LIKE %:nome% AND c.status = 'ATIVO'")
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

	@Query("SELECT c FROM Cliente c WHERE c.status = :status")
    List<Cliente> findAllAtivos(Status status);

}
