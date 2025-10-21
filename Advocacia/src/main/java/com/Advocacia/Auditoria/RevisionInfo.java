package com.Advocacia.Auditoria;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@Table(name = "REVINFO")
@RevisionEntity(CustomRevisionListener.class)
@Getter
@Setter
public class RevisionInfo extends DefaultRevisionEntity {
  private String usuario;
}

