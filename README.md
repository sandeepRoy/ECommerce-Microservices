# ECommerce-Microservices
## Microservices Architechture based Ecommerce APIs

### Plan: Post Payment Order Creation

#### Need : Microservice for Order Management of Customers & Admin(Role not created yet).

#### Description : 

####
* Order table to hold multiple orders of a customer, Customer : Order (@OneToMany)

* Order table should have coloumns of :
   * Payment Status : Fetch from Payment_Order.order_status table
   * Wishlist items : Customer.Cart
   * Delivery Address: Customer.Cart.DeliveryAddress
   * Contact Details: Customer
   * Order Statuses : []
   * Payment Informations : []

* Endpoints:
   * [POST] -/create-order :
     * trigger after Payment Confirmation Redirect to success page
     * take payment_order's - id, status, contact details; cart's item name, quantity & price; orderstatus enum's status, payment_order's required columns
     * map it with customer table
   * [GET] - /order-status - List<Order>
   * [Delete] - /cancel-order/{order_id}
####
