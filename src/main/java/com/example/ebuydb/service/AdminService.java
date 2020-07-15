package com.example.ebuydb.service;

import com.example.ebuydb.dao.AccountRepository;
import com.example.ebuydb.dto.AccountDTO;
import com.example.ebuydb.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AccountRepository accountRepository;

    public List<AccountDTO> getAllAccounts(){

        List<Account> accounts = accountRepository.findAll();

        return accountsToAccountsDTO(accounts);
    }

    public AccountDTO getAccountById(String clienteId){

        AccountDTO accountDTO;

        if(clienteId == null || clienteId.equals("")){
            accountDTO = null;
        } else {
            Account account = accountRepository.findByAccountId(new Integer(clienteId));
            if(account == null){
                accountDTO = null;
            } else {
                accountDTO = accountToAccountDTO(account);
            }
        }
        return accountDTO;
    }

    public void saveAccount(AccountDTO accountDTO){
        Account account = accountRepository.findByAccountId(accountDTO.getUserId());
        account.setNickname(accountDTO.getNickname());
        account.setEmail(accountDTO.getEmail());
        account.setPassword(accountDTO.getPassword());
        account.setIsadmin(accountDTO.getIsadmin());

        accountRepository.save(account);
    }

    public void deleteAccount(String clienteId){
        if(clienteId != null) {
            Account cliente = accountRepository.findByAccountId(Integer.parseInt(clienteId));
            if(cliente != null){
                accountRepository.delete(cliente);
            }
        }
    }

    private List<AccountDTO> accountsToAccountsDTO(List<Account> accounts){
        List<AccountDTO> accountsDTO = new ArrayList<>();
        for (Account account: accounts){
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setEmail(account.getEmail());
            accountDTO.setIsadmin(account.getIsadmin());
            accountDTO.setNickname(account.getNickname());
            accountDTO.setPassword(account.getPassword());
            accountDTO.setUserId(account.getUserId());
            accountsDTO.add(accountDTO);
        }
        return accountsDTO;
    }

    private AccountDTO accountToAccountDTO(Account account){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(account.getUserId());
        accountDTO.setEmail(account.getEmail());
        accountDTO.setNickname(account.getNickname());
        accountDTO.setPassword(account.getPassword());
        accountDTO.setIsadmin(account.getIsadmin());
        return accountDTO;
    }
}
