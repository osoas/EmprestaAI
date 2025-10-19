package br.edu.ifce.emprestaai.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;



@Entity
@Table(name = "Endereco")
@Data
public class Endereco {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_endereco")
        private Integer id;

        @Column(nullable = false, length = 8)
        private String cep;

        @Column(length = 50)
        private String pais;

        @Column(length = 50)
        private String estado;

        @Column(length = 50)
        private String cidade;

        @Column(length = 50)
        private String municipio;

        @Column(length = 50)
        private String bairro;

        @Column(length = 100)
        private String rua;

        private Integer numeroDaCasa;

        @ManyToOne
        @JoinColumn(name = "id_usuario")
        @JsonBackReference
        private User usuario;





}
