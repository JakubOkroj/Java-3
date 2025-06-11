package com.stockmarket.model;

import com.stockmarket.market.Tradable;

import java.util.Random;

// Klasa reprezentująca akcję giełdową (rozszerza Asset, implementuje Tradable)
public class Stock extends Asset implements Tradable {
    private double currentPrice;  // Aktualna cena rynkowa akcji

    // Konstruktor ustawia symbol, nazwę i początkową cenę
    public Stock(String symbol, String name, double initialPrice) {
        super(symbol, name, initialPrice);   // <- poprawka: dodano price do super()
        this.currentPrice = initialPrice;
    }

    // Getter – zwraca aktualną cenę
    @Override
    public double getCurrentPrice() {
        return currentPrice;
    }

    // Metoda aktualizująca cenę akcji (losowe wahanie ±5%)
    @Override
    public void updatePrice() {
        Random rand = new Random();
        double changePercent = (rand.nextDouble() * 10) - 5; // losowe od -5% do +5%
        double changeAmount = currentPrice * (changePercent / 100);
        currentPrice += changeAmount;
        currentPrice = Math.round(currentPrice * 100.0) / 100.0; // zaokrąglenie do 2 miejsc
    }
}
