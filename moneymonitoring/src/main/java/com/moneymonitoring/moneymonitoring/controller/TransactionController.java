package com.moneymonitoring.moneymonitoring.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.dto.TransactionDTO;
import com.moneymonitoring.moneymonitoring.entity.DepositEntity;
import com.moneymonitoring.moneymonitoring.entity.SavingCategoryEntity;
import com.moneymonitoring.moneymonitoring.entity.WithdrawEntity;
import com.moneymonitoring.moneymonitoring.repository.SavingCategoryRepository;
import com.moneymonitoring.moneymonitoring.service.TransactionService;
import com.moneymonitoring.moneymonitoring.utils.ApplicationException;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    @Autowired
    SavingCategoryRepository savingCatRepo;

    @RequestMapping(value = "/insertWithdrawal", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> insertWithdrawal(@RequestBody SavingDTO savingDto) throws Exception{
        
        Map<String, Object> result = new HashMap<>();
        WithdrawEntity dataWithdraw = new WithdrawEntity();
        SavingCategoryEntity dataSavingCategory = savingCatRepo.findBySavingName(savingDto.getSavingName());

        try{

            if(dataSavingCategory == null){
                dataWithdraw = null;
                savingDto.setMsg("Saving category for " + savingDto.getSavingName() + " not found");
            } else {
                dataWithdraw = transactionService.insertWithdrawal(savingDto);
                savingDto.setMsg("Withdrawal Success");
            }

            
            result.put("status", true);
            result.put("message", savingDto.getMsg());
            result.put("data", dataWithdraw);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(ApplicationException e) {

            result.put("status", false);
            result.put("message", "Insert Withdrawal Failed");
			result.put("log error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }
    }

    @RequestMapping(value = "/editWithdrawal", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> editWithdrawal(@RequestBody SavingDTO savingDto) throws Exception{
        
        Map<String, Object> result = new HashMap<>();
        WithdrawEntity dataWithdraw = new WithdrawEntity();
        SavingCategoryEntity dataSavingCategory = savingCatRepo.findBySavingName(savingDto.getSavingName());

        try{

            if(dataSavingCategory == null){
                dataWithdraw = null;
                savingDto.setMsg("Saving category for " + savingDto.getSavingName() + " not found");
            } else {
                dataWithdraw = transactionService.editWithdrawal(savingDto);
                savingDto.setMsg("Edit Withdrawal Success");
            }

            result.put("status", true);
            result.put("message", "Edit withdrawal success");
            result.put("data", dataWithdraw);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(ApplicationException e) {

            result.put("status", false);
            result.put("message", "Edit Withdrawal Failed");
			result.put("log error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }
    }

    @RequestMapping(value = "/insertDeposit", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> insertDeposit(@RequestBody SavingDTO savingDto) throws Exception{
        
        Map<String, Object> result = new HashMap<>();
        DepositEntity dataDeposit = new DepositEntity();
        SavingCategoryEntity dataSavingCategory = savingCatRepo.findBySavingName(savingDto.getSavingName());

        try{

            if(dataSavingCategory == null){
                dataDeposit = null;
                savingDto.setMsg("Saving category for " + savingDto.getSavingName() + " not found");
            } else {
                dataDeposit = transactionService.insertDeposit(savingDto);
                savingDto.setMsg("Deposit Success");
            }

            result.put("status", true);
            result.put("message", savingDto.getMsg());
            result.put("data", dataDeposit);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(ApplicationException e) {

            result.put("status", false);
            result.put("message", "Insert Deposit Failed");
			result.put("log error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }
    }

    @RequestMapping(value = "/editDeposit", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> editDeposit(@RequestBody SavingDTO savingDto) throws Exception{
        
        Map<String, Object> result = new HashMap<>();
        DepositEntity dataDeposit = new DepositEntity();

        try{

            dataDeposit = transactionService.editDeposit(savingDto);
            result.put("status", true);
            result.put("message", "Edit deposit success");
            result.put("data", dataDeposit);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(ApplicationException e) {

            result.put("status", false);
            result.put("message", "Edit Deposit Failed");
			result.put("log error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }
    }

    @RequestMapping(value = "/getTotalSaving", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getTotalSaving(){

         Map<String, Object> result = new HashMap<>();
         TransactionDTO dataSaving = new TransactionDTO();

          try{

            dataSaving = transactionService.getAllTotalSaving();
            result.put("status", true);
            result.put("message", "Total Saving");
            result.put("data", dataSaving);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(ApplicationException e) {

            result.put("status", false);
			result.put("log error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }

    }
    
}
