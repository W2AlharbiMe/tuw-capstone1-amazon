package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.Model.Merchant;
import com.example.capstone1amazon.Service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
public class MerchantsController {

    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity<Collection<Merchant>> getAllMerchants() {
        return ResponseEntity.ok(merchantService.getAllMerchants());
    }


}
