package br.edu.ifce.emprestaai.repository;

import br.edu.ifce.emprestaai.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {
    List<Emprestimo> findBySolicitacaoEmprestimoUsuarioId(Integer usuarioId);

    List<Emprestimo> findByItemProprietarioId(Integer proprietarioId);
}
