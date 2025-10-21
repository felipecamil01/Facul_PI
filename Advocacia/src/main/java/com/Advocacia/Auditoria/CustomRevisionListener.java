package com.Advocacia.Auditoria;

import org.hibernate.envers.RevisionListener;

public class CustomRevisionListener implements RevisionListener {
  @Override
  public void newRevision(Object revisionEntity) {
    RevisionInfo rev = (RevisionInfo) revisionEntity;
    rev.setUsuario(Seguranca.getUsuarioAtual());
  }
}
