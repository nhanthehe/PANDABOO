package com.asm.pandaboo.services;

import com.asm.pandaboo.entities.AccountEntity;
import com.asm.pandaboo.jpa.AccountsJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountsJPA accountsJPA;

    public AccountEntity findById(String id) {
        return accountsJPA.findById(id).orElse(null);
    }

    public AccountEntity findByUsername(String username) {
        return accountsJPA.getAccountsEntityByAcc(username);
    }

    public void save(AccountEntity account) {
        accountsJPA.save(account);
    }

    public List<AccountEntity> findAll() {
        return accountsJPA.findAll();
    }
}
