import { Component, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClienteService } from '../../services/cliente.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-cliente-editar',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './cliente-editar.component.html',
  styleUrl: './cliente-editar.component.scss'
})

export class ClienteEditarComponent implements OnInit {

  idCliente!:number;
  cliente: any = {
    nome: '',
    email: '',
    cpf: '',
    rg: '',
    profissao: '',
    telefone: '',
    dataNascimento: '',
    estadoCivil: '',
    endereco: {
      logradouro: '',
      numero: '',
      complemento: '',
      bairro: '',
      cidade: '',
      uf: '',
      cep: ''
    }
  };


  constructor(
    private route:ActivatedRoute,
    private clienteService:ClienteService,
    private router:Router

  ){};

  ngOnInit(): void {
    this.idCliente = Number(this.route.snapshot.paramMap.get('id'));
    this.clienteService.findById(this.idCliente).subscribe((data)=>{this.cliente = data;});
    console.log(this.cliente);
  }
  
  onSubmit(): void {
    
      this.clienteService.update(this.idCliente,this.cliente).subscribe({
        next: (response) => {
          Swal.fire({
            title:'Atualizado com sucesso',
            icon:'success',
            confirmButtonText:'OK'
          })
          this.router.navigate(['principal/clientes']);
        },
        error: erro =>{
          Swal.fire({
            title:'Erro ao Atualizar',
            icon: 'error',
            confirmButtonText:'OK'
          })
        }
      });
    
  }



}
