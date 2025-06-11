package com.stockmarket.main;

import com.opencsv.exceptions.CsvValidationException;
import com.stockmarket.market.Market;
import com.stockmarket.persistence.CsvAssetRepository;
import com.stockmarket.persistence.FilePortfolioRepository;
import com.stockmarket.portfolio.Portfolio;
import com.stockmarket.ui.ConsoleUI;

import java.io.IOException;

public class StockMarketApp {
    public static void main(String[] args) throws IOException, CsvValidationException {
        String assetsFile = "assets.csv";
        String portfolioFile = "portfolio.json";

        CsvAssetRepository assetRepository = new CsvAssetRepository(assetsFile);
        FilePortfolioRepository portfolioRepository = new FilePortfolioRepository(portfolioFile);

        Market market = new Market(assetRepository);
        Portfolio portfolio = new Portfolio(10000);

        portfolioRepository.load().ifPresent(loaded -> {
            System.out.println("Wczytano portfel z pliku!");
            portfolio.setCash(loaded.getCash());
            portfolio.setPositions(loaded.getPositions());
        });

        ConsoleUI ui = new ConsoleUI(market, portfolioRepository, portfolio);
        ui.start();

    }
}
