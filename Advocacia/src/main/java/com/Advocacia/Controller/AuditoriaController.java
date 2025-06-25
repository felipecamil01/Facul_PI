package com.Advocacia.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Advocacia.DTO.ClienteAudDTO;
import com.Advocacia.DTO.AgendaAudDTO;
import com.Advocacia.DTO.DespesaAudDTO;
import com.Advocacia.DTO.ProcessoAudDTO;
import com.Advocacia.Service.AuditoriaService;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping("/cliente")
    public List<ClienteAudDTO> audClientes() {
        return auditoriaService.getAuditoriaClientes();
    }

    @GetMapping("/agenda")
    public List<AgendaAudDTO> audContatos() {
        return auditoriaService.getAuditoriaContatos();
    }

    @GetMapping("/despesa")
    public List<DespesaAudDTO> audDespesas() {
        return auditoriaService.getAuditoriaDespesas();
    }

    @GetMapping("/processo")
    public List<ProcessoAudDTO> audProcessos() {
        return auditoriaService.getAuditoriaProcessos();
    }
} 