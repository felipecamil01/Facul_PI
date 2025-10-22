package com.Advocacia.Service;

import com.Advocacia.DTO.AgendaResumoDTO;
import com.Advocacia.DTO.ClienteDTO;
import com.Advocacia.DTO.ClienteOverviewDTO;
import com.Advocacia.DTO.DocumentoResumoDTO;
import com.Advocacia.DTO.ProcessoResumoDTO;
import com.Advocacia.DTO.PrazoImportanteDTO;
import com.Advocacia.Entity.Agenda;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Documento;
import com.Advocacia.Entity.Pagamento;
import com.Advocacia.Entity.Processo;
import com.Advocacia.Enum.StatusCliente;
import com.Advocacia.Repository.AgendaRepository;
import com.Advocacia.Repository.ClienteRepository;
import com.Advocacia.Repository.DocumentoRepository;
import com.Advocacia.Repository.PagamentoRepository;
import com.Advocacia.Repository.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    public Cliente save(Cliente cliente) {
        cliente.setStatusCliente(StatusCliente.ATIVO);
        return clienteRepository.save(cliente);
    }

    public Cliente update(Long id, Cliente clienteAtualizado) {
        clienteRepository.findById(id).orElseThrow();
        clienteAtualizado.setStatusCliente(StatusCliente.ATIVO);
        clienteAtualizado.setId(id);
        return clienteRepository.save(clienteAtualizado);
    }

    public void delete(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow();
        cliente.setStatusCliente(StatusCliente.INATIVO);
        clienteRepository.save(cliente);
    }

    public List<ClienteDTO> findAll() {

        List<Cliente> clientes = clienteRepository.findAll(); // isso retorna List<Cliente>
        var clientesdto = clientes.stream()
                .map(ClienteDTO::new) // aqui o construtor recebe Cliente, entÃ£o tÃ¡ ok
                .collect(Collectors.toList());
        return clientesdto;

    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElseThrow();
    }

    public List<Cliente> findByNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public ClienteOverviewDTO obterVisaoGeral(Long clienteId) {
        Cliente cliente = findById(clienteId);
        List<Processo> processos = processoRepository.findByClienteId(clienteId);
        List<ProcessoResumoDTO> processoDTOs = processos.stream()
            .map(this::mapearProcesso)
            .collect(Collectors.toList());

        List<Pagamento> pagamentos = pagamentoRepository.findByClienteId(clienteId);
        List<DocumentoResumoDTO> documentos = documentoRepository.findByProcessoClienteId(clienteId)
            .stream()
            .map(this::mapearDocumento)
            .collect(Collectors.toList());

        List<AgendaResumoDTO> prazos = agendaRepository.findByClienteVinculado(clienteId)
            .stream()
            .filter(Agenda::isPrazoImportante)
            .map(this::mapearAgenda)
            .sorted(Comparator.comparing(AgendaResumoDTO::getData))
            .collect(Collectors.toList());

        return ClienteOverviewDTO.builder()
            .cliente(cliente)
            .processos(processoDTOs)
            .pagamentos(pagamentos)
            .documentos(documentos)
            .prazosImportantes(prazos)
            .build();
    }

    public List<ClienteOverviewDTO> pesquisarVisoesGerais(String nome) {
        return findByNome(nome).stream()
            .map(cliente -> obterVisaoGeral(cliente.getId()))
            .collect(Collectors.toList());
    }

    private ProcessoResumoDTO mapearProcesso(Processo processo) {
        List<DocumentoResumoDTO> documentos = processo.getDocumentos() == null ? List.of() :
            processo.getDocumentos().stream()
                .map(this::mapearDocumento)
                .collect(Collectors.toList());

        return ProcessoResumoDTO.builder()
            .id(processo.getId())
            .numeroProcesso(processo.getNumeroProcesso())
            .areaAtuacao(processo.getAreaAtuacao())
            .situacaoAtual(processo.getSituacaoAtual())
            .dataInicio(processo.getDataInicio())
                    .prazosImportantes(processo.getPrazosImportantes() == null ? List.of() :
            processo.getPrazosImportantes().stream()
                .map(p -> PrazoImportanteDTO.builder()
                    .data(p.getData())
                    .descricao(p.getDescricao())
                    .build())
                .collect(Collectors.toList()))
            .documentos(documentos)
            .build();
    }

    private DocumentoResumoDTO mapearDocumento(Documento documento) {
        return DocumentoResumoDTO.builder()
            .id(documento.getId())
            .titulo(documento.getTitulo())
            .dataRecebimento(documento.getDataRecebimento())
            .statusDocumento(documento.getStatusDocumento())
            .observacao(documento.getObservacao())
            .processoId(documento.getProcesso() != null ? documento.getProcesso().getId() : null)
            .processoNumero(documento.getProcesso() != null ? documento.getProcesso().getNumeroProcesso() : null)
            .caminhoArquivo(documento.getArquivoPath())
            .build();
    }

    private AgendaResumoDTO mapearAgenda(Agenda agenda) {
        return AgendaResumoDTO.builder()
            .id(agenda.getId())
            .descricao(agenda.getDescricao())
            .tipo(agenda.getTipo())
            .prazoImportante(agenda.isPrazoImportante())
            .prioridade(agenda.getPrioridade())
            .data(agenda.getData())
            .processoId(agenda.getProcesso() != null ? agenda.getProcesso().getId() : null)
            .processoNumero(agenda.getProcesso() != null ? agenda.getProcesso().getNumeroProcesso() : null)
            .build();
    }
}

