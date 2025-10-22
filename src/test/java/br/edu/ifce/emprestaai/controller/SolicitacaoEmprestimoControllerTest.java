package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Emprestimo;
import br.edu.ifce.emprestaai.model.SolicitacaoEmprestimo;
import br.edu.ifce.emprestaai.model.StatusSolicitacao;
import br.edu.ifce.emprestaai.repository.EmprestimoRepository;
import br.edu.ifce.emprestaai.repository.SolicitacaoEmprestimoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class SolicitacaoEmprestimoControllerTest {

    @Mock
    private SolicitacaoEmprestimoRepository solicitacaoEmprestimoRepository;

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @InjectMocks
    private SolicitacaoEmprestimoController solicitacaoEmprestimoController;

    private SolicitacaoEmprestimo solicitacao;

    @BeforeEach
    void setUp() {
        solicitacao = new SolicitacaoEmprestimo();
        solicitacao.setId(1);
        solicitacao.setData_inicio(Date.valueOf(LocalDateTime.now().toLocalDate()));
        solicitacao.setData_fim(Date.valueOf(LocalDateTime.now().plusDays(7).toLocalDate()));
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
    }

    @Test
    void getSolicitacoes_DeveRetornarListaDeSolicitacoes() {
        // Arrange
        List<SolicitacaoEmprestimo> solicitacoes = Arrays.asList(solicitacao);
        when(solicitacaoEmprestimoRepository.findAll()).thenReturn(solicitacoes);

        // Act
        List<SolicitacaoEmprestimo> resultado = solicitacaoEmprestimoController.getSolicitacoes();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(solicitacao.getId(), resultado.get(0).getId());
        verify(solicitacaoEmprestimoRepository).findAll();
    }

    @Test
    void getSolicitacoesUser_DeveRetornarSolicitacoesDoUsuario() {
        // Arrange
        List<SolicitacaoEmprestimo> solicitacoes = Arrays.asList(solicitacao);
        when(solicitacaoEmprestimoRepository.findByUser(1)).thenReturn(solicitacoes);

        // Act
        List<SolicitacaoEmprestimo> resultado = solicitacaoEmprestimoController.getSolicitacoesUser(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(solicitacaoEmprestimoRepository).findByUser(1);
    }

    @Test
    void getSolicitacao_QuandoExiste_DeveRetornarSolicitacao() {
        // Arrange
        when(solicitacaoEmprestimoRepository.findById(1)).thenReturn(Optional.of(solicitacao));

        // Act
        SolicitacaoEmprestimo resultado = solicitacaoEmprestimoController.getSolicitacao(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(solicitacao.getId(), resultado.getId());
        assertEquals(solicitacao.getStatus(), resultado.getStatus());
        verify(solicitacaoEmprestimoRepository).findById(1);
    }

    @Test
    void getSolicitacao_QuandoNaoExiste_DeveRetornarNull() {
        // Arrange
        when(solicitacaoEmprestimoRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        SolicitacaoEmprestimo resultado = solicitacaoEmprestimoController.getSolicitacao(99);

        // Assert
        assertNull(resultado);
        verify(solicitacaoEmprestimoRepository).findById(99);
    }

    @Test
    void postSolicitacao_DeveSalvarNovaSolicitacao() {
        // Arrange
        when(solicitacaoEmprestimoRepository.save(any(SolicitacaoEmprestimo.class))).thenReturn(solicitacao);

        // Act
        SolicitacaoEmprestimo resultado = solicitacaoEmprestimoController.psotSolicitacao(solicitacao);

        // Assert
        assertNotNull(resultado);
        assertEquals(solicitacao.getData_inicio(), resultado.getData_inicio());
        verify(solicitacaoEmprestimoRepository).save(solicitacao);
    }

    @Test
    void mudarStatusSolicitacao_ParaAprovado_DeveCriarEmprestimo() {
        // Arrange
        when(solicitacaoEmprestimoRepository.findById(1)).thenReturn(Optional.of(solicitacao));
        when(solicitacaoEmprestimoRepository.save(any(SolicitacaoEmprestimo.class))).thenReturn(solicitacao);
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(new Emprestimo());

        // Act
        SolicitacaoEmprestimo resultado = solicitacaoEmprestimoController.mudarStatusSolicitacao(1, StatusSolicitacao.APROVADO);

        // Assert
        assertNotNull(resultado);
        assertEquals(StatusSolicitacao.APROVADO, resultado.getStatus());
        verify(emprestimoRepository).save(any(Emprestimo.class));
        verify(solicitacaoEmprestimoRepository).save(any(SolicitacaoEmprestimo.class));
    }

    @Test
    void mudarStatusSolicitacao_ParaRecusado_NaoDeveCriarEmprestimo() {
        // Arrange
        when(solicitacaoEmprestimoRepository.findById(1)).thenReturn(Optional.of(solicitacao));
        when(solicitacaoEmprestimoRepository.save(any(SolicitacaoEmprestimo.class))).thenReturn(solicitacao);

        // Act
        SolicitacaoEmprestimo resultado = solicitacaoEmprestimoController.mudarStatusSolicitacao(1, StatusSolicitacao.RECUSADO);

        // Assert
        assertNotNull(resultado);
        assertEquals(StatusSolicitacao.RECUSADO, resultado.getStatus());
        verify(emprestimoRepository, never()).save(any(Emprestimo.class));
        verify(solicitacaoEmprestimoRepository).save(any(SolicitacaoEmprestimo.class));
    }
}
