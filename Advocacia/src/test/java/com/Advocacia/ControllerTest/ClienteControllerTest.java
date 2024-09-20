package com.Advocacia.ControllerTest;

import com.Advocacia.Controller.ClienteController;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.EstadoCivil;
import com.Advocacia.Repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class ClienteControllerTest {
    @Autowired
    ClienteController clienteController;
    @MockBean
    ClienteRepository clienteRepository;

    @BeforeEach
    void setup() {
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNome("Fabiano");
        cliente1.setCpf("109.686.239-56");
        cliente1.setRg("9958511-0");
        cliente1.setDataNascimento(LocalDate.ofEpochDay(11/04/2004));
        cliente1.setEstadoCivil(EstadoCivil.SOLTEIRO);
        cliente1.setProfissao("Pedreiro");
        
    }
    //Teste criar cliente
    @Test
    void cenario1(){
        Cliente cliente1 = new Cliente();
        ResponseEntity<Cliente> retorno = this.clienteController.criarCliente(cliente1);
        assertEquals(HttpStatus.CREATED,retorno.getStatusCode());
    }
    //Teste Atualizar cliente;
    @Test
    void cenario2(){
        Cliente cliente1 = new Cliente();
        ResponseEntity<Cliente> retorno = this.clienteController.atualizarCliente(1L,cliente1);
        assertEquals(HttpStatus.OK,retorno.getStatusCode());
    }



}
