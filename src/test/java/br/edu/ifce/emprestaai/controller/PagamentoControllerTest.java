package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Pagamento;
import br.edu.ifce.emprestaai.repository.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PagamentoControllerTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoController pagamentoController;

    private Pagamento pagamento;

    @BeforeEach
    void setUp() {
        pagamento = new Pagamento();
        pagamento.setId(1);
        pagamento.setData_pagamento(Date.valueOf(LocalDateTime.now().toLocalDate()));
        pagamento.setValor(BigDecimal.valueOf(100.00));
    }

    @Test
    void getPagamentos_DeveRetornarListaDePagamentos() {
        // Arrange
        List<Pagamento> pagamentos = Arrays.asList(pagamento);
        when(pagamentoRepository.findAll()).thenReturn(pagamentos);

        // Act
        List<Pagamento> resultado = pagamentoController.getPagamentos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(pagamento.getId(), resultado.get(0).getId());
        verify(pagamentoRepository).findAll();
    }

    @Test
    void getPagamento_QuandoExiste_DeveRetornarPagamento() {
        // Arrange
        when(pagamentoRepository.findById(1)).thenReturn(Optional.of(pagamento));

        // Act
        Pagamento resultado = pagamentoController.getPagamento(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(pagamento.getId(), resultado.getId());
        assertEquals(pagamento.getValor(), resultado.getValor());
        verify(pagamentoRepository).findById(1);
    }

    @Test
    void getPagamento_QuandoNaoExiste_DeveRetornarNull() {
        // Arrange
        when(pagamentoRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        Pagamento resultado = pagamentoController.getPagamento(99);

        // Assert
        assertNull(resultado);
        verify(pagamentoRepository).findById(99);
    }

    @Test
    void criarPagamento_DeveSalvarNovoPagamento() {
        // Arrange
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        // Act
        Pagamento resultado = pagamentoController.criarPagamento(pagamento);

        // Assert
        assertNotNull(resultado);
        assertEquals(pagamento.getValor(), resultado.getValor());
        assertEquals(pagamento.getData_pagamento(), resultado.getData_pagamento());
        verify(pagamentoRepository).save(pagamento);
    }

    @Test
    void putPagamento_DeveAtualizarPagamento() {
        // Arrange
        Pagamento pagamentoAtualizado = new Pagamento();
        pagamentoAtualizado.setId(1);
        pagamentoAtualizado.setValor(BigDecimal.valueOf(150.00));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoAtualizado);

        // Act
        Pagamento resultado = pagamentoController.putPagamento(pagamentoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(150.0, resultado.getValor());
        verify(pagamentoRepository).save(pagamentoAtualizado);
    }

    @Test
    void deletePagamento_DeveDeletarPagamento() {
        // Arrange
        Integer id = 1;
        doNothing().when(pagamentoRepository).deleteById(id);

        // Act
        pagamentoController.deletePagamento(id);

        // Assert
        verify(pagamentoRepository).deleteById(id);
    }
}
