package com.stockmarket.market;

// Interfejs określający, że aktywo można handlować (ma symbol i aktualną cenę)
public interface Tradable {
    String getSymbol();        // Zwraca symbol (np. "CDR")
    double getCurrentPrice();  // Zwraca aktualną cenę rynkową
}
