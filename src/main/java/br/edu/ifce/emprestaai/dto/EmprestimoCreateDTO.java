package br.edu.ifce.emprestaai.dto;

public class EmprestimoCreateDTO {
    private Integer itemId;
    private Integer destinatarioId;
    private String data_inicio;
    private String data_devolucao_prevista;

    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public Integer getDestinatarioId() { return destinatarioId; }
    public void setDestinatarioId(Integer destinatarioId) { this.destinatarioId = destinatarioId; }

    public String getData_inicio() { return data_inicio; }
    public void setData_inicio(String data_inicio) { this.data_inicio = data_inicio; }

    public String getData_devolucao_prevista() { return data_devolucao_prevista; }
    public void setData_devolucao_prevista(String data_devolucao_prevista) { this.data_devolucao_prevista = data_devolucao_prevista; }
}

