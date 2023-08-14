package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.ApiResponse.ApiErrorResponse;
import com.example.capstone1amazon.ApiResponse.ApiResponseWithData;
import com.example.capstone1amazon.DTO.UpdateMerchantStockDTO;
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


    @GetMapping("/get")
    public ResponseEntity<Collection<MerchantStock>> getAllMerchantsStocks() {
        return ResponseEntity.ok(merchantStockService.getAllMerchantsStocks());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if(merchantStockService.containsId(merchantStock.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchantStock", "the id must be unique.", "id", "unique")));
        }

        if(!merchantStockService.getProductService().containsId(merchantStock.getProductId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchantStock", "No product have been found with the product id you provided.", "productId", "product_not_found")));
        }

        if(!merchantStockService.getMerchantService().containsId(merchantStock.getMerchantId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchantStock", "No merchant have been found with the merchant id you provided.", "merchantId", "merchant_not_found")));
        }

        if(merchantStockService.ensureOneProduct(merchantStock.getMerchantId(), merchantStock.getProductId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchantStock", "merchant stock can not have the same product twice.", "productId", "unique_product")));
        }

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((merchantStockService.getErrorsService().bulkAdd(errors).get()));
        }

        merchantStockService.createMerchantStock(merchantStock);

        return ResponseEntity.status(HttpStatus.CREATED).body((new ApiResponseWithData<MerchantStock>("a merchant stock have been created.", merchantStock)));
    }


    // PUT /api/v1/merchants/stock/1/increase
    // ---- OR ----
    // PUT /api/v1/merchants/stock/1/decrease
    @PutMapping("/{merchantId}/{productId}/{operation}")
    public ResponseEntity<?> stockOperation(@PathVariable Integer merchantId, @PathVariable Integer productId, @PathVariable String operation, @RequestBody @Valid UpdateMerchantStockDTO updateMerchantStockDTO, Errors errors) {
        if(!merchantStockService.ensureOneProduct(merchantId, productId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("merchantStock", "merchant stock not found.", "id", "not_found")));
        }

        if(merchantStockService.ensureStockOperation(operation)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchantStock", "invalid operation, it can only be increase or decrease.", "operation", "invalid_type")));
        }

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((merchantStockService.getErrorsService().bulkAdd(errors).get()));
        }

        MerchantStock merchantStock;

        try {
            merchantStock = merchantStockService.getStockByProductId(merchantId, productId);
            merchantStockService.validateOperations(merchantStock.getId(), operation, updateMerchantStockDTO);
        } catch (Exception e1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchantStock", e1.getMessage(), "operation", "invalid_operation")));
        }



        try {
           MerchantStock merchantStockUpdated = merchantStockService.stockOperation(operation, merchantStock.getId(), updateMerchantStockDTO);

           return ResponseEntity.ok((new ApiResponseWithData<MerchantStock>(merchantStockService.getOperationMessage(operation, updateMerchantStockDTO), merchantStockUpdated)));
        } catch (Exception e2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchantStock", e2.getMessage(), "operation", "invalid_type")));
        }
    }

    @DeleteMapping("/{merchantStockId}/delete")
    public ResponseEntity<?> deleteMerchantStock(@PathVariable Integer merchantStockId) {
        if(!merchantStockService.containsId(merchantStockId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("merchantStock", "merchant stock not found.", "id", "not_found")));
        }

        MerchantStock merchantStock = merchantStockService.deleteMerchantStock(merchantStockId);

        return ResponseEntity.ok((new ApiResponseWithData<MerchantStock>("a merchant stock have been deleted.", merchantStock)));
    }
}
