package com.msa.inventory.clients;

import com.msa.inventory.requests.CreateCategoryRequest;
import com.msa.inventory.requests.RemoveCategoryRequest;
import com.msa.inventory.requests.UpdateCategoryRequest;
import com.msa.inventory.responses.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "category", url = "http://localhost:8084/category")
public interface CategoryClient {
    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponse>> getAllCategory();

    @PostMapping("/new")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest);

    @PostMapping("/update")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody UpdateCategoryRequest updateCategoryRequest);

    @DeleteMapping("/remove")
    public ResponseEntity<List<CategoryResponse>> removeCategory(@RequestBody RemoveCategoryRequest removeCategoryRequest);
}
