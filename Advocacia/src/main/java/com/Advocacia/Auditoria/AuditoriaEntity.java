package com.Advocacia.Auditoria;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@Getter
@Setter
@RevisionEntity(CustomRevisionListener.class)
public class AuditoriaEntity extends DefaultRevisionEntity {
  private  String usuario;

}
