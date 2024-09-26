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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNome("Pedrao");
        cliente2.setCpf("109.686.239-56");
        cliente2.setRg("9958511-0");
        cliente2.setDataNascimento(LocalDate.ofEpochDay(11/04/2004));
        cliente2.setEstadoCivil(EstadoCivil.SOLTEIRO);
        cliente2.setProfissao("Uber");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente1));

        List<Cliente> lista = new ArrayList<>();
        lista.add(cliente1);
        lista.add(cliente2);
        when(clienteRepository.findAll()).thenReturn(lista);

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
    //Teste buscar id;
    @Test
    void cenario3(){
        Cliente cliente1 = new Cliente();
        ResponseEntity<Cliente> retorno = this.clienteController.buscarClientePorId(1L);
        assertEquals(HttpStatus.OK,retorno.getStatusCode());
    }
    //Teste buscar lista cliente;
    @Test
    void cenario4(){

        List<Cliente> lista = new ArrayList<>();
        ResponseEntity<List<Cliente>> retorno = this.clienteController.listarClientes();
        assertEquals(HttpStatus.OK,retorno.getStatusCode());


    }
    //Teste Atualiza cliente inexistente
    @Test
    void cenario5(){
        Cliente cliente1 = new Cliente();
        ResponseEntity<Cliente> retorno = this.clienteController.atualizarCliente(3L,cliente1);
        assertEquals(HttpStatus.NOT_FOUND,retorno.getStatusCode());
    }
    //Teste deleta cliente
    @Test
    void cenario6(){
        ResponseEntity<Void> retorno = this.clienteController.deletarCliente(1L);
        assertEquals(HttpStatus.NO_CONTENT,retorno.getStatusCode());
    }
    //Teste Deleta cliente inexistente
    @Test
    void cenario7(){
        ResponseEntity<Void> retorno = this.clienteController.deletarCliente(4L);
        assertEquals(HttpStatus.NOT_FOUND,retorno.getStatusCode());
    }
    //Teste busca pelo nome
    @Test
    void cenario8(){
        Cliente cliente1 = new Cliente();
        ResponseEntity<List<Cliente>> retorno = this.clienteController.buscarClientesPorNome(cliente1.getNome());
        assertEquals(HttpStatus.OK,retorno.getStatusCode());

    }




}
