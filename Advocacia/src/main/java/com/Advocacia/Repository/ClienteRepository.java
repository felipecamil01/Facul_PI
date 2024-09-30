package com.Advocacia.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.StatusCliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long> {
	
	@Query("SELECT c FROM Cliente c WHERE c.nome = :nome")
    List<Cliente> findByNome(String nome);
    
	@Query("SELECT c FROM Cliente c WHERE c.statusCliente = :status")
    List<Cliente> findAllAtivos(StatusCliente status);
    
}