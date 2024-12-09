package com.Advocacia.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.Advocacia.Entity.Processo;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo,Long> {
	
	@Query("SELECT p FROM Processo p WHERE p.numeroProcesso = :numeroProcesso")
    List<Processo> findByNumeroProcesso(String numeroProcesso);

}