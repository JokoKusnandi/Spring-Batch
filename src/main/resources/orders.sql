DROP TABLE orders IF EXISTS;

CREATE TABLE orders (
    Row_ID BIGINT IDENTITY NOT NULL PRIMARY KEY,
    Order_ID INT,
    Order_Date DATE,
    Ship_Date DATE,
    Ship_Mode VARCHAR(255),
    Customer_ID INT,
    Customer_Name VARCHAR(255),
    Segment VARCHAR(255),
    Country VARCHAR(255),
    City VARCHAR(255),
    State VARCHAR(255),
    Postal_Code INT,
    Region VARCHAR(255),
    Product_ID INT,
    Category VARCHAR(255),
    Sub_Category VARCHAR(255),
    Product_Name VARCHAR(255),
    Sales DECIMAL(10, 4),
    Quantity INT,
    Discount DECIMAL(4, 4),
    Profit DECIMAL(10, 4)
);



