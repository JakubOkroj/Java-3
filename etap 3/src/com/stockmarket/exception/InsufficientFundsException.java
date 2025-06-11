package com.stockmarket.exception;

// Wyjątek: brak środków na zakup
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message); // Przekazujemy treść błędu do klasy Exception
    }
}
