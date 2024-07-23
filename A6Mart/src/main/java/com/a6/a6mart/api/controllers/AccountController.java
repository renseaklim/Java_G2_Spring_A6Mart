package com.a6.a6mart.api.controllers;

import com.a6.a6mart.api.models.AppUser;
import com.a6.a6mart.api.models.RegisterDTO;
import com.a6.a6mart.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class AccountController {
    @Autowired
    private AppUserRepository repo;

    @GetMapping("/register")
    public String register(Model model)
    {
        RegisterDTO registerDTO = new RegisterDTO();
        model.addAttribute(registerDTO);
        model.addAttribute("success", false);

        return "register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute RegisterDTO registerDTO, BindingResult result)
    {
        if(!registerDTO.getPassword().equals(registerDTO.getConfirmPassword()))
        {
            result.addError(new FieldError("registerDTO", "confirmPassword",
                    "Password and Confirm Password does not match!"));
        }

        AppUser appUser = repo.findByEmail(registerDTO.getEmail());
        if(appUser != null)
        {
            result.addError(new FieldError("registerDTO", "email",
                    "Email address is already used!"));
        }

        if(result.hasErrors()){
            return "register";
        }

        try
        {
             // create the account or add registerDTO to AppUser
            var bCryptEncoder = new BCryptPasswordEncoder();

            AppUser newUser = new AppUser();
            newUser.setFirstname(registerDTO.getFirstname());
            newUser.setLastname(registerDTO.getLastname());
            newUser.setEmail(registerDTO.getEmail());
            newUser.setAddress(registerDTO.getAddress());
            newUser.setPhone(registerDTO.getPhone());
            newUser.setRole(registerDTO.getRole());
            newUser.setCreatedAt(new Date());
            newUser.setPassword(bCryptEncoder.encode(registerDTO.getPassword()));
            repo.save(newUser);

            model.addAttribute("registerDTO", new RegisterDTO());
            model.addAttribute("success", true);
        }
        catch(Exception ex)
        {
            result.addError(new FieldError("registerDTO", "firstName", ex.getMessage()));
        }
        return "register";
    }

}
