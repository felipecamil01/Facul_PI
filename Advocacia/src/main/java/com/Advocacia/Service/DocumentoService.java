package com.Advocacia.Service;

import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Documento;
import com.Advocacia.Entity.Processo;
import com.Advocacia.Repository.DocumentoRepository;
import com.Advocacia.Repository.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentoService {

    private static final String BASE_DIRECTORY = System.getProperty("user.home") + "/LionLaw/documentos";

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private ProcessoRepository processoRepository;

    @Transactional
    public Documento save(Documento documento) {
        prepararEntidade(documento);
        armazenarArquivoLocal(documento);
        Documento salvo = documentoRepository.save(documento);
        salvo.setArquivo(null);
        return salvo;
    }

    @Transactional
    public Documento update(Long id, Documento documentoAtualizado) {
        Documento existente = documentoRepository.findById(id).orElseThrow();
        documentoAtualizado.setId(id);
        prepararEntidade(documentoAtualizado);
        if (documentoAtualizado.getArquivo() != null && documentoAtualizado.getArquivo().length > 0) {
            armazenarArquivoLocal(documentoAtualizado);
        } else {
            documentoAtualizado.setArquivoPath(existente.getArquivoPath());
            documentoAtualizado.setNomeArquivo(existente.getNomeArquivo());
        }
        Documento salvo = documentoRepository.save(documentoAtualizado);
        salvo.setArquivo(null);
        return salvo;
    }

    public void delete(Long id) {
        Documento documento = documentoRepository.findById(id).orElseThrow();
        documentoRepository.delete(documento);
        if (documento.getArquivoPath() != null) {
            try {
                Files.deleteIfExists(Paths.get(documento.getArquivoPath()));
            } catch (IOException ignored) {
            }
        }
    }

    public List<Documento> findAll() {
        List<Documento> documentos = documentoRepository.findAll();
        documentos.forEach(doc -> doc.setArquivo(null));
        return documentos;
    }

    public Documento findById(Long id) {
        Documento documento = documentoRepository.findById(id).orElseThrow();
        documento.setArquivo(null);
        return documento;
    }

    public List<String> findStatusDocumento() {
        return documentoRepository.findStatusDocumento();
    }

    public List<Documento> findByCliente(Long clienteId) {
        List<Documento> documentos = documentoRepository.findByProcessoClienteId(clienteId);
        documentos.forEach(doc -> doc.setArquivo(null));
        documentos.sort(Comparator.comparing(Documento::getDataRecebimento, Comparator.nullsLast(Comparator.reverseOrder())));
        return documentos;
    }

    public byte[] downloadArquivo(Long id) {
        Documento documento = documentoRepository.findById(id).orElseThrow();
        if (documento.getArquivo() != null && documento.getArquivo().length > 0) {
            return documento.getArquivo();
        }
        if (documento.getArquivoPath() != null) {
            try {
                return Files.readAllBytes(Paths.get(documento.getArquivoPath()));
            } catch (IOException e) {
                throw new IllegalStateException("Não foi possível ler o arquivo armazenado localmente.", e);
            }
        }
        throw new IllegalStateException("Documento não possui arquivo associado.");
    }

    private void prepararEntidade(Documento documento) {
        if (documento.getProcesso() == null || documento.getProcesso().getId() == 0) {
            throw new IllegalArgumentException("Documento precisa estar associado a um processo válido.");
        }
        Processo processo = processoRepository.findById(documento.getProcesso().getId())
            .orElseThrow(() -> new IllegalArgumentException("Processo informado não foi encontrado."));
        Cliente cliente = processo.getCliente();
        if (cliente == null) {
            throw new IllegalArgumentException("Processo precisa estar vinculado a um cliente antes de anexar documentos.");
        }
        documento.setProcesso(processo);
        if (documento.getTitulo() == null || documento.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Informe um título para o documento.");
        }
    }

    private void armazenarArquivoLocal(Documento documento) {
        byte[] arquivo = documento.getArquivo();
        if (arquivo == null || arquivo.length == 0) {
            return;
        }

        String clienteFolder = "cliente-" + documento.getProcesso().getCliente().getId();
        Path destinoPasta = Paths.get(BASE_DIRECTORY, clienteFolder);
        try {
            Files.createDirectories(destinoPasta);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível criar o diretório para armazenar documentos.", e);
        }

        String nomeArquivo = documento.getNomeArquivo();
        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            nomeArquivo = gerarNomeArquivo(documento);
        }
        Path destinoArquivo = destinoPasta.resolve(nomeArquivo);

        try {
            Files.write(destinoArquivo, arquivo);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao salvar o arquivo no disco.", e);
        }

        documento.setArquivoPath(destinoArquivo.toString());
        documento.setNomeArquivo(nomeArquivo);
    }

    private String gerarNomeArquivo(Documento documento) {
        String tituloSanitizado = documento.getTitulo()
            .replaceAll("[^a-zA-Z0-9_\\-]", "_")
            .replaceAll("_+", "_");
        return tituloSanitizado + "_" + UUID.randomUUID() + ".pdf";
    }
}
