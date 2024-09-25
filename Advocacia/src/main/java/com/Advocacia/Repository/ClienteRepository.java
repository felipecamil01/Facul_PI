package com.Advocacia.Repository;

import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.StatusCliente;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long> {
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    List<Cliente> findAllByStatusCliente(StatusCliente status);
}


