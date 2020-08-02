# Data validation tool

Data validation tool (dvt) is a standalone application for columnar data comparison between two data sources. 

#### 1. Idea
 
The idea is to compare columnar data that represented as key-value data.    

For example, we have departments table, and we want to compare a data between the current version of our application and a new one.
So, we have to bring up two separate versions of our application and check the data:
 
 ![](docs/img/pic-1.png) 
 
Here we can see the difference in "ID" column and "RATIO" column.
So, in order to be able to find the discrepancies we have to compare the data using some unique key ("ID" in our case).
So, the comparison should look like:

![](docs/img/pic-2.png) 

and the comparison result should be something like that:

![](docs/img/pic-3.png)

Using this approach we've found rows that contains changed data. Now we can review
the discrepancies and will see that "RATIO" column precision has changed. The second
discrepancy is:
    1) ids: [9, 10] haven't been found in second datasource;
    2) ids: [20, 21] haven't been found in first datasource. 
    

The false-positive scenario can happen when we apply some data schema changes.
For example, in the first version we have two columns with the first and last name and in a second version of our software 
we decided to define a new column "NAME" that will contain both the first and the last name. We've also removed "FIRST_NAME" and "LAST_NAME" columns. 

![](docs/img/pic-4.png)

In this case it would be great to check the migration we performed. In order to do this we can apply some transformation function 
to the first data source and compare the results:g

![](docs/img/pic-5.png) 

#### 2. Solution

#### 3. Basic Architecture

#### 4. Supported datasources

#### 5. First Steps

#### 6. Configurationpic_5
