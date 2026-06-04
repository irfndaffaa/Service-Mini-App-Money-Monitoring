package com.moneymonitoring.moneymonitoring.service.impl;

import com.moneymonitoring.moneymonitoring.repository.*;
import com.moneymonitoring.moneymonitoring.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final CategoryRepository categoryRepo;
    private final SavingCategoryRepository savingCategoryRepo;
    private final SavingLogsRepository savingLogsRepo;
    private final DepositRepository depositRepo;
    private final WithdrawalRepository withdrawalRepo;
    private final CategoryBudgetRepository categoryBudgetRepo;

    @Override
    public List<Map<String, Object>> getMonthlyFinancialSummary() {
        return mapRows(categoryRepo.findMonthlyFinancialSummary(),
                "year", "month", "totalIncome", "totalExpense", "netSavings", "incomeCount", "expenseCount");
    }

    @Override
    public List<Map<String, Object>> getCategorySummary() {
        return mapRows(categoryRepo.findCategorySummary(),
                "categoryType", "totalCategories", "categoryList");
    }

    @Override
    public List<Map<String, Object>> getCategoryWithTransactionSummary(String categoryType) {
        return mapRows(categoryRepo.findCategoryWithTransactionSummary(categoryType),
                "categoryType", "categoryName", "depositCount", "totalDeposit", "withdrawalCount", "totalWithdrawal");
    }

    @Override
    public List<Map<String, Object>> getSavingCategoryBalances() {
        return mapRows(savingCategoryRepo.findSavingCategoryWithBalances(),
                "savingName", "maxOutcome", "totalSaving", "remainingLimit");
    }

    @Override
    public List<Map<String, Object>> getBudgetsExceeded() {
        return mapRows(categoryBudgetRepo.findBudgetsExceeded(),
                "categoryName", "budgetLimit", "totalSpent", "remaining");
    }

    @Override
    public List<Map<String, Object>> getBudgetByCategory(String categoryName) {
        return mapRows(categoryBudgetRepo.findBudgetByCategory(categoryName),
                "categoryName", "budgetLimit", "totalSpent", "remaining");
    }

    @Override
    public List<Map<String, Object>> getRunningBalance() {
        return mapRows(savingLogsRepo.findRunningBalance(),
                "idTransaction", "transactionType", "amount", "transactionDate", "runningBalance");
    }

    @Override
    public List<Map<String, Object>> getDepositSummaryByDateRange(String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return mapRows(depositRepo.findDepositSummaryByDateRange(start, end),
                    "savingType", "depositCount", "totalDeposit", "avgDeposit");
        } catch (Exception e) {
            log.error("Date parse error", e);
            return List.of();
        }
    }

    @Override
    public List<Map<String, Object>> getWithdrawalSummaryByDateRange(String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return mapRows(withdrawalRepo.findWithdrawalSummaryByDateRange(start, end),
                    "category", "withdrawalCount", "totalWithdrawal", "avgWithdrawal");
        } catch (Exception e) {
            log.error("Date parse error", e);
            return List.of();
        }
    }

    @Override
    public List<Map<String, Object>> getInactiveSavingCategories(String cutoffDate) {
        return mapRows(savingCategoryRepo.findInactiveSavingCategories(cutoffDate),
                "savingName", "maxOutcome", "lastTransactionDate");
    }

    @Override
    public List<Map<String, Object>> findTopCategoriesPerSavingType() {
        return mapRows(savingLogsRepo.findTopCategoriesPerSavingType(),
                "savingType", "category", "totalAmount", "rank");
    }

    @Override
    public List<Map<String, Object>> findWithdrawalWithWindowFunctions() {
        return mapRows(withdrawalRepo.findWithdrawalWithWindowFunctions(),
                "savingType", "totalExpense", "prevExpense", "expenseDiff");
    }

    @Override
    public List<Map<String, Object>> findDepositRankedByCategory() {
        return mapRows(depositRepo.findDepositRankedByCategory(),
                "category", "totalDeposit", "rank");
    }

    private List<Map<String, Object>> mapRows(List<Object[]> rows, String... keys) {
        return rows.stream()
                .map(row -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    for (int i = 0; i < keys.length && i < row.length; i++) {
                        map.put(keys[i], row[i]);
                    }
                    return map;
                })
                .collect(Collectors.toList());
    }
}
