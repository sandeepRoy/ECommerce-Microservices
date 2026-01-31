package com.msa.seller.clients;

import com.msa.seller.requests.product.CreateProductRequest;
import com.msa.seller.responses.product.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product", url = "http://localhost:8085/product")
public interface ProductClient {
    @PostMapping("/new")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest createProductRequest);

    @GetMapping("/products/{category_id}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategoryId(@PathVariable Integer category_id);

    @PostMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestBody List<Integer> productIds);
}
