package com.stockmarket.market;

import com.opencsv.exceptions.CsvValidationException;
import com.stockmarket.model.Asset;
import com.stockmarket.persistence.AssetRepository;

import java.io.IOException;
import java.util.*;

// Klasa reprezentująca rynek – przechowuje aktywa i aktualizuje ich ceny
public class Market {
    private Map<String, Asset> assetMap; // Mapa: symbol → aktywo

    // Konstruktor – tworzy pusty rynek (np. dla testów)
    public Market() {
        this.assetMap = new HashMap<>();
    }

    // Konstruktor – inicjalizuje rynek na podstawie repozytorium aktywów
    public Market(AssetRepository repository) {
        try {
            this.assetMap = repository.loadAssetDefinitions(); // Wczytaj dane z CSV
        } catch (IOException | CsvValidationException e) {
            System.out.println("❌ Błąd ładowania aktywów z repozytorium: " + e.getMessage());
            this.assetMap = new HashMap<>();
        }
    }

    public void addAsset(Asset asset) {
        assetMap.put(asset.getSymbol(), asset);
    }

    public Optional<Asset> getAsset(String symbol) {
        return Optional.ofNullable(assetMap.get(symbol));
    }

    public void updatePrices() {
        for (Asset asset : assetMap.values()) {
            asset.updatePrice();
        }
    }

    public Map<String, Asset> getAllAssets() {
        return Collections.unmodifiableMap(assetMap);
    }
}
