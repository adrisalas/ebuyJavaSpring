package com.example.ebuydb.controller;


import com.example.ebuydb.dao.AccountRepository;
import com.example.ebuydb.entity.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@AllArgsConstructor
public class UsuariosController {

    private final AccountRepository accountRepository;

    @GetMapping("/usuarioslistar")
    public String usuariosListar(HttpSession session, HttpServletRequest request){
        Account usuario = (Account) session.getAttribute("user");

        if(usuario == null || usuario.getIsadmin() == 0){
            return "redirect:/";
        }

        List<Account> listaUsuarios = accountRepository.findAll();

        request.setAttribute("listaUsuarios", listaUsuarios);

        //TODO go to template
        return "listadoUsuarios";
    }

    @GetMapping("/usuarioseditar")
    public String usuariosEditar(HttpSession session, HttpServletRequest request){
        Account usuario = (Account) session.getAttribute("user");
        if(usuario == null || usuario.getIsadmin() == 0){
            return "redirect:/";
        }

        String cliente_ID = request.getParameter("userId");
        if(cliente_ID == null){
            return "redirect:/usuarioslistar";
        }

        Account cliente = accountRepository.findByAccountId(new Integer(cliente_ID));
        if(cliente == null){
            return "redirect:/usuarioslistar";
        }

        request.setAttribute("cliente", cliente);
        //TODO go to template
        return "usuarioFormulario";
    }

    @PostMapping("/usuariosguardar")
    public String usuariosGuardar(HttpSession session, HttpServletRequest request){
        Account usuario = (Account) session.getAttribute("user");

        if(usuario == null || usuario.getIsadmin() == 0){
            return "redirect:/";
        }

        String cliente_ID = request.getParameter("userId");
        Account cliente = accountRepository.findByAccountId(Integer.parseInt(cliente_ID));
        cliente.setNickname(request.getParameter("nickname"));
        cliente.setEmail(request.getParameter("email"));
        cliente.setPassword(request.getParameter("pwd"));
        String isadmin = request.getParameter("isAdmin");

        isadmin = isadmin==null ? "0" : "1";
        cliente.setIsadmin(new Short(isadmin));

        accountRepository.save(cliente);
        return "redirect:/usuarioslistar";
    }

    @GetMapping("/usuariosborrar")
    public String usuariosBorrar(HttpSession session, HttpServletRequest request){
        Account usuario = (Account) session.getAttribute("user");

        if(usuario == null || usuario.getIsadmin() == 0){
            return "redirect:/";
        }

        String cliente_ID = request.getParameter("userId");
        if(cliente_ID != null) {
            Account cliente = accountRepository.findByAccountId(new Integer(cliente_ID));
            if(cliente != null){
                accountRepository.delete(cliente);
            }
        }
        return "redirect:/usuarioslistar";
    }




}
