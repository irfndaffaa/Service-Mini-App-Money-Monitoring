package com.moneymonitoring.moneymonitoring.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Map<String, Object>> getMonthlyFinancialSummary();
    List<Map<String, Object>> getCategorySummary();
    List<Map<String, Object>> getCategoryWithTransactionSummary(String categoryType);
    List<Map<String, Object>> getSavingCategoryBalances();
    List<Map<String, Object>> getBudgetsExceeded();
    List<Map<String, Object>> getBudgetByCategory(String categoryName);
    List<Map<String, Object>> getRunningBalance();
    List<Map<String, Object>> getDepositSummaryByDateRange(String startDate, String endDate);
    List<Map<String, Object>> getWithdrawalSummaryByDateRange(String startDate, String endDate);
    List<Map<String, Object>> getInactiveSavingCategories(String cutoffDate);
    List<Map<String, Object>> findTopCategoriesPerSavingType();
    List<Map<String, Object>> findWithdrawalWithWindowFunctions();
    List<Map<String, Object>> findDepositRankedByCategory();
}
