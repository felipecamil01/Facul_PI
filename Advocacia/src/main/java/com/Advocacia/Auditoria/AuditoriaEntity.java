package com.Advocacia.Auditoria;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AuditoriaEntity<U> {

  @CreatedBy
  @Column(updatable = false)
  protected U criadoPor;

  @CreatedDate
  @Column(updatable = false)
  protected LocalDateTime dataCriacao;

  @LastModifiedBy
  protected U ultimaModificacaoPor;

  @LastModifiedDate
  protected LocalDateTime dataUltimaModificacao;
}
