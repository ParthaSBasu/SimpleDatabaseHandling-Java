Simple Database Handling (Java + Swing + MySQL)

This is a beginner-friendly Java Swing project designed to understand how a Java application can connect to a database (MySQL) and perform basic CRUD operations (Create, Read, Update, Delete).


Project Overview

This mini-project demonstrates:

✅ Connecting Java applications to MySQL using JDBC

✅ Building a GUI application using Swing components

✅ Performing Insert, Update, Delete, Navigation (Next/Previous) operations

✅ Displaying database records in a flash table window (JWindow)

✅ Using ArrayList and Model classes for better data handling





Technologies Used

Java (JDK 8 or later) – Core programming language

Swing – For building the GUI

MySQL – To store student records

JDBC (Java Database Connectivity) – To connect Java and MySQL

ArrayList – To store and navigate data in memory




Database Setup

Install MySQL on your computer.

Open MySQL Workbench or Command Line.

Create a database:

CREATE DATABASE my_schema;
USE my_schema;

Create the students table:

CREATE TABLE students (
    roll INT PRIMARY KEY,
    name VARCHAR(50),
    address VARCHAR(100),
    age INT
);




Features

Insert: Add new student records into MySQL database.

Update: Modify existing student data by roll number.

Delete: Remove a student record.

Show: Display the first record.

Next/Previous: Navigate through records.

Flash Table View: Opens a temporary table (JWindow) showing all records.

Clear: Resets all input fields.
