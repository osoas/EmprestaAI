package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Avaliacao;
import br.edu.ifce.emprestaai.repository.AvaliacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AvaliacaoControllerTest {

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @InjectMocks
    private AvaliacaoController avaliacaoController;

    private Avaliacao avaliacao;

    @BeforeEach
    void setUp() {
        avaliacao = new Avaliacao();
        avaliacao.setId(1);
        avaliacao.setNota(BigDecimal.valueOf(5.00));
        avaliacao.setComentario("Ótimo serviço");
    }

    @Test
    void getAvaliacoes_DeveRetornarListaDeAvaliacoes() {
        // Arrange
        List<Avaliacao> avaliacoes = Arrays.asList(avaliacao);
        when(avaliacaoRepository.findAll()).thenReturn(avaliacoes);

        // Act
        List<Avaliacao> resultado = avaliacaoController.getAvaliacoes();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(avaliacao.getNota(), resultado.get(0).getNota());
        verify(avaliacaoRepository).findAll();
    }

    @Test
    void getAvaliacao_QuandoExiste_DeveRetornarAvaliacao() {
        // Arrange
        when(avaliacaoRepository.findById(1)).thenReturn(Optional.of(avaliacao));

        // Act
        Avaliacao resultado = avaliacaoController.getAvaliacao(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(avaliacao.getId(), resultado.getId());
        assertEquals(avaliacao.getNota(), resultado.getNota());
        verify(avaliacaoRepository).findById(1);
    }

    @Test
    void getAvaliacao_QuandoNaoExiste_DeveRetornarNull() {
        // Arrange
        when(avaliacaoRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        Avaliacao resultado = avaliacaoController.getAvaliacao(99);

        // Assert
        assertNull(resultado);
        verify(avaliacaoRepository).findById(99);
    }

    @Test
    void cadastrarAvaliacao_DeveSalvarNovaAvaliacao() {
        // Arrange
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        // Act
        Avaliacao resultado = avaliacaoController.cadastrarAvaliacao(avaliacao);

        // Assert
        assertNotNull(resultado);
        assertEquals(avaliacao.getNota(), resultado.getNota());
        verify(avaliacaoRepository).save(avaliacao);
    }

    @Test
    void putAvaliacao_DeveAtualizarAvaliacao() {
        // Arrange
        Avaliacao avaliacaoAtualizada = new Avaliacao();
        avaliacaoAtualizada.setId(1);
        avaliacaoAtualizada.setNota(BigDecimal.valueOf(4.00));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacaoAtualizada);

        // Act
        Avaliacao resultado = avaliacaoController.putAvaliacao(avaliacaoAtualizada);

        // Assert
        assertNotNull(resultado);
        assertEquals(4, resultado.getNota());
        verify(avaliacaoRepository).save(avaliacaoAtualizada);
    }

    @Test
    void deleteAvaliacao_DeveDeletarAvaliacao() {
        // Arrange
        Integer id = 1;
        doNothing().when(avaliacaoRepository).deleteById(id);

        // Act
        avaliacaoController.deleteAvaliacao(id);

        // Assert
        verify(avaliacaoRepository).deleteById(id);
    }
}
