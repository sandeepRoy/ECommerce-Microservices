package com.msa.customer.services;

import com.msa.customer.clients.*;
import com.msa.customer.dtos.*;
import com.msa.customer.exceptions.address.add.AddressAdditionException;
import com.msa.customer.exceptions.address.update.AddressUpdateException;
import com.msa.customer.exceptions.customer.firstLogin.CustomerLoginException;
import com.msa.customer.exceptions.customer.secondLogin.CustomerPreviouslyLoggedInException;
import com.msa.customer.model.*;
import com.msa.customer.repositories.*;
import com.msa.customer.responses.*;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CustomerService {

    private static Logger logger = Logger.getLogger(CustomerService.class.getName());

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    public static final String TOKEN_PREFIX = "Bearer ";
    private static String userEmail;
    private static String userName;

    @Autowired
    public CategoryWithProductsClient categoryWithProductsClient;

    @Autowired
    public ProductClient productClient;

    @Autowired
    public OrderClient orderClient;

    @Autowired
    public PDFGeneratorClient pdfGeneratorClient;

    @Autowired
    public MessegingClient messegingClient;

    @Autowired
    public WishlistRepository wishlistRepository;

    @Autowired
    public CustomerRepository customerRepository;

    @Autowired
    public AddressRepository addressRepository;

    @Autowired
    public CartRepository cartRepository;

    @Autowired
    public BuyLaterRepository buyLaterRepository;

    @Autowired
    public CustomerOrderRepository customerOrderRepository;

    @Autowired
    public CustomerPurchaseRepository customerPurchaseRepository;

    @Autowired
    public InvoiceRepository invoiceRepository;

    @Autowired
    public AuthenticationClient authenticationClient;

    @Autowired
    public UserClient userClient;

    @Autowired
    public CachingClient cachingClient;

    @Autowired
    public EmailingClient emailingClient;

    public static String TOKEN;

    public void setTOKEN(String TOKEN) {
        String processed_token = "";
        for(int i = 10; i < TOKEN.length() - 2; i++){
            processed_token += TOKEN.charAt(i);
        }
        CustomerService.TOKEN = processed_token;
        logger.info("CustomerService.TOKEN :: " + CustomerService.TOKEN);
    }

    // GET - List<Root>, return all Categories with Products associated with them
    public List<Root> getAllCategoryWithProducts() {
        List<Root> allCategoryWithProducts = categoryWithProductsClient.getAllCategoryWithProducts();
        return allCategoryWithProducts;
    }

    // GET - Logged In Customer's Profile
    public Customer getCustomerProfile(String token) throws CustomerLoginException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(token).getBody();

        String loggedInCustomerEmail = userProfileResponse.getEmail();

        Customer customer = new Customer();
        customer.setCustomer_email(loggedInCustomerEmail);

        Example<Customer> customerExample = Example.of(customer);
        Customer found_customer = customerRepository.findOne(customerExample).get();

        return found_customer;
    }

    // POST - After Login, make an entry in Customer table
    // Condition - Check Existing entry before inserting new!!!!
    public Customer addCustomer(String token) throws CustomerPreviouslyLoggedInException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(token).getBody();

        log.info(userProfileResponse.toString());

        List<Customer> all_customers = customerRepository.findAll();

        for(Customer customer : all_customers) {
            if(customer.getCustomer_email().equals(userProfileResponse.getEmail())){
                throw new CustomerPreviouslyLoggedInException("Login SuccessFull!");
            }
        }
        Customer new_customer = new Customer();
        new_customer.setCustomer_email(userProfileResponse.getEmail());
        new_customer.setCustomer_name(userProfileResponse.getFirstName() + " " + userProfileResponse.getLastName());
        Customer saved_customer = customerRepository.save(new_customer);
        return saved_customer;
    }

    // PUT - Update Logged In Customer's Profile
    public Customer updateCustomerProfile(String token, UpdateCustomerProfileDto updateCustomerProfileDto) throws CustomerLoginException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer found_customer = customerRepository.findOne(customerExample).get();

        found_customer.setCustomer_name(updateCustomerProfileDto.getCustomer_name());
        found_customer.setCustomer_mobile(updateCustomerProfileDto.getCustomer_mobile());
        found_customer.setGender(updateCustomerProfileDto.getGender());

        Customer updated_customer = customerRepository.save(found_customer);

        String[] name = updated_customer.getCustomer_name().split(" ");
        UpdateNameDto updateNameDto = UpdateNameDto.builder().first_name(name[0]).last_name(name[1]).build();

        userClient.update(token, updateNameDto);
        return updated_customer;
    }

    // POST - Add Address to Customer's profile by logged-in user's email
    // Condition - Limit Address by 2
    public Customer addAddressToCustomer(String access_token, AddressAddDto addressAddDto) throws CustomerLoginException, AddressAdditionException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer found_customer = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found!"));

        List<Address> addressList = found_customer.getAddressList();

        if(addressList.size() <= 1) {
            Address new_address = new Address();
            new_address.setCustomer(found_customer);
            new_address.setAddressType(addressAddDto.getAddressType());
            new_address.setAddress(addressAddDto.getAddress());
            new_address.setCity(addressAddDto.getCity());
            new_address.setState(addressAddDto.getState());
            new_address.setPincode(addressAddDto.getPincode());
            addressRepository.save(new_address);
            Customer updated_customer = customerRepository.save(found_customer);
            return updated_customer;
        }
        else{
            throw new AddressAdditionException("More than 2 Addresses are not allowed!");
        }
    }

    public Customer updateAddressOfCustomer(String access_token, String addressType, UpdateAddressDto updateAddressDto) throws CustomerLoginException, AddressUpdateException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        if(addressType != "HOME" && addressType != "WORK") {
            throw new AddressUpdateException("Incorrect Address Type Provided in Path");
        }

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());
        Example<Customer> customerExample = Example.of(customer);
        Customer found_customer = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found!"));

        List<Address> addressList = found_customer.getAddressList();

        for(Address address : addressList) {
            if(address.getAddressType() == AddressType.valueOf(addressType)) {
                address.setAddress(updateAddressDto.getAddress());
                address.setCity(updateAddressDto.getCity());
                address.setState(updateAddressDto.getState());
                address.setPincode(updateAddressDto.getPincode());

                addressRepository.save(address);
            }
        }
        return found_customer;
    }

    // DELETE - Remove an address of Logged-in Customer
    public Customer deleteAddressOfCustomer(String access_token, String addressType) throws CustomerLoginException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer found_customer = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found!"));

        List<Address> addressList = found_customer.getAddressList();
        for(Address address : addressList){
            if(address.getAddressType() == AddressType.valueOf(addressType)) {
                addressList.remove(address);
                addressRepository.delete(address);
            }
        }

        return found_customer;
    }

    public String deleteCustomer(String access_token) throws CustomerLoginException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer found_customer = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found!"));

        customerRepository.delete(found_customer);

        return userClient.delete(access_token).getBody();
    }

    // POST - Add Product to Cart with Logged-In User's email
    public Wishlist addToWishList(CreateWishlistDto createWishlistDto) throws CustomerLoginException {

        ProductList productByName = productClient.getProductByName(createWishlistDto.getProduct_name());

        if(userEmail == null) {
            throw new CustomerLoginException("Customer Not Logged In");
        }

        Customer customer = new Customer();
        customer.setCustomer_email(userEmail);

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setProduct_name(createWishlistDto.getProduct_name());
        wishlist.setProduct_manufacturer(createWishlistDto.getProduct_manufacturer());
        wishlist.setProduct_quantity(createWishlistDto.getProduct_quantity());
        wishlist.setPayable_amount(productByName.getProduct_price() * createWishlistDto.getProduct_quantity());
        wishlist.setCustomer(customer_found);

        Wishlist wishlist_user = wishlistRepository.save(wishlist);
        return wishlist_user;
    }

    // GET - Cart of a customer
    public Cart getCart(String access_token) throws CustomerLoginException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        Cart cart = customer_found.getCart();
        return cart;
    }

    // POST - Add Wishlist items, Delivery Address and Customer to Cart
    public Cart addToCart_wishlist(CreateCartDto createCartDto) throws CustomerLoginException {
        if(userEmail == null) {
            throw new CustomerLoginException("Customer Not Logged In");
        }

        Cart cart = new Cart();

        Customer customer = new Customer();
        customer.setCustomer_email(userEmail);

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        List<Address> foundCustomer_addressList = customer_found.getAddressList();
        List<Wishlist> foundCustomer_wishlist = customer_found.getWishlist();

        Address delivery_address = new Address();

        for(Address address : foundCustomer_addressList) {
            if(address.getAddressType().equals(AddressType.valueOf(createCartDto.getAddressType()))) {
                delivery_address = address;
            }
        }

        Double totalPayableAmount = getTotalPayableAmount(foundCustomer_wishlist);

        cart.setCustomer_name(customer_found.getCustomer_name());
        cart.setCustomer_mobile(customer_found.getCustomer_mobile());
        cart.setCustomer_email(customer_found.getCustomer_email());
        cart.setCustomer_gender(customer_found.getGender());
        cart.setCustomer(customer_found);
        cart.setTotal_amount(totalPayableAmount);
        cart.setModeOfPayment(createCartDto.getModeOfPayment());
        cart.setDelivery_address(delivery_address);

        for(Wishlist wishlist : foundCustomer_wishlist) {
            cart.addWishlistItem(wishlist);
        }

        Cart save = cartRepository.save(cart);

        return save;
    }

    // PUT - Update cart with new wishlist item
    public Cart updateCart_addProduct(String access_token, CreateWishlistDto createWishlistDto) throws CustomerLoginException {

        ProductList productByName = productClient.getProductByName(createWishlistDto.getProduct_name());

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        List<Wishlist> customer_wishlist = customer_found.getWishlist();

        Wishlist new_wish = new Wishlist();

        Cart cart = customer_found.getCart();

        // if no cart created earlier
        if(cart == null) {
            Cart new_cart = createNewCart();

            List<Wishlist> cartWishlist = new_cart.getWishlist();

            // assign data to wish
            new_wish.setProduct_name(productByName.getProduct_name());
            new_wish.setProduct_manufacturer(productByName.getProduct_manufacturer());
            new_wish.setProduct_quantity(createWishlistDto.getProduct_quantity());
            new_wish.setPayable_amount(productByName.getProduct_price() * createWishlistDto.getProduct_quantity());
            new_wish.setCustomer(customer_found);
            new_wish.setCart(new_cart);
            wishlistRepository.save(new_wish);

            // add newly created wish to the list holdable
            cartWishlist.add(new_wish);

            // total payable amount
            Double totalPayableAmount = getTotalPayableAmount(cartWishlist);

            // since a wish is being created assign it to customer
            customer_wishlist.add(new_wish);
            customer_found.setWishlist(customer_wishlist);
            customerRepository.save(customer_found);

            // assign data to cart
            new_cart.setCustomer(customer_found);
            new_cart.setCustomer_name(customer_found.getCustomer_name());
            new_cart.setCustomer_gender(customer_found.getGender());
            new_cart.setCustomer_email(customer_found.getCustomer_email());
            new_cart.setCustomer_mobile(customer_found.getCustomer_mobile());
            new_cart.setTotal_amount(totalPayableAmount);
            new_cart.setWishlist(cartWishlist);

            Cart cart_saved = cartRepository.save(new_cart);
            return cart_saved;
        }

        // if cart contains data
        else {
            List<Wishlist> cart_wishlist = cart.getWishlist();

            new_wish.setProduct_name(productByName.getProduct_name());
            new_wish.setProduct_manufacturer(productByName.getProduct_manufacturer());
            new_wish.setProduct_quantity(createWishlistDto.getProduct_quantity());
            new_wish.setPayable_amount(productByName.getProduct_price() * createWishlistDto.getProduct_quantity());
            new_wish.setCart(cart);
            new_wish.setCustomer(customer_found);
            wishlistRepository.save(new_wish);

            customer_wishlist.add(new_wish);
            cart_wishlist.add(new_wish);

            customer_found.setWishlist(customer_wishlist);
            customerRepository.save(customer_found);

            Double totalPayableAmount = getTotalPayableAmount(cart_wishlist);
            cart.setTotal_amount(totalPayableAmount);
            cart.setWishlist(cart_wishlist);
            Cart updated_cart = cartRepository.save(cart);
            return updated_cart;
        }
    }

    // PUT - Update Cart as per given product's quantity
    public Cart updateCart_changeQuantity(String access_token, String product_name, Integer quantity) throws CustomerLoginException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        Cart cart = customer_found.getCart();

        if(quantity == null) {
            return cart;
        }

        List<Wishlist> customer_wishlist = cart.getWishlist();

        for(Wishlist wishlist : customer_wishlist) {
            if(wishlist.getProduct_name().equals(product_name)) {
                ProductList productByName = productClient.getProductByName(wishlist.getProduct_name());

                if(productByName.getProduct_inStock() > quantity) {
                    wishlist.setProduct_quantity(quantity);
                    wishlist.setPayable_amount(productByName.getProduct_price() * quantity);
                    wishlistRepository.save(wishlist);
                }
                else {
                    throw new RuntimeException("Quantity required isn't available in stock");
                }
            }
        }
        Double totalPayableAmount = getTotalPayableAmount(customer_wishlist);
        cart.setTotal_amount(totalPayableAmount);
        Cart updated_cart = cartRepository.save(cart);
        return updated_cart;
    }

    // PUT - Update cart's delivery address as per given address type
    public Cart updateCart_changeDeliveryAddress(String access_token, String address_type) throws CustomerLoginException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        List<Address> addressList = customer_found.getAddressList();

        Cart cart = customer_found.getCart();

        Address deliveryAddress = cart.getDelivery_address();

        for(Address address : addressList) {
            if(address.getAddressType() == AddressType.valueOf(address_type)) {
                deliveryAddress = address;
            }
        }

        cart.setDelivery_address(deliveryAddress);
        Cart updated_cart = cartRepository.save(cart);
        return updated_cart;
    }

    // PUT - Cart, Update Mode of Payment as provided
    public Cart updateCart_modeOfPayment(String access_token, String payment_type) throws CustomerLoginException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        Cart cart = customer_found.getCart();
        cart.setModeOfPayment(payment_type);

        Cart updated_cart = cartRepository.save(cart);
        return updated_cart;
    }

    // PUT - Cart, DELETE : Wishlist, Remove a product from cart, recalculate total amount
    public Cart updateCart_removeProduct(String access_token, String product_name) throws CustomerLoginException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        Wishlist wish = new Wishlist();
        wish.setProduct_name(product_name);
        Example<Wishlist> wishlistExample = Example.of(wish);
        Wishlist wish_to_remove = wishlistRepository.findOne(wishlistExample).orElseThrow(() -> new RuntimeException("Product Not Found!"));
        wishlistRepository.delete(wish_to_remove);

        Cart cart = customer_found.getCart();
        List<Wishlist> cartWishlist = cart.getWishlist();
        cartWishlist.remove(wish_to_remove);

        Double totalPayableAmount = getTotalPayableAmount(cartWishlist);
        cart.setTotal_amount(totalPayableAmount);
        cart.setWishlist(cartWishlist);
        Cart cart_updated = cartRepository.save(cart);
        return cart_updated;
    }

    // POST - Create new entry for BuyLater as per given payload
    public BuyLater addBuyLater_newProduct(String access_token, CreateWishlistDto createWishlistDto) throws CustomerLoginException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        ProductList productByName = productClient.getProductByName(createWishlistDto.getProduct_name());

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        List<BuyLater> customer_buyLaterList = customer_found.getBuyLaterList();

        BuyLater buyLater = new BuyLater();
        buyLater.setBuylater_product_name(createWishlistDto.getProduct_name());
        buyLater.setBuylater_product_manufacturer(createWishlistDto.getProduct_manufacturer());
        buyLater.setBuylater_product_quantity(createWishlistDto.getProduct_quantity());
        buyLater.setBuylater_payable_amount(productByName.getProduct_price() * createWishlistDto.getProduct_quantity());
        buyLater.setCustomer(customer_found);
        BuyLater new_buyLater = buyLaterRepository.save(buyLater);

        // if customer's buylater list isn't created earlier
        if(customer_buyLaterList == null) {
            List<BuyLater> new_buyLaterList = new ArrayList<>();
            new_buyLaterList.add(buyLater);

            customer_found.setBuyLaterList(new_buyLaterList);
            customerRepository.save(customer_found);
        }
        // if customer's buylater list already created and have values
        else {
            customer_buyLaterList.add(buyLater);
            customer_found.setBuyLaterList(customer_buyLaterList);
            customerRepository.save(customer_found);
        }
        return new_buyLater;
    }

    private Double getTotalPayableAmount(List<Wishlist> foundCustomerWishlist) {
        Double totalPaybleAmount = 0.0;

        for(Wishlist wishlist : foundCustomerWishlist) {
            totalPaybleAmount += wishlist.getPayable_amount();
            System.out.println("468: Customer Service:: " + totalPaybleAmount);
        }


        return totalPaybleAmount;
    }

    private Cart createNewCart() {
        Cart cart = new Cart();
        List<Wishlist> wishlist = new ArrayList<>();
        cart.setWishlist(wishlist);

        Cart new_cart = cartRepository.save(cart);
        return new_cart;
    }

    public String isValidRequest(CreateWishlistDto createWishlistDto, ProductList productByName) {
        if(productByName.getProduct_inStock() < createWishlistDto.getProduct_quantity()) {
            return "Requested quantity greater than available stock";
        }
        else{
            return "Valid";
        }
    }

    // Development Phase - Add BuyLater items to Cart for Purchase, recalculate amount
    // PUT - Cart, ADD : BuyLater, Add Buylater items to cart, recalculate total amount
    public Cart updateCart_addBuyLater(String access_token) throws CustomerLoginException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        List<BuyLater> buyLaterList = customer_found.getBuyLaterList();
        Cart cart = customer_found.getCart();

        // check if cart exists by earlier transactions, like adding wishlist / buylater items
        if(cart == null) {
            log.info("Cart is null, resuming from here!");

            // a new cart is required to hold all the buylater items
            Cart newCart = createNewCart();

            List<Wishlist> wishlist = newCart.getWishlist();

            for(BuyLater buyLater : buyLaterList) {
                Wishlist buyLaterToWish = new Wishlist();
                buyLaterToWish.setProduct_name(buyLater.getBuylater_product_name());
                buyLaterToWish.setProduct_quantity(buyLater.getBuylater_product_quantity());
                buyLaterToWish.setProduct_manufacturer(buyLater.getBuylater_product_manufacturer());
                buyLaterToWish.setPayable_amount(buyLater.getBuylater_payable_amount() * buyLater.getBuylater_product_quantity());
                buyLaterToWish.setCart(newCart);
                buyLaterToWish.setCustomer(customer_found);

                wishlist.add(buyLaterToWish);

                wishlistRepository.save(buyLaterToWish);

                buyLaterToWish = null;
            }

            Double totalPayableAmount = getTotalPayableAmount(wishlist);

            newCart.setTotal_amount(totalPayableAmount);
            newCart.setCustomer(customer_found);
            newCart.setCustomer_name(customer_found.getCustomer_name());
            newCart.setCustomer_email(customer_found.getCustomer_email());
            newCart.setCustomer_gender(customer_found.getGender());
            newCart.setCustomer_mobile(customer_found.getCustomer_mobile());
            newCart.setWishlist(wishlist);

            Cart createdCart = cartRepository.save(newCart);
            return createdCart;
        }

        // since cart exists, just add buylayter items as wishes to it.
        else {

            log.info("Cart is isn't null, resuming from here!");

            // on existing cart's wishlist, buylater items are added as wishes
            List<Wishlist> wishlist = cart.getWishlist();

            for (BuyLater buyLater : buyLaterList) {
                Wishlist buyLaterToWishlistItem = new Wishlist();
                buyLaterToWishlistItem.setProduct_name(buyLater.getBuylater_product_name());
                buyLaterToWishlistItem.setProduct_quantity(buyLater.getBuylater_product_quantity());
                buyLaterToWishlistItem.setProduct_manufacturer(buyLater.getBuylater_product_manufacturer());
                buyLaterToWishlistItem.setPayable_amount(buyLater.getBuylater_payable_amount());
                buyLaterToWishlistItem.setCart(cart);
                buyLaterToWishlistItem.setCustomer(customer_found);

                wishlist.add(buyLaterToWishlistItem);

                wishlistRepository.save(buyLaterToWishlistItem);
                buyLaterToWishlistItem = null;
            }

            Double totalPayableAmount = getTotalPayableAmount(wishlist);
            cart.setTotal_amount(totalPayableAmount);

            Cart updated = cartRepository.save(cart);

            return updated;
        }
    }

    // Fetch the List<Orders> from Order-Service
    // Assign them as Customer with List<CustomerOrder>
    public CustomerOrder fetchOrders_fromOrderService(String access_token) throws CustomerLoginException {
        UserProfileResponse userProfile = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfile.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        OrderResponse orderResponse = orderClient.getLastOrder().getBody();

        logger.info("OrderResponse : "  + orderResponse.toString());

        CustomerOrder customer_order = CustomerOrder
                .builder()
                .order_id(orderResponse.getOrder_id())
                .razorpay_order_id(orderResponse.getRazorpay_order_id())
                .customer_email(orderResponse.getCustomer_email())
                .customer_name(orderResponse.getCustomer_name())
                .customer_phone(orderResponse.getCustomer_phone())
                .amount(orderResponse.getAmount())
                .expected_delivery_date(orderResponse.getExpected_delivery_date())
                .customer_delivery_address(orderResponse.getCustomer_delivery_address())
                .status(orderResponse.getStatus())
                .payment_date(orderResponse.getPayment_date())
                .order_date(orderResponse.getOrder_date())
                .customer(customer_found)
                .build();
        CustomerOrder customerOrder = customerOrderRepository.save(customer_order);

        return customerOrder;
    }

    public Customer addWishlist_toCustomerOrder(String access_token, CustomerOrder customerOrder) {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();
        Customer customer_found = customerOrder.getCustomer();

        if(!userProfileResponse.getEmail().equals(customer_found.getCustomer_email())) {
            throw new RuntimeException("Customer Mismatch!");
        }

        List<Wishlist> wishlist = customer_found.getWishlist();
        ArrayList<CustomerPurchase> customerPurchases = new ArrayList<>();

        for(Wishlist wish : wishlist) {
            CustomerPurchase customer_purchase = CustomerPurchase
                    .builder()
                    .product_name(wish.getProduct_name())
                    .product_price(wish.getPayable_amount())
                    .product_manufacturer(wish.getProduct_manufacturer())
                    .product_quantity(wish.getProduct_quantity())
                    .customer_order(customerOrder)
                    .build();
            CustomerPurchase new_customer_purchase = customerPurchaseRepository.save(customer_purchase);
            customerPurchases.add(new_customer_purchase);
        }

        customerOrder.setCustomer_purchase(customerPurchases);
        customerOrderRepository.save(customerOrder);

        return customer_found;
    }

    // Remove cart post order generation
    public String removeCart_postOrderGeneration(String access_token) throws CustomerLoginException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        cartRepository.delete(customer_found.getCart());
        customer_found.getWishlist().stream().forEach(wishlist -> wishlistRepository.delete(wishlist));

        customerRepository.save(customer_found);
        return "Shopping Cart is Empty!";
    }

    // Generate Bill for CustomerOrders
    public Invoice generateInvoice(String access_token) throws CustomerLoginException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new RuntimeException("Customer Not Found"));

        List<CustomerOrder> customerOrders = customer_found.getCustomerOrders();
        CustomerOrder last_order = customerOrders.get(customerOrders.size() - 1);
        last_order.setStatus("BILL_GENERATED");
        customerOrderRepository.save(last_order);

        // join the last_order with invoice?
        Invoice invoice = Invoice
                .builder()
                .invoice_number("#ECMS-RETAIL-01") // make it dynamic
                .invoice_generationDate(LocalDate.now())
                .customerOrder(last_order)
                .customer(customer_found)
                .build();

        Invoice new_invoice = invoiceRepository.save(invoice);

        orderClient.sendEmailInvoice(access_token, new_invoice);
        orderClient.sendTextMessage(access_token, new_invoice);

        return new_invoice;
    }

    public byte[] getInvoice(String access_token) throws IOException, CustomerLoginException {

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new CustomerLoginException("Customer Not Found"));

        List<Invoice> invoices = customer_found.getInvoices();
        Invoice last_invoice = invoices.get(invoices.size() - 1);

        byte[] bytes = downloadInvoice(last_invoice);
        return bytes;
    }

    public byte[] downloadInvoice(Invoice invoice) throws IOException {
        return pdfGeneratorClient.getPDF(invoice).getBody();
    }

    public OTPResponse generateOTP(String mobile) {
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(111111, 999999));
        System.out.println("OTP: " + otp);
        cachingClient.putOtpAndContactMediumInCache(otp, mobile);

        SMSRequest smsRequest = SMSRequest
                .builder()
                .message(otp)
                .phone_number("+91" + mobile)
                .build();
        messegingClient.sendSMS(smsRequest).getBody();

        return OTPResponse.builder().otp(otp).build();
    }

    public AuthResponse verifyOTP(String otp) {
        String mobileByOTPResponse = cachingClient.getContactMediumByOTP(otp);
        log.info("mobileByOTPResponse: " + mobileByOTPResponse);
        if(mobileByOTPResponse.equals("Not Found")) {
            return AuthResponse.builder().access_token(mobileByOTPResponse).build();
        }
        else {
            Optional<Customer> existing_customer = customerRepository.findByCustomerMobile(mobileByOTPResponse);
            String email;

            if(existing_customer.isPresent()) {
                email = existing_customer.get().getCustomer_email();
            }
            else {
                email = mobileByOTPResponse + "@ecms.com";

                Customer new_customer = new Customer();
                new_customer.setCustomer_mobile(mobileByOTPResponse);
                new_customer.setCustomer_email(email);
                customerRepository.save(new_customer);
            }
            return authenticationClient.otpLogin(email).getBody();
        }
    }

    public void sendOTPToEmail(String access_token, String email) throws CustomerLoginException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new CustomerLoginException("Customer Not Found"));

        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(111111, 999999));

        cachingClient.putOtpAndContactMediumInCache(otp, email);

        emailingClient.sendOTP(email, otp);
    }

    public Customer verifyEmailOTP(String access_token, String otp) throws CustomerLoginException {
        String emailByOTP = cachingClient.getContactMediumByOTP(otp);

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new CustomerLoginException("Customer Not Found"));

        customer_found.setCustomer_email(emailByOTP);
        customerRepository.save(customer_found);

        UpdateEmailDto updateEmailDto = new UpdateEmailDto();
        updateEmailDto.setEmail(emailByOTP);

        userClient.update(access_token, updateEmailDto);
        return customer_found;
    }

    public void sendOTPToMobile(String access_token, String mobile) throws CustomerLoginException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new CustomerLoginException("Customer Not Found"));

        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(111111, 999999));

        logger.info("OTP: " + otp);

        cachingClient.putOtpAndContactMediumInCache(otp, mobile);

        SMSRequest smsRequest = SMSRequest
                .builder()
                .message(otp)
                .phone_number("+91" + mobile)
                .build();

        // messegingClient.sendSMS(smsRequest);
    }

    public Customer verifyMobileOTP(String access_token, String otp) throws CustomerLoginException {
        String mobileByOTP = cachingClient.getContactMediumByOTP(otp);

        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(access_token).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new CustomerLoginException("Customer Not Found"));

        customer_found.setCustomer_mobile(mobileByOTP);
        customer_found.setCustomer_email(mobileByOTP + "@ecms.com");
        Customer save = customerRepository.save(customer_found);

        UpdateEmailDto updateEmailDto = new UpdateEmailDto();
        updateEmailDto.setEmail(mobileByOTP + "@ecms.com");
        userClient.update(access_token, updateEmailDto);

        return save;
    }

    public Customer changePassword(String accessToken, UpdatePasswordDto updatePasswordDto) throws CustomerLoginException {
        UserProfileResponse userProfileResponse = userClient.getLoggedInUser(accessToken).getBody();

        Customer customer = new Customer();
        customer.setCustomer_email(userProfileResponse.getEmail());

        Example<Customer> customerExample = Example.of(customer);
        Customer customer_found = customerRepository.findOne(customerExample).orElseThrow(() -> new CustomerLoginException("Customer Not Found"));


        userClient.update(accessToken, updatePasswordDto);

        return customer_found;
    }


//    public AuthResponse registerOrLoginOAuthUser(String token) throws CustomerPreviouslyLoggedInException {
//
//        String oauthLoggedInUserEmail = getUserEmail(token);
//
//        List<Customer> all_customers = customerRepository.findAll();
//
//        for(Customer customer : all_customers) {
//            if(customer.getCustomer_email().equals(oauthLoggedInUserEmail)){
//                throw new CustomerPreviouslyLoggedInException("Login SuccessFull!");
//            }
//        }
//
//        Customer new_customer = new Customer();
//        new_customer.setCustomer_email(userEmail);
//        customerRepository.save(new_customer);
//
//        AuthResponse authResponse = AuthResponse.builder().token(token).build();
//        return authResponse;
//    }
}
