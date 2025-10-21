package com.Advocacia.Controller;

import com.Advocacia.Entity.Parcela;
import com.Advocacia.Enum.StatusPagamento;
import com.Advocacia.Repository.ParcelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/parcela")
public class ParcelaController {

  @Autowired
  private ParcelaRepository parcelaRepository;

  @PutMapping("/pagar/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Parcela> marcarComoPago(@PathVariable Long id) {
    Parcela parcela = parcelaRepository.findById(id).orElseThrow();
    parcela.setStatusPagamento(StatusPagamento.PAGO);
    Parcela salva = parcelaRepository.save(parcela);
    return ResponseEntity.status(HttpStatus.OK).body(salva);
  }
}

