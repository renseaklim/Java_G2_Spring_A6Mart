package com.a6.a6mart.api.controllers;

import com.a6.a6mart.api.models.*;
import com.a6.a6mart.dao.ProductDAOImplement;
import com.a6.a6mart.repositories.AppUserRepository;
import com.a6.a6mart.repositories.ProductRepository;
import com.a6.a6mart.repositories.StockinRepository;
import com.a6.a6mart.repositories.StockoutRepository;
import com.a6.a6mart.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository repo;
    @Autowired
    private StockinRepository in_repo;
    @Autowired
    private StockoutRepository out_repo;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ProductDAOImplement productDao;

    @Autowired
    private AppUserRepository appUserRepository;

    //Endpoint to Display product/ stockin / stockout
    @GetMapping({"", "/"})
    public String showProductList(Model model)
    {
        List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);

        List<StockIn> stockIns = in_repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("stockIns", stockIns);

        List<StockOut> stockOuts = out_repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("stockOuts", stockOuts);

        StockinDTO stockinDTO = new StockinDTO();
        model.addAttribute("stockinDTO", stockinDTO);

        StockoutDTO stockoutDTO = new StockoutDTO();
        model.addAttribute("stockoutDTO", stockoutDTO);

        //For Out of stock product
        List<Product> outOfStockProducts = productDao.findOutOfStock();
        model.addAttribute("outOfStockProducts", outOfStockProducts);

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

        return "products/product";
    }


    //Endpoint to Create stockin

    @PostMapping({"", "/"})
    public String addStock(Model model, @Valid @ModelAttribute StockinDTO stockinDTO,
                           BindingResult result)
    {
        Date createAt = new Date();
        if(result.hasErrors()){
            return "products/product";
        }

        Product product = repo.findById(stockinDTO.getProduct_id()).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        // Update the product's quantity
        int updatedQuantity = product.getQuantity() + stockinDTO.getQuantity();
        product.setQuantity(updatedQuantity);
        repo.save(product);

        StockIn stockIn = new StockIn();
        stockIn.setCost(stockinDTO.getCost());
        stockIn.setDate(createAt);
        stockIn.setQuantity(stockinDTO.getQuantity());
        Product product_id = repo.findById(stockinDTO.getProduct_id()).get();
        stockIn.setProduct(product_id);
        in_repo.save(stockIn);

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

        return "redirect:/product";
    }

    //Edit StockIn Detail
    @GetMapping( "/editStock")
    public String showEditStockIn(Model model, @RequestParam int id)
    {
        try {
            StockIn stock_In = in_repo.findById(id).get();
            model.addAttribute("stock_In", stock_In);
            List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
            model.addAttribute("products", products);
            StockinDTO stockinDTO = new StockinDTO();
            stockinDTO.setId(stock_In.getId());
            stockinDTO.setProduct_id(stock_In.getProduct_id());
            stockinDTO.setCost(stock_In.getCost());
            stockinDTO.setQuantity(stock_In.getQuantity());
            model.addAttribute("stockinDTO", stockinDTO);
        }
        catch(Exception ex)
        {
            System.out.println("Exception : " + ex.getMessage());
            return "products/product";
        }

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
        return "products/editStock";
    }

    @PostMapping("/editStock")
    public String editStock(Model model, @Valid @ModelAttribute StockinDTO stockinDTO, @RequestParam int id,BindingResult result
                            )
    {
        try{

            if(result.hasErrors()){
                List<Product> products = repo.findAll();
                model.addAttribute("products", products);
                return "products/editStock";
            }
            StockIn stockIn = in_repo.findById(id).get();
            model.addAttribute("stockIn", stockIn);
            Product product = repo.findById(stockinDTO.getProduct_id()).get();
            model.addAttribute("product", product);
            stockIn.setProduct(product);
            stockIn.setCost(stockinDTO.getCost());
            stockIn.setQuantity(stockinDTO.getQuantity());
            in_repo.save(stockIn);

            // Update the product's quantity
            int updatedQuantity = product.getQuantity() + stockinDTO.getQuantity();
            product.setQuantity(updatedQuantity);
            repo.save(product);
        }
        catch (Exception ex){
            System.out.println("Exception:" + ex.getMessage());
        }


        return "redirect:/product";

    }

    //create new product endpoint
    @GetMapping("/create")
    public String showCreatePage(Model model)
    {
        ProductDTO productDTO = new ProductDTO();
        List<Category> categories = categoryService.getAllCategories();
        List<Supplier> suppliers = supplierService.getAllSupplier();
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("categories", categories);
        model.addAttribute("suppliers", suppliers);

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
        return "products/createproduct";
    }

    @PostMapping("/create")
    public String CreateProduct(@Valid @ModelAttribute ProductDTO productDTO, BindingResult result, Model model)
    {
        if(result.hasErrors())
        {
            List<Category> categories = categoryService.getAllCategories();
            List<Supplier> suppliers = supplierService.getAllSupplier();
            model.addAttribute("categories", categories);
            model.addAttribute("suppliers", suppliers);
            return "products/createproduct";
        }
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setStatus(productDTO.getStatus());
        Category category = categoryService.findById(productDTO.getCategory_id());
        product.setCategory(category);

        Supplier supplier = supplierService.findById(productDTO.getSupplier_id());
        product.setSupplier(supplier);
        product.setDescription(productDTO.getDescription());

        repo.save(product);
        return "redirect:/product";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id)
    {
        try
        {
            Product product = repo.findById(id).get();
            model.addAttribute("product", product);

            List<Category> categories = categoryService.getAllCategories();
            List<Supplier> suppliers = supplierService.getAllSupplier();
            model.addAttribute("categories", categories);
            model.addAttribute("suppliers", suppliers);

            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setPrice(product.getPrice());
            productDTO.setQuantity(product.getQuantity());
            productDTO.setStatus(product.getStatus());
            productDTO.setDescription(product.getDescription());
            productDTO.setCategory_id(product.getCategory_id());
            productDTO.setSupplier_id(product.getSupplierId());

            model.addAttribute("productDTO", productDTO);
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/product";
        }

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
        return "products/editproduct";
    }

    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam int id,
                                @Valid @ModelAttribute ProductDTO productDTO,
                                BindingResult result)
    {
        try
        {
            Product product = repo.findById(id).get();
            model.addAttribute("product", product);
            if(result.hasErrors())
            {
                return "products/editproduct";
            }

            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setQuantity(productDTO.getQuantity());
            product.setStatus(productDTO.getStatus());
            Category category = categoryService.findById(productDTO.getCategory_id());
            product.setCategory(category);

            Supplier supplier = supplierService.findById(productDTO.getSupplier_id());
            product.setSupplier(supplier);
            product.setDescription(productDTO.getDescription());

            repo.save(product);

        }
        catch (Exception ex)
        {
            System.out.println("Exception:" + ex.getMessage());
        }
        return "redirect:/product";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id){

        try
        {
            Product product = repo.findById(id).get();
            repo.delete(product);
            StockIn stockin = in_repo.findById(id).get();
            in_repo.delete(stockin);
        }
        catch (Exception ex)
        {
            System.out.println("Exception:" + ex.getMessage());
        }
        return "redirect:/product";
    }

    @GetMapping("/deleteStock")
    public String deleteStock(@RequestParam int id){

        try
        {

            StockIn stockin = in_repo.findById(id).get();
            in_repo.delete(stockin);
        }
        catch (Exception ex)
        {
            System.out.println("Exception:" + ex.getMessage());
        }
        return "redirect:/product";
    }


}
