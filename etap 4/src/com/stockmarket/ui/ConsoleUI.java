package com.stockmarket.ui;

import com.stockmarket.exception.AssetNotFoundException;
import com.stockmarket.exception.InsufficientAssetsException;
import com.stockmarket.exception.InsufficientFundsException;
import com.stockmarket.market.Market;
import com.stockmarket.market.Tradable;
import com.stockmarket.model.Asset;
import com.stockmarket.persistence.PortfolioRepository;
import com.stockmarket.portfolio.Portfolio;
import com.stockmarket.portfolio.PortfolioPosition;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

// Klasa obsługująca menu i logikę konsoli
public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in); // Odczyt wejścia od użytkownika
    private final Market market;                            // Rynek aktywów
    private final PortfolioRepository portfolioRepository;  // Repozytorium do zapisu/odczytu
    private Portfolio portfolio;                            // Portfel użytkownika

    public ConsoleUI(Market market, PortfolioRepository repository, Portfolio loadedPortfolio) {
        this.market = market;
        this.portfolioRepository = repository;
        this.portfolio = loadedPortfolio != null ? loadedPortfolio : new Portfolio(10000.0); // domyślny portfel
    }

    // Startuje główną pętlę menu
    public void start() {
        while (true) {
            System.out.println("""
                    
                    === MENU ===
                    1. Pokaż aktywa rynkowe
                    2. Pokaż mój portfel
                    3. Kup aktywo
                    4. Sprzedaj aktywo
                    5. Zapisz portfel do pliku
                    6. Wczytaj portfel z pliku
                    7. Aktualizuj ceny na rynku
                    0. Wyjdź
                    """);

            System.out.print("Wybierz opcję: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showMarket();
                case "2" -> showPortfolio();
                case "3" -> handleBuy();
                case "4" -> handleSell();
                case "5" -> handleSave();
                case "6" -> handleLoad();
                case "7" -> handleUpdatePrices();
                case "0" -> {
                    System.out.println("Zamykam aplikację.");
                    return;
                }
                default -> System.out.println("Nieprawidłowa opcja.");
            }
        }
    }

    // Wyświetla wszystkie aktywa z rynku
    private void showMarket() {
        System.out.println("=== AKTYWA NA RYNKU ===");
        for (Map.Entry<String, Asset> entry : market.getAllAssets().entrySet()) {
            Asset asset = entry.getValue();
            if (asset instanceof Tradable tradable) {
                System.out.printf("%s (%s): %.2f PLN%n", asset.getSymbol(), asset.getName(), tradable.getCurrentPrice());
            }
        }
    }

    // Wyświetla zawartość portfela
    private void showPortfolio() {
        System.out.printf("Gotówka: %.2f PLN%n", portfolio.getCash());
        System.out.println("Aktywa:");
        for (Map.Entry<String, PortfolioPosition> entry : portfolio.getPositions().entrySet()) {
            String symbol = entry.getKey();
            int qty = entry.getValue().getQuantity();
            Optional<Asset> opt = market.getAsset(symbol);
            if (opt.isPresent() && opt.get() instanceof Tradable tradable) {
                double price = tradable.getCurrentPrice();
                System.out.printf(" - %s: %d szt. @ %.2f PLN = %.2f PLN%n", symbol, qty, price, qty * price);
            }
        }
        System.out.printf("Całkowita wartość portfela: %.2f PLN%n", portfolio.calculateValue(market));
    }

    // Kupno aktywa
    private void handleBuy() {
        System.out.print("Symbol aktywa do zakupu: ");
        String symbol = scanner.nextLine().toUpperCase();

        System.out.print("Ilość do zakupu: ");
        int qty = Integer.parseInt(scanner.nextLine());

        try {
            portfolio.buy(symbol, qty, market);
            System.out.println("✅ Zakup zakończony sukcesem.");
        } catch (InsufficientFundsException | AssetNotFoundException e) {
            System.out.println("❌ Błąd: " + e.getMessage());
        }
    }

    // Sprzedaż aktywa
    private void handleSell() {
        System.out.print("Symbol aktywa do sprzedaży: ");
        String symbol = scanner.nextLine().toUpperCase();

        System.out.print("Ilość do sprzedaży: ");
        int qty = Integer.parseInt(scanner.nextLine());

        try {
            portfolio.sell(symbol, qty, market);
            System.out.println("✅ Sprzedaż zakończona sukcesem.");
        } catch (InsufficientAssetsException | AssetNotFoundException e) {
            System.out.println("❌ Błąd: " + e.getMessage());
        }
    }

    // Zapis portfela do pliku
    private void handleSave() {
        try {
            portfolioRepository.save(portfolio);
            System.out.println("✅ Portfel zapisany do pliku.");
        } catch (IOException e) {
            System.out.println("❌ Błąd zapisu pliku: " + e.getMessage());
        }
    }

    // Wczytanie portfela z pliku
    private void handleLoad() {
        try {
            Optional<Portfolio> loaded = portfolioRepository.load();
            if (loaded.isPresent()) {
                this.portfolio = loaded.get();
                System.out.println("✅ Portfel wczytany z pliku.");
            } else {
                System.out.println("⚠️ Brak zapisanego portfela.");
            }
        } catch (IOException e) {
            System.out.println("❌ Błąd odczytu pliku: " + e.getMessage());
        }
    }

    // Aktualizacja cen aktywów
    private void handleUpdatePrices() {
        market.updatePrices();
        System.out.println("✅ Ceny na rynku zostały zaktualizowane.");
    }
}
