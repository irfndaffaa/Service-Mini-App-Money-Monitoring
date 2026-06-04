package com.moneymonitoring.moneymonitoring.controller;

import com.moneymonitoring.moneymonitoring.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/monthly-summary")
    public ResponseEntity<Map<String, Object>> getMonthlySummary() {
        return ok("Monthly financial summary", reportService.getMonthlyFinancialSummary());
    }

    @GetMapping("/category-summary")
    public ResponseEntity<Map<String, Object>> getCategorySummary() {
        return ok("Category summary", reportService.getCategorySummary());
    }

    @GetMapping("/category-transactions")
    public ResponseEntity<Map<String, Object>> getCategoryTransactions(
            @RequestParam(defaultValue = "EXPENSE") String type) {
        return ok("Category transaction summary for " + type,
                reportService.getCategoryWithTransactionSummary(type));
    }

    @GetMapping("/saving-balances")
    public ResponseEntity<Map<String, Object>> getSavingBalances() {
        return ok("Saving category balances", reportService.getSavingCategoryBalances());
    }

    @GetMapping("/budgets-exceeded")
    public ResponseEntity<Map<String, Object>> getBudgetsExceeded() {
        return ok("Budgets exceeded", reportService.getBudgetsExceeded());
    }

    @GetMapping("/budget")
    public ResponseEntity<Map<String, Object>> getBudgetByCategory(
            @RequestParam String categoryName) {
        return ok("Budget for " + categoryName, reportService.getBudgetByCategory(categoryName));
    }

    @GetMapping("/running-balance")
    public ResponseEntity<Map<String, Object>> getRunningBalance() {
        return ok("Running balance", reportService.getRunningBalance());
    }

    @GetMapping("/deposit-summary")
    public ResponseEntity<Map<String, Object>> getDepositSummary(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ok("Deposit summary from " + startDate + " to " + endDate,
                reportService.getDepositSummaryByDateRange(startDate, endDate));
    }

    @GetMapping("/withdrawal-summary")
    public ResponseEntity<Map<String, Object>> getWithdrawalSummary(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ok("Withdrawal summary from " + startDate + " to " + endDate,
                reportService.getWithdrawalSummaryByDateRange(startDate, endDate));
    }

    @GetMapping("/inactive-savings")
    public ResponseEntity<Map<String, Object>> getInactiveSavings(
            @RequestParam(defaultValue = "2024-01-01") String cutoffDate) {
        return ok("Inactive saving categories", reportService.getInactiveSavingCategories(cutoffDate));
    }

    @GetMapping("/top-categories")
    public ResponseEntity<Map<String, Object>> getTopCategories() {
        return ok("Top categories per saving type", reportService.findTopCategoriesPerSavingType());
    }

    @GetMapping("/withdrawal-trends")
    public ResponseEntity<Map<String, Object>> getWithdrawalTrends() {
        return ok("Withdrawal trends with window functions",
                reportService.findWithdrawalWithWindowFunctions());
    }

    @GetMapping("/deposit-ranking")
    public ResponseEntity<Map<String, Object>> getDepositRanking() {
        return ok("Deposit ranked by category", reportService.findDepositRankedByCategory());
    }

    private ResponseEntity<Map<String, Object>> ok(String message, List<Map<String, Object>> data) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", true);
        result.put("message", message);
        result.put("data", data);
        return ResponseEntity.ok(result);
    }
}
