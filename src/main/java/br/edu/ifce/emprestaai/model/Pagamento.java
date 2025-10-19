package br.edu.ifce.emprestaai.model;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;


@Entity
@Table(name = "Pagamento")
@Data
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_emprestimo")
    private Emprestimo emprestimo;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User usuario;

    @Enumerated(EnumType.ORDINAL)
    private StatusEmprestimo statusPagamento = StatusEmprestimo.PENDENTE;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_pagamento = new Date(System.currentTimeMillis());

}
