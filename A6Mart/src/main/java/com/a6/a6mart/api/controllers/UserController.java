package com.a6.a6mart.api.controllers;

import com.a6.a6mart.api.models.AppUser;
import com.a6.a6mart.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller

public class UserController {
    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/user")
    public String showUser(Model model){
        //User Log in Role and detail
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser appUser = appUserRepository.findByEmail(email);
        model.addAttribute("appUser", appUser);
        String userRole = appUser.getRole();
        boolean isAdmin = false;
        if(userRole.equals("Admin")) {
            isAdmin = true;
        }
        model.addAttribute("userRole", userRole);
        model.addAttribute("isAdmin", isAdmin);

        // Get All user
        List<AppUser> alluser = appUserRepository.findAll();
        model.addAttribute("alluser", alluser);
        return "user";
    }
}
