# ECommerce-Microservices
Microservices Architechture based Ecommerce APIs

Plan: Post Payment Order Creation

Need : Microservice for Order Management of Customers & Admin(Role not created yet).

Description : 

1. Order table to hold multiple orders of a customer, Customer : Order (@OneToMany)

2. Order table should have coloumns of :
   2.1. Payment Status : Fetch from Payment_Order.order_status table
   2.2. Wishlist items : Customer.Cart
   2.3. Delivery Address: Customer.Cart.DeliveryAddress
   2.4. Contact Details: Customer

3. Endpoints:
   3.1. [POST] - /create-order :
        - trigger after Payment Confirmation Redirect to success page
        - take payment_order's - id, status, contact details; cart's item name, quantity & price
        - map it with customer table
   3.2 [GET] - /order-status - List<Order>
   3.3 [Delete] - /cancel-order/{order_id}
