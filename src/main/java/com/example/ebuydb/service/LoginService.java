package com.example.ebuydb.service;

import com.example.ebuydb.crypto.Hash;
import com.example.ebuydb.dao.AccountRepository;
import com.example.ebuydb.dto.AccountLoginDTO;
import com.example.ebuydb.dto.AccountSessionDTO;
import com.example.ebuydb.dto.AccountSignInDTO;
import com.example.ebuydb.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AccountRepository accountRepository;

    public String authentification(AccountLoginDTO accountLoginDTO, HttpSession session){
        String loginStatus = "";

        Account account = accountRepository.findByEmail(accountLoginDTO.getEmailOrNickname());
        if (account == null){
            account = accountRepository.findByNickname(accountLoginDTO.getEmailOrNickname());
        }

        String passwordHash = Hash.SHA256(accountLoginDTO.getPassword());

        if (account == null) {
            loginStatus = "El usuario no se encuentra en la base de datos";
        } else if (!passwordHash.equals(account.getPassword())) {
            loginStatus = "La clave es incorrecta";
        } else {
            session.setAttribute("user",accountToAccountSessionDTO(account));
        }
        return loginStatus;
    }

    public String signin(AccountSignInDTO accountSignInDTO){
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        String signinstatus = "";
        Account user;
        if(accountSignInDTO.getNickname().length() > 20){
            signinstatus = "ERROR. El nickname no debes tener mas de 20 carácteres";
        } else if(accountSignInDTO.getEmail1().length() > 50){
            signinstatus = "ERROR. El email no debe tener mas de 50 carácteres";
        } else if(accountSignInDTO.getPassword1().length() > 20){
            signinstatus = "ERROR. La contraseña no debes tener mas de 20 carácteres";
        } else if (accountSignInDTO.getNickname().equals("") || accountSignInDTO.getEmail1().equals("") || accountSignInDTO.getEmail2().equals("") || accountSignInDTO.getPassword1().equals("") || accountSignInDTO.getPassword2().equals("")){
            signinstatus = "ERROR. Debes rellenar todos los campos para registrarte";
        } else if(!accountSignInDTO.getEmail1().matches(emailRegex)){
            signinstatus = "ERROR. Email invalido";
        }else if(!accountSignInDTO.getEmail1().equalsIgnoreCase(accountSignInDTO.getEmail2())){
            signinstatus = "ERROR. Los email no coinciden";
        } else if (!accountSignInDTO.getPassword1().equals(accountSignInDTO.getPassword2())){
            signinstatus = "ERROR. Las contraseñas no coinciden";
        } else {
            user = accountRepository.findByEmail(accountSignInDTO.getEmail1());
            if (user != null){
                signinstatus = "ERROR. El email ya tiene una cuenta asociada.";
            } else {
                user = accountRepository.findByNickname(accountSignInDTO.getNickname());
                if(user != null){
                    signinstatus = "ERROR. El nickname no esta disponible";
                } else {
                    Account newUser = new Account();
                    newUser.setEmail(accountSignInDTO.getEmail1());
                    newUser.setIsadmin((short) 0);
                    newUser.setNickname(accountSignInDTO.getNickname());
                    newUser.setPassword(Hash.SHA256(accountSignInDTO.getPassword1()));
                    accountRepository.save(newUser);
                }
            }
        }
        return signinstatus;
    }

    private AccountSessionDTO accountToAccountSessionDTO(Account account){
        AccountSessionDTO accountSessionDTO = new AccountSessionDTO();
        accountSessionDTO.setUserId(account.getUserId());
        accountSessionDTO.setNickname(account.getNickname());
        accountSessionDTO.setEmail(account.getEmail());
        accountSessionDTO.setIsadmin(account.getIsadmin());
        return accountSessionDTO;
    }

}
