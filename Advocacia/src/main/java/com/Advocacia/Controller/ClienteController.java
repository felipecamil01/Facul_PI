package com.Advocacia.Controller;

import com.Advocacia.DTO.ClienteDTO;
import com.Advocacia.DTO.ClienteOverviewDTO;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/cliente")

public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/save")
    public ResponseEntity<Cliente> save(@RequestBody Cliente clienteNovo) {
        Cliente cliente = clienteService.save(clienteNovo);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        Cliente cliente = clienteService.update(id, clienteAtualizado);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<ClienteDTO> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @GetMapping("/findByNome/{nome}")
    public ResponseEntity<List<Cliente>> findByNome(@PathVariable String nome) {
        List<Cliente> clientes = clienteService.findByNome(nome);
        return ResponseEntity.status(HttpStatus.OK).body(clientes);
    }

    @GetMapping("/{id}/overview")
    public ResponseEntity<ClienteOverviewDTO> overview(@PathVariable Long id) {
        ClienteOverviewDTO overview = clienteService.obterVisaoGeral(id);
        return ResponseEntity.ok(overview);
    }

    @GetMapping("/overview")
    public ResponseEntity<List<ClienteOverviewDTO>> overviewPorNome(@RequestParam String nome) {
        List<ClienteOverviewDTO> overviews = clienteService.pesquisarVisoesGerais(nome);
        return ResponseEntity.ok(overviews);
    }

}
