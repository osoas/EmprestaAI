package br.edu.ifce.emprestaai.controller;

import br.edu.ifce.emprestaai.model.Endereco;
import br.edu.ifce.emprestaai.model.User;
import br.edu.ifce.emprestaai.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setNome("User Test");
        user.setEmail("test@test.com");
        user.setCpf("12345678901");
        user.setEnderecos(new ArrayList<>());

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getUsers_DeveRetornarListaDeUsuarios() {
        // Arrange
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> resultado = userController.getUsers();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(user.getNome(), resultado.get(0).getNome());
        verify(userRepository).findAll();
    }

    @Test
    void getUser_QuandoExiste_DeveRetornarUsuario() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        User resultado = userController.getUser(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(user.getId(), resultado.getId());
        assertEquals(user.getEmail(), resultado.getEmail());
        verify(userRepository).findById(1);
    }

    @Test
    void getUser_QuandoNaoExiste_DeveRetornarNull() {
        // Arrange
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        User resultado = userController.getUser(99);

        // Assert
        assertNull(resultado);
        verify(userRepository).findById(99);
    }

    @Test
    void cadastrarUser_DeveSalvarNovoUsuario() {
        // Arrange
        Endereco endereco = new Endereco();
        endereco.setRua("Rua Teste");
        user.getEnderecos().add(endereco);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<User> response = userController.cadastrarUser(user);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getNome(), response.getBody().getNome());
        verify(userRepository).save(any(User.class));

        // Verify endereco is properly linked to user
        assertTrue(response.getBody().getEnderecos().stream()
                .allMatch(end -> end.getUsuario() == response.getBody()));
    }
}
