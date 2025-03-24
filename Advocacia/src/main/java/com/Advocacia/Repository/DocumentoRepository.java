package com.Advocacia.Repository;

import com.Advocacia.Entity.Documento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento,Long> {

	@Query("SELECT d.statusDocumento " +
            "FROM Documento d " +
            "GROUP BY d.statusDocumento " +
            "ORDER BY COUNT(d) DESC")
    List<String> findStatusDocumento();
}
