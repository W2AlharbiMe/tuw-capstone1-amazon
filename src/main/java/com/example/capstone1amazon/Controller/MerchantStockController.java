package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.Model.MerchantStock;
import com.example.capstone1amazon.Service.MerchantStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/merchants/stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity<Collection<MerchantStock>> getAllMerchantsStocks() {
        return ResponseEntity.ok(merchantStockService.getAllMerchantsStocks());
    }

}
