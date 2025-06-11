package com.stockmarket.portfolio;

import com.stockmarket.exception.AssetNotFoundException;
import com.stockmarket.exception.InsufficientAssetsException;
import com.stockmarket.exception.InsufficientFundsException;
import com.stockmarket.market.Market;
import com.stockmarket.market.Tradable;
import com.stockmarket.model.Asset;

import java.util.HashMap;
import java.util.Map;

// Klasa reprezentująca portfel użytkownika
public class Portfolio {
    private double cash;  // Gotówka dostępna w portfelu
    private Map<String, PortfolioPosition> positions; // Mapa: symbol → pozycja (ilość)

    // Konstruktor – ustawia ilość początkowej gotówki
    public Portfolio(double initialCash) {
        this.cash = initialCash;
        this.positions = new HashMap<>();
    }

    // Metoda kupna aktywa (np. akcji)
    public void buy(String symbol, int quantity, Market market)
            throws InsufficientFundsException, AssetNotFoundException {

        // Szukamy aktywa na rynku – jeśli nie istnieje, rzucamy wyjątek
        Asset asset = market.getAsset(symbol)
                .orElseThrow(() -> new AssetNotFoundException("Aktywo o symbolu " + symbol + " nie istnieje na rynku."));

        // Sprawdzamy, czy aktywo jest handlowalne
        if (!(asset instanceof Tradable)) {
            throw new AssetNotFoundException("Aktywo o symbolu " + symbol + " nie jest handlowalne.");
        }

        Tradable tradable = (Tradable) asset;
        double price = tradable.getCurrentPrice();       // Pobieramy cenę
        double totalCost = price * quantity;             // Obliczamy koszt

        // Sprawdzamy, czy użytkownik ma wystarczającą ilość gotówki
        if (totalCost > cash) {
            throw new InsufficientFundsException("Za mało gotówki. Koszt: " + totalCost + " PLN, masz: " + cash + " PLN.");
        }

        cash -= totalCost; // Odejmujemy koszt z gotówki

        // Aktualizujemy pozycję w portfelu
        positions.compute(symbol, (k, pos) -> {
            if (pos == null) return new PortfolioPosition(quantity); // Nowa pozycja
            pos.addQuantity(quantity); return pos;                   // Istniejąca
        });
    }

    // Metoda sprzedaży aktywa
    public void sell(String symbol, int quantity, Market market)
            throws InsufficientAssetsException, AssetNotFoundException {

        // Sprawdzamy, czy użytkownik ma dane aktywo
        PortfolioPosition position = positions.get(symbol);
        if (position == null) {
            throw new AssetNotFoundException("Nie posiadasz aktywa o symbolu: " + symbol);
        }

        // Sprawdzamy, czy ma wystarczającą ilość
        if (quantity > position.getQuantity()) {
            throw new InsufficientAssetsException("Za mało aktywów do sprzedaży. Masz: "
                    + position.getQuantity() + " sztuk, próbujesz sprzedać: " + quantity);
        }

        // Sprawdzamy czy aktywo istnieje na rynku
        Asset asset = market.getAsset(symbol)
                .orElseThrow(() -> new AssetNotFoundException("Aktywo o symbolu " + symbol + " nie istnieje na rynku."));

        // Sprawdzamy, czy aktywo jest handlowalne
        if (!(asset instanceof Tradable)) {
            throw new AssetNotFoundException("Aktywo o symbolu " + symbol + " nie jest handlowalne.");
        }

        Tradable tradable = (Tradable) asset;
        double price = tradable.getCurrentPrice();       // Pobieramy cenę
        double totalValue = price * quantity;            // Obliczamy zysk

        cash += totalValue;                              // Dodajemy gotówkę
        position.subtractQuantity(quantity);             // Odejmujemy ilość

        // Jeśli ilość spadła do 0 – usuwamy pozycję
        if (position.getQuantity() <= 0) {
            positions.remove(symbol);
        }
    }

    // Getter – zwraca ilość gotówki
    public double getCash() {
        return cash;
    }

    // Setter – ustawia ilość gotówki (np. po wczytaniu z pliku)
    public void setCash(double cash) {
        this.cash = cash;
    }

    // Getter – zwraca mapę pozycji (symbol → ilość)
    public Map<String, PortfolioPosition> getPositions() {
        return positions;
    }

    // Setter – ustawia pozycje (np. po wczytaniu z pliku)
    public void setPositions(Map<String, PortfolioPosition> positions) {
        this.positions = positions;
    }

    // Oblicza łączną wartość portfela na podstawie cen rynkowych
    public double calculateValue(Market market) {
        double total = cash;
        for (Map.Entry<String, PortfolioPosition> entry : positions.entrySet()) {
            String symbol = entry.getKey();
            PortfolioPosition pos = entry.getValue();

            Asset asset = market.getAsset(symbol).orElse(null);
            if (asset instanceof Tradable tradable) {
                total += tradable.getCurrentPrice() * pos.getQuantity();
            }
        }
        return total;
    }
}
