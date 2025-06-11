package com.stockmarket.exception;

// Wyjątek: aktywo nie zostało znalezione (np. nieznany symbol)
public class AssetNotFoundException extends Exception {
    public AssetNotFoundException(String message) {
        super(message);
    }
}
