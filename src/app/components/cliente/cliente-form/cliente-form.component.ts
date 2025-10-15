import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ClienteService } from '../../../services/cliente.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, NgForm, FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { ViaCepService } from '../../../services/viacep.service';
import { LoginService } from '../../../auth/login.service';
import { OrgaoExpedidor } from '../../../models/EmissorEmissor.enum';
import { CommonModule, NgFor } from '@angular/common';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule,RouterModule,FormsModule,NgFor],
  templateUrl: './cliente-form.component.html',
  styleUrls: ['./cliente-form.component.scss']
})
export class ClienteFormComponent implements OnInit {
  
  loginService = inject(LoginService);
  orgaosAgrupados: { label: string; orgaos: { sigla: string; descricao: string; }[] }[] = [];
  clienteForm!: FormGroup;
  idCliente!: number;
  modoEdicao: boolean = false;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private route: Router,
    private viaCepService: ViaCepService,
    private activatedRoute: ActivatedRoute,
  ) {
    this.clienteForm = this.fb.group({
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', [Validators.required]],
      orgaoExpedidor:[''],
      rg: ['', [Validators.required]],
      profissao: ['', Validators.required],
      telefone: ['', Validators.required],
      dataNascimento: ['', Validators.required],
      estadoCivil: ['', Validators.required],
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

  ngOnInit(): void {
    const clienteId = this.activatedRoute.snapshot.paramMap.get('id');
    this.orgaosAgrupados= this.gerarOrgaosAgrupados();
    if (clienteId) {
      this.orgaosAgrupados = this.gerarOrgaosAgrupados();
      this.idCliente = Number(clienteId);
      this.modoEdicao = true;
      this.clienteService.findById(this.idCliente).subscribe(cliente => {
        this.clienteForm.patchValue({
          nome: cliente.nome,
          email: cliente.email,
          cpf: cliente.cpf,
          orgaoExpedidor:cliente.orgaoEmissor,
          rg: cliente.rg,
          profissao: cliente.profissao,
          telefone: cliente.telefone,
          dataNascimento: cliente.dataNascimento,
          estadoCivil: cliente.estadoCivil,
          endereco: {
            logradouro: cliente.endereco.logradouro,
            numero: cliente.endereco.numero,
            complemento: cliente.endereco.complemento,
            bairro: cliente.endereco.bairro,
            cidade: cliente.endereco.cidade,
            uf: cliente.endereco.uf,
            cep: cliente.endereco.cep
          }
        });
      });
    }
  }

  onSubmit(): void {
    if (this.clienteForm.valid) {
      const clienteData = this.clienteForm.value;
      if (this.idCliente) {
        this.clienteService.update(this.idCliente, clienteData).subscribe({
          next: () => {
            Swal.fire({
              title: 'Atualizado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            if(this.loginService.hasPermission("ADMIN")){
              this.route.navigate(['admin/cliente']);
            }else{
              this.route.navigate(['user/cliente']);
            }
          },
          error: (error) => {
            Swal.fire({
              title: 'Erro ao Atualizar',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          }
        });
      } else {
        this.clienteService.save(clienteData).subscribe({
          next: () => {
            Swal.fire({
              title: 'Cadastrado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            if(this.loginService.hasPermission("ADMIN")){
              this.route.navigate(['admin/cliente']);
            }else{
              this.route.navigate(['user/cliente']);
            }
          },
          error: (error) => {
            Swal.fire({
              title: 'Erro ao salvar',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          }
        });
      }
    }
  }

  buscarEndereco(): void {
    const cep = this.clienteForm.get('endereco.cep')?.value;
    if (cep) {
      this.viaCepService.buscarEndereco(cep).subscribe(
        (data) => {
          if (data) {
            this.clienteForm.patchValue({
              endereco: {
                logradouro: data.logradouro,
                bairro: data.bairro,
                cidade: data.localidade,
                uf: data.uf
              }
            });

            const paisField = document.getElementById('pais') as HTMLInputElement;
            if (paisField) {
              paisField.value = 'Brasil';
            }

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

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }
  private gerarOrgaosAgrupados() {
  return [
    {
      label: 'Forças Armadas e Polícia Federal',
      orgaos: [
        // CORRIGIDO: Usando o nome do Enum como 'sigla'
        { sigla: 'EXERCITO', descricao: 'Exército Brasileiro' },
        { sigla: 'MARINHA', descricao: 'Marinha do Brasil' },
        { sigla: 'AERONAUTICA', descricao: 'Força Aérea Brasileira' },
        { sigla: 'POLICIA_FEDERAL', descricao: 'Departamento de Polícia Federal' },
        { sigla: 'POLICIA_RODOVIARIA', descricao: 'Polícia Rodoviária Federal' }
      ]
    },
    {
      label: 'Conselhos Profissionais e Outros',
      orgaos: [
        // CORRIGIDO: Usando o nome do Enum como 'sigla'
        { sigla: 'OAB', descricao: 'Ordem dos Advogados do Brasil' },
        { sigla: 'CRM', descricao: 'Conselho Regional de Medicina' },
        { sigla: 'IFP', descricao: 'Instituto Félix Pacheco' },
        // Adicione outros conselhos aqui se necessário, sempre usando o nome do Enum
        // Ex: { sigla: 'CREA', descricao: 'Conselho Regional de Engenharia e Agronomia' }
      ]
    },
    {
      label: 'Secretarias de Segurança Pública (SSP)',
      orgaos: [
        // CORRIGIDO: Usando o nome do Enum com underline
        { sigla: 'SSP_AC', descricao: 'SSP - Acre' },
        { sigla: 'SSP_AL', descricao: 'SSP - Alagoas' },
        { sigla: 'SSP_AP', descricao: 'SSP - Amapá' },
        { sigla: 'SSP_AM', descricao: 'SSP - Amazonas' },
        { sigla: 'SSP_BA', descricao: 'SSP - Bahia' },
        { sigla: 'SSP_CE', descricao: 'SSP - Ceará' },
        { sigla: 'SSP_DF', descricao: 'SSP - Distrito Federal' },
        { sigla: 'SSP_ES', descricao: 'SSP - Espírito Santo' },
        { sigla: 'SSP_GO', descricao: 'SSP - Goiás' },
        { sigla: 'SSP_MA', descricao: 'SSP - Maranhão' },
        { sigla: 'SSP_MT', descricao: 'SSP - Mato Grosso' },
        { sigla: 'SSP_MS', descricao: 'SSP - Mato Grosso do Sul' },
        { sigla: 'SSP_MG', descricao: 'SSP - Minas Gerais' },
        { sigla: 'SSP_PA', descricao: 'SSP - Pará' },
        { sigla: 'SSP_PB', descricao: 'SSP - Paraíba' },
        { sigla: 'SSP_PR', descricao: 'SSP - Paraná' },
        { sigla: 'SSP_PE', descricao: 'SSP - Pernambuco' },
        { sigla: 'SSP_PI', descricao: 'SSP - Piauí' },
        { sigla: 'SSP_RJ', descricao: 'SSP - Rio de Janeiro' },
        { sigla: 'SSP_RN', descricao: 'SSP - Rio Grande do Norte' },
        { sigla: 'SSP_RS', descricao: 'SSP - Rio Grande do Sul' },
        { sigla: 'SSP_RO', descricao: 'SSP - Rondônia' },
        { sigla: 'SSP_RR', descricao: 'SSP - Roraima' },
        { sigla: 'SSP_SC', descricao: 'SSP - Santa Catarina' },
        { sigla: 'SSP_SP', descricao: 'SSP - São Paulo' },
        { sigla: 'SSP_SE', descricao: 'SSP - Sergipe' },
        { sigla: 'SSP_TO', descricao: 'SSP - Tocantins' },
      ]
    },
    {
      label: 'Outros',
      orgaos: [
        { sigla: 'OUTROS', descricao: 'Outro órgão emissor' }
      ]
    }
  ];
}
}
