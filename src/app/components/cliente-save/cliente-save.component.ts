import { Component, OnInit } from '@angular/core';
import { ClienteService } from '../../services/cliente.service';
import { FormBuilder, FormGroup, Validators,ReactiveFormsModule } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { ViaCepService } from '../../services/viacep.service';



@Component({
  selector: 'app-cliente-save',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './cliente-save.component.html',
  styleUrl: './cliente-save.component.scss'
})
export class ClienteSaveComponent implements OnInit{
  clienteForm!: FormGroup;

  constructor(private fb: FormBuilder, private clienteService: ClienteService,private route:Router, private viaCepService: ViaCepService) {}

  ngOnInit(): void {
    this.clienteForm = this.fb.group({
      nome: [''],
      email: [''],
      cpf: [''],
      rg: [''],
      profissao: [''],
      telefone: [''],
      dataNascimento: [''],
      estadoCivil: [''],
      endereco: this.fb.group({
        logradouro: [''],
        numero: [''],
        complemento: [''],
        bairro: [''],
        cidade: [''],
        uf: [''],
        cep: ['']
      })
    });
  }

  onSubmit(): void {
    if (this.clienteForm.valid) {
      this.clienteService.save(this.clienteForm.value).subscribe({
        next: (response) => {
          Swal.fire({
            title:'Cadastrado com sucesso',
            icon:'success',
            confirmButtonText:'OK'
          })
          this.route.navigate(['principal/clientes']);
        },
        error: erro =>{
          Swal.fire({
            title:'Erro ao salvar',
            icon: 'error',
            confirmButtonText:'OK'
          })
        }
      });
    }
  }
  buscarEndereco() {
    const cep = this.clienteForm.get('endereco.cep')?.value; // Corrigido para acessar o cep aninhado
    if (cep) {
      this.viaCepService.buscarEndereco(cep).subscribe(
        (data) => {
          if (data) {
            // Preencher os campos de endereço com os dados retornados
            this.clienteForm.patchValue({
              endereco: { // Adicionei o prefixo endereco
                logradouro: data.logradouro,
                bairro: data.bairro,
                cidade: data.localidade,
                uf: data.uf
              }
            });
          } else {
            Swal.fire({
              title: 'Erro',
              text: 'Endereço não encontrado para o CEP informado.',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          }
        },
        (error) => {
          Swal.fire({
            title: 'Erro',
            text: 'Erro ao buscar endereço. Verifique o CEP e tente novamente.',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      );
    } else {
      Swal.fire({
        title: 'Atenção',
        text: 'Por favor, insira um CEP válido.',
        icon: 'warning',
        confirmButtonText: 'OK'
      });
    }
  }
  
}
