package br.edu.ifce.emprestaai.model;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;


@Entity
@Table(name = "Emprestimo")
@Data
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emprestimo")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_solicitacao")
    private SolicitacaoEmprestimo solicitacaoEmprestimo;

    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "id_remetente")
    private User remetente;

    @ManyToOne
    @JoinColumn(name = "id_destinatario")
    private User destinatario;

    @ManyToOne
    @JoinColumn(name = "id_pagamento")
    private Pagamento pagamento;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "estado_pagamento")
    private StatusPagamento status = StatusPagamento.PENDENTE;

    @Enumerated(EnumType.ORDINAL)
    private FormaPagamento forma_pagamento;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_pagamento = new Date(System.currentTimeMillis());

}
