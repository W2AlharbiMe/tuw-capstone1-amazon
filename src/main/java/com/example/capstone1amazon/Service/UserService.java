package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.Model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Stream;

@Service
public class UserService {
    private final HashMap<Integer, User> users = new HashMap<>();
    private final HashMap<String, Integer> emails = new HashMap<>();


    public Collection<User> getAllUsers(String role) {
        Collection<User> all = users.values();
        boolean presentRole = role.isEmpty();
        boolean isAdmin = !presentRole &&  role.equalsIgnoreCase("admin");
        boolean isCustomer = !presentRole && role.equalsIgnoreCase("customer");

        if(presentRole) return all;


        if(isAdmin) {
            // only show admins
            return all.stream().filter(u -> u.getRole().equals("admin")).toList();
        }

        if(isCustomer) {
            // only show customers
            return all.stream().filter(u -> u.getRole().equals("customer")).toList();
        }

        return all;
    }

    public boolean containsId(Integer id) {
        return users.containsKey(id);
    }

    public boolean containsEmail(String email) {
        return emails.containsKey(email);
    }

    public void createUser(User user) {
        users.put(user.getId(), user);
        emails.put(user.getEmail(), user.getId());
    }


}
