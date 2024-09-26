package com.Advocacia.ControllerTest;

import com.Advocacia.Controller.EnderecoController;
import com.Advocacia.Entity.Endereco;
import com.Advocacia.Repository.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
public class EnderecoControllertest {
    @Autowired
    EnderecoController enderecoController;
    @MockBean
    EnderecoRepository  enderecoRepository;

    @BeforeEach
    void setup(){
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        endereco.setUf("PR");
        endereco.setBairro("Centro");
        endereco.setCep("85850000");
        endereco.setCidade("Foz do Iguaçu");
        endereco.setComplemento("casa");
        endereco.setNumero("431");
        endereco.setLogradouro("rua gramado");

        Endereco endereco2 = new Endereco();
        endereco2.setId(2L);
        endereco2.setUf("PR");
        endereco2.setBairro("Centro");
        endereco2.setCep("85850001");
        endereco2.setCidade("Foz do Iguaçu");
        endereco2.setComplemento("Casa");
        endereco2.setNumero("4331");
        endereco2.setLogradouro("ruaa gramado");

    when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));
        List<Endereco> lista =new ArrayList<>();
        lista.add(endereco);
        lista.add(endereco2);
        when(enderecoRepository.findAll()).thenReturn(lista);;
    }
    @Test
    void cenario1(){
        Endereco endereco = new Endereco();
        ResponseEntity<Endereco> retorno = this.enderecoController.criarEndereco(endereco);
        assertEquals(HttpStatus.CREATED,retorno.getStatusCode());
    }
    //Teste atualizar
    @Test
    void cenario2(){
        Endereco endereco = new Endereco();
        ResponseEntity<Endereco> retorno = this.enderecoController.atualizarEndereco(1L,endereco);
        assertEquals(HttpStatus.OK,retorno.getStatusCode());
    }
    //Teste atualizar
    @Test
    void cenario3(){
        Endereco endereco = new Endereco();
        ResponseEntity<Endereco> retorno = this.enderecoController.atualizarEndereco(5L,endereco);
        assertEquals(HttpStatus.NOT_FOUND,retorno.getStatusCode());
    }


}
