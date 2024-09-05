package com.Advocacia.Repository;

import com.Advocacia.Entity.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceiroRepository extends JpaRepository<Financeiro,Long> {
}
