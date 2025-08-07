package com.Advocacia.Controller;

/*
 * import com.Advocacia.DTO.HIstoricoGeralDTO;
 * import com.Advocacia.DTO.HistoricoDTO;
 * import com.Advocacia.Entity.Cliente;
 * import com.Advocacia.Entity.Contato;
 * import com.Advocacia.Entity.Despesa;
 * import com.Advocacia.Entity.Processo;
 * import com.Advocacia.Service.HistoricoService;
 * import org.springframework.security.access.prepost.PreAuthorize;
 * import org.springframework.web.bind.annotation.GetMapping;
 * import org.springframework.web.bind.annotation.PathVariable;
 * import org.springframework.web.bind.annotation.RequestMapping; // Importe
 * import org.springframework.web.bind.annotation.RestController; // Importe
 * 
 * import java.util.List;
 * 
 * @PreAuthorize("hasRole('ROLE_ADMIN')")
 * 
 * @RestController // <-- ESSENCIAL: Transforma a classe em um controller REST
 * 
 * @RequestMapping("/api/historico") // <-- ESSENCIAL: Define a URL base para
 * todos os métodos
 * public class HistoricoController {
 * 
 * private final HistoricoService historicoService;
 * 
 * // <-- Boa prática usar injeção de dependência via construtor
 * public HistoricoController(HistoricoService historicoService) {
 * this.historicoService = historicoService;
 * }
 * 
 * @PreAuthorize("hasRole('ROLE_ADMIN')")
 * // Este método agora responderá em GET /api/historico
 * 
 * @GetMapping
 * public List<HIstoricoGeralDTO> historicoGeral() {
 * return historicoService.buscarTudo();
 * }
 * 
 * @PreAuthorize("hasRole('ROLE_ADMIN')")
 * // Este método responderá em GET /api/historico/cliente/{id}
 * 
 * @GetMapping("/cliente/{id}")
 * public List<HistoricoDTO> historicoCliente(@PathVariable Long id) {
 * return historicoService.buscarHistorico(Cliente.class, id);
 * }
 * 
 * @PreAuthorize("hasRole('ROLE_ADMIN')")
 * 
 * @GetMapping("/despesa/{id}")
 * public List<HistoricoDTO> historicoDespesa(@PathVariable Long id) {
 * return historicoService.buscarHistorico(Despesa.class, id);
 * }
 * 
 * @PreAuthorize("hasRole('ROLE_ADMIN')")
 * 
 * @GetMapping("/processo/{id}")
 * public List<HistoricoDTO> historicoProcesso(@PathVariable Long id) {
 * return historicoService.buscarHistorico(Processo.class, id);
 * }
 * 
 * @PreAuthorize("hasRole('ROLE_ADMIN')")
 * 
 * @GetMapping("/contato/{id}")
 * public List<HistoricoDTO> historicoContato(@PathVariable Long id) {
 * return historicoService.buscarHistorico(Contato.class, id);
 * }
 * }
 * 
 */