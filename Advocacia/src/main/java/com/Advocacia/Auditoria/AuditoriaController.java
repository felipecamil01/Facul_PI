package com.Advocacia.Auditoria;

import com.Advocacia.Service.AuditoriaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {

  private final AuditoriaService auditoriaService;

  public AuditoriaController(AuditoriaService auditoriaService) {
    this.auditoriaService = auditoriaService;
  }

  @GetMapping
  public List<Object> buscarAuditoria(
    @RequestParam String entidade,
    @RequestParam(required = false) Long id,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime de,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ate
  ) throws ClassNotFoundException {
    // Altere para o seu pacote real
    Class<?> clazz = Class.forName("com.seuprojeto.model." + entidade);
    return auditoriaService.buscarRevisoes(clazz, id, de, ate);
  }
}
