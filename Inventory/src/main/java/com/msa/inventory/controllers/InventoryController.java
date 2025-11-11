package com.msa.inventory.controllers;

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
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return new ResponseEntity<>(inventoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping("/category/new")
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestHeader("Authorization") String access_token,
            @RequestBody CreateCategoryRequest createCategoryRequest
    ) {
        return new ResponseEntity<>(inventoryService.createCategory(createCategoryRequest), HttpStatus.CREATED);
    }

    @PutMapping("/category/update")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody UpdateCategoryRequest updateCategoryRequest) {
        return new ResponseEntity<>(inventoryService.updateCategory(updateCategoryRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/category/remove")
    public ResponseEntity<List<CategoryResponse>> removeCategory(@RequestBody RemoveCategoryRequest removeCategoryRequest) {
        return new ResponseEntity<>(inventoryService.removeCategory(removeCategoryRequest), HttpStatus.OK);
    }
}
