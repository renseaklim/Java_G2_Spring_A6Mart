package com.a6.a6mart.api.controllers;

import com.a6.a6mart.api.models.AppUser;
import com.a6.a6mart.api.models.MonthlyProductSale;
import com.a6.a6mart.api.models.MonthlySale;
import com.a6.a6mart.repositories.AppUserRepository;
import com.a6.a6mart.repositories.MonthlyProductSaleRepository;
import com.a6.a6mart.repositories.MonthlySaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ReportController {
    @Autowired
    private MonthlyProductSaleRepository monthlyProductSaleRepository;

    @Autowired
    private MonthlySaleRepository monthlySaleRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @GetMapping("/monthlyProductSales")
    public String getMonthlyProductSales(@RequestParam(required = false) Integer month,
                                         @RequestParam(required = false) Integer year,
                                         Model model) {
        if (month == null || year == null) {
            // Default to the current month and year
            LocalDate now = LocalDate.now();
            month = now.getMonthValue();
            year = now.getYear();
        }

        List<MonthlyProductSale> monthlyProductSales = monthlyProductSaleRepository.findByMonthAndYear(month, year);
        model.addAttribute("monthlyProductSales", monthlyProductSales);
        model.addAttribute("month", month);
        model.addAttribute("year", year);

        //User Role access and All user detail
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

        return "report/monthlyProductSale";
    }

    @GetMapping("/monthlySales")
    public String getMonthlySales(@RequestParam(required = false) Integer month,
                                  @RequestParam(required = false) Integer year,
                                  Model model) {
        if (month == null || year == null) {
            // Default to the current month and year
            LocalDate now = LocalDate.now();
            month = now.getMonthValue();
            year = now.getYear();
        }

        MonthlySale monthlySale = monthlySaleRepository.findByMonthAndYear(month, year);
        model.addAttribute("monthlySale", monthlySale);
        model.addAttribute("month", month);
        model.addAttribute("year", year);

        //User Role access and All user detail
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

        return "report/monthlySale";
    }
}
