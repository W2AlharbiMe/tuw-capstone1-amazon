package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.Model.Merchant;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;

@Service
public class MerchantService {

    private final HashMap<Integer, Merchant> merchants = new HashMap<>();


    public Collection<Merchant> getAllMerchants() {
        return merchants.values();
    }

    public boolean containsId(Integer id) {
        return merchants.containsKey(id);
    }

}
