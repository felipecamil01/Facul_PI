package com.Advocacia.ControllerTest;


import com.Advocacia.Controller.ProcessoController;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Processo;
import com.Advocacia.Repository.ProcessoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProcessosControllerTest {
    @Autowired
    ProcessoController processoController;
    @MockBean
    ProcessoRepository processoRepository;

    @BeforeEach
    void setUp() {
        Processo processo = new Processo();
        processo.setId(1L);
        processo.setTipoCliente("Pessoa Física");
        processo.setAreaAtuacao("Direito Trabalhista");
        processo.setNumeroProcesso("123456789");
        processo.setComarca("São Paulo");
        processo.setDataInicio(LocalDate.of(2023, 1, 15));
        processo.setDescricao("ATIVO");
        processo.setAndamento("Processo em fase de instrução");
        processo.setSituacaoAtual("Em andamento");
        processo.setPrazosImportantes(Arrays.asList(
                LocalDate.of(2024, 3, 10),
                LocalDate.of(2023, 5, 20)
        ));

        Processo processo2 = new Processo();
        processo2.setId(2L);
        processo2.setTipoCliente("Pessoa Física");
        processo2.setAreaAtuacao("Direito Trabalhista");
        processo2.setNumeroProcesso("123456789");
        processo2.setComarca("São Paulo");
        processo2.setDataInicio(LocalDate.of(2023, 1, 15));
        processo2.setDescricao("ATIO");
        processo2.setAndamento("Processo em fase de instrução");
        processo2.setSituacaoAtual("Em andamento");
        processo2.setPrazosImportantes(Arrays.asList(
                LocalDate.of(2025, 3, 10),
                LocalDate.of(2023, 5, 20)
        ));

        when(processoRepository.findById(1L)).thenReturn(Optional.of(processo));

        List<Processo> lista = new ArrayList<>();
        lista.add(processo);
        lista.add(processo2);
        when(processoRepository.findAll()).thenReturn(lista);

    }

    //Teste criar documento
    @Test
    void cenario1() {
        Processo processo = new Processo();
        ResponseEntity<Processo> retorno = this.processoController.criarProcesso(processo);
        assertEquals(HttpStatus.CREATED, retorno.getStatusCode());
    }

    //Teste Atualizar documento;
    @Test
    void cenario2() {
        Processo processo = new Processo();
        ResponseEntity<Processo> retorno = this.processoController.atualizarProcesso(1L, processo);
        assertEquals(HttpStatus.OK, retorno.getStatusCode());
    }

    //Teste buscar id;
    @Test
    void cenario3() {
        Processo processo = new Processo();
        ResponseEntity<Processo> retorno = this.processoController.buscarProcessoPorId(1L);
        assertEquals(HttpStatus.OK, retorno.getStatusCode());
    }

    //Teste Atualiza inexistente
    @Test
    void cenario5() {
        Processo processo = new Processo();
        ResponseEntity<Processo> retorno = this.processoController.atualizarProcesso(3L, processo);
        assertEquals(HttpStatus.NOT_FOUND, retorno.getStatusCode());
    }

    //Teste deleta cliente
//    @Test
//    void cenario6(){
//        ResponseEntity<Void> retorno = this.processoController.deletarProcesso(2L);
//        assertEquals(HttpStatus.NO_CONTENT,retorno.getStatusCode());
//    }
    //Teste Deleta cliente inexistente
    @Test
    void cenario7() {
        ResponseEntity<Void> retorno = this.processoController.deletarProcesso(4L);
        assertEquals(HttpStatus.NOT_FOUND, retorno.getStatusCode());
    }


    //Teste busca pelo nome
    @Test
    void cenario8() {
        Processo processo = new Processo();
        ResponseEntity<List<Processo>> retorno = this.processoController.buscarProcessosPorNumero(processo.getNumeroProcesso());
        Assertions.assertEquals(HttpStatus.OK, retorno.getStatusCode());

    }
}