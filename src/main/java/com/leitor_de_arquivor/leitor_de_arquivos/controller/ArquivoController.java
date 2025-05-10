package com.leitor_de_arquivor.leitor_de_arquivos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.leitor_de_arquivor.leitor_de_arquivos.model.Arquivo;
import com.leitor_de_arquivor.leitor_de_arquivos.model.ConversaoResponse;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.leitor_de_arquivor.leitor_de_arquivos.repository.ArquivoRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe responsável por gerenciar o upload e processamento de arquivos.
 * Esta classe é um controlador REST que permite o envio de arquivos para o servidor
 * e realiza a conversão de diferentes formatos de arquivo (CSV, Excel, JSON, XML e TXT)
 * em objetos que podem ser manipulados no sistema.
 */
@RestController
@RequestMapping("/arquivo")

public class ArquivoController {
    @Autowired //injeção de dependência do ArquivoRepository
    private ArquivoRepository arquivoRepository;
    private static final String UPLOAD_DIR = "src/main/resources/uploads";


    /**
     * Endpoint para upload de arquivos.
     * Este método recebe um arquivo enviado pelo cliente, identifica sua extensão
     * e processa o conteúdo de acordo com o formato do arquivo.
     *
     * @param file Arquivo enviado pelo cliente.
     * @return Uma resposta HTTP contendo o resultado do processamento ou uma mensagem de erro.
     * @throws IOException Caso ocorra um erro ao ler o arquivo.
     * @throws CsvValidationException Caso ocorra um erro ao validar o arquivo CSV.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException, CsvValidationException {
        String filename = file.getOriginalFilename();

        if (filename == null || filename.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo inválido.");
        }

        // Salva o arquivo na pasta uploads
        Path uploadPath = saveFile(file);

        // Obtém a extensão do arquivo
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

        // Processa o arquivo com base na extensão
        switch (extension) {
            case "csv":
                return ResponseEntity.ok(new ConversaoResponse(parseCsv(file)));
            case "xlsx":
                return ResponseEntity.ok(new ConversaoResponse(parseExcel(file)));
            case "json":
                return ResponseEntity.ok(new ConversaoResponse(parseJson(file)));
            case "xml":
                return ResponseEntity.ok(new ConversaoResponse(parseXml(file)));
            case "txt":
                return ResponseEntity.ok(new ConversaoResponse(parseTxt(file)));
            default:
                return ResponseEntity.badRequest().body("Formato não suportado: " + extension);
        }
    }

    private Path saveFile(MultipartFile file) throws IOException {
        Path uploadDir = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir); // Cria o diretório se não existir
        }

        Path filePath = uploadDir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath);

        Arquivo arquivo = new Arquivo();
        arquivo.setTipo(file.getContentType());
        arquivo.setCaminho_do_arquivo(filePath.toString());
        arquivo.setCriadoEm(LocalDateTime.now());
        // Salva o arquivo no banco de dados
        arquivoRepository.save(arquivo);

        // Salva o arquivo no diretório
        return filePath;
    }

    /**
     * Método para processar arquivos no formato CSV.
     * Lê o conteúdo do arquivo e converte cada linha em um mapa de chave-valor,
     * onde as chaves são os cabeçalhos do arquivo.
     *
     * @param file Arquivo CSV enviado.
     * @return Uma lista de mapas representando as linhas do arquivo.
     * @throws IOException Caso ocorra um erro ao ler o arquivo.
     * @throws CsvValidationException Caso ocorra um erro ao validar o arquivo CSV.
     */
    private List<Map<String, String>> parseCsv(MultipartFile file) throws IOException, CsvValidationException {
        List<Map<String, String>> result = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] headers = reader.readNext(); // Lê os cabeçalhos
            String[] line;
            while ((line = reader.readNext()) != null) {
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i], line[i]); // Associa os valores às chaves
                }
                result.add(row);
            }
        }
        return result;
    }

    /**
     * Método para processar arquivos no formato Excel (.xlsx).
     * Lê o conteúdo do arquivo e converte cada linha em um mapa de chave-valor,
     * onde as chaves são os cabeçalhos da planilha.
     *
     * @param file Arquivo Excel enviado.
     * @return Uma lista de mapas representando as linhas da planilha.
     * @throws IOException Caso ocorra um erro ao ler o arquivo.
     */
    private List<Map<String, String>> parseExcel(MultipartFile file) throws IOException {
        List<Map<String, String>> result = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira aba
            Row headerRow = sheet.getRow(0); // Lê a linha de cabeçalhos
            List<String> headers = new ArrayList<>();
            headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Lê as linhas de dados
                Row row = sheet.getRow(i);
                Map<String, String> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    rowData.put(headers.get(j), cell != null ? cell.toString() : "");
                }
                result.add(rowData);
            }
        }
        return result;
    }

    /**
     * Método para processar arquivos no formato JSON.
     * Converte o conteúdo do arquivo em um objeto genérico.
     *
     * @param file Arquivo JSON enviado.
     * @return Um objeto representando o conteúdo do arquivo.
     * @throws IOException Caso ocorra um erro ao ler o arquivo.
     */
    private Object parseJson(MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file.getInputStream(), Object.class);
    }

    /**
     * Método para processar arquivos no formato XML.
     * Converte o conteúdo do arquivo em um objeto genérico.
     *
     * @param file Arquivo XML enviado.
     * @return Um objeto representando o conteúdo do arquivo.
     * @throws IOException Caso ocorra um erro ao ler o arquivo.
     */
    private Object parseXml(MultipartFile file) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(file.getInputStream(), Object.class);
    }

    /**
     * Método para processar arquivos no formato TXT.
     * Lê cada linha do arquivo e as armazena em uma lista.
     *
     * @param file Arquivo TXT enviado.
     * @return Uma lista de strings representando as linhas do arquivo.
     * @throws IOException Caso ocorra um erro ao ler o arquivo.
     */
    private List<String> parseTxt(MultipartFile file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line); // Adiciona cada linha à lista
            }
        }
        return lines;
    }

}
