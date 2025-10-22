package br.edu.ifce.emprestaai.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


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

    private LocalDateTime data_solicitacao = LocalDateTime.now();

    private LocalDateTime data_inicio;

    private LocalDateTime data_fim;


}
