package com.a6.a6mart.services;

import com.a6.a6mart.api.models.MonthlyProductSale;
import com.a6.a6mart.api.models.MonthlySale;
import com.a6.a6mart.api.models.SellItem;
import com.a6.a6mart.api.models.StockIn;
import com.a6.a6mart.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MonthlySaleService {

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private StockinRepository stockInRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MonthlyProductSaleRepository monthlyProductSaleRepository;

    @Autowired
    private MonthlySaleRepository monthlySaleRepository;

    public void calculateMonthlySales(int month, int year) {
        // Calculate total product sold
        List<SellItem> sellItems = sellRepository.findByMonthAndYear(month, year);
        Map<Integer, Integer> productSales = new HashMap<>();

        for (SellItem sellItem : sellItems) {
            int productId = sellItem.getProductId();
            productSales.put(productId, productSales.getOrDefault(productId, 0) + sellItem.getQuantity());
        }

        // Save monthly product sales
        for (Map.Entry<Integer, Integer> entry : productSales.entrySet()) {
            int productId = entry.getKey();
            int totalSoldQty = entry.getValue();
            double totalSaleMoney = totalSoldQty * productRepository.findById(productId).get().getPrice();

            MonthlyProductSale monthlyProductSale = new MonthlyProductSale();
            monthlyProductSale.setMonth(month);
            monthlyProductSale.setYear(year);
            monthlyProductSale.setProductId(productId);
            monthlyProductSale.setTotalSoldQty(totalSoldQty);
            monthlyProductSale.setTotalSaleMoney(totalSaleMoney);

            monthlyProductSaleRepository.save(monthlyProductSale);
        }

        // Calculate total expense
        List<StockIn> stockIns = stockInRepository.findByMonthAndYear(month, year);
        double totalExpense = stockIns.stream().mapToDouble(stockIn -> stockIn.getQuantity() * stockIn.getCost()).sum();

        // Calculate total income
        double totalIncome = productSales.values().stream().mapToDouble(qty -> qty * productRepository.findById(qty).get().getPrice()).sum();

        // Calculate total products sold
        int totalProductSold = productSales.values().stream().mapToInt(Integer::intValue).sum();

        // Save monthly sale
        MonthlySale monthlySale = new MonthlySale();
        monthlySale.setMonth(month);
        monthlySale.setYear(year);
        monthlySale.setTotalProductSold(totalProductSold);
        monthlySale.setTotalExpense(totalExpense);
        monthlySale.setTotalIncome(totalIncome);

        monthlySaleRepository.save(monthlySale);
    }
}
