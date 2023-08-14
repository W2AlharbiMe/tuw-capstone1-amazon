package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.ApiResponse.ApiErrorResponse;
import com.example.capstone1amazon.ApiResponse.ApiResponseWithData;
import com.example.capstone1amazon.ApiResponse.ApiUserBuyResponse;
import com.example.capstone1amazon.DTO.BuyProductDTO;
import com.example.capstone1amazon.DTO.UpdateMerchantStockDTO;
import com.example.capstone1amazon.DTO.UpdateUserDTO;
import com.example.capstone1amazon.Model.MerchantStock;
import com.example.capstone1amazon.Model.Product;
import com.example.capstone1amazon.Model.User;
import com.example.capstone1amazon.Service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;


    // GET /api/v1/users/get?role=admin
    // ---- OR ----
    // GET /api/v1/users/get?role=customer
    // ---- OR ----
    // GET /api/v1/users/get
    @GetMapping("/get")
    public ResponseEntity<Collection<User>> getAllUsers(@RequestParam(value = "role", required = false) String role) {
        return ResponseEntity.ok(userService.getAllUsers(role));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user, Errors errors) {
        if(userService.containsId(user.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("user", "the id must be unique.", "id", "unique")));
        }

        if(userService.containsEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("user", "used email.", "email", "duplicate_value")));
        }

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userService.getErrorsService().bulkAdd(errors).get());
        }

        userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body((new ApiResponseWithData<User>("the user have been created.", user)));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid UpdateUserDTO updateUserDTO, Errors errors) {
        if(!userService.containsId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("user", "user not found.", "id", "not_found")));
        }

        if(userService.containsEmail(updateUserDTO.getEmail(), id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("user", "used email.", "email", "duplicate_value")));
        }

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userService.getErrorsService().bulkAdd(errors).get());
        }

        User user = userService.updateUser(id, updateUserDTO);

        return ResponseEntity.ok((new ApiResponseWithData<User>("the user have been updated.", user)));
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        if(!userService.containsId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("user", "user not found.", "id", "not_found")));
        }

        User user = userService.deleteUser(id);

        return ResponseEntity.ok((new ApiResponseWithData<User>("the user have been deleted.", user)));
    }

    @GetMapping("/get/{field}/{value}")
    public ResponseEntity<?> getUserByField(@PathVariable String field, @PathVariable String value) {

        if(userService.isEmailField(field)) {
            if(!userService.containsEmail(value)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("user", "user not found.", "email", "not_found")));
            }

            return ResponseEntity.ok(userService.getUserByEmail(value));
        }

        if(userService.isIdField(field)) {
            try {
                if(!userService.containsId(Integer.parseInt(value))) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("user", "user not found.", "id", "not_found")));
                }

                return ResponseEntity.ok(userService.getUserById(Integer.parseInt(value)));
            } catch (NumberFormatException e1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("request", "invalid id format, it must be number.", "id", "invalid_format")));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("request", "invalid field, it can only be 'id' or 'email'.", field, "invalid_field")));
    }

    @PutMapping("/buy")
    public ResponseEntity<?> buyProduct(@RequestBody @Valid BuyProductDTO buyProductDTO, Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userService.getErrorsService().bulkAdd(errors).get());
        }
        // 1. make sure the user exists.
        if(!userService.containsId(buyProductDTO.getUserId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("user", "user not found.", "id", "not_found")));
        }

        // 2. make sure the merchant exists.
        if(!userService.getMerchantService().containsId(buyProductDTO.getMerchantId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("user", "merchant not found.", "merchantId", "not_found")));
        }

        // 3. make sure the product exists
        if(!userService.getProductService().containsId(buyProductDTO.getProductId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("user", "product not found.", "productId", "not_found")));
        }

        // 4. make sure the merchant have stock
        if(!userService.getMerchantStockService().ensureMerchantHaveStocks(buyProductDTO.getMerchantId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("user", "the merchant does not have stocks of anything.", "merchantId", "no_stock")));
        }

        // 5. make sure the merchant does have the product
        // what if the product exists but it's exists in another merchant stock ?
        if(!userService.getMerchantStockService().ensureOneProduct(buyProductDTO.getMerchantId(), buyProductDTO.getProductId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("user", "the merchant does not have this product.", "merchantStock", "no_merchant_stock")));
        }

        Product product = userService.getProductService().getProductById(buyProductDTO.getProductId());

        // validate user balance
        // does the user have enough money to buy product ?
        if(userService.validateBalance(product.getPrice(), buyProductDTO.getUserId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("user", "insufficient balance", "balance", "balance_error")));
        }

        try {
            // what if there's a merchant but there's no merchant stock ?
            MerchantStock merchantStock = userService.getMerchantStockService().getStockByProductId(buyProductDTO.getMerchantId(), buyProductDTO.getProductId());
            UpdateMerchantStockDTO updateMerchantStockDTO = new UpdateMerchantStockDTO(buyProductDTO.getAmount());

            try {
                // ensure that there's enough stock
                userService.getMerchantStockService().validateOperations(merchantStock.getId(), "decrease", updateMerchantStockDTO);
            } catch (Exception e1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("user", e1.getMessage(), "amount", "invalid_amount")));
            }

            merchantStock = userService.getMerchantStockService().stockOperation("decrease", merchantStock.getId(), updateMerchantStockDTO);
            HashMap<String, Double> balanceResponse = userService.buyProduct(buyProductDTO.getUserId(), product.getPrice());

            return ResponseEntity.ok((new ApiUserBuyResponse(
                    "successful.",
                    userService.getUserById(buyProductDTO.getUserId()),
                    product,
                    userService.getMerchantService().getMerchantById(buyProductDTO.getMerchantId()),
                    merchantStock,
                    balanceResponse
            )));
        } catch (Exception e2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("user", "the merchant does not have a stock.", "merchantStock", "no_merchant_stock")));
        }
    }
}
