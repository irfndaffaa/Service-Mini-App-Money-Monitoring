package com.moneymonitoring.moneymonitoring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.entity.CategoryEntity;
import com.moneymonitoring.moneymonitoring.service.CategoryService;
import com.moneymonitoring.moneymonitoring.service.SavingService;
import com.moneymonitoring.moneymonitoring.utils.ApplicationException;

@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "/getAllCategory", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllCategory(){

        Map<String, Object> result = new HashMap<>();
        List<CategoryEntity> listDataCategory = new ArrayList<>();

        try{

            listDataCategory = categoryService.getAllCategory();
            result.put("status", true);
            result.put("message", "All data category found");
            result.put("data", listDataCategory);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(ApplicationException e){

            result.put("status", false);
            result.put("message", "failed");
			result.put("log error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }

    }

    @RequestMapping(value = "/getCategoryByType", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getCategoryByType(@RequestBody SavingDTO savingDto){

        Map<String, Object> result = new HashMap<>();
        List<CategoryEntity> listDataCategory = new ArrayList<>();

        try{

            listDataCategory = categoryService.getCategoryByType(savingDto);
            result.put("status", true);
            result.put("message", "All " + savingDto.getCategoryType() + " category found");
            result.put("data", listDataCategory);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(ApplicationException e){

            result.put("status", false);
            result.put("message", "failed");
			result.put("log error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }

    }

    
    
}
