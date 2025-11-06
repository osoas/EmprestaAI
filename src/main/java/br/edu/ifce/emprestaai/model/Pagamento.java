package br.edu.ifce.emprestaai.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "Pagamento")
@Data
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento")
    private Integer id;

    @JoinColumn(name = "id_emprestimo")
    @OneToOne
    @JsonBackReference
    private Emprestimo emprestimo;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private User usuario;

    @Enumerated(EnumType.ORDINAL)
    private StatusEmprestimo statusPagamento = StatusEmprestimo.PENDENTE;

    private LocalDateTime data_pagamento = LocalDateTime.now();

    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

}
