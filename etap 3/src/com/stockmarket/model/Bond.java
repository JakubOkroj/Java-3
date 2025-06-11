package com.stockmarket.model;

// Klasa Bond dziedziczy po Asset
public class Bond extends Asset {
    public Bond(String symbol, String name, double price) {
        super(symbol, name, price);
    }

    @Override
    public void updatePrice() {
        // Opcjonalnie: logika zmiany ceny obligacji
        // Można zostawić puste lub dodać np. bardzo małe wahania
    }
}
