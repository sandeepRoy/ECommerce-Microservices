package com.msa.product.services;

import com.msa.product.dtos.CreateProductDto;
import com.msa.product.entities.Product;
import com.msa.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // GET - List<Product>
    public List<Product> getAllProducts(List<Integer> productIds) {
        List<Product> allFoundProducts = new ArrayList<>();
        for(Integer productId : productIds) {
            allFoundProducts.add(productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product Not Found!")));
        }
        return allFoundProducts;
    }

    // GET - Product by product_id
    public Product getProductById(Integer product_id) {
        Product product = productRepository.findById(product_id).orElseThrow(() -> new RuntimeException("Product Not Found!"));
        return product;
    }

    // GET - List<Products> by category_id
    public List<Product> getProductsByCategoryId(Integer category_id) {
        List<Product> products_of_category = new ArrayList<>();
        List<Product> all_products = productRepository.findAll();
        for(Product product : all_products) {
            if(product.getCategory_id().equals(category_id)) {
                products_of_category.add(product);
            }
        }
        return products_of_category;
    }

    // POST - Create new Product
    public Product createProduct(CreateProductDto createProductDto) {
        Product newProduct = Product.builder()
                .product_name(createProductDto.getProduct_name())
                .product_manufacturer(createProductDto.getProduct_manufacturer())
                .product_description(createProductDto.getProduct_description())
                .product_status(createProductDto.getProduct_status())
                .product_listed_on(createProductDto.getProduct_listed_on())
                .product_updated_on(createProductDto.getProduct_updated_on())
                .category_id(createProductDto.getCategory_id())
                .build();

        productRepository.save(newProduct);
        return newProduct;
    }

    // GET - Product by product_name
    public Product getProductByName(String product_name) {
        Product filtered_product = new Product();
        filtered_product.setProduct_name(product_name);

        Example<Product> productExample = Example.of(filtered_product);
        Product product = productRepository.findOne(productExample).orElseThrow(() -> new RuntimeException("Product Not Found!"));
        return product;
    }
}
