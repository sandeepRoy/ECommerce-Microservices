package com.msa.category.services;

import com.msa.category.clients.ProductClient;
import com.msa.category.dtos.CategoryDto;
import com.msa.category.dtos.RemoveCategoryDto;
import com.msa.category.dtos.UpdateCategoryDto;
import com.msa.category.entities.Category;
import com.msa.category.repositories.CategoryRepository;
import com.msa.category.responses.AllCategoryProduct;
import com.msa.category.responses.CategoryWithProductsResponse;
import com.msa.category.responses.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    public ProductClient productClient;

    // POST - Create Category
    public Category createCategory(CategoryDto categoryDto) {
        return categoryRepository
                .findOne(Example.of(Category.builder().category_name(categoryDto.getCategory_name()).build()))
                .orElseGet(() -> buildNewCategory(categoryDto.getCategory_name()));
    }

    private Category buildNewCategory(String categoryName) {
        return categoryRepository.save(Category.builder().category_name(categoryName).build());
    }

    // GET - List<Category>
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    // GET - Category by ID
    public ResponseEntity<Category> getCategoryById(Integer category_id) {
        return new ResponseEntity<>(categoryRepository.findById(category_id).get(), HttpStatus.OK);
    }

    // GET - CategoryWithProductsResponse, Category with List<Products>
    public CategoryWithProductsResponse getProductsByCategoryId(Integer category_id) {
        CategoryWithProductsResponse categoryWithProductsResponse = new CategoryWithProductsResponse();

        Category category = categoryRepository.findById(category_id).orElseThrow(() -> new RuntimeException("Category Not Found!"));
        categoryWithProductsResponse.setCategory_id(category.getCategory_id());
        categoryWithProductsResponse.setCategory_name(category.getCategory_name());

        List<ProductResponse> productsByCategoryId = productClient.getProductsByCategoryId(category_id);
        categoryWithProductsResponse.setProductList(productsByCategoryId);

        return categoryWithProductsResponse;
    }

    // GET - List<AllCategoryProduct>, All Categories with their Products
    public List<AllCategoryProduct> getAllCategoryWithProducts() {
        List<AllCategoryProduct> list_allProductsWithCategory = new ArrayList<>();
        List<Category> all_categories = categoryRepository.findAll();

        for(Category c : all_categories){
            AllCategoryProduct allCategoryProduct = new AllCategoryProduct();
            allCategoryProduct.setCategory_id(c.getCategory_id());
            allCategoryProduct.setCategory_name(c.getCategory_name());
            List<ProductResponse> productsByCategoryId = productClient.getProductsByCategoryId(c.getCategory_id());
            allCategoryProduct.setProductList(productsByCategoryId);
            list_allProductsWithCategory.add(allCategoryProduct);
        }
        return list_allProductsWithCategory;
    }

    public Category updateCategory(UpdateCategoryDto updateCategoryDto) {
        Category category_toFind = new Category();
        category_toFind.setCategory_id(updateCategoryDto.getCategory_id());
        category_toFind.setCategory_name(updateCategoryDto.getCategory_name_old());

        Example<Category> categoryExample = Example.of(category_toFind);
        Category category = categoryRepository.findOne(categoryExample).orElseThrow(() -> new RuntimeException("Category Not Found by ID / Name"));

        category.setCategory_name(updateCategoryDto.getCategory_name_new());

        return categoryRepository.save(category);
    }

    public List<Category> removeCategory(RemoveCategoryDto removeCategoryDto) {
        Category category_toFind = new Category();
        category_toFind.setCategory_id(removeCategoryDto.getCategory_id());
        category_toFind.setCategory_name(removeCategoryDto.getCategory_name());

        Example<Category> categoryExample = Example.of(category_toFind);
        Category category = categoryRepository.findOne(categoryExample).orElseThrow(() -> new RuntimeException("Category Not Found by ID / Name"));

        categoryRepository.delete(category);

        return categoryRepository.findAll();
    }
}
