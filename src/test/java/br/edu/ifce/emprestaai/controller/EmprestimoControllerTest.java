package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Emprestimo;
import br.edu.ifce.emprestaai.repository.EmprestimoRepository;
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
class EmprestimoControllerTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @InjectMocks
    private EmprestimoController emprestimoController;

    private Emprestimo emprestimo;

    @BeforeEach
    void setUp() {
        emprestimo = new Emprestimo();
        emprestimo.setId(1);
        emprestimo.setData_inicio(Date.valueOf(LocalDateTime.now().toLocalDate()));
        emprestimo.setData_devolucao_prevista(Date.valueOf(LocalDateTime.now().plusDays(7).toLocalDate()));
    }

    @Test
    void getItens_DeveRetornarListaDeEmprestimos() {
        // Arrange
        List<Emprestimo> emprestimos = Arrays.asList(emprestimo);
        when(emprestimoRepository.findAll()).thenReturn(emprestimos);

        // Act
        List<Emprestimo> resultado = emprestimoController.getItens();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(emprestimo.getId(), resultado.get(0).getId());
        verify(emprestimoRepository).findAll();
    }

    @Test
    void getEmprestimo_QuandoExiste_DeveRetornarEmprestimo() {
        // Arrange
        when(emprestimoRepository.findById(1)).thenReturn(Optional.of(emprestimo));

        // Act
        Emprestimo resultado = emprestimoController.getEmprestimo(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(emprestimo.getId(), resultado.getId());
        assertEquals(emprestimo.getData_inicio(), resultado.getData_inicio());
        verify(emprestimoRepository).findById(1);
    }

    @Test
    void getEmprestimo_QuandoNaoExiste_DeveRetornarNull() {
        // Arrange
        when(emprestimoRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        Emprestimo resultado = emprestimoController.getEmprestimo(99);

        // Assert
        assertNull(resultado);
        verify(emprestimoRepository).findById(99);
    }

    @Test
    void cadastrarEmprestimo_DeveSalvarNovoEmprestimo() {
        // Arrange
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);

        // Act
        Emprestimo resultado = emprestimoController.cadastrarEmprestimo(emprestimo);

        // Assert
        assertNotNull(resultado);
        assertEquals(emprestimo.getData_inicio(), resultado.getData_inicio());
        verify(emprestimoRepository).save(emprestimo);
    }

    @Test
    void putEmprestimo_DeveAtualizarEmprestimo() {
        // Arrange
        Emprestimo emprestimoAtualizado = new Emprestimo();
        emprestimoAtualizado.setId(1);
        emprestimoAtualizado.setData_devolucao_prevista(Date.valueOf(LocalDateTime.now().plusDays(14).toLocalDate()));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimoAtualizado);

        // Act
        Emprestimo resultado = emprestimoController.putEmprestimo(emprestimoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(emprestimoAtualizado.getData_devolucao_prevista(), resultado.getData_devolucao_prevista());
        verify(emprestimoRepository).save(emprestimoAtualizado);
    }

    @Test
    void deleteEmprestimo_DeveDeletarEmprestimo() {
        // Arrange
        Integer id = 1;
        doNothing().when(emprestimoRepository).deleteById(id);

        // Act
        emprestimoController.deleteEmprestimo(id);

        // Assert
        verify(emprestimoRepository).deleteById(id);
    }
}
