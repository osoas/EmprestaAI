package br.edu.ifce.emprestaai.model;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;


@Entity
@Table(name = "Solicitacao_emprestimo_item")
@Data
public class SolicitacaoEmprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitacao")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_solicitante")
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;

    @Enumerated(EnumType.ORDINAL)
    private StatusSolicitacao status = StatusSolicitacao.PENDENTE;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_solicitacao = new Date(System.currentTimeMillis());

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_inicio;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_fim;


}
