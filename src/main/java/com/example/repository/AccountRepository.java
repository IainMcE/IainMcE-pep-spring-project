package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
    public boolean existsByUsername(String username);
    public boolean existsByAccountId(int accountId);
    public Optional<Account> findByUsername(String username);
}
