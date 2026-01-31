package com.msa.seller.controllers;

import com.msa.seller.requests.product.CreateProductRequest;
import com.msa.seller.responses.product.ProductResponse;
import com.msa.seller.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerInventoryController {

    @Autowired
    public InventoryService inventoryService;

    @PostMapping("/product/new")
    public ResponseEntity<ProductResponse> createNewProduct(
            @RequestHeader(value = "Authorization") String access_token,
            @RequestBody CreateProductRequest createProductRequest
    ) {
        return new ResponseEntity<ProductResponse>(inventoryService.createProduct(access_token, createProductRequest), HttpStatus.CREATED);
    }

    // only list the products those are created/updated by the logged in seller
    @GetMapping("/product/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestHeader(value = "Authorization") String access_token
    ) {
        return new ResponseEntity<List<ProductResponse>>(inventoryService.getAllProducts(access_token), HttpStatus.OK);
    }

    @GetMapping("/products/{category_id}")
    public ResponseEntity<List<ProductResponse>> getAllProductsOfCategory(
            @RequestHeader(value = "Authorization") String access_token,
            @PathVariable Integer category_id) {
        return new ResponseEntity<List<ProductResponse>>(new ArrayList<>(), HttpStatus.CREATED);
    }

    @GetMapping("/traded")
    public ResponseEntity<ProductResponse> getProductsSoldInfomation(
            @RequestHeader(value = "Authorization") String access_token
    ) {
        return new ResponseEntity<ProductResponse>(new ProductResponse(), HttpStatus.CREATED);
    }

    @GetMapping("/remaining")
    public ResponseEntity<ProductResponse> getProductsUnSoldInfomation(
            @RequestHeader(value = "Authorization") String access_token
    ) {
        return new ResponseEntity<ProductResponse>(new ProductResponse(), HttpStatus.CREATED);
    }
}
