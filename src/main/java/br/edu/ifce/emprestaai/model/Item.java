package br.edu.ifce.emprestaai.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "Item")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_proprietario")
    private User proprietario;

    @Column(length = 100)
    private String nome_item;

    private String descricao;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor_unitario;

    @Column(precision = 2, scale = 1)
    private BigDecimal estrelas = BigDecimal.valueOf(5.0);

    @Enumerated(EnumType.ORDINAL)
    private StatusDisponibilidade status_disponibilidade = StatusDisponibilidade.DISPONIVEL;

    private LocalDateTime data_cadastro = LocalDateTime.now();
}
