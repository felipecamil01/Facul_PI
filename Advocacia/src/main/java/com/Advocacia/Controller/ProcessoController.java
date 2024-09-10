import com.Advocacia.Entity.Processo;
import com.Advocacia.Service.ProcessoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/processos")
public class ProcessoController {

    @Autowired
    private ProcessoService processoService;

    @PostMapping
    public ResponseEntity<Processo> criarProcesso(@Valid @RequestBody Processo processo) {
        Processo novoProcesso = processoService.salvarProcesso(processo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProcesso);
    }

    @GetMapping
    public ResponseEntity<Page<Processo>> listarProcessos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Processo> processos = processoService.listarProcessos(pageable);
        return ResponseEntity.ok(processos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Processo> buscarProcessoPorId(@PathVariable Long id) {
        return processoService.buscarProcessoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Processo> atualizarProcesso(@PathVariable Long id, @Valid @RequestBody Processo processoAtualizado) {
        try {
            Processo processo = processoService.atualizarProcesso(id, processoAtualizado);
            return ResponseEntity.ok(processo);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProcesso(@PathVariable Long id) {
        try {
            processoService.deletarProcesso(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Processo>> buscarProcessosPorNumero(@RequestParam String numero) {
        List<Processo> processos = processoService.buscarProcessosPorNumero(numero);
        return ResponseEntity.ok(processos);
    }
}