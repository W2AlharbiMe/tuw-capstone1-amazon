package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.Model.MerchantStock;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;

@Service
public class MerchantStockService {

    private final HashMap<Integer, MerchantStock> merchantsStocks = new HashMap<>();

    public Collection<MerchantStock> getAllMerchantsStocks() {
        return merchantsStocks.values();
    }
}
