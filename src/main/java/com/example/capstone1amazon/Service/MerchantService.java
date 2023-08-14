package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.DTO.UpdateMerchantDTO;
import com.example.capstone1amazon.Model.Merchant;
import com.example.capstone1amazon.Model.MerchantStock;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final HashMap<Integer, Merchant> merchants = new HashMap<>();

    @Getter
    private final ErrorsService errorsService;



    public Collection<Merchant> getAllMerchants() {
        return merchants.values();
    }

    public boolean containsId(Integer id) {
        return merchants.containsKey(id);
    }

    public void saveMerchant(Merchant merchant) {
        merchants.put(merchant.getId(), merchant);
    }

    public Merchant updateMerchant(Integer id, UpdateMerchantDTO updateMerchantDTO) {
        Merchant saved_merchant = merchants.get(id);

        saved_merchant.setName(
                updateMerchantDTO.getName()
        );

        return saved_merchant;
    }

    public Merchant deleteMerchant(Integer id) {
        Merchant saved_merchant = merchants.get(id);

        merchants.remove(id);

        return saved_merchant;
    }

    public Merchant getMerchantById(Integer id) {
        return merchants.get(id);
    }

}
