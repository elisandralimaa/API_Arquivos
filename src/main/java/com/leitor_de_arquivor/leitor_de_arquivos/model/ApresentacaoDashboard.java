package com.leitor_de_arquivor.leitor_de_arquivos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "apresentacoes_dashboard")
public class ApresentacaoDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Arquivo arquivo;

    private String nome;
    private String autor;

    @Column(name = "caminho_do_arquivo", length = 1000)
    private String caminhoDoArquivo;

    private boolean publico;
    private boolean template;

    @Column(name = "configuracao_json", columnDefinition = "TEXT")
    private String configuracaoJson;

    private LocalDateTime criadoEm;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "coluna")
    private String coluna;

    @Column(name = "coluna_out")
    private String colunaOut;

    @Column(name = "cor")
    private String cor;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "min_dt")
    private String minDt;

    @Column(name = "max_dt")
    private String maxDt;

    @Column(name = "coluna_dt")
    private String colunaDt;

    @Column(name = "condicao")
    private String condicao;

    @Column(name = "coluna_condicao")
    private String colunaCondicao;

    @Column(name = "valor_condicao")
    private String valorCondicao;

    @Column(name = "sql_query", columnDefinition = "TEXT") // Se a SQL pode ser longa
    private String sql;

    @Column(name = "coluna_label")
    private String colunaLabel;

    @Column(name = "coluna_label_out")
    private String colunaLabelOut;

    @Column(name = "position")
    private String position;

    // Getters e setters para todos os campos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCaminhoDoArquivo() {
        return caminhoDoArquivo;
    }

    public void setCaminhoDoArquivo(String caminhoDoArquivo) {
        this.caminhoDoArquivo = caminhoDoArquivo;
    }

    public boolean isPublico() {
        return publico;
    }

    public void setPublico(boolean publico) {
        this.publico = publico;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    public String getConfiguracaoJson() {
        return configuracaoJson;
    }

    public void setConfiguracaoJson(String configuracaoJson) {
        this.configuracaoJson = configuracaoJson;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getColuna() {
        return coluna;
    }

    public void setColuna(String coluna) {
        this.coluna = coluna;
    }

    public String getColunaOut() {
        return colunaOut;
    }

    public void setColunaOut(String colunaOut) {
        this.colunaOut = colunaOut;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMinDt() {
        return minDt;
    }

    public void setMinDt(String minDt) {
        this.minDt = minDt;
    }

    public String getMaxDt() {
        return maxDt;
    }

    public void setMaxDt(String maxDt) {
        this.maxDt = maxDt;
    }

    public String getColunaDt() {
        return colunaDt;
    }

    public void setColunaDt(String colunaDt) {
        this.colunaDt = colunaDt;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public String getColunaCondicao() {
        return colunaCondicao;
    }

    public void setColunaCondicao(String colunaCondicao) {
        this.colunaCondicao = colunaCondicao;
    }

    public String getValorCondicao() {
        return valorCondicao;
    }

    public void setValorCondicao(String valorCondicao) {
        this.valorCondicao = valorCondicao;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getColunaLabel() {
        return colunaLabel;
    }

    public void setColunaLabel(String colunaLabel) {
        this.colunaLabel = colunaLabel;
    }

    public String getColunaLabelOut() {
        return colunaLabelOut;
    }

    public void setColunaLabelOut(String colunaLabelOut) {
        this.colunaLabelOut = colunaLabelOut;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
