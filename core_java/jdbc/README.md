# Java JDBC App

## Introduction
The Java JDBC app is an application that performs CRUD operations(create, read, update, delete) against an RDBMS. 
Java's `JDBC` API allows to connect to RDBMS and execute SQL statements. The RDBMS that I used in this project is
`PostgreSQL` which is actually an Object-RDBMS, a more object-oriented RDBMS. I provisioned a `psql` instance using `Docker`.
I also used `Maven` for package management, `Intellij` for source code organization, and `Git` for version control.

## Implementaiton

### ER Diagram


### Design Patterns
The DAO and Repository design patterns are architectural patterns provides an abstraction layer between
the source code and the data source. This enables a complete separation of concern between the business 
logic layer and the data layer. 

A DAO provides an interface with methods for each one of the CRUD operations: 
create, read, update, and delete. A DAO can be a simple abstraction or a true object. A DAO can use
a Data Transfer Object (DTO) as input or output.

A Repository pattern is a more abstract version of a DAO pattern. It defines a generic interface for accessing
and manipulating databases, without specifying how data is stored or retrieved. This makes it easier to replace
an implementation without affecting the rest of the application.

Both pattern present some tradeoffs. For example, the Repository pattern is better suited for single-table data
access per class and for applications that are horizontally scalable, while the DAO pattern is better for 
normalized databases and much better for atomic transactions that cross data tables. However, it is not 
uncommon for the two patterns to be used together.

## Test
I tested the app manually using a driver class with different SQL queries. The CRUD operations worked as expected.
Additionally, I used `Intellij`'s debugger to ensure methods were working correctly.