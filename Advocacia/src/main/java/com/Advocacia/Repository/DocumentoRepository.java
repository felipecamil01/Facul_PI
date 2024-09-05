package com.Advocacia.Repository;

import com.Advocacia.Entity.Documentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends JpaRepository<Documentos,Long> {
}
