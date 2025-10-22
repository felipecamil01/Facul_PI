import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, ValidatorFn, AbstractControl, FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { LoginService } from '../../../auth/login.service';
import { ClienteService } from '../../../services/cliente.service';
import { ViaCepService } from '../../../services/viacep.service';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, FormsModule],
  templateUrl: './cliente-form.component.html',
  styleUrls: ['./cliente-form.component.scss']
})
export class ClienteFormComponent implements OnInit {
  loginService = inject(LoginService);
  clienteForm!: FormGroup;
  idCliente!: number;
  modoEdicao = false;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private router: Router,
    private viaCepService: ViaCepService,
    private activatedRoute: ActivatedRoute
  ) {
    this.clienteForm = this.fb.group({
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', [Validators.required, this.cpfValidator()]],
      rg: ['', [Validators.required, this.rgValidator()]],
      profissao: ['', Validators.required],
      telefone: ['', [Validators.required, this.telefoneValidator()]],
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
    if (clienteId) {
      this.idCliente = Number(clienteId);
      this.modoEdicao = true;
      this.clienteService.findById(this.idCliente).subscribe(cliente => {
        this.clienteForm.patchValue({
          nome: cliente.nome,
          email: cliente.email,
          cpf: cliente.cpf,
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
    if (!this.clienteForm.valid) {
      Swal.fire('Atenção', 'Verifique os campos destacados antes de continuar.', 'warning');
      return;
    }

    const clienteBruto = this.clienteForm.value;
    const payload = {
      ...clienteBruto,
      cpf: this.apenasDigitos(clienteBruto.cpf),
      rg: this.normalizarRg(clienteBruto.rg),
      telefone: this.normalizarTelefone(clienteBruto.telefone)
    };

    const requisicao = this.idCliente
      ? this.clienteService.update(this.idCliente, payload)
      : this.clienteService.save(payload);

    requisicao.subscribe({
      next: () => {
        Swal.fire('Sucesso', `Cliente ${this.idCliente ? 'atualizado' : 'cadastrado'} com sucesso.`, 'success');
        const rota = this.loginService.hasPermission('ADMIN') ? 'admin/cliente' : 'user/cliente';
        this.router.navigate([rota]);
      },
      error: () => {
        Swal.fire('Erro', 'Não foi possível salvar os dados do cliente.', 'error');
      }
    });
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
            Swal.fire('Erro', 'Endereço não encontrado para o CEP informado.', 'error');
          }
        },
        () => Swal.fire('Erro', 'Não foi possível buscar o endereço. Verifique o CEP e tente novamente.', 'error')
      );
    } else {
      Swal.fire('Atenção', 'Por favor, informe um CEP válido.', 'warning');
    }
  }

  formatarCpf(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = this.apenasDigitos(input.value).slice(0, 11);
    let formatado = digits;
    if (digits.length > 3) {
      formatado = `${digits.slice(0, 3)}.${digits.slice(3)}`;
    }
    if (digits.length > 6) {
      formatado = `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6)}`;
    }
    if (digits.length > 9) {
      formatado = `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6, 9)}-${digits.slice(9, 11)}`;
    }
    input.value = formatado;
    this.clienteForm.get('cpf')?.setValue(formatado, { emitEvent: false });
  }

  formatarRg(event: Event): void {
    const input = event.target as HTMLInputElement;
    const valor = (input.value || '').toString().replace(/[^0-9A-Za-z]/g, '').toUpperCase();
    input.value = valor;
    this.clienteForm.get('rg')?.setValue(valor, { emitEvent: false });
  }

  formatarTelefone(event: Event): void {
    const input = event.target as HTMLInputElement;
    const texto = input.value || '';
    const possuiMais = texto.trim().startsWith('+');
    const digitos = texto.replace(/\D/g, '');

    let ddi = '';
    let restante = digitos;
    if (possuiMais && digitos.length > 2) {
      const tamanhoDdi = Math.min(3, Math.max(1, digitos.length - 8));
      ddi = digitos.slice(0, tamanhoDdi);
      restante = digitos.slice(tamanhoDdi);
    }

    const telefoneLocal = this.formatarTelefoneLocal(restante);
    const final = possuiMais && ddi
      ? `+${ddi}${telefoneLocal ? ' ' + telefoneLocal : ''}`
      : telefoneLocal || (possuiMais ? '+' + digitos : digitos);

    input.value = final.trim();
    this.clienteForm.get('telefone')?.setValue(final.trim(), { emitEvent: false });
  }

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }

  private cpfValidator(): ValidatorFn {
    return (control: AbstractControl) => {
      const digits = this.apenasDigitos(control.value);
      if (!digits) return null;
      return digits.length === 11 ? null : { cpfInvalido: true };
    };
  }

  private rgValidator(): ValidatorFn {
    return (control: AbstractControl) => {
      const valor = (control.value || '').toString().replace(/[^0-9A-Za-z]/g, '');
      if (!valor) return null;
      const digits = valor.replace(/\D/g, '');
      return digits.length >= 5 && digits.length <= 14 ? null : { rgInvalido: true };
    };
  }

  private telefoneValidator(): ValidatorFn {
    return (control: AbstractControl) => {
      const valor = (control.value || '').toString();
      const digits = valor.replace(/\D/g, '');
      return digits.length >= 8 && digits.length <= 15 ? null : { telefoneInvalido: true };
    };
  }

  private apenasDigitos(valor: string): string {
    return (valor || '').toString().replace(/\D/g, '');
  }

  private normalizarRg(valor: string): string {
    return (valor || '').toString().replace(/[^0-9A-Za-z]/g, '').toUpperCase();
  }

  private normalizarTelefone(valor: string): string {
    if (!valor) return '';
    const trimmed = valor.trim();
    const digits = this.apenasDigitos(trimmed);
    return trimmed.startsWith('+') ? `+${digits}` : digits;
  }

  private formatarTelefoneLocal(digitos: string): string {
    if (!digitos) return '';
    if (digitos.length <= 2) {
      return digitos;
    }
    const ddd = digitos.slice(0, 2);
    const restante = digitos.slice(2);
    if (!restante) {
      return `(${ddd}`;
    }
    if (restante.length <= 4) {
      return `(${ddd}) ${restante}`;
    }
    const parte1 = restante.slice(0, restante.length - 4);
    const parte2 = restante.slice(-4);
    return `(${ddd}) ${parte1}-${parte2}`;
  }
}
