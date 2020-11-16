# RESTAPI_Inventory




The project is created as a Maven project within the Eclipse IDE with Java 8, JDBC and MySql Database as backend.


1.


  The Database "online_store" contains 4 tables as below:

  a. Customer
  b. Category
  c. Product
  d. Orders

  a. Customer table has 4 columns:
  		
  		Customer ID, Customer Name, Customer Email, Customer Phone

  		Total customers = 6

  b. Category table has 5 columns:

  		ID, Category ID, Product ID, Category Name, Product_Count

  		6 categories with 5 products in each category (30 products)

  c. Product table has 5 columns:

  		Product ID, Category ID, Weight, Product Name, Manufacturer

  		Total 30 products

  d. Orders table has 6 columns:

  		Order ID, Order Product ID, Status, Created Date, Customer ID, Quantity

  		Total 19 orders are created.


  A Product belongs to one specific category and a category has atleast one product.


2.


	An order is created using RESTful API service as below:

		http://localhost:8080/shipt/online_store/orders/order/create/8/10-2,1-1,50-3

	This creates an order with a status "Ready" for the products with ID: 10,1,50 and quantity (2,2,3) respectively.

	The status of these orders are updated as REST API is called for any service (CRUD calls) with below changes in times.

		0 - 12 hours: Ready, 12 - 24 hours: On its Way, after 24 hours: Delivered

	Any create order without the mention of quantity is by default 1.	

	http://localhost:8080/shipt/online_store/orders/order/create/6/1,2,3

	If the quantity is not mentioned, the order will have by default 1 item with ID - 1,2,3.


3. 



	1. http://localhost:8080/shipt/online_store/orders/order/byDate/2020-08-24/2020-09-04
	
		Returns all the products sold between below dates with the Category and product names
	
		begin date and end date
		

		Returns the products sold between two dates

	2. http://localhost:8080/shipt/online_store/orders/order/onDate/2020-09-04

	   date

	   Returns the products sold on the particular date

4. 

	http://localhost:8080/shipt/online_store/orders/order/customer/6

	Returns the orders placed by customer with ID 6 (in JSON)

5.


	http://localhost:8080/shipt/online_store/orders/order/create/8/{order}

	Example 1:
			http://localhost:8080/shipt/online_store/orders/order/create/6/1-2,10-1,15-2

			order:

 
						product ID 10 with 1 quantity, 
						product ID 1 with 2 quantities, 
						product ID 15 with 2 quantities
 

    Example 2:
    		http://localhost:8080/shipt/online_store/orders/order/create/6/1,2,3

    		order:
 						product ID 1 with 1 quantity, 
						product ID 2 with 1 quantity, 
						product ID 3 with 1 quantity
 	


CRUD Operations:

1. Create an order:

	http://localhost:8080/shipt/online_store/orders/order/create/8/1-2,10-1,5-3

2. Read an order:

	http://localhost:8080/shipt/online_store/orders
	http://localhost:8080/shipt/online_store/orders/order/read/1

3. Update an order

	http://localhost:8080/shipt/online_store/orders/order/update/5/14/1-2,12-1,10-1

	Replaces the previous order with ID 5, with newly supplied product IDs.

4. Delete an order:

	http://localhost:8080/shipt/online_store/orders/order/delete/6











