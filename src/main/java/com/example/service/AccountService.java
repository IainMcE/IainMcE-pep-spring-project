package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public boolean hasAccountWithUsername(String username){
        return accountRepository.existsByUsername(username);
    }

    public boolean hasAccountWithId(int accountId){
        return accountRepository.existsByAccountId(accountId);
    }

    public Account validateAccountPassword(Account account){
        Optional<Account> matching = accountRepository.findByUsername(account.getUsername());
        if(matching.isPresent() && matching.get().getPassword().equals(account.getPassword())){
            return matching.get();
        }
        return null;
    }

    public Account addAccount(Account account){
        return accountRepository.save(account);
    }
}
