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
        this.cash = initialCash;                         // Przypisujemy gotówkę
        this.positions = new HashMap<>();                // Tworzymy pustą mapę pozycji
    }

    // Metoda kupna aktywa (np. akcji)
    public void buy(String symbol, int quantity, Market market)
            throws InsufficientFundsException, AssetNotFoundException {

        Asset asset = market.getAsset(symbol)
                .orElseThrow(() -> new AssetNotFoundException("Aktywo o symbolu " + symbol + " nie istnieje na rynku."));

        if (!(asset instanceof Tradable)) {
            throw new AssetNotFoundException("Aktywo o symbolu " + symbol + " nie jest handlowalne.");
        }

        Tradable tradable = (Tradable) asset;
        double price = tradable.getCurrentPrice();       // Pobieramy aktualną cenę
        double totalCost = price * quantity;             // Obliczamy koszt zakupu

        if (totalCost > cash) {
            throw new InsufficientFundsException("Za mało gotówki. Koszt: " + totalCost + " PLN, masz: " + cash + " PLN.");
        }

        cash -= totalCost; // Odejmujemy koszt od gotówki
        positions.compute(symbol, (k, pos) -> {
            if (pos == null) return new PortfolioPosition(quantity); // Nowa pozycja
            pos.addQuantity(quantity); return pos;                   // Aktualizacja istniejącej
        });
    }

    // Metoda sprzedaży aktywa
    public void sell(String symbol, int quantity, Market market)
            throws InsufficientAssetsException, AssetNotFoundException {

        PortfolioPosition position = positions.get(symbol);
        if (position == null) {
            throw new AssetNotFoundException("Nie posiadasz aktywa o symbolu: " + symbol);
        }

        if (quantity > position.getQuantity()) {
            throw new InsufficientAssetsException("Za mało aktywów do sprzedaży. Masz: "
                    + position.getQuantity() + " sztuk, próbujesz sprzedać: " + quantity);
        }

        Asset asset = market.getAsset(symbol)
                .orElseThrow(() -> new AssetNotFoundException("Aktywo o symbolu " + symbol + " nie istnieje na rynku."));

        if (!(asset instanceof Tradable)) {
            throw new AssetNotFoundException("Aktywo o symbolu " + symbol + " nie jest handlowalne.");
        }

        Tradable tradable = (Tradable) asset;
        double price = tradable.getCurrentPrice();       // Cena sprzedaży
        double totalValue = price * quantity;            // Wartość transakcji

        cash += totalValue;                              // Dodajemy gotówkę
        position.subtractQuantity(quantity);             // Odejmujemy ilość
        if (position.getQuantity() <= 0) {
            positions.remove(symbol);                    // Usuwamy pozycję, jeśli już 0
        }
    }

    // Getter – zwraca ilość gotówki
    public double getCash() {
        return cash;
    }

    // Getter – zwraca aktualne pozycje w portfelu
    public Map<String, PortfolioPosition> getPositions() {
        return positions;
    }

    // Oblicza łączną wartość pozycji w portfelu (po aktualnych cenach z rynku)
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
