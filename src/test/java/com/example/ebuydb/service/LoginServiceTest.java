package com.example.ebuydb.service;


import com.example.ebuydb.crypto.Hash;
import com.example.ebuydb.dao.AccountRepository;
import com.example.ebuydb.dto.AccountLoginDTO;
import com.example.ebuydb.dto.AccountSessionDTO;
import com.example.ebuydb.dto.AccountSignUpDTO;
import com.example.ebuydb.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private HttpSession httpSession;

    private LoginService loginService;
    private String email;
    private String password;

    @BeforeEach
    public void setup(){
        loginService = new LoginService(accountRepository);
        email = "test@test.com";
        password = "1234";
    }

    @Test
    public void successSignUp(){
        AccountSignUpDTO accountSignUpDTO = new AccountSignUpDTO();
        accountSignUpDTO.setEmail1(email);
        accountSignUpDTO.setEmail2(email);
        accountSignUpDTO.setNickname(email.split("@")[0]);
        accountSignUpDTO.setPassword1(password);
        accountSignUpDTO.setPassword2(password);

        loginService.signup(accountSignUpDTO);
        verify(accountRepository).save(argThat(account -> account.getEmail().equalsIgnoreCase(email)
                && account.getIsadmin() == 0
                && account.getNickname().equals(email.split("@")[0])
                && account.getPassword().equals(Hash.SHA256(password))));
    }

    @Test
    public void incorrectSignUpEmailsDontMatch(){
        AccountSignUpDTO accountSignUpDTO = new AccountSignUpDTO();
        accountSignUpDTO.setEmail1(email);
        accountSignUpDTO.setEmail2(email+"not");
        accountSignUpDTO.setNickname(email.split("@")[0]);
        accountSignUpDTO.setPassword1(password);
        accountSignUpDTO.setPassword2(password);

        String signupstatus = loginService.signup(accountSignUpDTO);
        assertEquals("ERROR. Los email no coinciden",signupstatus);
    }

    @Test
    public void incorrectSignUpPasswordsDontMatch(){
        AccountSignUpDTO accountSignUpDTO = new AccountSignUpDTO();
        accountSignUpDTO.setEmail1(email);
        accountSignUpDTO.setEmail2(email);
        accountSignUpDTO.setNickname(email.split("@")[0]);
        accountSignUpDTO.setPassword1(password);
        accountSignUpDTO.setPassword2(password+"not");

        String signupstatus = loginService.signup(accountSignUpDTO);
        assertEquals("ERROR. Las contraseñas no coinciden",signupstatus);
    }

    @Test
    public void incorrectSignUpEmailInUse(){
        AccountSignUpDTO accountSignUpDTO = new AccountSignUpDTO();
        accountSignUpDTO.setEmail1(email);
        accountSignUpDTO.setEmail2(email);
        accountSignUpDTO.setNickname(email.split("@")[0]);
        accountSignUpDTO.setPassword1(password);
        accountSignUpDTO.setPassword2(password);

        when(accountRepository.findByEmail(email)).thenReturn(new Account());
        String signupstatus = loginService.signup(accountSignUpDTO);
        assertEquals("ERROR. El email ya tiene una cuenta asociada.",signupstatus);
    }

    @Test
    public void incorrectSignUpNicknameInUse(){
        AccountSignUpDTO accountSignUpDTO = new AccountSignUpDTO();
        accountSignUpDTO.setEmail1(email);
        accountSignUpDTO.setEmail2(email);
        accountSignUpDTO.setNickname(email.split("@")[0]);
        accountSignUpDTO.setPassword1(password);
        accountSignUpDTO.setPassword2(password);

        when(accountRepository.findByNickname(email.split("@")[0])).thenReturn(new Account());
        String signupstatus = loginService.signup(accountSignUpDTO);
        assertEquals("ERROR. El nickname no esta disponible",signupstatus);
    }
    @Test
    public void incorrectSignUpNicknameTooLong(){
        AccountSignUpDTO accountSignUpDTO = new AccountSignUpDTO();
        accountSignUpDTO.setEmail1(email);
        accountSignUpDTO.setEmail2(email);
        accountSignUpDTO.setNickname(generateStringWithLength(21));
        accountSignUpDTO.setPassword1(password);
        accountSignUpDTO.setPassword2(password);

        String signupstatus = loginService.signup(accountSignUpDTO);
        assertEquals("ERROR. El nickname no debes tener mas de 20 carácteres",signupstatus);
    }

    public String generateStringWithLength(int length){
        StringBuilder sb = new StringBuilder();
        sb.ensureCapacity(length);
        for(int i = 0; i < length; i++){
            sb.append('a');
        }
        return sb.toString();
    }

    @Test
    public void incorrectSignUpEmailTooLong(){
        AccountSignUpDTO accountSignUpDTO = new AccountSignUpDTO();
        accountSignUpDTO.setEmail1(email+generateStringWithLength(51-email.length()));
        accountSignUpDTO.setEmail2(email+generateStringWithLength(51-email.length()));
        accountSignUpDTO.setNickname(email.split("@")[0]);
        accountSignUpDTO.setPassword1(password);
        accountSignUpDTO.setPassword2(password);

        String signupstatus = loginService.signup(accountSignUpDTO);
        assertEquals("ERROR. El email no debe tener mas de 50 carácteres",signupstatus);
    }
    @Test
    public void incorrectSignUpPasswordTooLong(){
        AccountSignUpDTO accountSignUpDTO = new AccountSignUpDTO();
        accountSignUpDTO.setEmail1(email);
        accountSignUpDTO.setEmail2(email);
        accountSignUpDTO.setNickname(email.split("@")[0]);
        accountSignUpDTO.setPassword1(password+generateStringWithLength(21 - password.length()));
        accountSignUpDTO.setPassword2(password+generateStringWithLength(21 - password.length()));

        String signupstatus = loginService.signup(accountSignUpDTO);
        assertEquals("ERROR. La contraseña no debes tener mas de 20 carácteres",signupstatus);
    }
    @Test
    public void incorrectSignUpEmptyField(){
        AccountSignUpDTO accountSignUpDTO = new AccountSignUpDTO();
        accountSignUpDTO.setEmail1(email);
        accountSignUpDTO.setEmail2(email);
        accountSignUpDTO.setNickname(email.split("@")[0]);
        accountSignUpDTO.setPassword1(password);
        accountSignUpDTO.setPassword2(password);

        for(int i=0;i<5;i++){
            switch(i){
                case 0:
                    accountSignUpDTO.setEmail1("");
                    assertEquals("ERROR. Debes rellenar todos los campos para registrarte",loginService.signup(accountSignUpDTO));
                    break;
                case 1:
                    accountSignUpDTO.setEmail1(email);
                    accountSignUpDTO.setEmail2("");
                    assertEquals("ERROR. Debes rellenar todos los campos para registrarte",loginService.signup(accountSignUpDTO));
                    break;
                case 2:
                    accountSignUpDTO.setEmail2(email);
                    accountSignUpDTO.setNickname("");
                    assertEquals("ERROR. Debes rellenar todos los campos para registrarte",loginService.signup(accountSignUpDTO));
                    break;
                case 3:
                    accountSignUpDTO.setNickname(email.split("@")[0]);
                    accountSignUpDTO.setPassword1("");
                    assertEquals("ERROR. Debes rellenar todos los campos para registrarte",loginService.signup(accountSignUpDTO));
                    break;
                case 4:
                    accountSignUpDTO.setPassword1(password);
                    accountSignUpDTO.setPassword2("");
                    assertEquals("ERROR. Debes rellenar todos los campos para registrarte",loginService.signup(accountSignUpDTO));
                    break;
            }
        }
    }

    @Test
    public void incorrectSignUpEmailBadFormat(){
        AccountSignUpDTO accountSignUpDTO = new AccountSignUpDTO();
        accountSignUpDTO.setEmail1("incorrect@email");
        accountSignUpDTO.setEmail2("incorrect@email");
        accountSignUpDTO.setNickname(email.split("@")[0]);
        accountSignUpDTO.setPassword1(password);
        accountSignUpDTO.setPassword2(password);

        assertEquals("ERROR. Email invalido",loginService.signup(accountSignUpDTO));
    }

    @Test
    public void successLoginWithEmail(){

        AccountLoginDTO accountLoginDTO = new AccountLoginDTO();
        accountLoginDTO.setEmailOrNickname(email);
        accountLoginDTO.setPassword(password);

        Account account = new Account();
        account.setPassword(Hash.SHA256(password));

        when(accountRepository.findByEmail(email)).thenReturn(account);

        String output = loginService.authentification(accountLoginDTO, httpSession);
        assertEquals("",output);
    }

    @Test
    public void successLoginWithNickname(){

        String nickname = email.split("@")[0];

        AccountLoginDTO accountLoginDTO = new AccountLoginDTO();
        accountLoginDTO.setEmailOrNickname(nickname);
        accountLoginDTO.setPassword(password);

        Account account = new Account();
        account.setPassword(Hash.SHA256(password));

        when(accountRepository.findByEmail(nickname)).thenReturn(null);
        when(accountRepository.findByNickname(nickname)).thenReturn(account);

        String output = loginService.authentification(accountLoginDTO, httpSession);
        Mockito.verify(httpSession).setAttribute(eq("user"),any(AccountSessionDTO.class));
        assertEquals("",output);
    }

    @Test
    public void incorrectLoginBadPassword(){

        AccountLoginDTO accountLoginDTO = new AccountLoginDTO();
        accountLoginDTO.setEmailOrNickname(email);
        accountLoginDTO.setPassword(password);

        Account account = new Account();
        account.setPassword(Hash.SHA256("0000"));

        when(accountRepository.findByEmail(email)).thenReturn(account);

        String output = loginService.authentification(accountLoginDTO, httpSession);
        assertEquals("La clave es incorrecta",output);
    }

    @Test
    public void incorrectLoginBadEmailOrNickname(){

        AccountLoginDTO accountLoginDTO = new AccountLoginDTO();
        accountLoginDTO.setEmailOrNickname(email);
        accountLoginDTO.setPassword(password);

        when(accountRepository.findByEmail(email)).thenReturn(null);
        when(accountRepository.findByNickname(email)).thenReturn(null);

        String output = loginService.authentification(accountLoginDTO, httpSession);
        assertEquals("El usuario no se encuentra en la base de datos",output);
    }
}
