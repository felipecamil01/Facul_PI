package com.Advocacia.Entity;

public enum OrgaoExpedidor {

    SSP_AC("SSP/AC", "Secretaria de Segurança Pública do Acre"),
    SSP_AL("SSP/AL", "Secretaria de Segurança Pública de Alagoas"),
    SSP_AP("SSP/AP", "Secretaria de Segurança Pública do Amapá"),
    SSP_AM("SSP/AM", "Secretaria de Segurança Pública do Amazonas"),
    SSP_BA("SSP/BA", "Secretaria de Segurança Pública da Bahia"),
    SSP_CE("SSP/CE", "Secretaria de Segurança Pública do Ceará"),
    SSP_DF("SSP/DF", "Secretaria de Segurança Pública do Distrito Federal"),
    SSP_ES("SSP/ES", "Secretaria de Segurança Pública do Espírito Santo"),
    SSP_GO("SSP/GO", "Secretaria de Segurança Pública de Goiás"),
    SSP_MA("SSP/MA", "Secretaria de Segurança Pública do Maranhão"),
    SSP_MT("SSP/MT", "Secretaria de Segurança Pública do Mato Grosso"),
    SSP_MS("SSP/MS", "Secretaria de Segurança Pública do Mato Grosso do Sul"),
    SSP_MG("SSP/MG", "Secretaria de Segurança Pública de Minas Gerais"),
    SSP_PA("SSP/PA", "Secretaria de Segurança Pública do Pará"),
    SSP_PB("SSP/PB", "Secretaria de Segurança Pública da Paraíba"),
    SSP_PR("SSP/PR", "Secretaria de Segurança Pública do Paraná"),
    SSP_PE("SSP/PE", "Secretaria de Segurança Pública de Pernambuco"),
    SSP_PI("SSP/PI", "Secretaria de Segurança Pública do Piauí"),
    SSP_RJ("SSP/RJ", "Secretaria de Segurança Pública do Rio de Janeiro"),
    SSP_RN("SSP/RN", "Secretaria de Segurança Pública do Rio Grande do Norte"),
    SSP_RS("SSP/RS", "Secretaria de Segurança Pública do Rio Grande do Sul"),
    SSP_RO("SSP/RO", "Secretaria de Segurança Pública de Rondônia"),
    SSP_RR("SSP/RR", "Secretaria de Segurança Pública de Roraima"),
    SSP_SC("SSP/SC", "Secretaria de Segurança Pública de Santa Catarina"),
    SSP_SP("SSP/SP", "Secretaria de Segurança Pública de São Paulo"),
    SSP_SE("SSP/SE", "Secretaria de Segurança Pública de Sergipe"),
    SSP_TO("SSP/TO", "Secretaria de Segurança Pública do Tocantins"),

    // Órgãos federais e conselhos profissionais
    DETRAN("DETRAN", "Departamento Estadual de Trânsito"),
    POLICIA_FEDERAL("DPF", "Departamento de Polícia Federal"),
    OAB("OAB", "Ordem dos Advogados do Brasil"),
    CRM("CRM", "Conselho Regional de Medicina"),
    CREA("CREA", "Conselho Regional de Engenharia e Agronomia"),
    CREFITO("CREFITO", "Conselho Regional de Fisioterapia e Terapia Ocupacional"),
    CRP("CRP", "Conselho Regional de Psicologia"),
    CRC("CRC", "Conselho Regional de Contabilidade"),
    CRF("CRF", "Conselho Regional de Farmácia"),
    CRO("CRO", "Conselho Regional de Odontologia"),
    CRBIO("CRBIO", "Conselho Regional de Biologia"),
    CFB("CFB", "Conselho Federal de Biblioteconomia"),
    CFM("CFM", "Conselho Federal de Medicina"),
    CFMV("CFMV", "Conselho Federal de Medicina Veterinária"),
    CFF("CFF", "Conselho Federal de Farmácia"),
    CFC("CFC", "Conselho Federal de Contabilidade"),
    CFBIO("CFBIO", "Conselho Federal de Biologia"),
    CFBM("CFBM", "Conselho Federal de Biomedicina"),
    CFFA("CFFA", "Conselho Federal de Fonoaudiologia"),
    COFECI("COFECI", "Conselho Federal de Corretores de Imóveis"),
    COFEN("COFEN", "Conselho Federal de Enfermagem"),
    COFFITO("COFFITO", "Conselho Federal de Fisioterapia e Terapia Ocupacional"),
    CONFEA("CONFEA", "Conselho Federal de Engenharia e Agronomia"),
    CONFEF("CONFEF", "Conselho Federal de Educação Física"),
    CONRERP("CONRERP", "Conselho Regional de Profissionais de Relações Públicas"),
    CRA("CRA", "Conselho Regional de Administração"),
    CRAS("CRAS", "Centro de Referência de Assistência Social"),
    CRBM("CRBM", "Conselho Regional de Biomedicina"),
    CRB("CRB", "Conselho Regional de Biblioteconomia"),
    CRN("CRN", "Conselho Regional de Nutrição"),
    CRQ("CRQ", "Conselho Regional de Química"),
    CRT("CRT", "Conselho Regional dos Técnicos Industriais"),
    CFT("CFT", "Conselho Federal dos Técnicos Industriais"),
    CFTA("CFTA", "Conselho Federal dos Técnicos Agrícolas"),
    CGPI("CGPI", "Coordenação Geral de Privilégios e Imunidades"),
    CGPMAF("CGPMAF", "Coordenadoria Geral de Polícia Marítima, Aeronáutica e de Fronteiras"),
    CIPC("CIPC", "Centro de Inteligência da Polícia Civil"),
    CNIG("CNIG", "Conselho Nacional de Imigração"),
    CNT("CNT", "Confederação Nacional do Transporte"),
    CNTV("CNTV", "Confederação Nacional de Vigilantes & Prestadores de Serviços"),
    COFECON("COFECON", "Conselho Federal de Economia"),
    COFEM("COFEM", "Conselho Federal de Museologia"),
    COMAER("COMAER", "Comando da Aeronáutica"),
    CONFE("CONFE", "Conselho Federal de Estatística"),
  CRESS("CRESS", "Conselho Regional de Serviço Social"),
  CTF("CTF", "Conselho dos Técnicos em Farmácia"),
  DPF("DPF", "Departamento de Polícia Federal"),
  EXERCITO("EX", "Exército Brasileiro"),
  MARINHA("MB", "Marinha do Brasil"),
  AERONAUTICA("FAB", "Força Aérea Brasileira"),
  INSS("INSS", "Instituto Nacional do Seguro Social"),
  IFP("IFP", "Instituto Félix Pacheco"),
  IML("IML", "Instituto Médico Legal"),
  MTE("MTE", "Ministério do Trabalho e Emprego"),
  MINISTERIO_DA_DEFESA("MD", "Ministério da Defesa"),
  MJ("MJ", "Ministério da Justiça"),
  MINISTERIO_PUBLICO("MP", "Ministério Público"),
  OMB("OMB", "Ordem dos Músicos do Brasil"),
  PREFEITURA("PREF", "Prefeitura Municipal"),
  POLICIA_CIVIL("PC", "Polícia Civil"),
  POLICIA_MILITAR("PM", "Polícia Militar"),
  POLICIA_RODOVIARIA("PRF", "Polícia Rodoviária Federal"),
  SEFAZ("SEFAZ", "Secretaria da Fazenda"),
  SEDUC("SEDUC", "Secretaria de Educação"),
  SES("SES", "Secretaria de Estado da Saúde"),
  TRE("TRE", "Tribunal Regional Eleitoral"),
  TRT("TRT", "Tribunal Regional do Trabalho"),
  TRF("TRF", "Tribunal Regional Federal"),
  TSE("TSE", "Tribunal Superior Eleitoral"),
  TST("TST", "Tribunal Superior do Trabalho"),
  STF("STF", "Supremo Tribunal Federal"),
  OUTROS("OUTROS", "Outro órgão emissor");
  private final String sigla;
  private final String descricao;

  OrgaoExpedidor(String sigla, String descricao) {
    this.sigla = sigla;
    this.descricao = descricao;
  }

  public String getSigla() {
    return sigla;
  }

  public String getDescricao() {
    return descricao;
  }

  @Override
  public String toString() {
    return sigla;
  }

}

