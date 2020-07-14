package com.example.ebuydb.controller;


import com.example.ebuydb.crypto.Hash;
import com.example.ebuydb.dao.AccountRepository;
import com.example.ebuydb.dto.AccountLoginDTO;
import com.example.ebuydb.dto.AccountSessionDTO;
import com.example.ebuydb.dto.AccountSignInDTO;
import com.example.ebuydb.entity.Account;
import com.example.ebuydb.service.LoginService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AccountRepository accountRepository;
    private final LoginService loginService;

    @PostMapping("/authentification")
    public String authentification(AccountLoginDTO accountLoginDTO, HttpServletRequest request, HttpSession session){

        String loginStatus = loginService.authentification(accountLoginDTO,session);

        if(loginStatus.equalsIgnoreCase("")) {
            AccountSessionDTO account = (AccountSessionDTO)session.getAttribute("user");
            if(account.getIsadmin()== 0){
                return "redirect:/productoslistar";
            } else {
                return "redirect:/usuarioslistar";
            }
        } else {
            request.setAttribute("loginStatus", loginStatus);
            request.setAttribute("emailOrNickname", accountLoginDTO.getEmailOrNickname());
            return "login";
        }
    }

    @PostMapping("/signin")
    public String signIn(AccountSignInDTO accountSignInDTO, HttpServletRequest request){

        String signinstatus = loginService.signin(accountSignInDTO);

        if(signinstatus.equals("") || signinstatus == null){
            request.setAttribute("signinstatusok", "Registrado con exito");
        } else {
            request.setAttribute("signinstatus", signinstatus);
            request.setAttribute("nickname", accountSignInDTO.getNickname());
            request.setAttribute("email1", accountSignInDTO.getEmail1());

            request.setAttribute("email2", accountSignInDTO.getEmail2());
        }
        return "login";
    }
}
