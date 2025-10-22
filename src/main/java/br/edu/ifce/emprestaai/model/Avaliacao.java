package br.edu.ifce.emprestaai.model;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "Avaliacao")
@Data
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_emprestimo")
    private Emprestimo emprestimo;


    private BigDecimal nota;

    private String comentario;

    private LocalDateTime data_avaliacao = LocalDateTime.now();

}
