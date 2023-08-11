package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.ApiResponse.ApiErrorResponse;
import com.example.capstone1amazon.ApiResponse.ApiResponseWithData;
import com.example.capstone1amazon.Model.Merchant;
import com.example.capstone1amazon.Service.ErrorsService;
import com.example.capstone1amazon.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
public class MerchantsController {

    private final MerchantService merchantService;
    private final ErrorsService errorsService;

    @GetMapping("/get")
    public ResponseEntity<Collection<Merchant>> getAllMerchants() {
        return ResponseEntity.ok(merchantService.getAllMerchants());
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveMerchant(@RequestBody @Valid Merchant merchant, Errors errors) {
        if(merchantService.containsId(merchant.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("merchant", "the id must be unique.", "id", "unique")));
        }

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((errorsService.bulkAdd(errors).get()));
        }

        merchantService.saveMerchant(merchant);

        return ResponseEntity.status(HttpStatus.CREATED).body((new ApiResponseWithData<Merchant>("the merchant have been created.", merchant)));
    }
}
