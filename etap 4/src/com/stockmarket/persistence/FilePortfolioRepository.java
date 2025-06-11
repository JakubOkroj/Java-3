package com.stockmarket.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.stockmarket.portfolio.Portfolio;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

// Implementacja zapisu i odczytu portfela do/z pliku JSON
public class FilePortfolioRepository implements PortfolioRepository {
    private final File file;                     // Ścieżka do pliku
    private final ObjectMapper objectMapper;     // Mapper Jacksona

    // Konstruktor przyjmujący ścieżkę do pliku
    public FilePortfolioRepository(String filePath) {
        this.file = new File(filePath);                         // ustawiamy ścieżkę
        this.objectMapper = new ObjectMapper();                 // tworzymy mapper
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // formatowanie JSON-a
    }

    @Override
    public void save(Portfolio portfolio) throws IOException {
        objectMapper.writeValue(file, portfolio); // Serializacja do JSON
    }

    @Override
    public Optional<Portfolio> load() throws IOException {
        if (!file.exists()) {
            return Optional.empty(); // Brak pliku = brak zapisanego stanu
        }
        Portfolio portfolio = objectMapper.readValue(file, Portfolio.class); // Deserializacja
        return Optional.of(portfolio);
    }
}
