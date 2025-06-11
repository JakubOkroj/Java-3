package com.stockmarket.persistence;

import com.stockmarket.portfolio.Portfolio;

import java.io.IOException;
import java.util.Optional;

// Interfejs do zapisu i odczytu portfela
public interface PortfolioRepository {
    void save(Portfolio portfolio) throws IOException; // Zapis do pliku
    Optional<Portfolio> load() throws IOException;     // Wczytanie portfela z pliku
}
