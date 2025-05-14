//package com.Advocacia.ControllerTest;
//
//import com.Advocacia.Controller.DocumentoController;
//import com.Advocacia.Entity.Documento;
//import com.Advocacia.Service.DocumentoService;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//class DocumentoControllerTest {
//
//    @InjectMocks
//    private DocumentoController documentoController;
//
//    @Mock
//    private DocumentoService documentoService;
//
//    private MultipartFile[] files;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        MultipartFile mockFile = mock(MultipartFile.class);
//        files = new MultipartFile[]{mockFile};
//    }
//
//    @Test
//    void testSaveDocumento() {
//        Documento documento = new Documento();
//        documento.setId(1L);
//        documento.setListaDocumentos(new ArrayList<>());
//
//        when(documentoService.save(any(Documento.class))).thenReturn(documento);
//
//        ResponseEntity<Map<String, String>> response = documentoController.handleFileUpload(files, 
//            LocalDate.now(), "PENDENTE", "Teste de documento");
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Arquivos enviados com sucesso!", response.getBody().get("message"));
//    }
//    
//    @Test
//    void testSaveDocumentoNotFound() {
//        when(documentoService.save(any(Documento.class)))
//            .thenThrow(new EntityNotFoundException("Documento não encontrado"));
//
//        MultipartFile[] files = new MultipartFile[] {};
//        ResponseEntity<Map<String, String>> response = documentoController.handleFileUpload(
//            files, LocalDate.now(), "PENDENTE", "Teste de documento");
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("Documento não encontrado", response.getBody().get("error"));
//    }
//
//    @Test
//    void testSaveDocumentoIOException() throws IOException {
//        MultipartFile[] files = new MultipartFile[] {
//            mock(MultipartFile.class)
//        };
//        when(files[0].isEmpty()).thenReturn(false);
//        when(files[0].getOriginalFilename()).thenReturn("file.txt");
//        doThrow(new IOException("Erro ao salvar o arquivo")).when(files[0]).transferTo(any(File.class));
//
//        ResponseEntity<Map<String, String>> response = documentoController.handleFileUpload(
//            files, LocalDate.now(), "PENDENTE", "Teste de documento");
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Falha ao enviar o arquivo: Erro ao salvar o arquivo", response.getBody().get("error"));
//    }
//
//    @Test
//    void testSaveDocumentoFileEmpty() {
//        MultipartFile[] files = new MultipartFile[] {
//            mock(MultipartFile.class)
//        };
//        when(files[0].isEmpty()).thenReturn(true);
//
//        ResponseEntity<Map<String, String>> response = documentoController.handleFileUpload(
//            files, LocalDate.now(), "PENDENTE", "Teste de documento");
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Por favor, selecione um arquivo para enviar.", response.getBody().get("error"));
//    }
//
//
//    @Test
//    void testUpdateDocumento() {
//        Long id = 1L;
//        Documento documentoAtualizado = new Documento();
//
//        when(documentoService.update(eq(id), any(Documento.class))).thenReturn(documentoAtualizado);
//
//        ResponseEntity<Documento> response = documentoController.update(id, documentoAtualizado);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(documentoAtualizado, response.getBody());
//    }
//
//    @Test
//    void testUpdateDocumentoNotFound() {
//        Long id = 1L;
//        Documento documentoAtualizado = new Documento();
//
//        when(documentoService.update(eq(id), any(Documento.class))).thenThrow(new EntityNotFoundException());
//
//        ResponseEntity<Documento> response = documentoController.update(id, documentoAtualizado);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    void testDeleteDocumento() {
//        Long id = 1L;
//
//        doNothing().when(documentoService).delete(id);
//
//        ResponseEntity<Void> response = documentoController.delete(id);
//
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//    }
//
//    @Test
//    void testDeleteDocumentoNotFound() {
//        Long id = 1L;
//
//        doThrow(new EntityNotFoundException()).when(documentoService).delete(id);
//
//        ResponseEntity<Void> response = documentoController.delete(id);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    void testFindAll() {
//        List<Documento> documentoList = new ArrayList<>();
//        documentoList.add(new Documento());
//
//        when(documentoService.findAll()).thenReturn(documentoList);
//
//        ResponseEntity<List<Documento>> response = documentoController.findAll();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(documentoList, response.getBody());
//    }
//
//    @Test
//    void testFindById() {
//        Long id = 1L;
//        Documento documento = new Documento();
//        documento.setId(id);
//
//        when(documentoService.findById(id)).thenReturn(Optional.of(documento));
//
//        ResponseEntity<Documento> response = documentoController.findById(id);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(documento, response.getBody());
//    }
//
//    @Test
//    void testFindByIdNotFound() {
//        Long id = 1L;
//
//        when(documentoService.findById(id)).thenReturn(Optional.empty());
//
//        ResponseEntity<Documento> response = documentoController.findById(id);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//}
