package com.stockmarket.exception;

// Wyjątek: brak wystarczającej ilości aktywów do sprzedaży
public class InsufficientAssetsException extends Exception {
    public InsufficientAssetsException(String message) {
        super(message);
    }
}
