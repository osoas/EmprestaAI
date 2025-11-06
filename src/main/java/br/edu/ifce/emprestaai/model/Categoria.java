package br.edu.ifce.emprestaai.model;
import jakarta.persistence.*;
import lombok.Data;



@Entity
@Table(name = "categoria_item")
@Data
public class Categoria {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer id;

    private String nome_categoria;



}
