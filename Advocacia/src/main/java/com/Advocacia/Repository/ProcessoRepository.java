package com.Advocacia.Repository;

import com.Advocacia.Entity.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo,Long> {



}
