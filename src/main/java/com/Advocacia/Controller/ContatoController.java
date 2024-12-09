package com.Advocacia.Controller;

import com.Advocacia.DTO.ContatoDto;
import com.Advocacia.Entity.Contato;
import com.Advocacia.Service.ContatoService;
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
    private ContatoService contatoService;

    @PostMapping("/save")
    public ResponseEntity<Contato> save(@RequestBody ContatoDto contatoDto) {
        try {
            Contato novoContato = contatoService.save(contatoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoContato);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Contato> update(@PathVariable Long id, @RequestBody ContatoDto contatoDto) {
        try {
            Contato contatoAtualizado = contatoService.update(id, contatoDto);
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
    public ResponseEntity<List<Contato>> findAll() {
        List<Contato> contatos = contatoService.findAll();
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Contato> findById(@PathVariable Long id) {
        return contatoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findByIdCliente/{clienteId}")
    public ResponseEntity<List<Contato>> findByIdCliente(@PathVariable Long clienteId) {
        List<Contato> contatos = contatoService.findByIdcliente(clienteId);
        return ResponseEntity.ok(contatos);
    }
}
