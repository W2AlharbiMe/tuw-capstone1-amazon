package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.ApiResponse.ApiErrorResponse;
import com.example.capstone1amazon.ApiResponse.ApiResponseWithData;
import com.example.capstone1amazon.DTO.UpdateMerchantDTO;
import com.example.capstone1amazon.Model.Merchant;
import com.example.capstone1amazon.Service.ErrorsService;
import com.example.capstone1amazon.Service.MerchantService;
import jakarta.validation.Path;
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


    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateMerchant(@PathVariable Integer id, @RequestBody @Valid UpdateMerchantDTO updateMerchantDTO, Errors errors) {
        if(!merchantService.containsId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("merchant", "merchant not found.", "id", "not_found")));
        }

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((errorsService.bulkAdd(errors).get()));
        }

        Merchant merchant = merchantService.updateMerchant(id, updateMerchantDTO);

        return ResponseEntity.ok((new ApiResponseWithData<Merchant>("the merchant have been updated.", merchant)));
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteMerchant(@PathVariable Integer id) {
        if(!merchantService.containsId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("merchant", "merchant not found.", "id", "not_found")));
        }

        Merchant merchant = merchantService.deleteMerchant(id);

        return ResponseEntity.ok((new ApiResponseWithData<Merchant>("the merchant have been deleted.", merchant)));
    }
}
