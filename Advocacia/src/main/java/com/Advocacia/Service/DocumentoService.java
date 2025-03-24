package com.Advocacia.Service;

import com.Advocacia.Entity.Documento;
import com.Advocacia.Repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    public Documento save(Documento documento) {
        return documentoRepository.save(documento);
    }

    public Documento update(Long id, Documento documentoAtualizado) {
        documentoRepository.findById(id).orElseThrow();
        documentoAtualizado.setId(id);
        return documentoRepository.save(documentoAtualizado);
    }

    public void delete(Long id) {
        Documento documento = documentoRepository.findById(id).orElseThrow();
        documentoRepository.delete(documento);
    }

    public List<Documento> findAll() {
        return documentoRepository.findAll();
    }

    public Documento findById(Long id) {
        return documentoRepository.findById(id).orElseThrow();
    }
    public List<String> findStatusDocumento(){
        return documentoRepository.findStatusDocumento();
    }
}