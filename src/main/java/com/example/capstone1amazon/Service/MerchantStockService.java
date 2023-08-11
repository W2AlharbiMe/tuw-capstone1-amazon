package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.Model.MerchantStock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Service
public class MerchantStockService {

    private final HashMap<Integer, MerchantStock> merchantsStocks = new HashMap<>();
    private final HashMap<Integer, ArrayList<Integer>> merchantProducts = new HashMap<>();

    public Collection<MerchantStock> getAllMerchantsStocks() {
        return merchantsStocks.values();
    }

    public boolean containsId(Integer id) {
        return merchantsStocks.containsKey(id);
    }

    public void createMerchantStock(MerchantStock merchantStock) {
        ArrayList<Integer> productsIds;

        if(merchantProducts.containsKey(merchantStock.getMerchantId())) {
            productsIds = merchantProducts.get(merchantStock.getMerchantId());
            productsIds.add(merchantStock.getProductId());
        } else {
            productsIds = new ArrayList<>();
            productsIds.add(merchantStock.getProductId());

            merchantProducts.put(merchantStock.getMerchantId(), productsIds);
        }

        merchantsStocks.put(merchantStock.getId(), merchantStock);
    }

    // a merchant stock can not have the same product twice.
    // each stock must have unique product id
    public boolean ensureOneProduct(Integer productId, Integer merchantId) {
        if(!merchantProducts.containsKey(merchantId)) {
            return false;
        }


        return merchantProducts.get(merchantId).contains(productId);
    }
}
