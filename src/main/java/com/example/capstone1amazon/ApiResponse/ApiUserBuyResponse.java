package com.example.capstone1amazon.ApiResponse;

import com.example.capstone1amazon.Model.Merchant;
import com.example.capstone1amazon.Model.MerchantStock;
import com.example.capstone1amazon.Model.Product;
import com.example.capstone1amazon.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class ApiUserBuyResponse {
    private String message;
    private User user;
    private Product product;
    private Merchant merchant;
    private MerchantStock merchantStock;
    HashMap<String, Double> balanceResponse;
}
