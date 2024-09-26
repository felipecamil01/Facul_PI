package com.Advocacia.ControllerTest;

import com.Advocacia.Controller.FinanceiroController;

import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Financeiro;

import com.Advocacia.Entity.StatusPagamento;
import com.Advocacia.Repository.FinanceiroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FinanceiroControllerTest {
    @Autowired
    FinanceiroController financeiroController;
    @MockBean
    FinanceiroRepository financeiroRepository;

    @BeforeEach
    void setUp() {
        Financeiro financeiro = new Financeiro();
        financeiro.setId(1L);
        financeiro.setHonorado(new BigDecimal("1000.00"));
        financeiro.setFormaPagamento("Cartão de Crédito");
        financeiro.setStatusPagamento(StatusPagamento.PENDENTE);
        financeiro.setDataVencimentoParcelas(LocalDate.now().plusMonths(1));
        financeiro.setDespesasAdicionais(new BigDecimal("200.00"));
        Financeiro financeiro2 = new Financeiro();
        financeiro2.setId(2L);
        financeiro2.setHonorado(new BigDecimal("1000.00"));
        financeiro2.setFormaPagamento("Cartão de Crédito");
        financeiro2.setStatusPagamento(StatusPagamento.PENDENTE);
        financeiro2.setDataVencimentoParcelas(LocalDate.now().plusMonths(1));
        financeiro2.setDespesasAdicionais(new BigDecimal("200.00"));





//        when(financeiroController.buscarFinanceiroPorId(1L)).thenReturn(ResponseEntity.ok(financeiro));
//
//        List<Financeiro> lista = new ArrayList<>();
//        lista.add(financeiro);
//        lista.add(financeiro2);
//        when(financeiroController.listar()).thenReturn(lista);

    }

    //Teste criar
    @Test
    void cenario1(){
        Financeiro financeiro = new Financeiro();
        ResponseEntity<Financeiro> retorno= this.financeiroController.criarFinanceiro(financeiro);
        assertEquals(HttpStatus.CREATED,retorno.getStatusCode());
    }
//    //Teste Atualizar ;
//    @Test
//    void cenario2(){
//        Financeiro financeiro = new Financeiro();
//        ResponseEntity<Financeiro> retorno= this.financeiroController.atualizarFinanceiro(1L,financeiro);
//        assertEquals(HttpStatus.OK,retorno.getStatusCode());
//    }
//    //Teste buscar id;
//    @Test
//    void cenario3(){
//        Financeiro financeiro = new Financeiro();
//        ResponseEntity<Financeiro> retorno = this.financeiroController.buscarFinanceiroPorId(1L);
//        assertEquals(HttpStatus.OK,retorno.getStatusCode());
//    }
    //Teste buscar lista ;
//    @Test
//    void cenario4(){
//
//        List<Cliente> lista = new ArrayList<>();
//        ResponseEntity<List<Financeiro>> retorno = this.financeiroController.listarFinanceiros();
//        assertEquals(HttpStatus.OK,retorno.getStatusCode());
//
//
//    }
    //Teste Atualiza  inexistente
    @Test
    void cenario5(){
     Financeiro financeiro = new Financeiro();
        ResponseEntity<Financeiro> retorno = this.financeiroController.atualizarFinanceiro(3L,financeiro);
        assertEquals(HttpStatus.NOT_FOUND,retorno.getStatusCode());
   }
//    //Teste deleta
//    @Test
//    void cenario6(){
//        ResponseEntity<Void> retorno = this.financeiroController.deletarFinanceiro(1L);
//        assertEquals(HttpStatus.NO_CONTENT,retorno.getStatusCode());
//    }
    //Teste Deleta inexistente
    @Test
    void cenario7(){
        ResponseEntity<Void> retorno = this.financeiroController.deletarFinanceiro(11L);
        assertEquals(HttpStatus.NOT_FOUND,retorno.getStatusCode());
    }
   // Teste busca Pendentes
    @Test
    void cenario8(){
        Financeiro financeiro = new Financeiro();
        ResponseEntity<List<Financeiro>> retorno = this.financeiroController.buscarPendentes(financeiro.getStatusPagamento());
        assertEquals(HttpStatus.ACCEPTED,retorno.getStatusCode());

    }
    // Teste busca Pendentes
    @Test
    void cenario9(){
        Financeiro financeiro = new Financeiro();
        ResponseEntity<List<Financeiro>> retorno = this.financeiroController.buscarPendentes(financeiro.getStatusPagamento());
        assertEquals(HttpStatus.ACCEPTED,retorno.getStatusCode());

    }
    // Teste busca Status
    @Test
    void cenario10(){
        Financeiro financeiro = new Financeiro();
        ResponseEntity<List<Financeiro>> retorno = this.financeiroController.buscarPorStatus(financeiro.getStatusPagamento());
        assertEquals(HttpStatus.ACCEPTED,retorno.getStatusCode());

    }
//    //POR data
//    @Test
//    void cenario12(){
//        Financeiro financeiro = new Financeiro();
//        ResponseEntity<List<Financeiro>> retorno = this.financeiroController.buscarVencimentos(financeiro.getStatusPagamento(),);
//        assertEquals(HttpStatus.ACCEPTED,retorno.getStatusCode());
//
//    }
//    //POR data
//    @Test
//    void cenario12(){
//        Financeiro financeiro = new Financeiro();
//        ResponseEntity<List<Financeiro>> retorno = this.financeiroController.buscarVencimentos(financeiro.getStatusPagamento(),);
//        assertEquals(HttpStatus.ACCEPTED,retorno.getStatusCode());
//
//
//    }
//    @Test
//    void cenario9(){
//        Financeiro financeiro = new Financeiro();
//        ResponseEntity<List<Financeiro>> retorno = this.financeiroController.(financeiro.getStatusPagamento());
//        assertEquals(HttpStatus.ACCEPTED,retorno.getStatusCode());
//
//    }
}
