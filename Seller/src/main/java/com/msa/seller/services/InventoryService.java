package com.msa.seller.services;

import com.msa.seller.clients.ProductClient;
import com.msa.seller.clients.UserClient;
import com.msa.seller.entities.Seller;
import com.msa.seller.repositories.SellerRepository;
import com.msa.seller.requests.product.CreateProductRequest;
import com.msa.seller.responses.product.ProductResponse;
import com.msa.seller.responses.profile.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    public ProductClient productClient;

    @Autowired
    public UserClient userClient;

    @Autowired
    public SellerRepository sellerRepository;

    public ProductResponse createProduct(String access_token, CreateProductRequest createProductRequest) {
        ProductResponse productResponse = productClient.createProduct(createProductRequest).getBody();
        Integer productId = productResponse.getProduct_id();
        String loggedInSellerEmail = userClient.getLoggedInUser(access_token).getBody().getEmail();

        Seller sellerToFind = Seller.builder().seller_email(loggedInSellerEmail).build();

        Example<Seller> sellerExample = Example.of(sellerToFind);
        Seller seller = sellerRepository.findOne(sellerExample).get();
        seller.setProduct_id(productId);
        seller.setProduct_in_stock(createProductRequest.getProduct_in_stock());
        seller.setProduct_price(createProductRequest.getProduct_price());
        sellerRepository.save(seller);
        return productResponse;
    }

    public List<ProductResponse> getProductByCategoryId(String access_token, Integer category_id) {
        return productClient.getProductsByCategoryId(category_id).getBody();
    }

    public List<ProductResponse> getAllProducts(String access_token) {
        return productClient
                .getAllProducts(
                        sellerRepository
                                .findAll(Example.of(Seller.builder().seller_email(userClient.getLoggedInUser(access_token).getBody().getEmail()).build()))
                                .stream()
                                .map(Seller::getProduct_id)
                                .collect(Collectors.toList()))
                .getBody();
    }
}
