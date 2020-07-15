package com.example.ebuydb.controller;


import com.example.ebuydb.dto.AccountDTO;
import com.example.ebuydb.dto.AccountSessionDTO;
import com.example.ebuydb.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class UsuariosController {

    private final AdminService adminService;

    @GetMapping("/usuarioslistar")
    public String usuariosListar(HttpSession session, HttpServletRequest request){
        AccountSessionDTO usuario = (AccountSessionDTO) session.getAttribute("user");

        if(usuario == null || usuario.getIsadmin() == 0){
            return "redirect:/";
        }

        request.setAttribute("listaUsuarios", adminService.getAllAccounts());

        return "listadoUsuarios";
    }

    @GetMapping("/usuarioseditar")
    public String usuariosEditar(@RequestParam("userId") String clienteId, Model model, HttpSession session){
        AccountSessionDTO usuario = (AccountSessionDTO) session.getAttribute("user");
        if(usuario == null || usuario.getIsadmin() == 0){
            return "redirect:/";
        }

        model.addAttribute("cliente", adminService.getAccountById(clienteId));

        return "usuarioFormulario";
    }

    @PostMapping("/usuariosguardar")
    public String usuariosGuardar(@RequestParam("userId") String clienteId,@RequestParam("nickname") String nickname,
                                  @RequestParam("email") String email, @RequestParam("pwd") String pwd, @RequestParam(value = "isAdmin",required = false) String isadmin,
                                  HttpSession session){
        AccountSessionDTO usuario = (AccountSessionDTO) session.getAttribute("user");
        if(usuario == null || usuario.getIsadmin() == 0){
            return "redirect:/";
        }

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(Integer.valueOf(clienteId));
        accountDTO.setPassword(pwd);
        accountDTO.setNickname(nickname);
        accountDTO.setEmail(email);
        isadmin = isadmin==null ? "0" : "1";
        accountDTO.setIsadmin(new Short(isadmin));

        adminService.saveAccount(accountDTO);

        return "redirect:/usuarioslistar";
    }

    @GetMapping("/usuariosborrar")
    public String usuariosBorrar(@RequestParam("userId") String clienteId, HttpSession session){
        AccountSessionDTO usuario = (AccountSessionDTO) session.getAttribute("user");

        if(usuario == null || usuario.getIsadmin() == 0){
            return "redirect:/";
        }

        adminService.deleteAccount(clienteId);
        return "redirect:/usuarioslistar";
    }




}
