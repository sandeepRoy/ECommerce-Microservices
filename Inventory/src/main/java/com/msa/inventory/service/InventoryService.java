package com.msa.inventory.service;

import com.msa.inventory.clients.CategoryClient;
import com.msa.inventory.requests.CreateCategoryRequest;
import com.msa.inventory.requests.RemoveCategoryRequest;
import com.msa.inventory.requests.UpdateCategoryRequest;
import com.msa.inventory.responses.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    public CategoryClient categoryClient;

    public List<CategoryResponse> getAllCategories() {
        return categoryClient.getAllCategory().getBody();
    }

    public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        return categoryClient.createCategory(createCategoryRequest).getBody();
    }

    public CategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        return categoryClient.updateCategory(updateCategoryRequest).getBody();
    }

    public List<CategoryResponse> removeCategory(RemoveCategoryRequest removeCategoryRequest) {
        return categoryClient.removeCategory(removeCategoryRequest).getBody();
    }
}
