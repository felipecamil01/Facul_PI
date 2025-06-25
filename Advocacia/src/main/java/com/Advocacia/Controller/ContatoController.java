package com.Advocacia.Controller;

import com.Advocacia.DTO.AgendaDTO;
import com.Advocacia.Entity.Agenda;
import com.Advocacia.Service.AgendaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contato")
@CrossOrigin("*")
public class ContatoController {

    @Autowired
    private AgendaService contatoService;

    @PostMapping("/save")
    public ResponseEntity<Agenda> save(@RequestBody AgendaDTO contatoDto) {
        try {
            Agenda novoContato = contatoService.save(contatoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoContato);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Agenda> update(@PathVariable Long id, @RequestBody AgendaDTO contatoDto) {
        try {
            Agenda contatoAtualizado = contatoService.update(id, contatoDto);
            return ResponseEntity.ok(contatoAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            contatoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Agenda>> findAll() {
        List<Agenda> contatos = contatoService.findAll();
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Agenda> findById(@PathVariable Long id) {
        return contatoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findByIdCliente/{clienteId}")
    public ResponseEntity<List<Agenda>> findByIdCliente(@PathVariable Long clienteId) {
        List<Agenda> contatos = contatoService.findByIdcliente(clienteId);
        return ResponseEntity.ok(contatos);
    }
}
