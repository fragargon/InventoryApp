# Inventory App, Stage 1

*Created as part of Udacity's Android Basics by Google Nanodegree Program*
____________

The goal is to create a a Inventory App that will allow a store to keep track of its inventory of products. 
The app will need to store information about the product and allow the user to track sales and shipments and 
make it easy for the user to order more from the listed supplier.
____________

# Project Specifications

## Layout

1. **Main Screen**. No UI is required for this project.
2. **Hint:**
	        - At minimum, you will need a main activity that has methods to read data, a Contract Java class, and a DbHelper Java class.
	Note: 
      - Even though UI is not required for this Stage, we highly recommend that you test your insert/read methods with log calls. 	
	    - Often, students do not realize their code has SQL syntax errors until the app is run and the methods are called which results in the project not passing.

3. **Best practices**. The code adheres to all of the following best practices:
   * Text sizes are defined in sp.
   * Lengths are defined in dp.
   * Padding and margin is used appropriately, such that the views are not crammed up against each other.
   
## Functionnality

1. **Compile Time Errors**. The code compiles without errors.
2. **Table Definition:** 	There exists a contract class that defines name of table and constants.
				                  Inside the contract class, there is an inner class for each table created.
				                  The contract contains at minimum constants for the Product Name, Price, Quantity, Supplier Name, and Supplier Phone Number.
3. **Table Creation**. There exists a subclass of SQLiteOpenHelper that overrides onCreate() and onUpgrade().
4. **Data Insertion:** There is a single insert method that adds:
								                                               **Product Name
								                                                - Price
								                                                - Quantity
								                                                - Supplier Name
								                                                - Supplier Phone Number**
	            - however, it is required that there are at least 2 different datatypes (e.g. INTEGER, STRING).
5. **Data Reading**. 	
      There is a single method that uses a Cursor from the database to perform a query on the table to retrieve at least 
			one column of data. Also the method should close the Cursor after it's done reading from it.

____________

InventoryApp stage 2

<img src="https://github.com/fragargon/InventoryApp/raw/master/InventoryApp.gif" height="420" width="240"> 


