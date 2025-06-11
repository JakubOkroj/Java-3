package com.stockmarket.persistence;

import com.opencsv.exceptions.CsvValidationException;
import com.stockmarket.model.Asset;

import java.io.IOException;
import java.util.Map;

public interface AssetRepository {
    Map<String, Asset> loadAssetDefinitions() throws IOException, CsvValidationException;
}
