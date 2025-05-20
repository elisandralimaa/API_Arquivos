package com.leitor_de_arquivor.leitor_de_arquivos.repository;

import com.leitor_de_arquivor.leitor_de_arquivos.model.ApresentacaoDashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApresentacaoDashboardRepository extends JpaRepository<ApresentacaoDashboard, Long> {

    List<ApresentacaoDashboard> findByPublicoTrue();

    List<ApresentacaoDashboard> findByArquivo_Id(Long Id);
}
