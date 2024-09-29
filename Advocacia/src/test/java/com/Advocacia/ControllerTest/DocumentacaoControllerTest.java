package com.Advocacia.ControllerTest;


import com.Advocacia.Controller.DocumentacaoController;

import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Documentos;

import com.Advocacia.Repository.DocumentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DocumentacaoControllerTest {

    @Autowired
    DocumentacaoController documentacaoController;
    @MockBean
    DocumentoRepository documentoRepository;

    @BeforeEach
    void setup() {
        Documentos documentos = new Documentos();
        documentos.setId(1L);
        documentos.setStatusDocumento("ATIVO");
        documentos.setObservacao("Bla BLa BLa");
        documentos.setDataRecebimento(LocalDate.ofEpochDay(11/04/2024));

        Documentos documentos2 = new Documentos();
        documentos.setId(2L);
        documentos.setStatusDocumento("INATIVO");
        documentos.setObservacao("Bla BLa BLa");
        documentos.setDataRecebimento(LocalDate.ofEpochDay(18/04/2024));

        when(documentoRepository.findById(1L)).thenReturn(Optional.of(documentos));

        List<Documentos> lista = new ArrayList<>();
        lista.add(documentos);
        lista.add(documentos2);
        when(documentoRepository.findAll()).thenReturn(lista);

    }

    //Teste criar documento
    @Test
    void cenario1(){
       Documentos documentos = new Documentos();
        ResponseEntity<Documentos> retorno = this.documentacaoController.criarDocumentacao(documentos);
        assertEquals(HttpStatus.CREATED,retorno.getStatusCode());
    }
    //Teste Atualizar documento;
    @Test
    void cenario2(){
        Documentos documentos = new Documentos();
        ResponseEntity<Documentos> retorno = this.documentacaoController.atualizarDocumentacao(1L,documentos);
        assertEquals(HttpStatus.OK,retorno.getStatusCode());
    }
    //Teste buscar id;
    @Test
    void cenario3(){
        Documentos documentos = new Documentos();
        ResponseEntity<Documentos> retorno = this.documentacaoController.buscarDocumentacaoPorId(1L);
        assertEquals(HttpStatus.OK,retorno.getStatusCode());
    }
    //Teste Atualiza inexistente
    @Test
    void cenario5(){
        Documentos documentos = new Documentos();
        ResponseEntity<Documentos> retorno = this.documentacaoController.atualizarDocumentacao(3L,documentos);
        assertEquals(HttpStatus.NOT_FOUND,retorno.getStatusCode());
    }
    //Teste deleta cliente
//    @Test
//    void cenario6(){
//        ResponseEntity<Void> retorno = this.documentacaoController.deletarDocumentacao(2L);
//        assertEquals(HttpStatus.NO_CONTENT,retorno.getStatusCode());
//    }
    //Teste Deleta cliente inexistente
    @Test
    void cenario7(){
        ResponseEntity<Void> retorno = this.documentacaoController.deletarDocumentacao(4L);
        assertEquals(HttpStatus.NOT_FOUND,retorno.getStatusCode());
    }
    }

