package com.stockmarket.persistence;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.stockmarket.model.Asset;
import com.stockmarket.model.Bond;
import com.stockmarket.model.Stock;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Repozytorium do ładowania aktywów z pliku CSV
public class CsvAssetRepository implements AssetRepository {
    private final String filePath;

    // Konstruktor z podaniem ścieżki do pliku CSV
    public CsvAssetRepository(String filePath) {
        this.filePath = filePath;
    }

    // Wczytuje definicje aktywów z pliku CSV i tworzy mapę aktywów
    @Override
    public Map<String, Asset> loadAssetDefinitions() throws IOException, CsvValidationException {
        Map<String, Asset> assets = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            boolean first = true;

            while ((line = reader.readNext()) != null) {
                if (first) {
                    first = false; // Pominięcie nagłówka
                    continue;
                }

                String symbol = line[0];
                String name = line[1];
                String type = line[2];
                double price = Double.parseDouble(line[3]);

                if (type.equalsIgnoreCase("STOCK")) {
                    assets.put(symbol, new Stock(symbol, name, price));
                } else if (type.equalsIgnoreCase("BOND")) {
                    assets.put(symbol, new Bond(symbol, name, price));
                }
            }
        }

        return assets;
    }
}
