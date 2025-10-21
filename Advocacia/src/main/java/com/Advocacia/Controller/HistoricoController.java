package com.Advocacia.Controller;

import com.Advocacia.DTO.HIstoricoGeralDTO;
import com.Advocacia.DTO.HistoricoDTO;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Processo;
import com.Advocacia.Service.HistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/historico")
public class HistoricoController {

  @Autowired
  private HistoricoService historicoService;

  @GetMapping
  public ResponseEntity<List<HIstoricoGeralDTO>> historicoGeral() {
    return ResponseEntity.ok(historicoService.buscarTudo());
  }

  @GetMapping("/cliente/{id}")
  public ResponseEntity<List<HistoricoDTO>> historicoCliente(@PathVariable Long id) {
    return ResponseEntity.ok(historicoService.buscarHistorico(Cliente.class, id));
  }

  @GetMapping("/processo/{id}")
  public ResponseEntity<List<HistoricoDTO>> historicoProcesso(@PathVariable Long id) {
    return ResponseEntity.ok(historicoService.buscarHistorico(Processo.class, id));
  }
}

