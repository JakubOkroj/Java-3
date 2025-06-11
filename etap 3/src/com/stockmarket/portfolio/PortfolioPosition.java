package com.stockmarket.portfolio;

// Klasa pomocnicza – przechowuje informacje o ilości danego aktywa w portfelu
public class PortfolioPosition {
    private int quantity; // Ilość posiadanych sztuk

    // Konstruktor
    public PortfolioPosition(int quantity) {
        this.quantity = quantity;
    }

    // Getter
    public int getQuantity() {
        return quantity;
    }

    // Dodaje podaną ilość do pozycji
    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    // Odejmuje podaną ilość z pozycji
    public void subtractQuantity(int amount) {
        this.quantity -= amount;
    }
}
