package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.DTO.UpdateUserDTO;
import com.example.capstone1amazon.Model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
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

    public boolean containsEmail(String email, Integer id) {
        if(emails.containsKey(email)) {
            return !Objects.equals(users.get(emails.get(email)).getId(), id);
        }

        return false;
    }


    public void createUser(User user) {
        users.put(user.getId(), user);
        emails.put(user.getEmail(), user.getId());
    }


    public User updateUser(Integer id, UpdateUserDTO updateUserDTO) {
        User saved_user = users.get(id);

        saved_user.setUsername(updateUserDTO.getUsername());
        saved_user.setPassword(updateUserDTO.getPassword());
        saved_user.setBalance(updateUserDTO.getBalance());
        saved_user.setEmail(updateUserDTO.getEmail().toLowerCase());
        saved_user.setRole(updateUserDTO.getRole().toLowerCase());

        return saved_user;
    }

    public User deleteUser(Integer id) {
        User saved_user = users.get(id);

        users.remove(id);
        emails.remove(saved_user.getEmail());

        return saved_user;
    }

    public User getUserById(Integer id) {
        return users.get(id);
    }

    public User getUserByEmail(String email) {
        return users.get(emails.get(email));
    }

    public boolean validateBalance(double price, Integer id) {
        return getUserById(id).getBalance() < price;
    }

    public HashMap<String, Double> buyProduct(Integer id, double price) {
        User user = getUserById(id);
        Double previous_balance = user.getBalance();

        user.setBalance((previous_balance - price));
        Double new_balance = user.getBalance();


        HashMap<String, Double> balanceResponse = new HashMap<>();
        balanceResponse.put("previous_balance", previous_balance);
        balanceResponse.put("new_balance", new_balance);

        return balanceResponse;
    }
}
