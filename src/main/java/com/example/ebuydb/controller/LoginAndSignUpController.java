package com.example.ebuydb.controller;


import com.example.ebuydb.dao.AccountRepository;
import com.example.ebuydb.dto.AccountLoginDTO;
import com.example.ebuydb.dto.AccountSignUpDTO;
import com.example.ebuydb.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginAndSignUpController {

    private final AccountRepository accountRepository;
    private final LoginService loginService;

    @PostMapping("/authentification")
    public String authentification(AccountLoginDTO accountLoginDTO,
                                   Model model, HttpSession session){

        String loginStatus = loginService.authentification(accountLoginDTO,session);

        if(loginStatus.isEmpty()) {
            return "redirect:/";
        } else {
            model.addAttribute("loginStatus", loginStatus);
            model.addAttribute("emailOrNickname", accountLoginDTO.getEmailOrNickname());
            return "login";
        }
    }

    @PostMapping("/signup")
    public String signUp(AccountSignUpDTO accountSignUpDTO, Model model){

        String signupstatus = loginService.signup(accountSignUpDTO);

        if(signupstatus.equals("") || signupstatus == null){
            model.addAttribute("signupstatusok", "Registrado con exito");
        } else {
            model.addAttribute("signupstatus", signupstatus);
            model.addAttribute("nickname", accountSignUpDTO.getNickname());
            model.addAttribute("email1", accountSignUpDTO.getEmail1());
            model.addAttribute("email2", accountSignUpDTO.getEmail2());
        }
        return "login";
    }
}
