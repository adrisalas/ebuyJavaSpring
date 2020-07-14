package com.example.ebuydb.controller;

import com.example.ebuydb.entity.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class NavController {

    @GetMapping
    public String index(HttpSession session){
        Account account = (Account) session.getAttribute("user");
        if(session.getAttribute("user") != null){
            if(account.getIsadmin() == 1){
                return "redirect:/usuarioslistar";
            } else {
                return "redirect:/productoslistar";
            }
        }
        return "login";
    }

    @GetMapping("logout")
    public String logOut(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
}
