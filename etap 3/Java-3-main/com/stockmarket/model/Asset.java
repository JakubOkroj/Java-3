package com.stockmarket.model;

// Klasa bazowa reprezentująca aktywo finansowe
public abstract class Asset {
    protected String symbol;      // Unikalny symbol (np. "CDR")
    protected String name;        // Pełna nazwa (np. "CD Projekt")

    // Konstruktor ustawia symbol i nazwę aktywa
    public Asset(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    // Getter – zwraca symbol aktywa
    public String getSymbol() {
        return symbol;
    }

    // Getter – zwraca nazwę aktywa
    public String getName() {
        return name;
    }

    // Metoda abstrakcyjna – każda podklasa musi zaimplementować aktualizację ceny
    public abstract void updatePrice();
}
