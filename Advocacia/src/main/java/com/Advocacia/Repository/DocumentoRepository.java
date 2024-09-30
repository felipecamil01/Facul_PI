package com.Advocacia.Repository;

import com.Advocacia.Entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento,Long> {
}
