package com.a6.a6mart.api.controllers;

import com.a6.a6mart.api.models.*;
import com.a6.a6mart.dao.ReceiptDAOImplement;
import com.a6.a6mart.repositories.AppUserRepository;
import com.a6.a6mart.repositories.ProductRepository;
import com.a6.a6mart.repositories.ReceiptRepository;
import com.a6.a6mart.repositories.SellRepository;
import com.a6.a6mart.services.ProductService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/pos")
public class PosController {
    @Autowired
    private SellRepository  sellrepo;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReceiptRepository receiptRepository;
    private List<SellItemDTO> cart = new ArrayList<>();

    @Autowired
    private ProductService productService;

    @Autowired
    private AppUserRepository appUserRepository;
    private ReceiptDAOImplement receiptDAOImplement;

    @GetMapping({"", "/"})
    public String showPosPage(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        AddItemDTO addItem = new AddItemDTO();
        model.addAttribute("addItem", addItem);
        SellItemDTO sellItemDTO = new SellItemDTO();
        model.addAttribute("sellItemDTO",sellItemDTO);
        Product foundProduct = new Product();
        model.addAttribute("foundProduct", foundProduct);
        SellItem sellItem = new SellItem();
        model.addAttribute("sellItem", sellItem);
        model.addAttribute("cart", cart);


        //Current Date
        Date currentDate = new Date();
        model.addAttribute("currentDate", currentDate);
        //total cost of all items in cart
        double totalItemCost = 0.0;
        totalItemCost = cart.stream()
                .mapToDouble(SellItemDTO::getTotal)
                .sum();
        model.addAttribute("totalItemCost", totalItemCost);

        //Receipt number
        int lastestReceiptnum = receiptRepository.countDistinctReceiptIds() + 1;
        model.addAttribute("lastestReceiptnum", lastestReceiptnum);

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
        return "pos";
    }

    @PostMapping({"","/"})
    public String addSellItem(@Valid @ModelAttribute AddItemDTO addItem, Model model, BindingResult result){
        Product foundProduct = productRepository.findById(addItem.getId()).get();
        model.addAttribute("foundProduct", foundProduct);
        model.addAttribute("addItem", new AddItemDTO());

        SellItemDTO sellItemDTO = new SellItemDTO();
        model.addAttribute("sellItemDTO",sellItemDTO);

        //Add item to Receipt
        ReceiptDTO receiptDTO = new ReceiptDTO();
        model.addAttribute("receiptDTO",receiptDTO);
        model.addAttribute("cart", cart);

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

        return "pos";
    }

    @GetMapping("/addToCart")
    public String showAddToCart(@RequestParam int id, @ModelAttribute AddItemDTO addItem, Model model) {
        model.addAttribute("addItem", addItem);
        Product foundProduct = productRepository.findById(id).orElse(null);
        model.addAttribute("foundProduct", foundProduct);

        // Add Item to SellItem
        SellItemDTO sellItemDTO = new SellItemDTO();
        model.addAttribute("sellItemDTO", sellItemDTO);


        if (foundProduct != null) {
            sellItemDTO.setProductId(foundProduct.getId());
            sellItemDTO.setName(foundProduct.getName());
            sellItemDTO.setPrice(foundProduct.getPrice());

        }
        model.addAttribute("cart", cart);


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
        return "pos";
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        List<SellItem> cartItems = sellrepo.findAll();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cart", cart);

        return "redirect:/pos";
    }


    @PostMapping("/addToCart/sell")
    public String addToCart(@ModelAttribute SellItemDTO sellItemDTO, Model model) {
        Product foundProduct = productService.findById(sellItemDTO.getProductId());
        model.addAttribute("foundProduct", foundProduct);
        AddItemDTO addItem = new AddItemDTO();
        model.addAttribute("addItem", addItem);
        SellItem sellItem = new SellItem();
        Date date = new Date();
        model.addAttribute("cart", cart);

        double total = foundProduct.getPrice() * sellItemDTO.getQuantity();
        if (sellItemDTO.getQuantity() > 0) {
            sellItemDTO.setTotal(total);
            sellItem.setProductId(sellItemDTO.getProductId());
            sellItem.setDate(date);
            sellItem.setName(sellItemDTO.getName());
            sellItem.setQuantity(sellItemDTO.getQuantity());
            sellItem.setPrice(sellItemDTO.getPrice());
            sellItem.setTotal(total);
            sellrepo.save(sellItem);
        }
        // Update the product's quantity
        int updatedQuantity = foundProduct.getQuantity() - sellItem.getQuantity();
        foundProduct.setQuantity(updatedQuantity);
        productRepository.save(foundProduct);
        model.addAttribute("sellItem", sellItem);
        cart.add(sellItemDTO);

        List<Object> cartItems = new ArrayList<>();
        cartItems.add(sellItem);
        model.addAttribute("cartItems", cartItems);


        return "redirect:/pos/cart";
    }
    @PostMapping("/receipt")
    public String createReceipt(Model model) {
        // Create a new receipt
        Receipt receipt = new Receipt();
        List<ReceiptItem> receiptItems = new ArrayList<>();
        double total = 0;

        // Retrieve items from the cart
        for (SellItemDTO sellItemDTO : cart) {
            ReceiptItem receiptItem = new ReceiptItem();
            Product product = productRepository.findById(sellItemDTO.getProductId()).orElse(null);
            if (product != null) {
                receiptItem.setProduct(product);
                receiptItem.setName(sellItemDTO.getName());
                receiptItem.setPrice(sellItemDTO.getPrice());
                receiptItem.setQuantity(sellItemDTO.getQuantity());
                receiptItem.setTotal(sellItemDTO.getTotal());
                receiptItem.setReceipt(receipt);
                receiptItems.add(receiptItem);
                total += sellItemDTO.getTotal();
            }
        }

        receipt.setItems(receiptItems);
        receipt.setTotal(total);

        // Save the receipt to the database
        receiptRepository.save(receipt);

        // Clear the cart
        cart.clear();

        // Redirect to POS page
        return "redirect:/pos";
    }

}
