package com.stockmarket.model;

// Klasa bazowa reprezentująca aktywo finansowe
public abstract class Asset {
    protected String symbol;      // Unikalny symbol (np. "CDR")
    protected String name;        // Pełna nazwa (np. "CD Projekt")
    protected double price;       // Aktualna cena aktywa

    // Konstruktor ustawia symbol, nazwę i cenę aktywa
    public Asset(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    // Getter – zwraca symbol aktywa
    public String getSymbol() {
        return symbol;
    }

    // Getter – zwraca nazwę aktywa
    public String getName() {
        return name;
    }

    // Getter – zwraca aktualną cenę aktywa
    public double getPrice() {
        return price;
    }

    // Setter – ustawia cenę aktywa
    public void setPrice(double price) {
        this.price = price;
    }

    // Metoda abstrakcyjna – każda podklasa musi zaimplementować aktualizację ceny
    public abstract void updatePrice();
}
