package com.msa.inventory.service;

import com.msa.inventory.clients.AuthenticationClient;
import com.msa.inventory.clients.CategoryClient;
import com.msa.inventory.clients.UserClient;
import com.msa.inventory.exception.custom.NonAdminAccessException;
import com.msa.inventory.requests.CreateCategoryRequest;
import com.msa.inventory.requests.RemoveCategoryRequest;
import com.msa.inventory.requests.UpdateCategoryRequest;
import com.msa.inventory.responses.CategoryResponse;
import com.msa.inventory.responses.profile.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    public UserClient userClient;

    @Autowired
    public CategoryClient categoryClient;

    public List<CategoryResponse> getAllCategories(String token) throws NonAdminAccessException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(token).getBody();
        if(!userProfileResponse.getRole().equalsIgnoreCase("ADMIN")) {
            throw new NonAdminAccessException("REQUIRED TO BE LOGGED IN AS ADMIN");
        }
        return categoryClient.getAllCategory().getBody();
    }

    public CategoryResponse createCategory(String token, CreateCategoryRequest createCategoryRequest) throws NonAdminAccessException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(token).getBody();
        if(!userProfileResponse.getRole().equalsIgnoreCase("ADMIN")) {
            throw new NonAdminAccessException("REQUIRED TO BE LOGGED IN AS ADMIN");
        }
        return categoryClient.createCategory(createCategoryRequest).getBody();
    }

    public CategoryResponse updateCategory(String token, UpdateCategoryRequest updateCategoryRequest) throws NonAdminAccessException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(token).getBody();
        if(!userProfileResponse.getRole().equalsIgnoreCase("ADMIN")) {
            throw new NonAdminAccessException("REQUIRED TO BE LOGGED IN AS ADMIN");
        }
        return categoryClient.updateCategory(updateCategoryRequest).getBody();
    }

    public List<CategoryResponse> removeCategory(String token, RemoveCategoryRequest removeCategoryRequest) throws NonAdminAccessException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(token).getBody();
        if(!userProfileResponse.getRole().equalsIgnoreCase("ADMIN")) {
            throw new NonAdminAccessException("REQUIRED TO BE LOGGED IN AS ADMIN");
        }
        return categoryClient.removeCategory(removeCategoryRequest).getBody();
    }
}
