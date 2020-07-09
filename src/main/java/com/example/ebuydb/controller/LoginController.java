package com.example.ebuydb.controller;


import com.example.ebuydb.dao.AccountRepository;
import com.example.ebuydb.dao.ReviewRepository;
import com.example.ebuydb.entity.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class LoginController {

    private final AccountRepository accountRepository;

    @GetMapping
    public String index(Model model, HttpSession session){
        Account account = (Account) session.getAttribute("user");
        if(session.getAttribute("user") != null){
            if(account.getIsadmin() == 1){
                return "mock";
            } else {
                return "listadoProductos";
            }
        }
        return "login";
    }

    @PostMapping("/authentification")
    public String authentification(@RequestParam(value = "emailOrNickname") String emailOrNickname, @RequestParam(value = "password") String password,
                                   HttpServletRequest request, HttpSession session){
        Account user;
        String loginStatus;

        user = accountRepository.findByEmail(emailOrNickname);
        if (user == null){
            user = accountRepository.findByNickname(emailOrNickname);
        }

        if (user == null) {
            loginStatus = "El usuario no se encuentra en la base de datos";
        } else if (!password.equals(user.getPassword())) {
            loginStatus = "La clave es incorrecta";
        } else {
            session.setAttribute("user", user);

            if(user.getIsadmin()== 0){
                //TODO return userMenu
                return "mock";
            } else {
                //TODO return adminMenu
                return "mock";
            }
        }
        request.setAttribute("loginStatus", loginStatus);
        request.setAttribute("emailOrNickname", emailOrNickname);
        return "login";
    }

    @PostMapping("/signin")
    public String signIn(@RequestParam(value = "nickname") String nickname,
                         @RequestParam(value = "email1") String email1,
                         @RequestParam(value = "email2") String email2,
                         @RequestParam(value = "password1") String password1,
                         @RequestParam(value = "password2") String password2,
                         HttpServletRequest request){
        String signinstatus = "";
        String signinstatusok = "";
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        request.setAttribute("nickname", nickname);
        request.setAttribute("email1", email1);
        request.setAttribute("email2", email2);

        Account user;
        if(nickname.length() > 20){
            request.setAttribute("signinstatus", "ERROR. El nickname no debes tener mas de 20 carácteres");
        } else if(email1.length() > 50){
            request.setAttribute("signinstatus", "ERROR. El email no debe tener mas de 50 carácteres");
        } else if(password1.length() > 20){
            request.setAttribute("signinstatus", "ERROR. La contraseña no debes tener mas de 20 carácteres");
        } else if (nickname.equals("") || email1.equals("") || email2.equals("") || password1.equals("") || password2.equals("")){
            request.setAttribute("signinstatus", "ERROR. Debes rellenar todos los campos para registrarte");
        } else if(!email1.matches(emailRegex)){
            request.setAttribute("signinstatus", "ERROR. Email invalido");
        }else if(!email1.equalsIgnoreCase(email2)){
            request.setAttribute("signinstatus", "ERROR. Los email no coinciden");
        } else if (!password1.equals(password2)){
            request.setAttribute("signinstatus", "ERROR. Las contraseñas no coinciden");
        } else {
            user = accountRepository.findByEmail(email1);
            if (user != null){
                signinstatus = "ERROR. El email ya tiene una cuenta asociada.";
                request.setAttribute("signinstatus", signinstatus);
            } else {
                user = accountRepository.findByNickname(nickname);
                if(user != null){
                    request.setAttribute("signinstatus", "ERROR. El nickname no esta disponible");
                } else {
                    request.setAttribute("signinstatus", "");
                    request.setAttribute("signinstatusok", "Registrado con exito");
                    request.removeAttribute("nickname");
                    request.removeAttribute("email1");
                    request.removeAttribute("email2");
                    request.removeAttribute("password1");
                    request.removeAttribute("password2");

                    Account newUser = new Account();
                    newUser.setEmail(email1);
                    newUser.setIsadmin((short) 0);
                    newUser.setNickname(nickname);
                    newUser.setPassword(password1);
                    accountRepository.save(newUser);
                }
            }
        }
        return "login";
    }

    @GetMapping("signout")
    public String signOut(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
}
