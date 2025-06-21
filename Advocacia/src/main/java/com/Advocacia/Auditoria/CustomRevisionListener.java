package com.Advocacia.Auditoria;

import org.hibernate.envers.RevisionListener;

public class CustomRevisionListener implements RevisionListener {
  @Override
  public void newRevision(Object revisionEntity) {
    AuditoriaEntity rev = (AuditoriaEntity) revisionEntity;
    rev.setUsuario(Seguranca.getUsuarioAtual());
  }
}
