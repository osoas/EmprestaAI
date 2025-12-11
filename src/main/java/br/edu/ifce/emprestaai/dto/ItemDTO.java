package br.edu.ifce.emprestaai.dto;

import br.edu.ifce.emprestaai.model.Categoria;

import java.math.BigDecimal;

public class ItemDTO {
    private Integer id;
    private String nome_item;
    private String descricao;
    private BigDecimal valor_unitario;
    private BigDecimal estrelas;
    private Double ratingAvg;
    private Integer ratingCount;
    private Categoria categoria_item;
    private Integer proprietarioId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome_item() { return nome_item; }
    public void setNome_item(String nome_item) { this.nome_item = nome_item; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor_unitario() { return valor_unitario; }
    public void setValor_unitario(BigDecimal valor_unitario) { this.valor_unitario = valor_unitario; }

    public BigDecimal getEstrelas() { return estrelas; }
    public void setEstrelas(BigDecimal estrelas) { this.estrelas = estrelas; }

    public Double getRatingAvg() { return ratingAvg; }
    public void setRatingAvg(Double ratingAvg) { this.ratingAvg = ratingAvg; }

    public Integer getRatingCount() { return ratingCount; }
    public void setRatingCount(Integer ratingCount) { this.ratingCount = ratingCount; }

    public Categoria getCategoria_item() { return categoria_item; }
    public void setCategoria_item(Categoria categoria_item) { this.categoria_item = categoria_item; }

    public Integer getProprietarioId() { return proprietarioId; }
    public void setProprietarioId(Integer proprietarioId) { this.proprietarioId = proprietarioId; }
}
