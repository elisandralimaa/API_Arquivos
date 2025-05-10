package com.leitor_de_arquivor.leitor_de_arquivos.repository;

import com.leitor_de_arquivor.leitor_de_arquivos.model.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Integer> {
}
