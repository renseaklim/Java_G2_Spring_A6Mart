package com.a6.a6mart.api.controllers;

import com.a6.a6mart.api.models.AppUser;
import com.a6.a6mart.api.models.StockIn;
import com.a6.a6mart.repositories.AppUserRepository;
import com.a6.a6mart.repositories.ProductRepository;
import com.a6.a6mart.repositories.SellRepository;
import com.a6.a6mart.repositories.StockinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class HomeController {

    @Autowired
    private AppUserRepository appUserRepository;
    @GetMapping({"", "/"})
    public String welcome(){
       return "index";
    }

    @Autowired
    private SellRepository sellItemRepo;

    @Autowired
    private StockinRepository stockinRepository;

    @Autowired
    private ProductRepository productRepository;


    @GetMapping( "/home")
    public String home(Model model){
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

        //Total Sold Product Quantity
        int totalSoldProduct = sellItemRepo.getTotalSoldQuantity();
        model.addAttribute("totalSoldProduct", totalSoldProduct);


        //Total Expense for StockIn
        double totalExpense = stockinRepository.getTotalStockInAmount();
        model.addAttribute("totalExpense", String.format("%.2f", totalExpense));
        //Total sell amount
        double totalSellAmount = sellItemRepo.getTotalSalesAmount();
        model.addAttribute("totalSellAmount", String.format("%.2f", totalSellAmount));


        //Count all Staff
        int allStaff = appUserRepository.countUsersByRoleStaff();
        model.addAttribute("allStaff", allStaff);

        //Total Product uninquely
        long totalProductCount = productRepository.count();
        model.addAttribute("totalProductCount", totalProductCount);

        return "home";
    }
}
