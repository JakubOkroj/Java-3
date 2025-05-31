package com.stockmarket.main;

import com.stockmarket.exception.AssetNotFoundException;
import com.stockmarket.exception.InsufficientAssetsException;
import com.stockmarket.exception.InsufficientFundsException;
import com.stockmarket.market.Market;
import com.stockmarket.model.Stock;
import com.stockmarket.portfolio.Portfolio;
import com.stockmarket.portfolio.PortfolioPosition;package com.stockmarket.main;

import com.stockmarket.exception.AssetNotFoundException;
import com.stockmarket.exception.InsufficientAssetsException;
import com.stockmarket.exception.InsufficientFundsException;
import com.stockmarket.market.Market;
import com.stockmarket.model.Stock;
import com.stockmarket.portfolio.Portfolio;
import com.stockmarket.portfolio.PortfolioPosition;
import com.stockmarket.market.Tradable;

import java.util.Map;

// Klasa główna – zawiera metodę main do uruchamiania aplikacji
public class StockMarketSimStage3 {
    public static void main(String[] args) {
        // Tworzymy rynek
        Market market = new Market();

        // Tworzymy przykładowe akcje
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);
        Stock pko = new Stock("PKO", "PKO BP", 40.0);
        Stock kgh = new Stock("KGH", "KGHM", 120.0);

        // Dodajemy aktywa do rynku
        market.addAsset(cdr);
        market.addAsset(pko);
        market.addAsset(kgh);

        // Tworzymy portfel z 10000 PLN
        Portfolio portfolio = new Portfolio(10000.0);

        // Symulujemy zmiany cen
        market.updatePrices();

        // Próby zakupu
        try {
            portfolio.buy("CDR", 10, market);  // Kupujemy 10 CDR
            portfolio.buy("PKO", 100, market); // Kupujemy 100 PKO
        } catch (InsufficientFundsException | AssetNotFoundException e) {
            System.out.println("❌ Błąd zakupu: " + e.getMessage());
        }

        // Wyświetlamy stan portfela
        System.out.println("\n=== Stan portfela po zakupie ===");
        showPortfolio(portfolio, market);

        // Próba sprzedaży
        try {
            portfolio.sell("CDR", 5, market); // Sprzedaż 5 CDR
            portfolio.sell("PKO", 200, market); // Próba sprzedaży 200 PKO (błąd)
        } catch (InsufficientAssetsException | AssetNotFoundException e) {
            System.out.println("❌ Błąd sprzedaży: " + e.getMessage());
        }

        // Ostateczny stan
        System.out.println("\n=== Końcowy stan portfela ===");
        showPortfolio(portfolio, market);
    }

    // Metoda pomocnicza – wyświetla stan portfela
    public static void showPortfolio(Portfolio portfolio, Market market) {
        System.out.printf("Gotówka: %.2f PLN%n", portfolio.getCash());

        System.out.println("Aktywa w portfelu:");
        for (Map.Entry<String, PortfolioPosition> entry : portfolio.getPositions().entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue().getQuantity();
            double price = market.getAsset(symbol)
                    .filter(asset -> asset instanceof Tradable)
                    .map(asset -> ((Tradable) asset).getCurrentPrice())
                    .orElse(0.0);
            double value = quantity * price;

            System.out.printf(" - %s: %d szt. @ %.2f PLN = %.2f PLN%n", symbol, quantity, price, value);
        }

        double total = portfolio.calculateValue(market);
        System.out.printf("Wartość całkowita portfela: %.2f PLN%n", total);
    }
}


import java.util.Map;

// Klasa główna – zawiera metodę main do uruchamiania aplikacji
public class StockMarketSimStage3 {
    public static void main(String[] args) {
        // Tworzymy rynek
        Market market = new Market();

        // Tworzymy przykładowe akcje
        Stock cdr = new Stock("CDR", "CD Projekt", 300.0);
        Stock pko = new Stock("PKO", "PKO BP", 40.0);
        Stock kgh = new Stock("KGH", "KGHM", 120.0);

        // Dodajemy aktywa do rynku
        market.addAsset(cdr);
        market.addAsset(pko);
        market.addAsset(kgh);

        // Tworzymy portfel z 10000 PLN
        Portfolio portfolio = new Portfolio(10000.0);

        // Symulujemy zmiany cen
        market.updatePrices();

        // Próby zakupu
        try {
            portfolio.buy("CDR", 10, market);  // Kupujemy 10 CDR
            portfolio.buy("PKO", 100, market); // Kupujemy 100 PKO
        } catch (InsufficientFundsException | AssetNotFoundException e) {
            System.out.println("❌ Błąd zakupu: " + e.getMessage());
        }

        // Wyświetlamy stan portfela
        System.out.println("\n=== Stan portfela po zakupie ===");
        showPortfolio(portfolio, market);

        // Próba sprzedaży
        try {
            portfolio.sell("CDR", 5, market); // Sprzedaż 5 CDR
            portfolio.sell("PKO", 200, market); // Próba sprzedaży 200 PKO (błąd)
        } catch (InsufficientAssetsException | AssetNotFoundException e) {
            System.out.println("❌ Błąd sprzedaży: " + e.getMessage());
        }

        // Ostateczny stan
        System.out.println("\n=== Końcowy stan portfela ===");
        showPortfolio(portfolio, market);
    }

    // Metoda pomocnicza – wyświetla stan portfela
    public static void showPortfolio(Portfolio portfolio, Market market) {
        System.out.printf("Gotówka: %.2f PLN%n", portfolio.getCash());

        System.out.println("Aktywa w portfelu:");
        for (Map.Entry<String, PortfolioPosition> entry : portfolio.getPositions().entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue().getQuantity();
            double price = market.getAsset(symbol)
                    .filter(asset -> asset instanceof com.stockmarket.market.Tradable)
                    .map(asset -> ((com.stockmarket.market.Tradable) asset).getCurrentPrice())
                    .orElse(0.0);
            double value = quantity * price;

            System.out.printf(" - %s: %d szt. @ %.2f PLN = %.2f PLN%n", symbol, quantity, price, value);
        }

        double total = portfolio.calculateValue(market);
        System.out.printf("Wartość całkowita portfela: %.2f PLN%n", total);
    }
}
