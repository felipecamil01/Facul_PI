package com.Advocacia.Controller;

import com.Advocacia.Entity.Endereco;
import com.Advocacia.Service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping("/save")
    public ResponseEntity<Endereco> save(@RequestBody Endereco enderecoNovo) {
        Endereco endereco = enderecoService.save(enderecoNovo);
        return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Endereco> update(@PathVariable Long id, @RequestBody Endereco enderecoAtualizado) {
    	Endereco endereco = enderecoService.update(id, enderecoAtualizado);
	    return ResponseEntity.status(HttpStatus.OK).body(endereco);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	enderecoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Endereco>> findAll() {
        List<Endereco> enderecos = enderecoService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(enderecos);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Endereco> findById(@PathVariable Long id) {
    	Endereco endereco = enderecoService.findById(id);
    	return ResponseEntity.status(HttpStatus.OK).body(endereco);
    }
}