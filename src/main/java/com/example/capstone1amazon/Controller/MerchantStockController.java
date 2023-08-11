package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.ApiResponse.ApiErrorResponse;
import com.example.capstone1amazon.ApiResponse.ApiResponseWithData;
import com.example.capstone1amazon.Model.MerchantStock;
import com.example.capstone1amazon.Service.ErrorsService;
import com.example.capstone1amazon.Service.MerchantService;
import com.example.capstone1amazon.Service.MerchantStockService;
import com.example.capstone1amazon.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/merchants/stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;
    private final ProductService productService;
    private final MerchantService merchantService;
    private final ErrorsService errorsService;


    @GetMapping("/get")
    public ResponseEntity<Collection<MerchantStock>> getAllMerchantsStocks() {
        return ResponseEntity.ok(merchantStockService.getAllMerchantsStocks());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if(merchantStockService.containsId(merchantStock.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchantStock", "the id must be unique.", "id", "unique")));
        }

        if(!productService.containsId(merchantStock.getProductId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchantStock", "No product have been found with the product id you provided.", "productId", "product_not_found")));
        }

        if(!merchantService.containsId(merchantStock.getMerchantId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchantStock", "No merchant have been found with the merchant id you provided.", "merchantId", "merchant_not_found")));
        }

        if(merchantStockService.ensureOneProduct(merchantStock.getProductId(), merchantStock.getProductId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchantStock", "merchant stock can not have the same product twice.", "productId", "unique_product")));
        }

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((errorsService.bulkAdd(errors).get()));
        }

        merchantStockService.createMerchantStock(merchantStock);

        return ResponseEntity.status(HttpStatus.CREATED).body((new ApiResponseWithData<MerchantStock>("a merchant stock have been created.", merchantStock)));
    }


}
