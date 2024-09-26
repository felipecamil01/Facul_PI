package com.Advocacia.ControllerTest;

import com.Advocacia.Controller.ContatoController;
import com.Advocacia.Entity.Cliente;
import com.Advocacia.Entity.Contato;
import com.Advocacia.Entity.EstadoCivil;
import com.Advocacia.Repository.ContatoRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ContatoControllerTest {
    @MockBean
    ContatoRepository contatoRepository ;
    @Autowired
    ContatoController contatoController;
    @BeforeEach
    void setup() {
        Contato contato1 = new Contato();
        contato1.setId(1L);
        contato1.setMeioContato("Presencial");
        contato1.setNotasContato("AVAliacao");
        contato1.setDataUltimoContato(LocalDate.ofEpochDay(20/10/2024));
        contato1.setProximoPassos("agendado");

        Contato contato2 = new Contato();
        contato1.setId(2L);
        contato1.setMeioContato("Presencial");
        contato1.setNotasContato("AVAliacao");
        contato1.setDataUltimoContato(LocalDate.ofEpochDay(20/10/2024));
        contato1.setProximoPassos("agendado");


        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato1));

        List<Contato> lista = new ArrayList<>();
        lista.add(contato1);
        lista.add(contato2);
        when(contatoRepository.findAll()).thenReturn(lista);



    }
    //Teste criar cliente
    @Test
    void cenario1(){
        Contato contato1 = new Contato();
        ResponseEntity<Contato> retorno = this.contatoController.criarContato(contato1);
        assertEquals(HttpStatus.CREATED,retorno.getStatusCode());
    }
    @Test
    void cenario2(){
        Contato contato1 =new Contato();
        ResponseEntity<Contato> retorno = this.contatoController.atualizarContato(1L,contato1);
        assertEquals(HttpStatus.OK,retorno.getStatusCode());
    }

    @Test
    void cenario3(){
        Contato contato1 =new Contato();
        ResponseEntity<Contato> retorno = this.contatoController.atualizarContato(10L,contato1);
        assertEquals(HttpStatus.NOT_FOUND,retorno.getStatusCode());
    }

    @Test
    void cenario4(){
        Contato contato = new Contato();
        ResponseEntity<Contato> retorno = this.contatoController.buscarContatoPorId(1L);
        assertEquals(HttpStatus.OK,retorno.getStatusCode());
    }
//Teste
    @Test
    void cenario5(){
        Contato contato1 =new Contato();
        ResponseEntity<Contato> retorno = this.contatoController.buscarContatoPorId(10L);
        assertEquals(HttpStatus.NOT_FOUND,retorno.getStatusCode());
    }
//    @Test
//    void cenario6(){
//        ResponseEntity<Void> retorno = this.contatoController.deletarContato(1L);
//        assertEquals(HttpStatus.NO_CONTENT,retorno.getStatusCode());
//    }
    //Teste Deleta inexistente
    @Test
    void cenario7(){
        ResponseEntity<Void> retorno = this.contatoController.deletarContato(4L);
        assertEquals(HttpStatus.NOT_FOUND,retorno.getStatusCode());
    }
}
