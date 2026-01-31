package com.msa.inventory.controllers;

import com.msa.inventory.exception.custom.NonAdminAccessException;
import com.msa.inventory.requests.CreateCategoryRequest;
import com.msa.inventory.requests.RemoveCategoryRequest;
import com.msa.inventory.requests.UpdateCategoryRequest;
import com.msa.inventory.responses.CategoryResponse;
import com.msa.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class InventoryController {

    @Autowired
    public InventoryService inventoryService;

    @GetMapping("/category/all")
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @RequestHeader(value = "Authorization") String access_token
    ) throws NonAdminAccessException {
        List<CategoryResponse> allCategories = inventoryService.getAllCategories(access_token);
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @PostMapping("/category/new")
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestHeader(value = "Authorization") String access_token,
            @RequestBody CreateCategoryRequest createCategoryRequest
    ) throws NonAdminAccessException {
        return new ResponseEntity<>(inventoryService.createCategory(access_token, createCategoryRequest), HttpStatus.CREATED);
    }

    @PutMapping("/category/update")
    public ResponseEntity<CategoryResponse> updateCategory(
            @RequestHeader(value = "Authorization") String access_token,
            @RequestBody UpdateCategoryRequest updateCategoryRequest
    ) throws NonAdminAccessException {
        return new ResponseEntity<>(inventoryService.updateCategory(access_token, updateCategoryRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/category/remove")
    public ResponseEntity<List<CategoryResponse>> removeCategory(
            @RequestHeader(value = "Authorization") String access_token,
            @RequestBody RemoveCategoryRequest removeCategoryRequest) throws NonAdminAccessException {
        return new ResponseEntity<>(inventoryService.removeCategory(access_token, removeCategoryRequest), HttpStatus.OK);
    }

    /*@GetMapping("/get-inventory")
    public ResponseEntity<Inventory> getInventory() {}*/
}
