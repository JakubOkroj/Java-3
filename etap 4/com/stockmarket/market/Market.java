package com.stockmarket.market;

import com.stockmarket.model.Asset;

import java.util.*;

// Klasa reprezentująca rynek – przechowuje aktywa i aktualizuje ich ceny
public class Market {
    private Map<String, Asset> assetMap; // Mapa: symbol → aktywo

    // Konstruktor – tworzy pustą mapę
    public Market() {
        this.assetMap = new HashMap<>();
    }

    // Dodaje aktywo do rynku (np. akcję)
    public void addAsset(Asset asset) {
        assetMap.put(asset.getSymbol(), asset);
    }

    // Zwraca Optional z aktywem na podstawie symbolu (jeśli istnieje)
    public Optional<Asset> getAsset(String symbol) {
        return Optional.ofNullable(assetMap.get(symbol));
    }

    // Aktualizuje ceny wszystkich aktywów na rynku
    public void updatePrices() {
        for (Asset asset : assetMap.values()) {
            asset.updatePrice(); // Wywołuje metodę updatePrice() na każdym aktywie
        }
    }

    // Zwraca niemodyfikowalną mapę wszystkich aktywów (do odczytu)
    public Map<String, Asset> getAllAssets() {
        return Collections.unmodifiableMap(assetMap);
    }
}
