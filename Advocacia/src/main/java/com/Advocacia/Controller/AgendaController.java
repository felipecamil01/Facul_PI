package com.Advocacia.Controller;

import com.Advocacia.Entity.Agenda;
import com.Advocacia.Service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/agenda")
public class AgendaController {

    @Autowired
    private AgendaService contatoService;

    @PostMapping("/save")
    public ResponseEntity<Agenda> save(@RequestBody Agenda agendaNova) {
        Agenda contato = contatoService.save(agendaNova);
        return ResponseEntity.status(HttpStatus.CREATED).body(contato);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Agenda> update(@PathVariable Long id, @RequestBody Agenda agendaAtualizada) {
        Agenda contato = contatoService.update(id, agendaAtualizada);
        return ResponseEntity.status(HttpStatus.OK).body(contato);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contatoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Agenda>> findAll() {
        List<Agenda> contatos = contatoService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(contatos);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Agenda> findById(@PathVariable Long id) {
    	Agenda contato = contatoService.findById(id);
    	return ResponseEntity.status(HttpStatus.OK).body(contato);
    }
}