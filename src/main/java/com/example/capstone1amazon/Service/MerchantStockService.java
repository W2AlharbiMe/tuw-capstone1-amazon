package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.DTO.UpdateMerchantStockDTO;
import com.example.capstone1amazon.Model.Merchant;
import com.example.capstone1amazon.Model.MerchantStock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

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
    public boolean ensureOneProduct(Integer merchantId, Integer productId) {
        if(!merchantProducts.containsKey(merchantId)) {
            return false;
        }


        return merchantProducts.get(merchantId).contains(productId);
    }

    public boolean ensureStockOperation(String operation) {
        if(operation.equalsIgnoreCase("increase") || operation.equalsIgnoreCase("decrease")) {
            return false;
        }

        return true;
    }


    public void validateOperations(Integer merchantStockId, String operation, UpdateMerchantStockDTO updateMerchantStockDTO) throws Exception {
        if(operation.equalsIgnoreCase("decrease")) {
            Integer stock = merchantsStocks.get(merchantStockId).getStock();

            if(stock == 0) {
                throw new Exception("this merchant stock is already out of stock.");
            }

            if(stock < updateMerchantStockDTO.getAmount()) {
                throw new Exception("invalid amount. the merchant stock only have " + stock + " products.");
            }
        }
    }


    public MerchantStock stockOperation(String operation, Integer merchantStockId, UpdateMerchantStockDTO updateMerchantStockDTO) throws Exception {
        if(operation.equalsIgnoreCase("increase")) {
           return increase(merchantStockId, updateMerchantStockDTO);
        }

        if(operation.equalsIgnoreCase("decrease")) {
            return decrease(merchantStockId, updateMerchantStockDTO);
        }

        throw new Exception("invalid operation. it can only be increase or decrease.");
    }

    public MerchantStock increase(Integer merchantStockId, UpdateMerchantStockDTO updateMerchantStockDTO) {
        MerchantStock saved_merchant_stock = merchantsStocks.get(merchantStockId);

        saved_merchant_stock.setStock(
                saved_merchant_stock.getStock() + updateMerchantStockDTO.getAmount()
        );

        return saved_merchant_stock;
    }

    public MerchantStock decrease(Integer merchantStockId, UpdateMerchantStockDTO updateMerchantStockDTO) {
        MerchantStock saved_merchant_stock = merchantsStocks.get(merchantStockId);

        saved_merchant_stock.setStock(
                saved_merchant_stock.getStock() - updateMerchantStockDTO.getAmount()
        );

        return saved_merchant_stock;
    }

    public String getOperationMessage(String operation, UpdateMerchantStockDTO updateMerchantStockDTO) {
        return operation.equalsIgnoreCase("increase") ? "the merchant stock have been increased by "+ updateMerchantStockDTO.getAmount() +" product." : "the merchant stock have been decreased by "+ updateMerchantStockDTO.getAmount() +" product.";
    }

    public MerchantStock deleteMerchantStock(Integer id) {
        MerchantStock saved_merchant_stock = merchantsStocks.get(id);

        ArrayList<Integer> products =  merchantProducts.get(saved_merchant_stock.getMerchantId());

        for (int i = 0; i < products.size(); i++) {
            Integer p = products.get(i);

            if(Objects.equals(p, saved_merchant_stock.getProductId())) {
                products.remove(i);
                break;
            }
        }

        merchantsStocks.remove(id);

        return saved_merchant_stock;
    }

    public MerchantStock getStockByProductId(Integer merchantId, Integer productId) throws Exception {
        Collection<MerchantStock> stocks = getAllMerchantsStocks();

        for (MerchantStock stock : stocks) {
            if(Objects.equals(stock.getMerchantId(), merchantId) && Objects.equals(stock.getProductId(), productId)) {
                return stock;
            }
        }

        throw new Exception("Stock Not Found.");
    }

    public boolean ensureMerchantHaveStocks(Integer merchantId) {
        return merchantProducts.containsKey(merchantId);
    }
}
