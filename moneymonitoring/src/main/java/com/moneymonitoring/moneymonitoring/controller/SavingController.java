package com.moneymonitoring.moneymonitoring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.entity.CategoryEntity;
import com.moneymonitoring.moneymonitoring.entity.SavingCategoryEntity;
import com.moneymonitoring.moneymonitoring.entity.WithdrawEntity;
import com.moneymonitoring.moneymonitoring.repository.SavingCategoryRepository;
import com.moneymonitoring.moneymonitoring.service.SavingService;
import com.moneymonitoring.moneymonitoring.utils.ApplicationException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class SavingController {

    @Autowired
    SavingService savingService;
    @Autowired
    SavingCategoryRepository savingCategoryRepository;

    // test if the app already running
    @RequestMapping(value = "/", method = RequestMethod.GET)
	public String testConnection() throws Exception{
		String index = "";	
		
		try {
			index = "Mini Project - Money Monitoring App";
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return index;
	}

    @RequestMapping(value = "/insertSavingCategory", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> insertSavingCategory(@RequestBody SavingDTO savingDto) throws Exception {
        //TODO: process POST request
        Map<String, Object> result = new HashMap<>();
        SavingCategoryEntity savingCategory = new SavingCategoryEntity();

        try{

            savingCategory = savingService.insertCategorySaving(savingDto);
            result.put("status", true);
            result.put("message", "Added successfully");
            result.put("data", savingCategory);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(ApplicationException e){

            result.put("status", false);
            result.put("message", "Insert failed");
			result.put("log error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }
        
    }

    @RequestMapping(value = "/editSavingCategory", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> editSavingCategory(@RequestBody SavingDTO savingDto) throws Exception {
        
        Map<String, Object> result = new HashMap<>();
        SavingCategoryEntity savingCategory = new SavingCategoryEntity();

        try{

            savingCategory = savingService.editCategorySaving(savingDto);
            result.put("status", true);
            result.put("message", "Edit Succesful");
            result.put("data", savingCategory);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(ApplicationException e){

            result.put("status", false);
            result.put("message", "Edit failed");
			result.put("log error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }
    }

    @RequestMapping(value = "/getAllSavings", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllCategorySaving() {

        Map<String, Object> result = new HashMap<>();
        List<SavingCategoryEntity> listSavingCategory = new ArrayList<>();

        try{

            listSavingCategory = savingService.getAllCategorySaving();

            if(listSavingCategory.isEmpty()){

                result.put("status", true);
                result.put("message", "You don't have any saving category");
                result.put("data", listSavingCategory);
                return new ResponseEntity<>(result, HttpStatus.OK);

            } else {

                result.put("status", true);
                result.put("message", "Get all data success");
                result.put("data", listSavingCategory);
                return new ResponseEntity<>(result, HttpStatus.OK);

            }

        } catch(ApplicationException e){

            result.put("status", false);
            result.put("message", "failed");
			result.put("log error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }

    }

    @RequestMapping(value = "/deleteCategorySaving", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteCategorySaving(@RequestBody SavingDTO savingDto) throws Exception{

        Map<String, Object> result = new HashMap<>();
        SavingDTO dataSavingDto = new SavingDTO();

        try{

            dataSavingDto = savingService.deleteCategorySaving(savingDto);
            result.put("status", true);
            result.put("message", dataSavingDto.getTransactionId() + " " + dataSavingDto.getMsg());
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(ApplicationException e){

            result.put("status", false);
            result.put("message", "failed");
			result.put("log error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }

    }
    
    
}
